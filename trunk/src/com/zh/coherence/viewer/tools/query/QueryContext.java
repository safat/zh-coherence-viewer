package com.zh.coherence.viewer.tools.query;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 09.04.12
 * Time: 22:39
 */
public class QueryContext {
    private QueryTool queryTool;

    public QueryContext(QueryTool queryTool) {
        this.queryTool = queryTool;
    }

    public void setBusy(boolean busy){
        queryTool.getStatusBar().setBusy(busy);
    }

    public QueryTool getQueryTool() {
        return queryTool;
    }

    public void setQueryTool(QueryTool queryTool) {
        this.queryTool = queryTool;
    }

    public void setTime(long time){
        queryTool.getStatusBar().setTime(time);
    }
}
