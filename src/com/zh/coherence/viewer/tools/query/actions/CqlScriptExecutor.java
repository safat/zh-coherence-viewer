package com.zh.coherence.viewer.tools.query.actions;

import com.tangosol.coherence.dslquery.CoherenceQuery;
import com.tangosol.coherence.dslquery.CoherenceQueryLanguage;
import com.tangosol.coherence.dslquery.SQLOPParser;
import com.tangosol.coherence.dsltools.precedence.TokenTable;
import com.tangosol.coherence.dsltools.termtrees.NodeTerm;
import com.tangosol.coherence.dsltools.termtrees.Term;
import com.tangosol.util.Filter;
import com.tangosol.util.filter.LimitFilter;
import com.zh.coherence.viewer.tools.query.QueryTool;

import javax.swing.*;
import java.io.IOException;
import java.io.Writer;
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
    private QueryTool queryTool;
    private Integer topLimiter = null;

    public CqlScriptExecutor(QueryTool queryTool) {
        this.queryTool = queryTool;
    }

    public void execute() {
        TokenTable toks = CoherenceQueryLanguage.getSqlTokenTable(true);
        String script = queryTool.getScript();
        queryTool.getHistory().add(script);

        Pattern pattern = Pattern.compile("^select top \\d*");

        if(pattern.matcher(script).find()){
            int start = script.indexOf("top");
            start = script.indexOf(" ", start);
            int  stop = script.indexOf(" ", start + 1);
            String top = script.substring(start, stop);
            topLimiter = Integer.parseInt(top.trim());

            script = script.replaceFirst("top \\d* ", "");
        }else {
            topLimiter = null;
        }

        SQLOPParser p = new SQLOPParser(script, toks);

        Term tn;
        try {
            tn = p.parse();
        } catch (Exception ex) {
            queryTool.traceText("Parsing exception");
            queryTool.traceText(ex.getMessage() + "\n");

            return;
        }

        long time = System.currentTimeMillis();
        CoherenceQuery query = new CoherenceQuery(true);
        boolean buildResult = query.build((NodeTerm) tn);
        if(!buildResult){
            return;
        }

        if(topLimiter != null){
            try {
                Field field = query.getClass().getDeclaredField("m_filter");
                field.setAccessible(true);
                Filter filter = (Filter) field.get(query);
                LimitFilter limitFilter = new LimitFilter(filter, topLimiter);
                field.set(query, limitFilter);
            } catch (Exception e) {
                queryTool.traceText(e.getMessage());
            }
        }


        Object ret = null;
        try {
            ret = query.execute();
        } catch (Exception ex) {
            queryTool.traceText("Executing exception");
            queryTool.traceText(ex.getMessage() + "\n");
        }
        if (ret == null) {
        } else if (ret instanceof Map) {
            int size = checkSize(((Map) ret).size());
            if (size == 0) {
                queryTool.traceText("result is Map with size: 0");
            } else {
                queryTool.showResult(ret, tn, size);
            }
        } else if (ret instanceof Collection) {
            int size = checkSize(((Collection) ret).size());
            if (size == 0) {
                queryTool.traceText("result is Collection with size: 0");
            } else {
                queryTool.showResult(ret, tn, size);
            }
        } else if (ret instanceof Integer || ret instanceof String) {
            queryTool.showResult(ret, tn, 1);
        } else {
            System.err.println("class: " + ret.getClass());
            try {
                Writer writer = queryTool.getConsolePrintWriter();
                System.err.println("class: " + ret);
                writer.append(ret.toString());
                writer.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        System.err.println("Time: " + (System.currentTimeMillis() - time));
    }

    private int checkSize(int size) {
        if (size > 500) {
            String res = JOptionPane.showInputDialog("<html>Size of result more then 100.<br>The application can be crushed.<br>" +
                    "Input number of lines and click the 'Ok' button to cut output data<br>or click the 'Cancel' button to ignore this message</html>", 500);
            if (res != null) {
                try {
                    return Integer.parseInt(res);
                } catch (NumberFormatException ex) {
                    //todo send message to console
                    ex.printStackTrace();
                }
            }
        }
        return size;
    }
}
