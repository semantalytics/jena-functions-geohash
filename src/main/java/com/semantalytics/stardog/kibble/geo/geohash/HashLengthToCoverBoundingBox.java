package com.semantalytics.stardog.kibble.geo.geohash;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.UserDefinedFunction;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.literal;
import static com.github.davidmoten.geo.GeoHash.hashLengthToCoverBoundingBox;

public final class HashLengthToCoverBoundingBox extends AbstractFunction implements UserDefinedFunction {

    protected HashLengthToCoverBoundingBox() {
        super(4, GeoHashVocabulary.hashLengthToCoverBoundingBox.stringValue());
    }

    private HashLengthToCoverBoundingBox(final HashLengthToCoverBoundingBox hashLengthToCoverBoundingBox) {
        super(hashLengthToCoverBoundingBox);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final double topLeftLatitude = assertNumericLiteral(values[0]).doubleValue();
        final double topLeftLongitude = assertNumericLiteral(values[1]).doubleValue();
        final double bottomRightLatitude = assertNumericLiteral(values[2]).doubleValue();
        final double bottomRightLongitude = assertNumericLiteral(values[3]).doubleValue();

        return literal(hashLengthToCoverBoundingBox(topLeftLatitude, topLeftLongitude, bottomRightLatitude, bottomRightLongitude));
    }

    @Override
    public HashLengthToCoverBoundingBox copy() {
        return new HashLengthToCoverBoundingBox(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return GeoHashVocabulary.hashLengthToCoverBoundingBox.name();
    }

}
