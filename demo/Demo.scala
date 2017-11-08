package demo

import java.math.BigInteger
import com.micronautics.web3j.{Ethereum, EthereumASynchronous, EthereumSynchronous}
import demo.Cmd.{isMac, isWindows}
import org.web3j.protocol.Web3j
import org.web3j.protocol.ipc.{UnixIpcService, WindowsIpcService}
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * @author mslin_000 */
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
