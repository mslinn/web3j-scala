package demo

import org.web3j.protocol.core.methods.response
import rx.{Observable, Subscription}

class DemoFilters(demo: Demo) {
  import demo._

  //  web3j functional-reactive nature makes it really simple to setup observers that notify subscribers of events taking place on the blockchain.
  //  To receive all new blocks as they are added to the blockchain:
  subscribe(5)(web3j.blockObservable(false)) { ethBlock =>
    println(reportEthBlock(ethBlock))
  }

//  To receive all new transactions as they are added to the blockchain:
  subscribe(5)(web3j.transactionObservable) { ethBlock =>
    println(reportEthBlock(ethBlock))
  }

  //  To receive all pending transactions as they are submitted to the network (i.e. before they have been grouped into a block together):
  web3j.pendingTransactionObservable.subscribe { tx =>
    println(reportTx(tx))
  }

  /** Utility method that only runs fn n times on the given Observable[T] */
  def subscribe[T](n: Int)
                  (observable: Observable[T])
                  (fn: response.EthBlock => Unit): Subscription = {
    var counter = n
    val subscription: Subscription = observable.subscribe { t: T =>
        fn(t)
        counter = counter - 1
        if (counter<=0) subscription.unsubscribe()
    }
    subscription
  }

  def reportEthBlock(ethBlock: response.EthBlock): String = {
    val block = ethBlock.getBlock
    s"""ETH block:
       |  Author               = ${ block.getAuthor }
       |  Difficulty           = ${ block.getDifficulty }
       |  Extra data           = ${ block.getExtraData }
       |  Gas limit            = ${ block.getGasLimit }
       |  Gas used             = ${ block.getGasUsed }
       |  Hash                 = ${ block.getHash }
       |  Bloom logs           = ${ block.getLogsBloom }
       |  Miner                = ${ block.getMiner }
       |  Mix hash             = ${ block.getMixHash }
       |  Nonce                = ${ block.getNonce }
       |  Number               = ${ block.getNumber }
       |  Parent hash          = ${ block.getParentHash }
       |  Receipts root        = ${ block.getReceiptsRoot }
       |  Seal fields          = ${ block.getSealFields }
       |  SHA3 uncles          = ${ block.getSha3Uncles }
       |  Size                 = ${ block.getSize }
       |  State root           = ${ block.getStateRoot }
       |  Timestamp            = ${ block.getTimestamp }
       |  Transactions.size    = ${ block.getTransactions.size }
       |  Transactions root    = ${ block.getTransactionsRoot }
       |  Uncles               = ${ block.getUncles }
       |  Raw difficulty       = ${ block.getDifficultyRaw }
       |  Raw gas limit        = ${ block.getGasLimitRaw }
       |  Raw gas used         = ${ block.getGasUsedRaw }
       |  Raw nonce            = ${ block.getNonceRaw }
       |  Raw number           = ${ block.getNumberRaw }
       |  Raw size             = ${ block.getSizeRaw }
       |  Raw timestamp        = ${ block.getTimestampRaw}
       |  Total difficulty     = ${ block.getTotalDifficulty }
       |  Raw total difficulty = ${ block.getTotalDifficultyRaw }
       |""".stripMargin
  }

  def reportTx(tx: response.Transaction): String =
    s"""Transaction:
       |  Block hash            = ${ tx.getBlockHash }
       |  Block number          = ${ tx.getBlockNumber }
       |  Block number raw      = ${ tx.getBlockNumberRaw }
       |  Creates               = ${ tx.getCreates }
       |  From                  = ${ tx.getFrom }
       |  Gas                   = ${ tx.getGas }
       |  Gas price             = ${ tx.getGasPrice }
       |  Hash                  = ${ tx.getHash }
       |  Input                 = ${ tx.getInput }
       |  Nonce                 = ${ tx.getNonce }
       |  Public key            = ${ tx.getPublicKey }
       |  R                     = ${ tx.getR }
       |  To                    = ${ tx.getTo }
       |  Transaction index     = ${ tx.getTransactionIndex }
       |  V                     = ${ tx.getV }
       |  Value                 = ${ tx.getValue }
       |  Raw                   = ${ tx.getRaw }
       |  Raw gas               = ${ tx.getGasRaw }
       |  Raw gas price         = ${ tx.getGasPriceRaw }
       |  Raw nonce             = ${ tx.getNonceRaw }
       |  Raw transaction index = ${ tx.getTransactionIndexRaw }
       |  Raw value             = ${ tx.getValueRaw }
       |""".stripMargin


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

  //  Note: filters are not supported on Infura.
  //  For further information refer to Filters and Events and the Web3jRx interface.
}
