JPlotFormants
=============

JPlotFormants: Linguistics software to plot formant frequencies. A successor of Peter Ladefoged's original Plot Formants.

Code
====

https://github.com/rogthefrog/JPlotFormants

Building and Running JPlotFormants
==================================

You need the Java SDK to build the program.

Unix-like (including Mac)
-------------------------

Build:

```
$ ./build.sh 
```
This will create a JAR file named JPlotFormants.jar with everything you need in it. Then:

Run:

```
$ ./run.sh
```

Windows
-------

Build:

```
mkdir bin
cd src
javac *.java -d ../bin
```

Run:

```
cd bin
java JPlotFormants
```


License
=======

MIT License http://opensource.org/licenses/MIT

Disclaimers
===========

No guarantees of any kind. This is ancient software with a good deal of bit rot and very little quality to begin with. I hope it's useful to somebody.

Please submit improvements, bug fixes, patches, etc. via pull requests via GitHub.

The original source code was lost, and what you see here was reconstructed by decompiling binary class files. This explains the lack of comments and bad variable names.

Credits
=======

Very much inspired by Peter Ladefoged's Plot Formants (with Peter's blessing). Written while I was a grad student at the UCLA Phonetics Lab. Many thanks to all the faculty, staff and students I met while there.

Thanks to:

* Peter Ladefoged (initial Plot Formants program, support, awesomeness)
* Marek Przezdziecki (ellipse drawing code)
* Juerg Bigler (restoring the source)
* Hugh Paterson III (suggesting github to share this code)

