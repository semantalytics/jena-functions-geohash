package com.semantalytics.stardog.kibble.geo.geohash;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.UserDefinedFunction;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.*;
import static com.github.davidmoten.geo.GeoHash.*;

public final class Top extends AbstractFunction implements UserDefinedFunction {

    protected Top() {
        super(1, GeoHashVocabulary.top.stringValue());
    }

    private Top(final Top top) {
        super(top);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final String hash = assertStringLiteral(values[0]).stringValue();

        return literal(top(hash));
    }

    @Override
    public Top copy() {
        return new Top(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return GeoHashVocabulary.top.name();
    }

}
