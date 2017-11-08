import com.micronautics.web3j._
import org.web3j.crypto.{Credentials, WalletUtils}
import org.web3j.protocol.Web3j
import org.web3j.protocol.ipc.{UnixIpcService, WindowsIpcService}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import Cmd._

/** Adapted from [[https://docs.web3j.io/getting_started.html#start-a-client Web3J Getting Started]].
  *
  * Before running this program, start up an Ethereum client if you don’t already have one running, such as `geth`:
  * {{{geth --rpcapi personal,db,eth,net,web3 --rpc --rinkeby}}}
  *
  * Run this program by typing the following at a shell prompt:
  * {{{sbt test:run}}} */
object Main extends App {
  // Setup from running command lines from Scala
  val cmd = new Cmd()

  // Instantiate an instance of the underlying Web3J library:
  val web3j: Web3j = Ethereum.fromHttp()  // defaults to http://localhost:8545/

  // To send synchronous requests:
  val ethSync: EthereumSynchronous = new EthereumSynchronous(web3j)
  val web3ClientVersion1: String = ethSync.versionWeb3J
  println(s"Web3J version = $web3ClientVersion1")

  // To send asynchronous requests:
  val ethASync: EthereumASynchronous = new EthereumASynchronous(web3j)
  val web3ClientVersion2: String = Await.result(ethASync.versionWeb3J, Duration.Inf)
  println(s"Web3J version = $web3ClientVersion2")

  val ethereumDir: String = if (isWindows) "~/AppData/Roaming/Ethereum"
  else if (isMac) "~/Library/Ethereum/"
  else "~/.ethereum/"

  // Web3J supports fast inter-process communication (IPC) via file sockets to clients running on the same host as Web3J.
  // To connect simply use the relevant IpcService implementation instead of HttpService when you create your service:
  val web3J3: Web3j = if (isWindows)
    Web3j.build(new WindowsIpcService(ethereumDir))
  else
    Web3j.build(new UnixIpcService(ethereumDir))

  // Working with smart contracts with Java smart contract wrappers
  // web3j can auto-generate smart contract wrapper code to deploy and interact with smart contracts without leaving the JVM.
  // To generate the wrapper code, compile your smart contract:
  val solcCmd = cmd.getOutputFrom("solc", "basic_info_getter.sol", "--bin", "--abi", "--optimize", "-o", "abi/")

  // Generate the wrapper code using web3j’s Command Line Tools
  val makeWrapper = cmd.getOutputFrom(
    "web3j", "solidity",
      "generate", "<smart-contract>.bin", "abi/<smart-contract>.abi",
      "-o", "src/main/java",
      "-p", "com.micronautics.solidity"
  )

  // Now you can create and deploy your smart contract:
  val walletDir = if (isWindows) s"${ sys.props("user.home") }\\AppData\\Roaming\\Ethereum\\keystore"
    else if (isMac) "~/Library/Application Support/Mist/"
    else "~/.config/Mist"
  val credentials: Credentials = WalletUtils.loadCredentials("password", walletDir)

//  val contract1 = YourSmartContract.deploy(web3j, credentials, gasPrice, gasLimit, param1, paramN).send()

  // Or use an existing contract:
//  val contract2 = YourSmartContract.load( "0x<address>", web3j, credentials, gasPrice, gasLimit)

  // To transact with a smart contract:
//  val result = contract.someMethod(param1, paramN).send()
}
