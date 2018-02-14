package com.semantalytics.stardog.kibble.strings.string;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.string.StringFunction;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.literal;

public final class ArraySeparator extends AbstractFunction implements StringFunction {

    protected ArraySeparator() {
        super(0, StringVocabulary.arraySeparator.stringValue());
    }

    private ArraySeparator(final ArraySeparator arraySeparator) {
        super(arraySeparator);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {
      
        return literal("\u001f");
    }

    @Override
    public ArraySeparator copy() {
        return new ArraySeparator(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return StringVocabulary.arraySeparator.name();
    }
}