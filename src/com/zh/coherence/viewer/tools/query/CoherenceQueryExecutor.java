package com.zh.coherence.viewer.tools.query;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 09.06.12
 * Time: 12:07
 */
public interface CoherenceQueryExecutor {

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
}
