package demo

/** Adapted from [[https://docs.web3j.io/getting_started.html#start-a-client Web3J Getting Started]].
  *
  * 1. Before running this program, start up an Ethereum client if you don’t already have one running, such as `geth`:
  * {{{$ geth --rpcapi personal,db,eth,net,web3 --rpc --rinkeby --ipcpath "geth.ipc"}}}
  *
  * 2. Create the smart contract JVM wrapper by running `CreateSmartContracts` defined in `demoContext/DemoSmartContracts.scala`:
  * {{{$ sbt "test:runMain demoContext.CreateSmartContracts"}}}
  *
  * 3. Run this program by typing the following at a shell prompt:
  * {{{$ sbt "test:runMain demoContext.Main"}}} */
object Main extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  val demoContext = new DemoContext
  new DemoObservables(demoContext)
  new DemoSmartContracts(demoContext)
  new DemoTransactions(demoContext)
}
