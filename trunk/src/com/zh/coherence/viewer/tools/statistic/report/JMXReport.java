package com.zh.coherence.viewer.tools.statistic.report;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

public class JMXReport {
    private List<List> clusterInfo;

    private NodeReport nodeReport = new NodeReport();

    private CacheReport cacheReport = new CacheReport();

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

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

    public void refresh(){
        nodeReport.updateData();

        for(ChangeListener l : listeners){
            l.stateChanged(new ChangeEvent(this));
        }
    }

    public void addListener(ChangeListener listener){
        listeners.add(listener);
    }
}
