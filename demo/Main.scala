package demo

/** Adapted from [[https://docs.web3j.io/getting_started.html#start-a-client Web3J Getting Started]].
  *
  * 1. Before running this program, start up an Ethereum client if you don’t already have one running, such as `geth`:
  * {{{$ geth --rpcapi personal,db,eth,net,web3 --rpc --rinkeby --ipcpath "geth.ipc"}}}
  *
  * 2. Create the smart contract JVM wrapper by running `CreateSmartContracts` defined in `demo/DemoSmartContracts.scala`:
  * {{{$ sbt "test:runMain demo.CreateSmartContracts"}}}
  *
  * 3. Run this program by typing the following at a shell prompt:
  * {{{$ sbt "test:runMain demo.Main"}}} */
object Main extends App {
  val demo = new Demo
  new DemoObservables(demo)
  new DemoSmartContracts(demo)
  new DemoTransaction(demo)
}
