package com.zh.coherence.viewer.tools.backup;

import com.zh.coherence.viewer.eventlog.EventLogRenderer;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BackupLogRenderer implements EventLogRenderer {
    private final String header = "<div width='100%'><div width='100%' style='background: #CEE2D3;'>";

    public String getCssStyle(){
        return "";
    }

    @Override
    public String renderMessage(Object msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(header);
        if(msg instanceof BackupLogEvent){
            BackupLogEvent event = (BackupLogEvent) msg;
            sb.append("<table width='100%'><tr><td width='100%'><b>").append(event.getAction()).append("</b> ")
                    .append(event.getCacheName()).append(" <b>time:</b> ")
                    .append(getTime(event))
                    .append("</td><td width='200px'>")
                    .append(DateFormat.getDateTimeInstance().format(new Date(event.getEndTime())))
                    .append("</td></tr></table>");
            sb.append("</div><div>&nbsp;&nbsp;&nbsp;")
                    .append(event.getMessage())
                    .append("</div></div><br>");
        }else{
            sb.append(msg);
        }

        return sb.toString();
    }

    private String getTime(BackupLogEvent event){
        long millis = event.getEndTime() - event.getStartTime();
        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
}
