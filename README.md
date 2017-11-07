<img src='https://docs.web3j.io/_static/web3j.png' align='right' width='15%'>
# Web3J-Scala Library

[![Build Status](https://travis-ci.org/mslinn/web3j-scala.svg?branch=master)](https://travis-ci.org/mslinn/web3j-scala)
[![GitHub version](https://badge.fury.io/gh/mslinn%2Fweb3j-scala.svg)](https://badge.fury.io/gh/mslinn%2Fweb3j-scala)

`Web3J-Scala` is an idiomatic Scala wrapper around [Web3J](https://www.web3j.io) for Ethereum.

## Use As a Library
Add this to your SBT project's `build.sbt`:

    resolvers += "micronautics/scala on bintray" at "http://dl.bintray.com/micronautics/scala"

    libraryDependencies += "com.micronautics" %% "web3j-scala" % "0.1.0" withSources()

Only Scala 2.12 with JDK 8 is supported at present; this is a limitation of the Scala ecosystem as of November 7, 2017.

## Run the Demo Program
The `bin/run` Bash script assembles this project into a fat jar and runs it.
Type the following to run the [Main](http://blog.mslinn.com/web3j-scala/latest/api/com/micronautics/web3j/Main$.html) 
entry point in `src/main/scala/com.micronautics.web3j/Main.scala`:

```
$ bin/run
```
Building the fat jar takes a while. Subsequent invocations happen much more quickly.

## Developers
### Scaladoc
The Scaladoc is [here](http://mslinn.github.io/web3j-scala/latest/api/com/micronautics/web3j/index.html).

### Rebuilding the Fat Jar
The `bin/run` script accepts a `-j` option, which forces a rebuild of the fat jar. 
Use it after modifying the source code.

```
$ bin/run -j
```

### Publishing
Publish a new version of this library, including source code and Scaladoc with this command:

    sbt publish
   
Publish the Scaladoc for this project with this command:

    sbt ";doc ;ghpagesPushSite"

Do all of the above with this command:

    sbt ";publish ;doc ;ghpagesPushSite"

## Sponsor
This project is sponsored by [Micronautics Research Corporation](http://www.micronauticsresearch.com/),
the company that delivers online Scala training via [ScalaCourses.com](http://www.ScalaCourses.com).
You can learn Scala by taking the [Introduction to Scala](http://www.ScalaCourses.com/showCourse/40),
and [Intermediate Scala](http://www.ScalaCourses.com/showCourse/45) courses.

Micronautics Research also offers Ethereum and Scala consulting.
Please [contact us](mailto:sales@micronauticsresearch.com) to discuss your organization&rsquo;s needs.

## License
This software is published under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
