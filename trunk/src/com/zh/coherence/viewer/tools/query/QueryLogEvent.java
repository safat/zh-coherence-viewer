package com.zh.coherence.viewer.tools.query;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 10.04.12
 * Time: 23:55
 */
public class QueryLogEvent {
    public static enum EventType{MESSAGE, ERROR, WARNING}

    public String message;
    public Throwable error;
    public EventType type;

    public QueryLogEvent(Throwable error, String message, EventType type) {
        this.error = error;
        this.message = message;
        this.type = type;
    }
}
