package com.zh.coherence.viewer.tools.query.engine;

import com.tangosol.coherence.dslquery.PropertyBuilder;
import com.tangosol.util.Filter;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.AndFilter;
import com.tangosol.util.filter.BetweenFilter;
import com.tangosol.util.filter.OrFilter;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

public class WhereFilterBuilder implements ExpressionVisitor {

    private Filter filter = null;

    public Filter buildFilter(Expression expression){
        expression.accept(this);

        return filter;
    }

    private Object object;

    public Object getValue(Expression expression){
        expression.accept(this);

        return object;
    }

    @Override
    public void visit(NullValue nullValue) {
    }

    @Override
    public void visit(Function function) {
    }

    @Override
    public void visit(InverseExpression inverseExpression) {
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {
    }

    @Override
    public void visit(DoubleValue doubleValue) {
        object = Double.valueOf(doubleValue.getValue());
    }

    @Override
    public void visit(LongValue longValue) {
        object = Long.valueOf(longValue.getValue());
    }

    @Override
    public void visit(DateValue dateValue) {
    }

    @Override
    public void visit(TimeValue timeValue) {
    }

    @Override
    public void visit(TimestampValue timestampValue) {
    }

    @Override
    public void visit(Parenthesis parenthesis) {
    }

    @Override
    public void visit(StringValue stringValue) {
        object = stringValue.getValue();
    }

    @Override
    public void visit(Addition addition) {
    }

    @Override
    public void visit(Division division) {
    }

    @Override
    public void visit(Multiplication multiplication) {
    }

    @Override
    public void visit(Subtraction subtraction) {
    }

    @Override
    public void visit(AndExpression andExpression) {
        WhereFilterBuilder leftBuilder = new WhereFilterBuilder();
        WhereFilterBuilder rightBuilder = new WhereFilterBuilder();
        filter = new AndFilter(
                leftBuilder.buildFilter(andExpression.getLeftExpression()),
                rightBuilder.buildFilter(andExpression.getRightExpression()));
    }

    @Override
    public void visit(OrExpression orExpression) {
        WhereFilterBuilder leftBuilder = new WhereFilterBuilder();
        WhereFilterBuilder rightBuilder = new WhereFilterBuilder();
        filter = new OrFilter(
                leftBuilder.buildFilter(orExpression.getLeftExpression()),
                rightBuilder.buildFilter(orExpression.getRightExpression()));
    }

    @Override
    public void visit(Between between) {
        String left = (String) getValue(between.getLeftExpression());
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        ReflectionExtractor ref = new ReflectionExtractor(propertyBuilder.extractorStringFor(left));

        Object start = getValue(between.getBetweenExpressionStart());

        Object end = getValue(between.getBetweenExpressionEnd());

        filter = new BetweenFilter(ref, (Comparable)start, (Comparable) end);

    }

    @Override
    public void visit(EqualsTo equalsTo) {
    }

    @Override
    public void visit(GreaterThan greaterThan) {
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
    }

    @Override
    public void visit(InExpression inExpression) {
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
    }

    @Override
    public void visit(LikeExpression likeExpression) {
    }

    @Override
    public void visit(MinorThan minorThan) {
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
    }

    @Override
    public void visit(Column column) {
        object = column.getWholeColumnName();
    }

    @Override
    public void visit(SubSelect subSelect) {
    }

    @Override
    public void visit(CaseExpression caseExpression) {
    }

    @Override
    public void visit(WhenClause whenClause) {
    }

    @Override
    public void visit(ExistsExpression existsExpression) {
    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {
    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {
    }

    @Override
    public void visit(Concat concat) {
    }

    @Override
    public void visit(Matches matches) {
    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {
    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {
    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {
    }
}
