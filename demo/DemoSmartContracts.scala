package demo

import org.web3j.crypto.{Credentials, WalletUtils}

/** Working with smart contracts with Java smart contract wrappers.
  * Create the smart contract wrapper before running the demo */
object DemoSmartContracts extends App {
  val cmd = new Cmd()

  // web3j can auto-generate smart contract wrapper code to deploy and interact with smart contracts without leaving the JVM.
  // To generate the wrapper code, compile your smart contract:
  val solcOutput: String = cmd.getOutputFrom(
    "solc",
    "test/resources/basic_info_getter.sol",
    "--bin", "--abi",
    "--optimize",
    "-o", "abi/"
  )
  println(solcOutput)

  // Generate the wrapper code using web3j’s Command Line Tools
  val makeWrapperOutput: String = cmd.getOutputFrom(
    "web3j", "solidity",
      "generate", "basic_info_getter.bin", "abi/basic_info_getter.abi",
      "-o", "abi/",
      "-p", "com.micronautics.solidity"
  )

  println(makeWrapperOutput)
}

/** Create and deploy smart contracts */
class DemoSmartContracts(demo: Demo) {
  import Demo._, demo._

  try {
    // todo java.io.FileNotFoundException: /home/mslinn/.ethereum (Is a directory)
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
