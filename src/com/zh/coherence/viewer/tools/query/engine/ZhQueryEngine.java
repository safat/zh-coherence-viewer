package com.zh.coherence.viewer.tools.query.engine;

import com.zh.coherence.viewer.tools.query.QueryEngine;
import com.zh.coherence.viewer.tools.query.QueryResult;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

import java.io.Reader;
import java.io.StringReader;

public class ZhQueryEngine implements QueryEngine{
    CCJSqlParserManager manager = new CCJSqlParserManager();

    @Override
    public String getJavaSource(String query) {
        return null;
    }

    @Override
    public String getPlan(String query) {
        return null;
    }

    @Override
    public QueryResult execute(String query) {
        Reader reader = new StringReader(query);

        try {
            Statement statement = manager.parse(reader);
            System.err.println("statement: " + statement);
            if(statement instanceof Select){
                Select sel = (Select) statement;
                SelectProcessor processor = new SelectProcessor();
                sel.getSelectBody().accept(processor);
            }

        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

        return null;
    }
}
