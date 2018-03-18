package com.semantalytics.stardog.kibble.geo.geohash;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.UserDefinedFunction;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.*;
import static com.github.davidmoten.geo.GeoHash.*;

public final class Latitude extends AbstractFunction implements UserDefinedFunction {

    protected Latitude() {
        super(1, GeoHashVocabulary.latitude.iri.stringValue());
    }

    private Latitude(final Latitude latitude) {
        super(latitude);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final String hash = assertStringLiteral(values[0]).stringValue();

        return literal(decodeHash(hash).getLat());
    }

    @Override
    public Latitude copy() {
        return new Latitude(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return GeoHashVocabulary.latitude.name();
    }

}
