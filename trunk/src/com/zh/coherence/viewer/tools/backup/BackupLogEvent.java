package com.zh.coherence.viewer.tools.backup;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 07.04.12
 * Time: 20:38
 */
public class BackupLogEvent {
    private long startTime;
    private String cacheName;
    private long endTime;
    private String message;
    private String action;

    public BackupLogEvent(long startTime, String cacheName, long endTime, String message, String action) {
        this.cacheName = cacheName;
        this.endTime = endTime;
        this.message = message;
        this.startTime = startTime;
        this.action = action;
    }

    public String getCacheName() {
        return cacheName;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getMessage() {
        return message;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getAction() {
        return action;
    }
}
