package com.zh.coherence.viewer.tools.query;

public interface QueryEngine {
    /**
     * returns java source of query
     * @param query coh QL query
     * @return java source of query
     */
    public String getJavaSource(String query);

    public String getPlan(String query);

    /**
     * executes query
     * @param query coh QL query
     * @return result of query
     */
    public QueryResult execute(String query);

    public Object getBackend();
}
