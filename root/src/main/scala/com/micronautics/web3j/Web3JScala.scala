package com.micronautics.web3j

import com.micronautics.web3j.InfuraNetwork._
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.protocol.infura.InfuraHttpService
import rx.Observable
import scala.concurrent.ExecutionContext

/** [[https://www.web3j.io Web3J]] builders and stateless methods. */
object Web3JScala {
  lazy val cmd: Cmd = new Cmd

  /** @see See [[https://docs.web3j.io/management_apis.html?highlight=httpservice Management APIs]] */
  def fromHttp(url: String = "http://localhost:8545"): Web3j = {
    val web3j = Web3j.build(new HttpService(url))
    verifyConnection(web3j)
    web3j
  }

  /** @see See [[http://www.ziggify.com/blog/blockchain-stack-1-installing-ethereum-geth-smart-contract/ Installing Ethereum Geth and your first smart contract]]
    * @see See the [[http://faucet.ropsten.be:3001/ Ethereum Ropsten Faucet]]
    * @see See [[https://docs.web3j.io/infura.html Using Infura with web3j]]
    * @param network defaults to the Ropsten test network */
  def fromInfura(token: String, network: InfuraNetwork = ROPSTEN): Web3j = {
    val web3j = Web3j.build(new InfuraHttpService(s"https://${ network.name }.infura.io/$token"))
    verifyConnection(web3j)
    web3j
  }

  /** Invokes fn on all elements observed from the given Observable[T] */
  def observe[T](observable: Observable[T])
                (fn: T => Unit): Unit =
    observable.subscribe(fn(_))

  /** Only runs fn on the first n elements observed from the given Observable[T] */
  def observe[T](n: Int)
                (observable: Observable[T])
                (fn: T => Unit): Unit =
    observable.limit(n).doOnEach { t =>
      fn(t.getValue.asInstanceOf[T])
    }

  /** Compile the smart contract.
    * Note that `solc` generates [[https://en.wikipedia.org/wiki/Camel_case camelCase]] names from
    * [[https://en.wikipedia.org/wiki/Snake_case snake_case]] names.
    * {{{solc --bin --abi --optimize --overwrite -o abi/ src/test/resources/basic_info_getter.sol}}} */
  def solc(solCFileName: String, outputDirectory: String="abi/"): String = cmd.getOutputFrom(
    "solc",
      "--bin",
      "--abi",
      "--optimize",
      "--overwrite",
      "-o", outputDirectory,
      solCFileName
  )

  /** Verify web3j is connected to a JSON-RPC endpoint */
  def verifyConnection(web3j: Web3j): Boolean = try {
      web3j.web3ClientVersion.send.getWeb3ClientVersion
      true
    } catch {
      case e: Exception =>
        println(s"${ e.getMessage }. Is geth or eth running?")
        System.exit(0)
        false
    }

  /** Generate the wrapper code from the compiled smart contract using web3j’s command-line tools
    * The `bin` and `abi` files are both read from the same directory, specified by `-o`.
    * {{{bin/web3j solidity generate abi/basic_info_getter.bin abi/basic_info_getter.abi -o abiWrapper/ -p com.micronautics.solidity}}} */
  def wrapAbi(
    filename: String,
    packageName: String = "com.micronautics.solidity",
    inputDirectory: String = "abi/",
    outputDirectory: String = "abiWrapper/"
  ): String = cmd.getOutputFrom(
    "bin/web3j", "solidity",
      "generate", s"$inputDirectory/${ toCamelCase(filename) }.bin", s"$inputDirectory/${ toCamelCase(filename) }.abi",
      "-o", outputDirectory,
      "-p", packageName
  )

  protected def toCamelCase(s: String): String = {
    val words = s.split("_")
    val tail = words.tail.map { word => word.head.toUpper + word.tail }
    words.head + tail.mkString
  }
}

/** Wrapper for Web3J */
class Web3JScala(val web3j: Web3j)
                (implicit ec: ExecutionContext) {
  lazy val async = new EthereumASynchronous(web3j)
  lazy val sync  = new EthereumSynchronous(web3j)
}
