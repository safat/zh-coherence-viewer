package com.zh.coherence.viewer.tools.statistic.report;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.tools.statistic.report.nodeinfo.MachineInfo;
import com.zh.coherence.viewer.tools.statistic.report.nodeinfo.NodeInfo;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NodeReport implements Named {

    private Map<String, MachineInfo> data = new HashMap<String, MachineInfo>();

    private int memAvailable = 0;

    private int memMaximum = 0;

    private int units = 0;

    public void updateData() {
        long time = System.currentTimeMillis();
        ExecutorService es = Executors.newFixedThreadPool(5);
        try {
            data.clear();
            memAvailable = 0;
            memMaximum = 0;

            final MBeanServerConnection server = JMXManager.getInstance().getServer();
            Set<ObjectName> cacheNamesSet = server.queryNames(new ObjectName("Coherence:type=Node,*"), null);
            for (final ObjectName on : cacheNamesSet) {
                es.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AttributeList attrs = server.getAttributes(
                                    on, new String[]{"MachineName", "MemoryAvailableMB", "MemoryMaxMB", "MemberName", "Id"});
                            List<Attribute> attributes = attrs.asList();
                            String name = (String) attributes.get(0).getValue();
                            Integer available = (Integer) attributes.get(1).getValue();
                            Integer max = (Integer) attributes.get(2).getValue();
                            String member = (String) attributes.get(3).getValue();
                            Integer id = (Integer) attributes.get(4).getValue();
                            Integer unit = 0;//(Integer) attributes.get(5).getValue();

                            memAvailable += available;
                            memMaximum += max;
                            units += unit;

                            MachineInfo info = data.get(name);
                            if (info == null) {
                                info = new MachineInfo(name);
                                data.put(name, info);
                            }
                            info.incMemAvailable(available);
                            info.incMemMax(max);
                            info.incUnits(unit);

                            info.getNodes().add(new NodeInfo(id + " (" + member + ")", available, max, unit));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

            }
            System.err.println("nodes report time : " + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
            try {
                if (!es.awaitTermination(7, TimeUnit.MINUTES)) {
                    es.shutdownNow();
                    if (!es.awaitTermination(5, TimeUnit.MINUTES)) {
                        System.err.println("ScheduledCalculate pool did not terminate.");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int getAverage() {
        if (data.size() == 0) {
            return 0;
        }
        int res = 0;
        for (MachineInfo info : data.values()) {
            res += info.getAverage();
        }
        res = res / data.size();
        return res;
    }

    public List<MachineInfo> getData() {
        return new ArrayList<MachineInfo>(data.values());
    }

    public int size() {
        return data.size();
    }

    public int getMemAvailable() {
        return memAvailable;
    }

    public int getMemMaximum() {
        return memMaximum;
    }

    public int getMemBusy() {
        return memMaximum - memAvailable;
    }

    public int getUnits() {
        return units;
    }

    @Override
    public String getName() {
        return "Memory report [" + getData().size() + "]";
    }
}
