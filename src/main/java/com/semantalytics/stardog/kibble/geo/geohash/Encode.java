package com.semantalytics.stardog.kibble.geo.geohash;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.Function;
import com.complexible.stardog.plan.filter.functions.UserDefinedFunction;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.*;
import static com.github.davidmoten.geo.GeoHash.*;

public final class Encode extends AbstractFunction implements UserDefinedFunction {

    protected Encode() {
        super(2, GeoHashVocabulary.encode.stringValue());
    }

    private Encode(final Encode encode) {
        super(encode);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final double latitude = assertNumericLiteral(values[0]).doubleValue();
        final double longitude = assertNumericLiteral(values[1]).doubleValue();

        return literal(encodeHash(latitude, longitude));
    }

    @Override
    public Function copy() {
        return new Encode(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return GeoHashVocabulary.encode.name();
    }

}
