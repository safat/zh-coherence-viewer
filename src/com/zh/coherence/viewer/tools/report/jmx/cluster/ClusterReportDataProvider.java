package com.zh.coherence.viewer.tools.report.jmx.cluster;

import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import com.zh.coherence.viewer.utils.FileUtils;

import java.util.Map;

public class ClusterReportDataProvider {

    JMXReport report;

    public ClusterReportDataProvider() {
        report = JMXReport.getInstance();
        report.refreshReport();
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

    public class MemoryInfo {
        int maxMB;
        int availableMB;
        String maxLabel;
        String busyLabel;
    }
}
