package com.zh.coherence.viewer.tools.backup;

import com.zh.coherence.viewer.eventlog.EventLogPane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BackupContext {
    public enum BackupAction {BACKUP, RESTORE}

    protected JProgressBar generalProgress, cacheProgress;

    public EventLogPane logPane;

    private BackupAction action;
    private String path;
    private int bufferSize = 200;
    private List<CacheInfo> cacheInfoList = new ArrayList<CacheInfo>();

    public BackupContext(BackupAction action) {
        this.action = action;
    }

    public void updateGeneralProgress(){
        generalProgress.setString((Math.rint(1000.0 * generalProgress.getPercentComplete()) / 10.0)  + " %");
    }

    public void incrementGeneralProgress(){
        incrementGeneralProgress(1);
    }

    public void incrementGeneralProgress(int inc){
        generalProgress.setValue(generalProgress.getValue() + inc);
        updateGeneralProgress();
    }

    public void incrementCacheProgress(String name){
        incrementCacheProgress(name, 1);
    }

    public void incrementCacheProgress(String name, int inc){
        cacheProgress.setValue(cacheProgress.getValue() + inc);
        updateCacheProgress(name);
    }

    public void updateCacheProgress(String name) {
        cacheProgress.setString("["+name+"] - " + (Math.rint(100.0 * cacheProgress.getPercentComplete()))
                + " %");
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

    public void setBufferSize(String bufferSize) {
        try{
            this.bufferSize = Integer.parseInt(bufferSize);
        }catch (NumberFormatException ex){
            //nothing to do
        }
    }

    public List<CacheInfo> getCacheInfoList() {
        return cacheInfoList;
    }
}
