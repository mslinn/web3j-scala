package demo

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
