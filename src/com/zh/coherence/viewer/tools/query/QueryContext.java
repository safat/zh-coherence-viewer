package com.zh.coherence.viewer.tools.query;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 09.04.12
 * Time: 22:39
 */
public class QueryContext {
    public static final String TABLE_VIEW = "table-view";
    public static final String TEXT_VIEW = "text-view";
    public static final String EVENT_LOG = "event-log";
    public static final String NO_DATA = "no-data";
    public static final String ERROR = "error";

    private QueryTool queryTool;
    private String currentOutputTool = TABLE_VIEW;

    public QueryContext(QueryTool queryTool) {
        this.queryTool = queryTool;
    }

    public void setBusy(boolean busy){
        queryTool.getStatusBar().setBusy(busy);
    }

    public QueryTool getQueryTool() {
        return queryTool;
    }

    public void setTime(long time){
        queryTool.getStatusBar().setTime(time);
    }

    public void showOutputPane(String name){
        queryTool.getOutputCardLayout().show(queryTool.getOutput(), name);
    }

    public void logEvent(QueryLogEvent event){
        queryTool.getEventLogPane().addMessage(event);
    }

    public void showShortMessage(String msg){
        queryTool.getStatusBar().showShortMessage(msg);
    }

    public void setSize(int size){
        queryTool.getStatusBar().setRowsSize(size);
    }

    public String getCurrentOutputTool() {
        return currentOutputTool;
    }

    public void setCurrentOutputTool(String currentOutputTool) {
        this.currentOutputTool = currentOutputTool;
    }
}
