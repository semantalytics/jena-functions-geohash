package com.semantalytics.stardog.kibble.geo.geohash;

import com.complexible.common.rdf.model.StardogValueFactory;
import org.openrdf.model.IRI;

public enum GeoHashVocabulary {

    right,
    left,
    top,
    bottom,
    decode,
    encode,
    latitude,
    hashLengthToCoverBoundingBox,
    hashContains,
    longitude,
    heightDegrees,
    widthDegrees,
    addLongitude;

    public static final String NAMESPACE = "http://semantalytics.com/2017/09/ns/stardog/kibble/geo/hash/";
    public final IRI iri;

    GeoHashVocabulary() {
        iri = StardogValueFactory.instance().createIRI(NAMESPACE, name());
    }

    public static String sparqlPrefix(String prefixName) {
        return "PREFIX " + prefixName + ": <" + NAMESPACE + "> ";
    }

    public String stringValue() {
        return iri.stringValue();
    }
}
