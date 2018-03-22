package com.semantalytics.stardog.kibble.geo.geohash;

import java.util.List;
import java.util.stream.Collectors;

import com.complexible.common.openrdf.query.BindingSets;
import com.complexible.common.rdf.model.StardogValueFactory;
import com.complexible.stardog.plan.eval.ExecutionException;
import com.google.common.collect.Lists;
import com.semantalytics.stardog.kibble.AbstractStardogTest;
import info.aduna.iteration.Iterations;
import org.junit.Test;
import org.openrdf.model.Literal;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;

import static com.complexible.common.rdf.model.Values.literal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestDecode extends AbstractStardogTest {

    private final String sparqlPrefix = GeoHashVocabulary.sparqlPrefix("geohash");

    @Test(expected = ExecutionException.class)
    public void tooManyResultsThrowsError() {

        final String aQueryStr = sparqlPrefix +
                " select * where { (?too ?many ?args) geohash:decode (\"gbsuv7ztqzpt\") }";

        final TupleQueryResult aResult = connection.select(aQueryStr).execute();
        try {
            fail("Should not have successfully executed");
        } finally {
            aResult.close();
        }
    }


    @Test(expected = ExecutionException.class)
    public void resultTermsWhichAreNotVariablesShouldBeAnError() {

        final String aQueryStr = sparqlPrefix +
                " select * where { (\"no literals allowed\") geohash:decode (\"gbsuv7ztqzpt\") }";

        final TupleQueryResult aResult = connection.select(aQueryStr).execute();
        try {
            fail("Should not have successfully executed");
        } finally {
            aResult.close();
        }
    }

    @Test(expected = ExecutionException.class)
    public void tooManyInputsThrowsError() {
        final String aQueryStr = sparqlPrefix +
                " select * where { ?result geohash:decode (\"gbsuv7ztqzpt\" 5) }";


        final TupleQueryResult aResult = connection.select(aQueryStr).execute();

        try {
            fail("Should not have successfully executed");
        } finally {
            aResult.close();
        }
    }

    @Test(expected = ExecutionException.class)
    public void argCannotBeANonnumericLiteral() {
        final String aQueryStr = sparqlPrefix +
                " select * where { ?result geohash:decode (5) }";


        final TupleQueryResult aResult = connection.select(aQueryStr).execute();
        try {
            fail("Should not have successfully executed");
        } finally {
            aResult.close();
        }
    }

    @Test(expected = ExecutionException.class)
    public void argCannotBeAnIRI() {
        final String aQueryStr = sparqlPrefix +
                " select * where { ?result geohash:decode (<http://example.com>) }";


        final TupleQueryResult aResult = connection.select(aQueryStr).execute();
        try {
            fail("Should not have successfully executed");
        } finally {
            aResult.close();
        }
    }

    @Test
    public void argCannotBeABNode() {
        final String aQueryStr = sparqlPrefix +
                " select * where { ?result geohash:decode (_:bnode) }";

        try(final TupleQueryResult aResult = connection.select(aQueryStr).execute()) {
            assertFalse("Should have no more results", aResult.hasNext());
        }
    }

    @Test
    public void varInputWithNoResultsShouldProduceZeroResults() {
        final String aQueryStr = sparqlPrefix +
                " select * where { ?result geohash:decode (?input) }";


        final TupleQueryResult aResult = connection.select(aQueryStr).execute();
        try {
            assertFalse(aResult.hasNext());
        } finally {
            aResult.close();
        }
    }

    @Test
    public void decodeWithVarInput() {
        final String aQueryStr = sparqlPrefix +
                " select * where { (?latitude ?longitude) geohash:decode (?in) . values ?in { \"gbsuv7ztqzpt\"} }";

        BindingSet aBindingSet;

        try(final TupleQueryResult aResult = connection.select(aQueryStr).execute()) {

            aBindingSet = aResult.next();

            assertEquals(literal("star"), aBindingSet.getValue("result"));
            assertEquals(literal(0, StardogValueFactory.Datatype.INTEGER), aBindingSet.getValue("idx"));

            //aBindingSet = aResult.next();

            //assertEquals(literal("dog"), aBindingSet.getValue("result"));
            //assertEquals(literal(1, StardogValueFactory.Datatype.INTEGER), aBindingSet.getValue("idx"));

            assertFalse("Should have no more results", aResult.hasNext());
        }
    }

    @Test
    public void decodeWithConstInput() {
        final String aQueryStr = sparqlPrefix +
                " select * where { (?latitude ?longitude) geohash:decode (\"gbsuv7ztqzpt\") }";

        BindingSet aBindingSet;

        try(final TupleQueryResult aResult = connection.select(aQueryStr).execute()) {

            aBindingSet = aResult.next();

            assertEquals(StardogValueFactory.Datatype.DOUBLE.iri(), ((Literal)aBindingSet.getValue("latitude")).getDatatype());
            assertEquals(48.669, ((Literal)aBindingSet.getValue("latitude")).doubleValue(), 0.0001);
            assertEquals(StardogValueFactory.Datatype.DOUBLE.iri(), ((Literal)aBindingSet.getValue("longitude")).getDatatype());
            assertEquals(-4.329, ((Literal)aBindingSet.getValue("longitude")).doubleValue(), 0.0001);

            //aBindingSet = aResult.next();

            //assertEquals(literal("dog"), aBindingSet.getValue("result"));
            //assertEquals(literal(1, StardogValueFactory.Datatype.INTEGER), aBindingSet.getValue("idx"));

            assertFalse("Should have no more results", aResult.hasNext());
        }
    }

    /*
    @Test
    public void costAndCardinalityShouldBeCorrect() throws Exception {
        final String aQueryStr = GeoHashVocabulary.sparqlPrefix("geohash") +
            "select * where { (?result ?idx) geohash:decode (\"star\u001fdog\") }";

        Optional<PlanNode> aResult = PlanNodes.find(new QueryParserImpl().parseQuery(aQueryStr, Namespaces.STARDOG).getNode(),
                                                    PlanNodes.is(ArrayPropertyFunction.ArrayPlanNode.class));

        assertTrue(aResult.isPresent());

        ArrayPropertyFunction.ArrayPlanNode aNode = (ArrayPropertyFunction.ArrayPlanNode) aResult.get();

        new ArrayPropertyFunction().estimate(aNode);

        assertEquals(5d, aNode.getCost(), .00001);

        assertEquals(Accuracy.ACCURATE, aNode.getCardinality().accuracy());
        assertEquals(5d, aNode.getCardinality().value(), .00001);
    }

    @Test
    public void costAndCardinalityShouldBeCorrectWithArg() throws Exception {

        final String aQueryStr = GeoHashVocabulary.sparqlPrefix("geohash") +
            "select * where { (?result ?idx) geohash:decode (?in) . values ?in { \"star\u001fdog"} }";

        Optional<PlanNode> aResult = PlanNodes.find(new QueryParserImpl().parseQuery(aQueryStr, Namespaces.STARDOG).getNode(),
                                                  PlanNodes.is(ArrayPropertyFunction.ArrayPlanNode.class));

        assertTrue(aResult.isPresent());

        ArrayPropertyFunction.ArrayPlanNode aNode = (ArrayPropertyFunction.ArrayPlanNode) aResult.get();

        aNode.getArg().setCardinality(Cardinality.of(3, Accuracy.ACCURATE));
        aNode.getArg().setCost(3);

        new ArrayPropertyFunction().estimate(aNode);

        assertEquals(18d, aNode.getCost(), .00001);

        assertEquals(Accuracy.UNKNOWN, aNode.getCardinality().accuracy());
        assertEquals(15d, aNode.getCardinality().value(), .00001);
    }

*/
    @Test
    public void shouldRenderACustomExplanation() {

        final String aQueryStr = GeoHashVocabulary.sparqlPrefix("geohash") +
                "select * where { (?result ?idx) geohash:decode (\"star\u001fdog\") }";

        assertTrue(connection.select(aQueryStr).explain().contains("StringArray("));
    }
}
