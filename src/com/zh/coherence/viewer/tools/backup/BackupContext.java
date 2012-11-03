package com.zh.coherence.viewer.tools.backup;

import com.zh.coherence.viewer.eventlog.EventLogPane;
import com.zh.coherence.viewer.tools.backup.networkchart.NetworkChartModel;

import javax.swing.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class BackupContext {
    public enum BackupAction {
        BACKUP, RESTORE
    }

    protected JProgressBar generalProgress, cacheProgress;

    public EventLogPane logPane;

    private BackupAction action;

    @XmlElement
    private String path;

    @XmlAttribute
    private int bufferSize;

    @XmlAttribute
    private int threads;

    @XmlElement
    private List<CacheInfo> cacheInfoList = new ArrayList<CacheInfo>();

    private NetworkChartModel networkChartModel = new NetworkChartModel();

    public BackupContext() {
    }

    public BackupContext(BackupAction action) {
        this.action = action;
        switch (this.action) {
            case BACKUP:
                this.bufferSize = 7;
                this.threads = 5;
                break;
            case RESTORE:
                this.bufferSize = 350;
                this.threads = 15;
        }
    }

    public void updateGeneralProgress() {
        generalProgress.setString((Math.rint(1000.0 * generalProgress.getPercentComplete()) / 10.0) + " %");
    }

    public void incrementGeneralProgress() {
        incrementGeneralProgress(1);
    }

    public void incrementGeneralProgress(int inc) {
        generalProgress.setValue(generalProgress.getValue() + inc);
        updateGeneralProgress();
    }

    public void incrementCacheProgress(String name) {
        incrementCacheProgress(name, 1);
    }

    public void incrementCacheProgress(String name, int inc) {
        cacheProgress.setValue(cacheProgress.getValue() + inc);
        updateCacheProgress(name);
    }

    public void updateCacheProgress(String name) {
        double percentComplete = cacheProgress.getPercentComplete();
        if(Double.isNaN(percentComplete) && cacheProgress.getMinimum() == cacheProgress.getMaximum()){
            percentComplete = 1;
        }
        cacheProgress.setString("[" + name + "] - " + (Math.rint(100.0 * percentComplete)) + " %");
    }

    public BackupAction getAction() {
        return action;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public List<CacheInfo> getCacheInfoList() {
        return cacheInfoList;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setCacheInfoList(List<CacheInfo> cacheInfoList) {
        this.cacheInfoList = cacheInfoList;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public NetworkChartModel getNetworkChartModel() {
        return networkChartModel;
    }
}
