package com.zh.coherence.viewer.tools.query.actions;

import com.tangosol.coherence.dslquery.CoherenceQuery;
import com.tangosol.coherence.dslquery.CoherenceQueryLanguage;
import com.tangosol.coherence.dslquery.SQLOPParser;
import com.tangosol.coherence.dsltools.precedence.TokenTable;
import com.tangosol.coherence.dsltools.termtrees.NodeTerm;
import com.tangosol.coherence.dsltools.termtrees.Term;
import com.tangosol.util.Filter;
import com.tangosol.util.filter.LimitFilter;
import com.zh.coherence.viewer.tools.query.QueryContext;
import com.zh.coherence.viewer.tools.query.QueryLogEvent;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 15.02.12
 * Time: 21:21
 */
public class CqlScriptExecutor {
    QueryContext context;
    private Integer topLimiter = null;

    public CqlScriptExecutor(QueryContext context) {
        this.context = context;
    }

    public void execute() {
        TokenTable toks = CoherenceQueryLanguage.getSqlTokenTable(true);
        String script = context.getQueryTool().getScript();
        context.getQueryTool().getHistory().add(script);

        try {
            Pattern pattern = Pattern.compile("^select top \\d*");

            if (pattern.matcher(script.toLowerCase()).find()) {
                int start = script.indexOf("top");
                start = script.indexOf(" ", start);
                int stop = script.indexOf(" ", start + 1);
                String top = script.substring(start, stop);
                topLimiter = Integer.parseInt(top.trim());

                script = script.replaceFirst("top \\d* ", "");
            } else {
                topLimiter = null;
            }
        } catch (Exception ex) {
            context.showShortMessage("Parsing exception");
            context.showOutputPane(QueryContext.ERROR);
            context.logEvent(new QueryLogEvent(ex, ex.getMessage(), QueryLogEvent.EventType.ERROR));
        }
        SQLOPParser p = new SQLOPParser(script, toks);

        Term tn;
        boolean buildResult = false;
        CoherenceQuery query = null;
        try {
            tn = p.parse();
            query = new CoherenceQuery(true);
            buildResult = query.build((NodeTerm) tn);
        } catch (Exception ex) {
            context.showShortMessage("Parsing exception");
            context.showOutputPane(QueryContext.ERROR);
            context.logEvent(new QueryLogEvent(ex, ex.getMessage(), QueryLogEvent.EventType.ERROR));
            return;
        }

        if (!buildResult) {
            context.showShortMessage(query.getErrorString());
            context.logEvent(new QueryLogEvent(null, query.getErrorString(), QueryLogEvent.EventType.ERROR));
            context.showOutputPane(QueryContext.ERROR);
            return;
        }

        if (topLimiter != null) {
            try {
                Field field = query.getClass().getDeclaredField("m_filter");
                field.setAccessible(true);
                Filter filter = (Filter) field.get(query);
                LimitFilter limitFilter = new LimitFilter(filter, topLimiter);
                field.set(query, limitFilter);
            } catch (Exception ex) {
                context.showOutputPane(QueryContext.ERROR);
                context.showShortMessage("Parsing exception");
                context.logEvent(new QueryLogEvent(ex, ex.getMessage(), QueryLogEvent.EventType.ERROR));
            }
        }

        Object ret = null;
        try {
            ret = query.execute();
        } catch (Exception ex) {
            context.showShortMessage("Executing exception");
            context.showOutputPane(QueryContext.ERROR);
            context.logEvent(new QueryLogEvent(ex, ex.getMessage(), QueryLogEvent.EventType.ERROR));
        }
        if (ret == null) {
            context.showOutputPane(QueryContext.NO_DATA);
            context.showShortMessage("OK");
        } else if (ret instanceof Map) {
            int size = ((Map) ret).size();
            if (size == 0) {
                context.showOutputPane(QueryContext.NO_DATA);
            } else {
                context.showOutputPane(QueryContext.TABLE_VIEW);
                context.getQueryTool().showResult(ret, tn, size);
            }
            context.showShortMessage("OK");
        } else if (ret instanceof Collection) {
            int size = ((Collection) ret).size();
            if (size == 0) {
                context.showOutputPane(QueryContext.NO_DATA);
            } else {
                context.showOutputPane(QueryContext.TABLE_VIEW);
                context.getQueryTool().showResult(ret, tn, size);
            }
            context.showShortMessage("OK");
        } else if (ret instanceof Number || ret instanceof String) {
            context.showOutputPane(QueryContext.TABLE_VIEW);
            context.getQueryTool().showResult(ret, tn, 1);
            context.showShortMessage("OK");
        } else {
            context.showOutputPane(QueryContext.ERROR);
            context.logEvent(new QueryLogEvent(null, "unknown class: " + ret.getClass(), QueryLogEvent.EventType.MESSAGE));
        }
    }
}
