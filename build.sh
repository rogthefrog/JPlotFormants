#!/bin/bash
rm -rf bin
mkdir bin
javac src/*.java -d bin/
cp src/*.htm bin/
cp src/*.gif bin/
cd bin
jar cfe ../JPlotFormants.jar JPlotFormants *
