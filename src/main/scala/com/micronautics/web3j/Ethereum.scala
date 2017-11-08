package com.micronautics.web3j

import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.protocol.infura.InfuraHttpService

/** [[https://www.web3j.io Web3J]] builders and any other misc. stuff might end up in this class. */
object Ethereum {
  def fromInfura(token: String): Web3j =
    Web3j.build(new InfuraHttpService(s"https://ropsten.infura.io/$token"))

  def fromHttp(url: String = "http://localhost:8545"): Web3j = Web3j.build(new HttpService(url))

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
