package demo

import rx.Subscription

class DemoFilters(demo: Demo) {
  import demo._

  //  web3j functional-reactive nature makes it really simple to setup observers that notify subscribers of events taking place on the blockchain.
  //  To receive all new blocks as they are added to the blockchain:
  // todo implement Web3jRx wrapper
  val subscription1: Subscription = web3j.blockObservable(false).subscribe { block =>
    ???
  }

//  To receive all new transactions as they are added to the blockchain:
  val subscription2: Subscription = web3j.transactionObservable.subscribe { tx =>
    ???
  }

  //  To receive all pending transactions as they are submitted to the network (i.e. before they have been grouped into a block together):
  val subscription3: Subscription = web3j.pendingTransactionObservable.subscribe { tx =>
    ???
  }

    //  Or, if you’d rather replay all blocks to the most current, and be notified of new subsequent blocks being created:
  //  val subscription = web3j.catchUpToLatestAndSubscribeToNewBlocksObservable(startBlockNumber, fullTxObjects).subscribe { block =>
  //    ???
  //  }

  //  There are a number of other transaction and block replay Observables described in Filters and Events.
  //  Topic filters are also supported:
  //  val filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, contractAddress)
  //               .addSingleTopic("???") // .addOptionalTopics("???", "???)
  //  web3j.ethLogObservable(filter).subscribe { log =>
  //    ???
  //  }

  //  Subscriptions should always be cancelled when no longer required:
  subscription1.unsubscribe()
  subscription2.unsubscribe()
  subscription3.unsubscribe()

  //  Note: filters are not supported on Infura.
  //  For further information refer to Filters and Events and the Web3jRx interface.
}
