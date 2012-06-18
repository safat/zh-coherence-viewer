package com.zh.coherence.viewer.tools.query.engine;

import com.tangosol.coherence.dslquery.CoherenceQuery;
import com.tangosol.coherence.dslquery.CoherenceQueryLanguage;
import com.tangosol.coherence.dslquery.SQLOPParser;
import com.tangosol.coherence.dsltools.precedence.TokenTable;
import com.tangosol.coherence.dsltools.termtrees.NodeTerm;
import com.tangosol.coherence.dsltools.termtrees.Term;
import com.tangosol.util.Filter;
import com.tangosol.util.filter.LimitFilter;
import com.zh.coherence.viewer.tools.query.QueryEngine;
import com.zh.coherence.viewer.tools.query.QueryResult;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class OracleCoherenceQueryEngine implements QueryEngine {

    @Override
    public String getJavaSource(String query) {
        return null;
    }

    @Override
    public String getPlan(String query) {
        return null;
    }

    @Override
    public QueryResult execute(String script) {
        QueryResult result = new QueryResult();
        TokenTable tokenTable = CoherenceQueryLanguage.getSqlTokenTable(true);

        try {
            Pattern pattern = Pattern.compile("^select top \\d*");

            Integer topLimiter;
            if (pattern.matcher(script.toLowerCase()).find()) {
                int start = script.indexOf("top");
                start = script.indexOf(" ", start);
                int stop = script.indexOf(" ", start + 1);
                String top = script.substring(start, stop);
                topLimiter = Integer.parseInt(top.trim());
                result.setTop(topLimiter);
                script = script.replaceFirst("top \\d* ", "");
            } else {
                topLimiter = null;
            }

            SQLOPParser parser = new SQLOPParser(script, tokenTable);
            Term term;
            boolean buildResult;
            CoherenceQuery query;
            term = parser.parse();
            result.setTerm(term);
            query = new CoherenceQuery(true);
            buildResult = query.build((NodeTerm) term);

            if (!buildResult) {
                throw new RuntimeException(query.getErrorString());
            }

            if (topLimiter != null) {
                Field field = query.getClass().getDeclaredField("m_filter");
                field.setAccessible(true);
                Filter filter = (Filter) field.get(query);
                LimitFilter limitFilter = new LimitFilter(filter, topLimiter);
                field.set(query, limitFilter);
            }

            try {
                result.setResult(query.execute());
            } catch (Exception ex) {
                result.setError(ex, "Executing exception");
            }
        } catch (Exception ex) {
            result.setEndTime(System.currentTimeMillis());
            result.setError(ex, "Parsing exception");
        }

        result.setEndTime(System.currentTimeMillis());
        return result;
    }
}
