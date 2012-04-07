package com.zh.coherence.viewer.eventlog;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 07.04.12
 * Time: 16:35
 */
public interface EventLogRenderer {
    public String getCssStyle();

    public String renderMessage(Object msg);
}
