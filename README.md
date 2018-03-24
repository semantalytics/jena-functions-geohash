[![Build Status](https://travis-ci.org/semantalytics/stardog-kibbles.svg?branch=master)](https://travis-ci.org/semantalytics/stardog-kibbles)

# Stardog Kibbles

A collection of [Stardog](http://stardog.com) plugins for manipulating geohashes

namespace: "http://semantalytics.com/2017/09/ns/stardog/kibble/geo/hash/

prefix: geohash:

functions:

    right
    left
    top
    bottom
    decode
    encode
    latitude
    hashLengthToCoverBoundingBox
    hashContains
    heightDegrees
    longitude
    neighbors
    widthDegrees
    
property functions:

    decode      (?latitude ?longitude) geohash:decode("jkjk43243")
    neighbors   (?top ?bottom ?left ?right) geohash:neighbors("jkjk43243")
