//package com.zh.coherence.viewer.tools.query.engine;
//
//import com.tangosol.util.Filter;
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.statement.select.PlainSelect;
//import net.sf.jsqlparser.statement.select.SelectVisitor;
//import net.sf.jsqlparser.statement.select.Union;
//
//public class SelectProcessor implements SelectVisitor {
//    @Override
//    public void visit(PlainSelect plainSelect) {
//        System.err.println("visit select: " + plainSelect);
//
//        Filter whereFilter;
//        Expression where = plainSelect.getWhere();
//        if(where != null){
//            WhereFilterBuilder filterBuilder = new WhereFilterBuilder();
//            whereFilter = filterBuilder.buildFilter(where);
//            System.err.println("whereFilter: " + whereFilter);
//        }
//    }
//
//    @Override
//    public void visit(Union union) {
//    }
//}
