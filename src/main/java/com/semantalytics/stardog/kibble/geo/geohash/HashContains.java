package com.semantalytics.stardog.kibble.geo.geohash;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.UserDefinedFunction;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.literal;
import static com.github.davidmoten.geo.GeoHash.hashContains;

public final class HashContains extends AbstractFunction implements UserDefinedFunction {

    protected HashContains() {
        super(3, GeoHashVocabulary.hashContains.stringValue());
    }

    private HashContains(final HashContains hashContains) {
        super(hashContains);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final String hash = assertStringLiteral(values[0]).stringValue();
        final double latitude = assertNumericLiteral(values[1]).doubleValue();
        final double longitude = assertNumericLiteral(values[2]).doubleValue();

        return literal(hashContains(hash, latitude, longitude));
    }

    @Override
    public HashContains copy() {
        return new HashContains(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return GeoHashVocabulary.hashContains.name();
    }

}
