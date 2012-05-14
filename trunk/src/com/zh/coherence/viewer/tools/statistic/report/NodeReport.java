package com.zh.coherence.viewer.tools.statistic.report;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.tools.statistic.report.nodeinfo.MachineInfo;
import com.zh.coherence.viewer.tools.statistic.report.nodeinfo.NodeInfo;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.*;

public class NodeReport {

    private Map<String, MachineInfo> data = new HashMap<String, MachineInfo>();

    private int memAvailable = 0;

    private int memMaximum = 0;

    public void updateData() {
        try {
            data.clear();
            memAvailable = 0;
            memMaximum = 0;

            MBeanServerConnection server = JMXManager.getInstance().getServer();
            Set<ObjectName> cacheNamesSet = server.queryNames(new ObjectName("Coherence:type=Node,*"), null);
            for (ObjectName on : cacheNamesSet) {

                AttributeList attrs = server.getAttributes(
                        on, new String[]{"MachineName", "MemoryAvailableMB", "MemoryMaxMB", "MemberName", "Id"});
                List<Attribute> attributes = attrs.asList();
                String name = (String) attributes.get(0).getValue();
                Integer available = (Integer) attributes.get(1).getValue();
                Integer max = (Integer) attributes.get(2).getValue();
                String member = (String) attributes.get(3).getValue();
                Integer id = (Integer) attributes.get(4).getValue();

                memAvailable += available;
                memMaximum += max;

                MachineInfo info = data.get(name);
                if (info == null) {
                    info = new MachineInfo(name);
                    data.put(name,info);
                }
                info.incMemAvailable(available);
                info.incMemMax(max);

                info.getNodes().add(new NodeInfo(id + " (" + member + ")", available, max));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public int getMemBusy(){
        return memMaximum - memAvailable;
    }
}
