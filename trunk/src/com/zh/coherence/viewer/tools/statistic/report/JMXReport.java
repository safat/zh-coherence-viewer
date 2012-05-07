package com.zh.coherence.viewer.tools.statistic.report;

import java.util.List;

public class JMXReport {
    private List<List> clusterInfo;

    private NodeReport nodeReport;

    private CacheReport cacheReport;

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
}
