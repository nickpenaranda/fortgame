#!/bin/bash

javac -classpath lib/lwjgl.jar:lib/slick.jar \
    src/org/n4p/mountainking/*.java \
    src/org/n4p/mountainking/items/*.java \
    src/org/n4p/mountainking/terrain/*.java \
    src/org/n4p/mountainking/units/*.java
