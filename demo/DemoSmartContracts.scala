package demo

import org.web3j.crypto.{Credentials, WalletUtils}

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
  import Demo._

  // Compile the smart contract before generating the wrapper code
  println(solc("src/test/resources/basic_info_getter.sol"))
  println(wrapAbi("basic_info_getter"))

  try {
    // todo java.io.FileNotFoundException: /home/mslinn/.ethereum (is a directory)
    val credentials: Credentials = WalletUtils.loadCredentials("password", walletDir)

  //  val contract1 = YourSmartContract.deploy(web3j, credentials, gasPrice, gasLimit, param1, paramN).send()

    // Or use an existing contract:
  //  val contract2 = YourSmartContract.load( "0x<address>", web3j, credentials, gasPrice, gasLimit)

    // To transact with a smart contract:
  //  val result = contract1.someMethod(param1, paramN).send()
  } catch {
    case e: Throwable =>
      System.err.println(e.getMessage)
  }
}
