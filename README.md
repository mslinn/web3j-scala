# Web3J-Scala Library

[![Build Status](https://travis-ci.org/mslinn/web3j-scala.svg?branch=master)](https://travis-ci.org/mslinn/web3j-scala)
[![GitHub version](https://badge.fury.io/gh/mslinn%2web3j-scala.svg)](https://badge.fury.io/gh/mslinn%2Fweb3j-scala)

Scala wrapper around [Web3J](https://www.web3j.io) for Ethereum.
This project is built with Scala 2.12, and requires the Java 8 runtime; it is not yet compatible with Java 9.

## Running the Demo Program
The `bin/run` Bash script assembles this project into a fat jar and runs it.
Type the following to run the `Main` entry point in `src/main/scala/com.micronautics.web3j/Main.scala`:

```
$ bin/run
```
Building the fat jar takes a while. Subsequent invocations happen much more quickly.

## Developers
### Rebuilding the Fat Jar
The `bin/run` script accepts a `-j` option, which forces a rebuild of the fat jar. 
Use it after modifying the source code.

```
$ bin/run -j
```

### Scaladoc
The Scaladoc is [here](http://mslinn.github.io/web3j-scala/latest/api/com/micronautics/web3j/index.html).

Publish the Scaladoc for this project with this command:

    sbt ";doc ;ghpagesPushSite"
