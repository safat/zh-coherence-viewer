package com.zh.coherence.viewer.tools.statistic.report;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.tools.statistic.config.ConfigPropertyContainer;
import org.jdesktop.swingx.JXErrorPane;

import javax.management.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JMXReport {

    private static JMXReport instance = new JMXReport();

    public static JMXReport getInstance() {
        return instance;
    }

    private List<List> clusterInfo;

    private NodeReport nodeReport = new NodeReport();

    private CacheReport cacheReport = new CacheReport();

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    private ConfigPropertyContainer propertyContainer = null;

    private Map<CacheKey, Map<String, Object>> cacheInfo = new HashMap<CacheKey, Map<String, Object>>();
    private Map<Integer, Map<String, Object>> nodeInfo = new HashMap<Integer, Map<String, Object>>();
    private Map<CacheKey, Map<String, Object>> serviceInfo = new HashMap<CacheKey, Map<String, Object>>();
    private Map<String, Object> clusterJmxInfo = new HashMap<String, Object>();

    public CacheReport getCacheReport() {
        return cacheReport;
    }

    public void setCacheReport(CacheReport cacheReport) {
        this.cacheReport = cacheReport;
    }

    public List<List> getClusterInfo() {
        return clusterInfo;
    }

    public void setClusterInfo(List<List> clusterInfo) {
        this.clusterInfo = clusterInfo;
    }

    public NodeReport getNodeReport() {
        return nodeReport;
    }

    public void setNodeReport(NodeReport nodeReport) {
        this.nodeReport = nodeReport;
    }

    public void refresh() {
        nodeReport.updateData();
        cacheReport.updateData();

        notifyAllListeners();
    }

    public void notifyAllListeners() {
        for (ChangeListener l : listeners) {
            l.stateChanged(new ChangeEvent(this));
        }
    }

    private synchronized void putToMap(Map map, Object key, Object value){
        map.put(key, value);
    }

    public void refreshReport() {
        long time = System.currentTimeMillis();
        ExecutorService es = Executors.newFixedThreadPool(15);
        try {
            //collect info
            collectCacheInfo(es);
            collectNodeInfo(es);
            collectServiceInfo(es);
            collectClusterInfo(es);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
            try {
                if (!es.awaitTermination(10, TimeUnit.MINUTES)) {
                    es.shutdownNow();
                    if (!es.awaitTermination(5, TimeUnit.MINUTES)) {
                        System.err.println("ScheduledCalculate pool did not terminate.");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.err.println("nodes report time : " + (System.currentTimeMillis() - time));
        notifyAllListeners();
    }

    private void collectCacheInfo(ExecutorService es) {

        try {
            final MBeanServerConnection server = JMXManager.getInstance().getServer();
            Set<ObjectName> cacheNamesSet = server.queryNames(new ObjectName("Coherence:type=Cache,*"), null);
            for (final Object aCacheNamesSet : cacheNamesSet) {
                es.execute(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> cacheMap;
                        try {
                            ObjectName cacheNameObjName = (ObjectName) aCacheNamesSet;
                            String name = cacheNameObjName.getKeyProperty("name");
                            Integer id = Integer.valueOf(cacheNameObjName.getKeyProperty("nodeId"));
                            List<Attribute> attributes = server.getAttributes(
                                    cacheNameObjName, propertyContainer.getFilteredNames("cache")).asList();
                            cacheMap = new HashMap<String, Object>();
                            cacheMap.put("service", cacheNameObjName.getKeyProperty("service"));
                            for (Attribute attribute : attributes) {
                                cacheMap.put(attribute.getName(), attribute.getValue());
                            }
                            putToMap(cacheInfo, new CacheKey(id, name), cacheMap);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void collectNodeInfo(ExecutorService es) {
        try {
            final MBeanServerConnection server = JMXManager.getInstance().getServer();
            Set<ObjectName> cacheNamesSet = server.queryNames(new ObjectName("Coherence:type=Node,*"), null);

            for (final Object aCacheNamesSet : cacheNamesSet) {
                es.execute(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> cacheMap;
                        try {
                            ObjectName cacheNameObjName = (ObjectName) aCacheNamesSet;
                            Integer id = Integer.valueOf(cacheNameObjName.getKeyProperty("nodeId"));
                            List<Attribute> attributes = server.getAttributes(
                                    cacheNameObjName, propertyContainer.getFilteredNames("node")).asList();
                            cacheMap = new HashMap<String, Object>();
                            for (Attribute attribute : attributes) {
                                cacheMap.put(attribute.getName(), attribute.getValue());
                            }
                            putToMap(nodeInfo, id, cacheMap);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void collectServiceInfo(ExecutorService es) {
        try {
            Map<String, Object> cacheMap;
            final MBeanServerConnection server = JMXManager.getInstance().getServer();
            Set<ObjectName> cacheNamesSet = server.queryNames(new ObjectName("Coherence:type=Service,*"), null);
            for (final Object aCacheNamesSet : cacheNamesSet) {
                es.execute(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> cacheMap;
                        try {
                            ObjectName cacheNameObjName = (ObjectName) aCacheNamesSet;
                            Integer id = Integer.valueOf(cacheNameObjName.getKeyProperty("nodeId"));
                            String name = cacheNameObjName.getKeyProperty("name");
                            List<Attribute> attributes = server.getAttributes(
                                    cacheNameObjName, propertyContainer.getFilteredNames("service")).asList();
                            cacheMap = new HashMap<String, Object>();
                            for (Attribute attribute : attributes) {
                                cacheMap.put(attribute.getName(), attribute.getValue());
                            }
                            putToMap(serviceInfo, new CacheKey(id, name), cacheMap);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void collectClusterInfo(ExecutorService es) {
        try {
            MBeanServerConnection server = JMXManager.getInstance().getServer();
            ObjectName oName = new ObjectName("Coherence:type=Cluster");
            List<Attribute> attributes = server.getAttributes(
                    oName, propertyContainer.getFilteredNames("cluster")).asList();
            for (Attribute attribute : attributes) {
                putToMap(clusterJmxInfo, attribute.getName(), attribute.getValue());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public JMXReport() {
        initProperties();
    }

    private File getFile() {
        File home = new File("config");
        if (!home.exists()) {
            home.mkdir();
        }
        return new File(home, "jmx-report.xml");
    }

    private void initProperties() {
        try {
            JAXBContext context = JAXBContext.newInstance(ConfigPropertyContainer.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            System.err.println("path: " + getFile().getAbsolutePath());
            propertyContainer = (ConfigPropertyContainer) unmarshaller.unmarshal(getFile());
        } catch (Exception ex) {
            JXErrorPane.showDialog(ex);
        }
    }

    public class CacheKey {
        private String name;
        private int id;

        private CacheKey(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;

            if (id != cacheKey.id) return false;
            if (name != null ? !name.equals(cacheKey.name) : cacheKey.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + id;
            return result;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public Map<String, Object> getClusterJmxInfo() {
        return clusterJmxInfo;
    }

    public Map<CacheKey, Map<String, Object>> getServiceInfo() {
        return serviceInfo;
    }

    public Map<Integer, Map<String, Object>> getNodeInfo() {
        return nodeInfo;
    }

    public Map<CacheKey, Map<String, Object>> getCacheInfo() {
        return cacheInfo;
    }

    public ConfigPropertyContainer getPropertyContainer() {
        return propertyContainer;
    }
}
