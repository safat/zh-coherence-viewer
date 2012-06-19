//package com.zh.coherence.viewer.tools.query.parser;
//
//import net.sf.jsqlparser.parser.CCJSqlParserManager;
//import net.sf.jsqlparser.statement.Statement;
//import net.sf.jsqlparser.statement.select.*;
//
//import java.io.Reader;
//import java.io.StringReader;
//
///**
// * Created by IntelliJ IDEA.
// * User: Живко
// * Date: 06.06.12
// * Time: 22:45
// */
//public class Test implements SelectVisitor {
//    public static void main(String[] args) {
//        try {
//            new Test();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Test() throws Exception {
//        CCJSqlParserManager manager = new CCJSqlParserManager();
//        Reader reader = new StringReader("select count(*) as c from \"cache0\" where id = 'test'");
//        Statement statement = manager.parse(reader);
//        System.err.println("statement: " + statement);
//        if(statement instanceof Select){
//            Select sel = (Select) statement;
//            sel.getSelectBody().accept(this);
//        }
//    }
//
//    @Override
//    public void visit(PlainSelect plainSelect) {
//        System.err.println("visit select: " + plainSelect);
//        System.err.println("plainSelect.getSelectItems(): " + plainSelect.getSelectItems());
//        SelectExpressionItem exp = (SelectExpressionItem) plainSelect.getSelectItems().get(0);
////        exp.getExpression().accept(new EV());
////        System.err.println("exp: " + );
//        System.err.println("exp: " + exp.getAlias());
//    }
//
//    @Override
//    public void visit(Union union) {
//        System.err.println("visit union: " + union);
//    }
//}
