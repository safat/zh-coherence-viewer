package com.zh.coherence.viewer.tools.report.jmx.cluster;

import com.zh.coherence.viewer.tools.report.jmx.cluster.transfer.ServiceInfo;
import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import com.zh.coherence.viewer.utils.FileUtils;

import java.util.*;

public class ClusterReportDataProvider {

    private JMXReport report;

    public ClusterReportDataProvider() {
        report = JMXReport.getInstance();
    }

    public String getClusterName() {
        return (String) report.getClusterJmxInfo().get("ClusterName");
    }

    public Integer getClusterSize() {
        return (Integer) report.getClusterJmxInfo().get("ClusterSize");
    }

    public String getLicense() {
        return (String) report.getClusterJmxInfo().get("LicenseMode");
    }

    public MemoryInfo getTotalMemory() {
        //collect memory
        MemoryInfo info = new MemoryInfo();
        for (Map<String, Object> map : report.getNodeInfo().values()) {
            info.maxMB += (Integer) map.get("MemoryMaxMB");
            info.availableMB += (Integer) map.get("MemoryAvailableMB");
        }
        info.busyLabel = FileUtils.convertToStringRepresentation(info.maxMB - info.availableMB, "MB");
        info.maxLabel = FileUtils.convertToStringRepresentation(info.maxMB, "MB");
        return info;
    }

    public List<ServiceInfo> collectServiceData() {
        Map<JMXReport.CacheKey, Map<String, Object>> serviceInfo = report.getServiceInfo();
        Map<String, ServiceInfo> services = new HashMap<String, ServiceInfo>();
        ServiceInfo info;
        if (serviceInfo != null) {
            for (JMXReport.CacheKey key : serviceInfo.keySet()) {
                info = services.get(key.getName());
                if (info == null) {
                    info = new ServiceInfo();
                    services.put(key.getName(), info);
                    info.name = key.getName();
                    info.storageNodes = (Integer) serviceInfo.get(key).get("StorageEnabledCount");
                    info.senior = (Integer) serviceInfo.get(key).get("SeniorMemberId");
                    info.type = (String) serviceInfo.get(key).get("Type");
                }
                String stat = (String) serviceInfo.get(key).get("StatusHA");
                if (info.status != null) {
                    if (!info.status.contains(stat)) {
                        info.status += "," + stat;
                    }
                } else {
                    info.status = stat;
                }
                info.totalNodes++;
            }
        }
        Map<JMXReport.CacheKey, Map<String, Object>> cacheInfo = report.getCacheInfo();
        if (cacheInfo != null) {
            Map<String, Set<String>> caches = new HashMap<String, Set<String>>();
            for (JMXReport.CacheKey key : cacheInfo.keySet()) {
                Map<String, Object> map = cacheInfo.get(key);
                String service = (String) map.get("service");
                info = services.get(service);
                info.objects += (Integer) map.get("Size");
                info.units += (Integer) map.get("Units") * (Integer) map.get("UnitFactor");

                Set<String> set = caches.get(service);
                if (set == null) {
                    set = new HashSet<String>();
                    caches.put(service, set);
                }
                set.add(key.getName());
            }
            for (String service : caches.keySet()) {
                services.get(service).caches = caches.get(service).size();
            }
        }

        return new ArrayList<ServiceInfo>(services.values());
    }

    public Map<String, Object> getClusterAttributes(){
        return report.getClusterJmxInfo();
    }

    public String getPropertyDescription(String name){
        return report.getPropertyContainer().getDescription(name);
    }

    public class MemoryInfo {
        int maxMB;
        int availableMB;
        String maxLabel;
        String busyLabel;
    }
}
