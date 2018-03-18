package com.semantalytics.stardog.kibble.geo.geohash;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.UserDefinedFunction;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.*;
import static com.github.davidmoten.geo.GeoHash.decodeHash;

public final class Longitude extends AbstractFunction implements UserDefinedFunction {

    protected Longitude() {
        super(1, GeoHashVocabulary.longitude.iri.stringValue());
    }

    private Longitude(final Longitude longitude) {
        super(longitude);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final String hash = assertStringLiteral(values[0]).stringValue();

        return literal(decodeHash(hash).getLon());
    }

    @Override
    public Longitude copy() {
        return new Longitude(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return GeoHashVocabulary.longitude.name();
    }

}
