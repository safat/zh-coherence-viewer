package com.zh.coherence.viewer.tools.query.actions;

import com.tangosol.coherence.dslquery.CoherenceQuery;
import com.tangosol.coherence.dslquery.CoherenceQueryLanguage;
import com.tangosol.coherence.dslquery.SQLOPParser;
import com.tangosol.coherence.dsltools.precedence.TokenTable;
import com.tangosol.coherence.dsltools.termtrees.NodeTerm;
import com.tangosol.coherence.dsltools.termtrees.Term;
import com.zh.coherence.viewer.tools.query.QueryTool;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 15.02.12
 * Time: 21:21
 */
public class CqlScriptExecutor {
    private QueryTool queryTool;

    public CqlScriptExecutor(QueryTool queryTool) {
        this.queryTool = queryTool;
    }

    public void execute(){
        TokenTable toks = CoherenceQueryLanguage.getSqlTokenTable(true);
        SQLOPParser p = new SQLOPParser(queryTool.getScript(), toks);

        Term tn;
        try{
            tn = p.parse();
        }catch (Exception ex){
            queryTool.traceText("Parsing exception");
            queryTool.traceText(ex.getMessage() + "\n");

            return;
        }

        CoherenceQuery query = new CoherenceQuery(true);
        PrintWriter printWriter = new PrintWriter(System.out);
        if (query.build((NodeTerm)tn)){
            query.showPlan(printWriter);
        }
        Object ret = null;
        try{
            ret =query.execute(printWriter, true);
        }catch (Exception ex){
            queryTool.traceText("Executing exception");
            queryTool.traceText(ex.getMessage() + "\n");
        }
        if(ret == null){
        }else if(ret instanceof Map){
            int size = checkSize(((Map)ret).size());
            if(size == 0){
                queryTool.traceText("result is Map with size: 0");
            }else{
                queryTool.showResult(ret, tn, size);
            }
        }else if (ret instanceof Collection){
            int size = checkSize(((Collection)ret).size());
            if(size == 0){
                queryTool.traceText("result is Collection with size: 0");
            }else{
                queryTool.showResult(ret, tn, size);
            }
        }else if(ret instanceof Integer || ret instanceof String){
            queryTool.showResult(ret, tn, 1);
        }else{
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
        printWriter.flush();
    }

    private int checkSize(int size){
        if(size > 1000){
            String res = JOptionPane.showInputDialog("<html>Size of result more then 1000.<br>The application can be crushed.<br>" +
                    "Input number of lines and click the 'Ok' button to cut output data<br>or click the 'Cancel' button to ignore this message</html>", 1000);
            if(res != null){
                try{
                    return Integer.parseInt(res);
                }catch(NumberFormatException ex){
                    //todo send message to console
                    ex.printStackTrace();
                }
            }
        }
        return size;
    }
}
