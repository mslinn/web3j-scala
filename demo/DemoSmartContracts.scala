package demo

import org.web3j.crypto.{Credentials, WalletUtils}
import scala.concurrent.ExecutionContext

object CreateSmartContracts extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  new DemoSmartContracts(new Demo)
}

/** web3j can auto-generate smart contract wrapper code to deploy and interact with smart contracts without leaving the JVM.
  * This program creates a smart contract wrapper around a [[../src/test/resources/basic_info_getter.sol sample smart contract]]
  * and demonstrates how to work with smart contracts using JVM wrappers.
  *
  * Run this program before [[Main running the demo]].
  *
  * @see See [[https://web3j.readthedocs.io/en/latest/smart_contracts.html Web3J Smart Contracts]] */
class DemoSmartContracts(demo: Demo) {
  import Demo._, demo._

  // To generate the wrapper code, compile the smart contract:
  // solc --bin --abi --optimize --overwrite -o abi/ src/test/resources/basic_info_getter.sol
  val solcOutput: String = cmd.getOutputFrom(
    "solc",
      "--bin",
      "--abi",
      "--optimize",
      "--overwrite",
      "-o", "abi/",
      "src/test/resources/basic_info_getter.sol"
  )
  println(solcOutput)

  // Generate the wrapper code using web3j’s command-line tools
  val makeWrapperOutput: String = cmd.getOutputFrom(
    "bin/web3j", "solidity",
      "generate", "basic_info_getter.bin", "abi/basic_info_getter.abi", // todo should a directory be created for the bin file?
      "-o", "abi/",
      "-p", "com.micronautics.solidity"
  )
  println(makeWrapperOutput)

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
