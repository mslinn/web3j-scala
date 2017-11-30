# Web3J-Scala Library

<img src='https://docs.web3j.io/_static/web3j.png' align='right' width='15%'>

<!--[![GitHub version](https://badge.fury.io/gh/mslinn%2Fweb3j-scala.svg)](https://badge.fury.io/gh/mslinn%2Fweb3j-scala)-->
[![Build Status](https://travis-ci.org/mslinn/web3j-scala.svg?branch=master)](https://travis-ci.org/mslinn/web3j-scala)
[![Contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/dwyl/esta/issues)

`web3j-scala` is an idiomatic Scala wrapper around [Web3J](https://www.web3j.io) for Ethereum.
Web3J is a lightweight, reactive, somewhat type safe Java and Android library for integrating with nodes on Ethereum blockchains.

This project promotes idiomatic Scala in the following ways:
  - Variables and no-argument methods are actually names of properties, so `set` and `get` prefixes are not used.
    This means some properties do not have exactly the same name as their Web3J counterpart.
  - In general, zero-argument methods only require parentheses if they perform side effects.
  - Scala data types are used to the maximum extent that makes sense.
    For example, [scala.concurrent.Future](http://www.scala-lang.org/api/current/scala/concurrent/Future.html).
  - A functional programming style is encouraged by always returning immutable data types from methods.
    For example, [scala.collection.immutable.List](http://www.scala-lang.org/api/current/scala/collection/immutable/List.html)

Web3J features RxJava extensions, and `web3j-scala` wraps that syntax in Scala goodness.
For example, the `web3j-scala` [observable methods](http://mslinn.github.io/web3j-scala/latest/api/com/micronautics/web3j/Web3JScala$.html)
provide [simple and efficient application code](https://github.com/mslinn/web3j-scala/blob/master/demo/DemoObservables.scala#L14-L22):
```
//  Display all new blocks as they are added to the blockchain:
observe(web3j.blockObservable(false)) { ethBlock =>
  println(format(ethBlock))
}

// Display only the first 10 new transactions as they are added to the blockchain:
observe(10)(web3j.transactionObservable) { tx =>
  println(format(tx))
}
```

Scala's [value classes are used](https://github.com/mslinn/web3j-scala/blob/master/src/main/scala/com/micronautics/web3j/ValueClasses.scala) 
to provide much stronger type safety than Web3J, without incurring a runtime penalty.
Implicit conversions are provided that make it easy to obtain instances of the desired value classes, without sacrificing type safety.
For example, the following code implicitly converts the `String` returned by `basicInfoContract.send.getContractAddress`
into an `Address`:

    val basicInfoContractAddress: Address = basicInfoContract.send.getContractAddress
    
## Use As a Library
Add this to your SBT project's `build.sbt`:

    resolvers += "micronautics/scala on bintray" at "http://dl.bintray.com/micronautics/scala"

    libraryDependencies += "com.micronautics" %% "web3j-scala" % "0.2.0" withSources()

Only Scala 2.12 with JDK 8 is supported at present; this is a limitation of the Scala ecosystem as of November 7, 2017.

## Demos Require an Ethereum Client
An Ethereum client such as `geth` needs to be running before the demos can work.
The `bin/runGeth` script invokes `geth` with the following options, which are convenient for development 
but not secure enough for production:
 - The Ethereum data directory is set to `~/.ethereum`, or a subdirectory that depends on the network chosen; 
   the directory will be created if required.
 - HTTP-RPC server at `localhost:8545` is enabled, and all APIs are allowed.
 - Ethereum's experimental Whisper message facility is enabled.
 - Inter-process communication will be via a virtual file called `geth.ipc`, 
   located at `~/.ethereum` or a subdirectory.
 - WS-RPC server at `localhost:8546` is enabled, and all APIs are allowed.
 - Verbosity level `info` is specified.
 - A log file for the `geth` output will be written, or overwritten, in `logs/geth.log`;
   the `logs/` directory will be created if it does not already exist.

When you run the script you will see the message `No etherbase set and no accounts found as default`.
Etherbase is the index into `personal.listAccounts` which determines the account to send Ether too.
You can specify this value with the option `--etherbase 0`.

`geth` will continuously scroll output so long as it continues to run, so you must run the demo programs in another shell.

## Running the Demo Programs
The demo programs follow the general outline of the 
[Web3J Getting Started](https://docs.web3j.io/getting_started.html#start-sending-requests) documentation, 
adapted for Web3J-Scala, including synchronous and asynchronous versions of the available methods.

Each demo program starts with a 
[DemoContext](https://github.com/mslinn/web3j-scala/blob/master/demo/DemoContext.scala), 
which performs some setup common to all the demo programs.
`DemoContext` demonstrates how to use `web3j-scala`'s 
[synchronous and asynchronous APIs](https://github.com/mslinn/web3j-scala/blob/master/demo/DemoContext.scala).

The demo programs are:
 - `DemoObservables` - Web3J's functional-reactive nature makes it easy to set up observers that notify subscribers of events taking place on the blockchain.
   This demo shows how to work with [RxJava's Observables from Scala](https://github.com/mslinn/web3j-scala/blob/master/demo/DemoObservables.scala).
 - `DemoSmartContracts` - Compiles an example Solidity program that defines a smart contract,
   [creates a JVM wrapper](https://github.com/mslinn/web3j-scala/blob/master/demo/DemoSmartContracts.scala) for the
   [sample smart contract](https://github.com/mslinn/web3j-scala/blob/master/src/test/resources/basic_info_getter.sol), 
   and exercises the smart contract from Scala.
 - `DemoTransaction` - Demonstrates enhanced support beyond what Web3J provides for working with Ethereum wallet files
   and Ethereum client admin commands for sending 
   [transactions](https://github.com/mslinn/web3j-scala/blob/master/demo/DemoTransactions.scala).

The help message for the `bin/demo` script appears if you do not specify any arguments:
```
$ bin/demo
This script can run some or all of the web3j-scala demo programs.
An Ethereum client must be running before any of these demos will work.
If you have installed a recent version of the geth Ethereum client, run it as follows:

  bin/runGeth

Once geth is running, run a demo programs by typing the following into another console:

  bin/demo Observables     # Demonstrates how Scala works with the RxJava functionality provided with Web3J.
  bin/demo SmartContracts  # Demonstrates JVM wrappers around Ethereum smart contracts.
  bin/demo Transactions    # Demonstrates of Ethereum transactions using wallet files and the Ethereum client.
  bin/demo All             # Run all of the above
```

## More Scripts!
The following scripts are provided in the `bin/` directory:
- [bin/attachHttp](https://github.com/mslinn/web3j-scala/blob/master/bin/attachHttp) &ndash;
  Attach to a running geth instance via HTTP and open a 
  [JavaScript console](https://godoc.org/github.com/robertkrimen/otto)
- [bin/attachIpc](https://github.com/mslinn/web3j-scala/blob/master/bin/attachIpc) &ndash;
  Attach to a running geth instance via IPC and open a JavaScript console.
  This script might need to be edited if a network other than `devnet` is used.
- [bin/getApis](https://github.com/mslinn/web3j-scala/blob/master/bin/gethApis) &ndash;
  Reports the available APIs exposed by this `geth` instance.
- [bin/isGethListening](https://github.com/mslinn/web3j-scala/blob/master/bin/isGethListening) &ndash;
  Verifies that `geth` is listening on HTTP port 8545
- [bin/web3j](https://github.com/mslinn/web3j-scala/blob/master/bin/web3j) &ndash; 
  Runs the [Web3J command-line console](https://docs.web3j.io/command_line.html).
  The script builds a fat jar the first time it is run, so the command runs quickly on subsequent invocations.
  Invoke the script without any arguments to see the help message:
  ```
                _      _____ _     _
               | |    |____ (_)   (_)
  __      _____| |__      / /_     _   ___
  \ \ /\ / / _ \ '_ \     \ \ |   | | / _ \
   \ V  V /  __/ |_) |.___/ / | _ | || (_) |
    \_/\_/ \___|_.__/ \____/| |(_)|_| \___/
                           _/ |
                          |__/
  
  Usage: web3j version|wallet|solidity ...
  ```
  Now we know that the `web3j` script accepts three subcommand: `version`, `wallet` and `solidity`.
  To see the help message for `web3j wallet`, simply type that in:
  ```
  $ bin/web3j wallet
  
                _      _____ _     _
               | |    |____ (_)   (_)
  __      _____| |__      / /_     _   ___
  \ \ /\ / / _ \ '_ \     \ \ |   | | / _ \
   \ V  V /  __/ |_) |.___/ / | _ | || (_) |
    \_/\_/ \___|_.__/ \____/| |(_)|_| \___/
                           _/ |
                          |__/
  
  wallet create|update|send|fromkey
  ```
   
## Developers
### API Documentation
* [The Scaladoc for both the library and the demo is here](http://mslinn.github.io/web3j-scala/index.html); 
you can go directly to the [library Scaladoc](http://mslinn.github.io/web3j-scala/latest/api/root/com/micronautics/web3j/index.html) 
and the [demo Scaladoc](http://mslinn.github.io/web3j-scala/latest/api/demo/demo/index.html).

* [The Web3J JavaDoc is here](https://jar-download.com/java-documentation-javadoc.php?a=core&g=org.web3j&v=3.0.2),
  and here is the [Web3J gitter channel](https://gitter.im/web3j/web3j).

### Previewing Scaladoc
To preview Scaladoc, you can either run the `previewSite` task, which launches a static web server, or
run the `previewAuto` task, which launches a dynamic server that updates its content at each modification in your source files.
Both servers run from port 4000 and both SBT tasks attempt to connect your browser to `http://localhost:4000/`.

To change the server port, set `previewFixedPort`: 

    previewFixedPort := Some(9999)
   
### Publishing
1. Update the version string in `build.sbt` and in this `README.md` before attempting to publish to Bintray.
2. Commit changes with a descriptive comment:
   ```
   $ git add -a && git commit -m "Comment here"
   ```
3. Tell the `sbt-git` SBT plugin where the `.git` directory is:
   ```
   export GIT_DIR="$(pwd)/.git"
   ```
4. Publish a new version of this library, including committing changes and updating the Scaladoc with this command:
   ```
   $ sbt publishAndTag
   ```

### Updating Scaladoc
The documentation for this project is generated separately for both subprojects: `root` (the library) and `demo`.
For usage, simply type:
```
$ bin/doc
Publish 0.2.1
Usage: bin/doc [options]

  -a, --autoCheckIn <value>
                           Stop program if any files need to be committed or pushed
  -c, --copyright <value>  Scaladoc footer
  -d, --deleteAfterUse <value>
                           remove the GhPages temporary directory when the program ends
  -n, --gitHubName <value>
                           Github ID for project
  -o, --overWriteIndex <value>
                           Do not preserve any pre-existing index.html in the Scaladoc root
  -r, --dryRun <value>     Show the commands that would be run
  -s, --subProjectNames <value>
                           Comma-delimited names of subprojects to generate Scaladoc for
```

## Sponsor
<img src='https://www.micronauticsresearch.com/images/robotCircle400shadow.png' align='right' width='15%'>

This project is sponsored by [Micronautics Research Corporation](http://www.micronauticsresearch.com/),
the company that delivers online Scala training via [ScalaCourses.com](http://www.ScalaCourses.com).
You can learn Scala by taking the [Introduction to Scala](http://www.ScalaCourses.com/showCourse/40),
and [Intermediate Scala](http://www.ScalaCourses.com/showCourse/45) courses.

Micronautics Research also offers Ethereum and Scala consulting.
Please [contact us](mailto:sales@micronauticsresearch.com) to discuss your organization&rsquo;s needs.

## License
This software is published under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
