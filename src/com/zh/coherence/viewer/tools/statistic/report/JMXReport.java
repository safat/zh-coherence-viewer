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

public class JMXReport {
    private List<List> clusterInfo;

    private NodeReport nodeReport = new NodeReport();

    private CacheReport cacheReport = new CacheReport();

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    private ConfigPropertyContainer propertyContainer = null;

    private Map<CacheKey, Map<String, Object>> cacheInfo = new HashMap<CacheKey, Map<String, Object>>();
    private Map<Integer, Map<String, Object>> nodeInfo = new HashMap<Integer, Map<String, Object>>();
    private Map<CacheKey, Map<String, Object>> serviceInfo = new HashMap<CacheKey, Map<String, Object>>();

    public CacheReport getCacheReport() {
        return cacheReport;
    }

    private Map<Integer, Map<String, Object>> data = null;

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
        //collect info
        data = new HashMap<Integer, Map<String, Object>>();
        collectCacheInfo();
        collectNodeInfo();
        collectServiceInfo();

        for (ChangeListener l : listeners) {
            l.stateChanged(new ChangeEvent(this));
        }
    }

    private void collectCacheInfo() {
        try {
            Map<String, Object> cacheMap;
            MBeanServerConnection server = JMXManager.getInstance().getServer();
            Set<ObjectName> cacheNamesSet = server.queryNames(new ObjectName("Coherence:type=Cache,*"), null);
            for (Object aCacheNamesSet : cacheNamesSet) {
                ObjectName cacheNameObjName = (ObjectName) aCacheNamesSet;
                String name = cacheNameObjName.getKeyProperty("name");
                Integer id = Integer.valueOf(cacheNameObjName.getKeyProperty("nodeId"));
                List<Attribute> attributes = server.getAttributes(
                        cacheNameObjName, propertyContainer.getFilteredNames("cache")).asList();
                cacheMap = new HashMap<String, Object>();
                for (Attribute attribute : attributes) {
                    cacheMap.put(attribute.getName(), attribute.getValue());
                }
                cacheInfo.put(new CacheKey(id, name), cacheMap);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void collectNodeInfo() {
        try {
            Map<String, Object> cacheMap;
            MBeanServerConnection server = JMXManager.getInstance().getServer();
            Set<ObjectName> cacheNamesSet = server.queryNames(new ObjectName("Coherence:type=Node,*"), null);
            for (Object aCacheNamesSet : cacheNamesSet) {
                ObjectName cacheNameObjName = (ObjectName) aCacheNamesSet;
                Integer id = Integer.valueOf(cacheNameObjName.getKeyProperty("nodeId"));
                List<Attribute> attributes = server.getAttributes(
                        cacheNameObjName, propertyContainer.getFilteredNames("cache")).asList();
                cacheMap = new HashMap<String, Object>();
                for (Attribute attribute : attributes) {
                    cacheMap.put(attribute.getName(), attribute.getValue());
                }
                nodeInfo.put(id, cacheMap);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void collectServiceInfo() {
        try {
            Map<String, Object> cacheMap;
            MBeanServerConnection server = JMXManager.getInstance().getServer();
            Set<ObjectName> cacheNamesSet = server.queryNames(new ObjectName("Coherence:type=Service,*"), null);
            for (Object aCacheNamesSet : cacheNamesSet) {
                ObjectName cacheNameObjName = (ObjectName) aCacheNamesSet;
                Integer id = Integer.valueOf(cacheNameObjName.getKeyProperty("nodeId"));
                String name = cacheNameObjName.getKeyProperty("name");
                List<Attribute> attributes = server.getAttributes(
                        cacheNameObjName, propertyContainer.getFilteredNames("service")).asList();
                cacheMap = new HashMap<String, Object>();
                for (Attribute attribute : attributes) {
                    cacheMap.put(attribute.getName(), attribute.getValue());
                }
                serviceInfo.put(new CacheKey(id, name), cacheMap);
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

    private class CacheKey {
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
}
