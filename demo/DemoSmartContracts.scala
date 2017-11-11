package demo

import com.micronautics.web3j.{Address, Password, Wallet}
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.RemoteCall

object CreateSmartContracts extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  new DemoSmartContracts(new Demo)
}

/** Web3J can auto-generate smart contract wrapper code to deploy and interact with smart contracts without leaving the JVM.
  * This program creates a smart contract wrapper around a [[../src/test/resources/basic_info_getter.sol sample smart contract]]
  * and demonstrates how to work with smart contracts using JVM wrappers.
  *
  * Run this program before [[Main running the demo]].
  *
  * @see See [[https://web3j.readthedocs.io/en/latest/smart_contracts.html Web3J Smart Contracts]] */
class DemoSmartContracts(demo: Demo) {
  import com.micronautics.web3j.Web3JScala.{solc,wrapAbi}
  import com.micronautics.solidity._
  import Demo._

  // Compile the smart contract before generating the wrapper code
  val solidityFileName = "basic_info_getter"
  println(solc(s"src/test/resources/$solidityFileName.sol"))
  println(wrapAbi(solidityFileName))

  try {
    val password = Password("bogus") // todo make this real
    val credentials: Credentials = Wallet.loadCredentials(password, walletDir)

    val basicInfoContract: RemoteCall[BasicInfoGetter] = BasicInfoGetter.deploy(demo.web3j, credentials, gasPrice.bigInteger, gasLimit)
    val wtf: BasicInfoGetter = basicInfoContract.send
    println(wtf)

    // Or use an existing contract:
    val address = Address("address")  // todo make this real
     val basicInfoContract2 = BasicInfoGetter.load(address.value, demo.web3j, credentials, gasPrice.wei.bigInteger, gasLimit)

    // Transact with a smart contract; e.g. obtain the address of the contract.
    // Would be nice to generate a Scala wrapper instead of a Java wrapper!
    // Force the implicit conversion from String to Address by specifying the desired type
    val basicInfoContractAddress: Address = basicInfoContract.send.getContractAddress
    println(s"basicInfoGetter.getContractAddress = $basicInfoContractAddress")
  } catch {
    case e: Throwable =>
      System.err.println(e.getMessage)
  }
}
