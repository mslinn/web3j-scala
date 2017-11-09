package demo

import org.web3j.protocol.core.DefaultBlockParameterName._
import org.web3j.protocol.core.methods.{request, response}
import rx.Observable

/** Web3J's functional-reactive nature makes it easy to set up observers that notify subscribers of events taking place on the blockchain.
  * The functional-reactive programming style works really well with Scala. */
class DemoObservables(demo: Demo) {
  import demo._

  //  Display all new blocks as they are added to the blockchain:
  observe(web3j.blockObservable(false)) { ethBlock =>
    println(format(ethBlock))
  }

  //  Display only the first 5 new transactions as they are added to the blockchain:
  observe(5)(web3j.transactionObservable) { tx =>
    println(format(tx))
  }

  //  Display all pending transactions as they are submitted to the network, before they have been grouped into a block:
  web3j.pendingTransactionObservable.subscribe { tx =>
    println(format(tx))
  }

  //  Replay all blocks to the most current, and be notified of new subsequent blocks being created:
  web3j.catchUpToLatestAndSubscribeToNewBlocksObservable(EARLIEST, false).subscribe { ethBlock =>
    println(format(ethBlock))
  }

  //  Other transaction and block replay Observables are described in [[https://docs.web3j.io/filters.html Filters and Events]].

  //  Topic Filter Demo
  //  Filters are not supported on the Infura network.
  val contractAddress = "todo do something intelligent here"

  val ethFilter: request.EthFilter =
    new request
      .EthFilter(EARLIEST, LATEST, contractAddress)
      .addSingleTopic("???")
      .addOptionalTopics("???", "???")

  web3j.ethLogObservable(ethFilter).subscribe { log =>
    println(format(log))
  }


  protected def format(ethBlock: response.EthBlock): String = {
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

  protected def format(log: response.Log): String =
    s"""Response Log:
       |  Address               = ${ log.getAddress }
       |  Block hash            = ${ log.getBlockHash }
       |  Block number          = ${ log.getBlockNumber }
       |  Data                  = ${ log.getData }
       |  Log index             = ${ log.getLogIndex }
       |  Topics                = ${ log.getTopics }
       |  Transaction hash      = ${ log.getTransactionHash }
       |  Transaction index     = ${ log.getTransactionIndex }
       |  Type                  = ${ log.getType }
       |  Removed               = ${ log.isRemoved }
       |  Raw block number      = ${ log.getBlockNumberRaw }
       |  Raw log index         = ${ log.getLogIndexRaw }
       |  Raw transaction index = ${ log.getTransactionIndexRaw }
       |""".stripMargin

  protected def format(tx: response.Transaction): String =
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

  /** Invokes fn on all elements streamed from the given Observable[T] */
  protected def observe[T](observable: Observable[T])
                          (fn: T => Unit): Unit =
    observable.subscribe(fn(_))

  /** Only runs fn on the first n elements streamed from the given Observable[T] */
  protected def observe[T](n: Int)
                          (observable: Observable[T])
                          (fn: T => Unit): Unit =
    observable.limit(n).doOnEach { t =>
      fn(t.getValue.asInstanceOf[T])
    }
}
