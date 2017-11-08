import java.math.BigInteger
import Cmd._
import com.micronautics.web3j._
import org.web3j.protocol.Web3j
import org.web3j.protocol.ipc.{UnixIpcService, WindowsIpcService}
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/** Adapted from [[https://docs.web3j.io/getting_started.html#start-a-client Web3J Getting Started]].
  *
  * Before running this program, start up an Ethereum client if you don’t already have one running, such as `geth`:
  * {{{geth --rpcapi personal,db,eth,net,web3 --rpc --rinkeby}}}
  *
  * Run this program by typing the following at a shell prompt:
  * {{{sbt test:run}}} */
object Main extends App {
  val demo = new Demo
  new DemoSmartContracts(demo)
  new DemoFilters(demo)
}

class Demo {
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

  val gasPrice: BigInteger = BigInt(1).bigInteger
  val gasLimit: BigInteger = BigInt(2).bigInteger
}
