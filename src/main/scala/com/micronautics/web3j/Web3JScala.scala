package com.micronautics.web3j

import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.protocol.infura.InfuraHttpService
import InfuraNetwork._
import rx.Observable
import scala.concurrent.ExecutionContext

/** [[https://www.web3j.io Web3J]] builders and stateless methods. */
object Web3JScala {
  /** @see See [[https://docs.web3j.io/management_apis.html?highlight=httpservice Management APIs]] */
  def fromHttp(url: String = "http://localhost:8545"): Web3j = {
    val web3j = Web3j.build(new HttpService(url))
    verifyConnection(web3j)
    web3j
  }

  /** @see See [[https://docs.web3j.io/infura.html Using Infura with web3j]]
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
}

/** Wrapper for Web3J */
class Web3JScala(val web3j: Web3j)
                (implicit ec: ExecutionContext) {
  lazy val async = new EthereumASynchronous(web3j)
  lazy val sync = new EthereumSynchronous(web3j)
}
