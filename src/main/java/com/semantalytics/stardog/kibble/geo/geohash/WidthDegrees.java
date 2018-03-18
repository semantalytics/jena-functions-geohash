package com.semantalytics.stardog.kibble.geo.geohash;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.UserDefinedFunction;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.literal;
import static com.github.davidmoten.geo.GeoHash.widthDegrees;

public final class WidthDegrees extends AbstractFunction implements UserDefinedFunction {

    protected WidthDegrees() {
        super(1, GeoHashVocabulary.widthDegrees.stringValue());
    }

    private WidthDegrees(final WidthDegrees widthDegrees) {
        super(widthDegrees);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final int length = assertNumericLiteral(values[0]).intValue();

        return literal(widthDegrees(length));
    }

    @Override
    public WidthDegrees copy() {
        return new WidthDegrees(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return GeoHashVocabulary.widthDegrees.name();
    }

}
