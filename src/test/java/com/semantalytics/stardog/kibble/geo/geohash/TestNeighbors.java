package com.semantalytics.stardog.kibble.geo.geohash;

import com.complexible.common.rdf.model.StardogValueFactory;
import com.complexible.stardog.plan.eval.ExecutionException;
import com.semantalytics.stardog.kibble.AbstractStardogTest;
import org.junit.Test;
import org.openrdf.model.Literal;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;

import static com.complexible.common.rdf.model.Values.literal;
import static org.junit.Assert.*;

public class TestNeighbors extends AbstractStardogTest {

    private final String sparqlPrefix = GeoHashVocabulary.sparqlPrefix("geohash");

    @Test(expected = ExecutionException.class)
    public void tooManyResultsThrowsError() {

        final String aQueryStr = sparqlPrefix +
                " select * where { (?one ?two ?three ?four ?five) geohash:neighbors (\"gbsuv7ztqzpt\") }";

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
                " select * where { (\"one\" \"two\" \"three\" \"four\") geohash:neighbors (\"gbsuv7ztqzpt\") }";

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
                " select * where { ?result geohash:neighbors (\"gbsuv7ztqzpt\" 5) }";


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
                " select * where { ?result geohash:neighbors (5) }";


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
                " select * where { ?result geohash:neighbors (<http://example.com>) }";


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
                " select * where { ?result geohash:neighbors (_:bnode) }";

        try(final TupleQueryResult aResult = connection.select(aQueryStr).execute()) {
            assertFalse("Should have no more results", aResult.hasNext());
        }
    }

    @Test
    public void varInputWithNoResultsShouldProduceZeroResults() {
        final String aQueryStr = sparqlPrefix +
                " select * where { ?result geohash:neighbors (?input) }";


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
                " select * where { (?top ?bottom ?left ?right) geohash:neighbors (?in) . values ?in { \"gbsuv7ztqzpt\"} }";

        BindingSet aBindingSet;

        try(final TupleQueryResult aResult = connection.select(aQueryStr).execute()) {

            aBindingSet = aResult.next();

            assertEquals(literal("gbsuv7ztqzpw"), aBindingSet.getValue("top"));
            assertEquals(literal("gbsuv7ztqzps"), aBindingSet.getValue("bottom"));
            assertEquals(literal("gbsuv7ztqzpm"), aBindingSet.getValue("left"));
            assertEquals(literal("gbsuv7ztqzpv"), aBindingSet.getValue("right"));
            //assertEquals(literal(0, StardogValueFactory.Datatype.INTEGER), aBindingSet.getValue("idx"));

            //aBindingSet = aResult.next();

            //assertEquals(literal("dog"), aBindingSet.getValue("result"));
            //assertEquals(literal(1, StardogValueFactory.Datatype.INTEGER), aBindingSet.getValue("idx"));

            assertFalse("Should have no more results", aResult.hasNext());
        }
    }

    @Test
    public void neighborsWithConstInput() {
        final String aQueryStr = sparqlPrefix +
                " select * where { (?top ?bottom ?left ?right) geohash:neighbors (\"gbsuv7ztqzpt\") }";

        BindingSet aBindingSet;

        try(final TupleQueryResult aResult = connection.select(aQueryStr).execute()) {

            aBindingSet = aResult.next();

            assertEquals("gbsuv7ztqzpw", aBindingSet.getValue("top").stringValue());
            assertEquals("gbsuv7ztqzps", aBindingSet.getValue("bottom").stringValue());
            assertEquals("gbsuv7ztqzpm", aBindingSet.getValue("left").stringValue());
            assertEquals("gbsuv7ztqzpv", aBindingSet.getValue("right").stringValue());

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
            "select * where { (?result ?idx) geohash:neighbors (\"star\u001fdog\") }";

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
            "select * where { (?result ?idx) geohash:neighbors (?in) . values ?in { \"star\u001fdog"} }";

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
                "select * where { (?result ?idx) geohash:neighbors (\"star\u001fdog\") }";

        assertTrue(connection.select(aQueryStr).explain().contains("StringArray("));
    }
}
