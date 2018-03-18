package com.semantalytics.stardog.kibble.geo.geohash;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.UserDefinedFunction;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.literal;
import static com.github.davidmoten.geo.GeoHash.*;

public final class HeightDegrees extends AbstractFunction implements UserDefinedFunction {

    protected HeightDegrees() {
        super(1, GeoHashVocabulary.heightDegrees.stringValue());
    }

    private HeightDegrees(final HeightDegrees heightDegrees) {
        super(heightDegrees);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final int length = assertNumericLiteral(values[0]).intValue();

        return literal(heightDegrees(length));
    }

    @Override
    public HeightDegrees copy() {
        return new HeightDegrees(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return GeoHashVocabulary.heightDegrees.name();
    }

}
