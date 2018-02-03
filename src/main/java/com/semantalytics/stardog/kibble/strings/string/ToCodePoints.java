package com.semantalytics.stardog.kibble.strings.string;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.string.StringFunction;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.*;

public final class ToCodePoints extends AbstractFunction implements StringFunction {

    protected ToCodePoints() {
        super(2, StringVocabulary.toCodePoints.stringValue());
    }

    private ToCodePoints(final ToCodePoints toCodePoints) {
        super(toCodePoints);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final String string = assertStringLiteral(values[0]).stringValue();

        return literal(Joiner.on("\u001f").join(StringUtils.toCodePoints(string)));
    }

    @Override
    public ToCodePoints copy() {
        return new ToCodePoints(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return StringVocabulary.toCodePoints.name();
    }
}
