package demo

import demo.Cmd.{isMac, isWindows}
import org.web3j.crypto.{Credentials, WalletUtils}

/** Working with smart contracts with Java smart contract wrappers.
  * Create the smart contract wrapper before running the demo */
object DemoSmartContracts extends App {
  val cmd = new Cmd()

  // web3j can auto-generate smart contract wrapper code to deploy and interact with smart contracts without leaving the JVM.
  // To generate the wrapper code, compile your smart contract:
  val solcOutput: String = cmd.getOutputFrom("solc", "basic_info_getter.sol", "--bin", "--abi", "--optimize", "-o", "abi/")
  println(solcOutput)

  // Generate the wrapper code using web3j’s Command Line Tools
  val makeWrapperOutput: String = cmd.getOutputFrom(
    "web3j", "solidity",
      "generate", "<smart-contract>.bin", "abi/<smart-contract>.abi",
      "-o", "src/main/java",
      "-p", "com.micronautics.solidity"
  )

  println(makeWrapperOutput)
}

class DemoSmartContracts(demo: Demo) {
  import demo._

  // Now you can create and deploy your smart contract:
  val walletDir: String = if (isWindows) s"${ sys.props("user.home") }\\AppData\\Roaming\\Ethereum\\keystore"
    else if (isMac) "~/Library/Application Support/Mist/"
    else "~/.config/Mist"
  val credentials: Credentials = WalletUtils.loadCredentials("password", walletDir)

//  val contract1 = YourSmartContract.deploy(web3j, credentials, gasPrice, gasLimit, param1, paramN).send()

  // Or use an existing contract:
//  val contract2 = YourSmartContract.load( "0x<address>", web3j, credentials, gasPrice, gasLimit)

  // To transact with a smart contract:
//  val result = contract.someMethod(param1, paramN).send()
}
