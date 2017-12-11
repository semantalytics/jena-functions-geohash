package com.semantalytics.stardog.kibble.strings.string;

import com.complexible.stardog.plan.filter.ExpressionEvaluationException;
import com.complexible.stardog.plan.filter.ExpressionVisitor;
import com.complexible.stardog.plan.filter.functions.AbstractFunction;
import com.complexible.stardog.plan.filter.functions.string.StringFunction;
import org.apache.commons.lang3.StringUtils;
import org.openrdf.model.Value;

import static com.complexible.common.rdf.model.Values.*;

public class IndexOfIgnoreCase extends AbstractFunction implements StringFunction {

    protected IndexOfIgnoreCase() {
        super(1, StringVocabulary.indexOfIgnoreCase.toString());
    }

    private IndexOfIgnoreCase(final IndexOfIgnoreCase indexOfIgnoreCase) {
        super(indexOfIgnoreCase);
    }

    @Override
    protected Value internalEvaluate(final Value... values) throws ExpressionEvaluationException {

        final String string = assertStringLiteral(values[0]).stringValue();
        final String searchChars = assertStringLiteral(values[1]).stringValue();

        //TODO handle multiple searchchars

        return literal(StringUtils.indexOfIgnoreCase(string, searchChars));
    }

    @Override
    public IndexOfIgnoreCase copy() {
        return new IndexOfIgnoreCase(this);
    }

    @Override
    public void accept(final ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return StringVocabulary.indexOfIgnoreCase.name();
    }
}