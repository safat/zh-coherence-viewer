package com.zh.coherence.viewer.tools.backup;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 19.03.12
 * Time: 20:56
 */
public class BackupContext {
    public enum BackupAction {BACKUP, RESTORE}
    public enum Target {FOLDER, ZIP}

    protected JProgressBar generalProgress, cacheProgress;

    private BackupAction action = BackupAction.BACKUP;
    private Target target = Target.FOLDER;
    private String path;
//    private List<String> caches = new ArrayList<String>();
    private DefaultListModel caches = new DefaultListModel();

    public void updateGeneralProgress(){
        generalProgress.setString((generalProgress.getPercentComplete()*100) + " %");
    }

    public void incrementGeneralProgress(){
        generalProgress.setValue(generalProgress.getValue() + 1);
        updateGeneralProgress();
    }

    public void incrementCacheProgress(String name){
        cacheProgress.setValue(cacheProgress.getValue() + 1);
        updateCacheProgress(name);
    }

    public void updateCacheProgress(String name) {
        cacheProgress.setString("["+name+"] - " + (cacheProgress.getPercentComplete()*100) + " %");
    }

    public BackupAction getAction() {
        return action;
    }

    public void setAction(BackupAction action) {
        this.action = action;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return target;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public DefaultListModel getCaches() {
        return caches;
    }

    public void setCaches(DefaultListModel caches) {
        this.caches = caches;
    }
}
