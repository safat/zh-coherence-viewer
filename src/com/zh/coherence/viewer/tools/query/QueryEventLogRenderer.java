package com.zh.coherence.viewer.tools.query;

import com.zh.coherence.viewer.eventlog.EventLogRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 10.04.12
 * Time: 23:19
 */
public class QueryEventLogRenderer implements EventLogRenderer {
    private final String simpleMsg = "<div width='100%'>";
    private final String errorMsg = "<div width='100%' style='color:red;font-weight:bold;'>";

    private final String endLine = "</div><br>";
    @Override
    public String getCssStyle() {
        return null;
    }

    @Override
    public String renderMessage(Object msg) {
        if (msg == null) {
            throw new IllegalArgumentException("msg");
        }
        if (msg instanceof QueryLogEvent) {
            QueryLogEvent event = (QueryLogEvent) msg;
            switch (event.type){
                case MESSAGE:
                    return simpleMsg + event.message + endLine;
                case ERROR:
                    StringBuilder sb = new StringBuilder();
                    sb.append(errorMsg).append(event.message).append(endLine);
                    if(event.error != null){
                        for(StackTraceElement element : event.error.getStackTrace()){
                            sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(element.toString());
                            sb.append("<br>");
                        }
                        sb.append("<hr><br>");
                    }
                    //todo change icon
                    return sb.toString();
            }
        } else {
            return simpleMsg + msg.toString() + endLine;
        }

        return "?";
    }
}
