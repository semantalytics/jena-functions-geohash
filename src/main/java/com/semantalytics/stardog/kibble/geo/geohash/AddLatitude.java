package com.semantalytics.stardog.kibble.geo.geohash;

import com.complexible.common.rdf.model.Values;
import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.UserDefinedFunction;
import com.github.davidmoten.geo.GeoHash;
import org.openrdf.model.Value;

public final class AddLatitude extends AbstractFunction implements UserDefinedFunction {

    protected AddLatitude() {
        super(1, GeoHashVocabulary.decode.iri.stringValue());
    }

    private AddLatitude(final AddLatitude addLatitude) {
        super(addLatitude);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {
        final String hash = assertLiteral(values[0]).stringValue();
        final double latitude = assertNumericLiteral(values[1]).doubleValue();
        final double longitude = assertNumericLiteral(values[2]).doubleValue();

        return Values.literal(GeoHash.decodeHash(hash).add(latitude, longitude).getLat());
    }

    @Override
    public AddLatitude copy() {
        return new AddLatitude(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return GeoHashVocabulary.decode.name();
    }

}
