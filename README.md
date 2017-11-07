# Scala Project Template

[![Build Status](https://travis-ci.org/mslinn/sbtTemplate.svg?branch=master)](https://travis-ci.org/mslinn/sbtTemplate)
[![GitHub version](https://badge.fury.io/gh/mslinn%2FsbtTemplate.svg)](https://badge.fury.io/gh/mslinn%2FsbtTemplate)

Scala wrapper around [Web3J](https://www.web3j.io) for Ethereum.
This project is built with Scala 2.12, which requires Java 8+.

## Developers
The Scaladoc is [here](http://mslinn.github.io/web3j-scala/latest/api/com/micronautics/web3j/index.html).

Publish the Scaladoc for this project with this command:

    sbt ";doc ;ghpagesPushSite"

## Running the Program
The `bin/run` Bash script assembles this project into a fat jar and runs it.
Sample usage, which runs the `Main` entry point in `src/main/scala/com.micronautics.web3j/Main.scala`:

```
$ bin/run
```

The `-j` option forces a rebuild of the fat jar. 
Use it after modifying the source code.

```
$ bin/run -j
```
