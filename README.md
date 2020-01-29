# Web3J-Scala Library

<img src='https://raw.githubusercontent.com/mslinn/web3j-scala/gh-pages/images/web3j.png' align='right' width='15%'>

[![GitHub version](https://badge.fury.io/gh/mslinn%2Fweb3j-scala.svg)](https://badge.fury.io/gh/mslinn%2Fweb3j-scala)

`web3j-scala` is an idiomatic Scala wrapper around [Web3J](https://www.web3j.io) for Ethereum.
Web3J is a lightweight, reactive, somewhat type safe Java and Android library for integrating with nodes on Ethereum blockchains.

Web3J features RxJava extensions, and `web3j-scala` wraps that syntax in Scala goodness.
For example, the `web3j-scala` [observable methods](http://mslinn.github.io/web3j-scala/latest/api/com/micronautics/web3j/Web3JScala$.html)
provide [simple and efficient application code](https://github.com/mslinn/web3j-scala/blob/master/demo/DemoObservables.scala#L14-L22).
Scala's [value classes are used](https://github.com/mslinn/web3j-scala/blob/master/src/main/scala/com/micronautics/web3j/ValueClasses.scala) 
to provide much stronger type safety than Web3J, without incurring a runtime penalty.

## Use As a Library
Add this to your SBT project's `build.sbt`:

    resolvers ++= Seq(
      "micronautics/scala on bintray" at "https://dl.bintray.com/micronautics/scala",
      "ethereum" at "https://dl.bintray.com/ethereum/maven/"
    )

    libraryDependencies += "com.micronautics" %% "web3j-scala" % "4.5.13" withSources()

This library is cross-built for Scala 2.12 and 2.13. Tested with Oracle JDK 8 and OpenJDK 8 & 11.

## Questions and Problems
This library merely wraps [Web3J](https://www.web3j.io), so if you have questions about how to use this library, 
please [read their docs](https://web3j.readthedocs.io/en/stable/quickstart.html), and participate in [their Gitter channel](https://gitter.im/web3j/web3j).

If you find a bug in this library you can [post an issue here](https://github.com/mslinn/web3j-scala/issues).

## Run the Demo Program
The demo program performs the following:
 - Follows the outline of the [Web3J Getting Started](https://docs.web3j.io/getting_started.html#start-sending-requests) documentation,
   adapted for Web3J-Scala, including synchronous and asynchronous versions of the available methods.
 - Compiles an example Solidity program that defines a smart contract.
 - Creates a JVM wrapper from an example smart contract.

To run the demo:
1. Start up an Ethereum client if you don’t already have one running, such as `geth`.
   The `bin/runGeth` script invokes `geth` with the following options, which are convenient for development but not secure enough for production:
     - The Ethereum data directory is set to `~/.ethereum`, or a subdirectory that depends on the network chosen;
       the directory will be created if required.
     - HTTP-RPC server at `localhost:8545` is enabled, and all APIs are allowed.
     - Ethereum's experimental Whisper message facility is enabled.
     - Inter-process communication will be via a virtual file called `geth.ipc`,
       located at `~/.ethereum` or a subdirectory.
     - WS-RPC server at `localhost:8546` is enabled, and all APIs are allowed.
     - Info verbosity is specified.
     - A log file for the `geth` output will be written, or overwritten, in `logs/geth.log`;
       the `log/` directory will be created if it does not already exist.
   ```
   $ mkdir logs/
   $ geth \
      #--datadir .ethereum/devnet --dev \      # boots quickly but has no deployed contracts from others
      --datadir .ethereum/rinkeby --rinkeby \  # takes about 15 minutes to boot, but has contracts
      --ipcpath geth.ipc \
      --metrics \
      --rpc \
      --rpcapi eth,net,web3,clique,debug,eth,miner,personal,rpc,ssh,txpool \
      --shh \
      --ws \
      --wsapi eth,net,web3,clique,debug,eth,miner,personal,rpc,ssh,txpool \
      --verbosity 2
   ```
   You will see the message `No etherbase set and no accounts found as default`.
   Etherbase is the index into `personal.listAccounts` which determines the account to send Ether too.
   You can specify this value with the option `--etherbase 0`.
2. The shell that you just used will continuously scroll output so long as `geth` continues to run,
   so type the following into another shell:
   ```
   $ bin/demo
   ```
   The demo has two major components:
   1. [Creates a JVM wrapper](https://github.com/mslinn/web3j-scala/blob/master/demo/DemoSmartContracts.scala)
      for the [sample smart contract](https://github.com/mslinn/web3j-scala/blob/master/src/test/resources/basic_info_getter.sol).
   2. The second portion of the demo consists of the following:
      - Examples of using `web3j-scala`'s [synchrounous and asynchronous APIs](https://github.com/mslinn/web3j-scala/blob/master/demo/Demo.scala)
      - Examples of working with [RxJava's Observables from Scala](https://github.com/mslinn/web3j-scala/blob/master/demo/DemoObservables.scala)
      - Examples of working with [JVM wrappers around Ethereum smart contracts](https://github.com/mslinn/web3j-scala/blob/master/demo/DemoSmartContracts.scala).
      - Examples of using [transactions](https://github.com/mslinn/web3j-scala/blob/master/demo/DemoTransaction.scala)
        with Ethereum wallet files and the Ethereum client.
3. The `bin/web3j` script runs the [Web3J command-line console](https://docs.web3j.io/command_line.html).
   The script builds a fat jar the first time it is run, so the command runs quickly on subsequent invocations.
4. More scripts are provided in the `bin/` directory, including:
   - [bin/attachHttp](https://github.com/mslinn/web3j-scala/blob/master/bin/attachHttp) -
     Attach to a running geth instance via HTTP and open a
     [JavaScript console](https://godoc.org/github.com/robertkrimen/otto)
   - [bin/attachIpc](https://github.com/mslinn/web3j-scala/blob/master/bin/attachIpc) -
     Attach to a running geth instance via IPC and open a JavaScript console.
     This script might need to be edited if a network other than `devnet` is used.
   - [bin/getApis](https://github.com/mslinn/web3j-scala/blob/master/bin/gethApis) -
     Reports the available APIs exposed by this `geth` instance.
   - [bin/isGethListening](https://github.com/mslinn/web3j-scala/blob/master/bin/isGethListening) -
     Verifies that `geth` is listening on HTTP port 8545

## Developers
### API Documentation
* [This library's Scaladoc is here](http://mslinn.github.io/web3j-scala/latest/api/com/micronautics/web3j/index.html) 
  and the [gitter channel is here](https://gitter.im/web3j-scala/Lobby).

* [The Web3J JavaDoc is here](https://jar-download.com/java-documentation-javadoc.php?a=core&g=org.web3j&v=3.0.2),
  and here is the [Web3J gitter channel](https://gitter.im/web3j/web3j).

### Publishing
1. Update the version string in `build.sbt` and in this `README.md` before attempting to publish to Bintray.
2. Commit changes with a descriptive comment:
   ```
   $ git add -a && git commit -m "Comment here"
   ```
3. Publish a new version of this library, including committing changes and updating the Scaladoc with this command:
   ```
   $ sbt publishAndTag
   ```

### Updating Scaladoc
1. Use the Scaladoc project; first do a [preflight check of the Scaladoc output](https://github.com/mslinn/multi-scaladoc#preflight-check-optimize-your-scaladoc-source):
   ```
    sbt "; project web3j-scala; doc; project demo; doc"
   ```

2. Now [Update the Git Version String and Make a New Tag](https://github.com/mslinn/multi-scaladoc#update-the-git-version-string-and-make-a-new-tag).

3. Now [edit the multi-scaladoc settings](https://github.com/mslinn/multi-scaladoc#running-this-program)
   ```
   export SCALADOC_SUB_PROJECT_NAMES="web3j-scala,demo"
   ``` 

4. Run multi-scaladoc:
   ```
   ../scaladoc/bin/run
   ```

### Updating Scaladoc and Committing Changes Without Publishing a New Version
This task rebuilds the docs, commits the git repository, and publishes the updated Scaladoc without publishing a new version:

    $ sbt commitAndDoc

## Sponsor
<img src='https://www.micronauticsresearch.com/images/robotCircle400shadow.png' align='right' width='15%'>

This project is sponsored by [Micronautics Research Corporation](http://www.micronauticsresearch.com/),
the company that delivers online Scala training via [ScalaCourses.com](http://www.ScalaCourses.com).
You can learn Scala by taking the [Introduction to Scala](http://www.ScalaCourses.com/showCourse/40),
and [Intermediate Scala](http://www.ScalaCourses.com/showCourse/45) courses.

Please [contact us](mailto:sales@micronauticsresearch.com) to discuss your organization&rsquo;s training needs.

## License
This software is published under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
