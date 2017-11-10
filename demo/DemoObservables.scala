package demo

import com.micronautics.web3j._
import com.micronautics.web3j.Web3JScala._
import org.web3j.protocol.core.DefaultBlockParameterName._
import org.web3j.protocol.core.methods.{request, response}

/** Web3J's functional-reactive nature makes it easy to set up observers that notify subscribers of events taking place on the blockchain.
  * The functional-reactive programming style works really well with Scala.
  *
  * Other transaction and block replay [[org.web3j.utils.Observables]] are described in [[https://docs.web3j.io/filters.html Filters and Events]]. */
class DemoObservables(demo: Demo) {
  import demo._

  //  Display the first 2 new blocks as they are added to the blockchain:
  observe(2)(web3j.blockObservable(false)) { ethBlock =>
    println(format(ethBlock))
  }

  // Display only the first 2 new transactions as they are added to the blockchain:
  observe(2)(web3j.transactionObservable) { tx =>
    println(format(tx))
  }

  // Display the first 2 pending transactions as they are submitted to the network, before they have been grouped into a block:
  observe(2)(web3j.pendingTransactionObservable) { tx =>
    println(format(tx))
  }

  // Replay all blocks to the most current, and be notified of new subsequent blocks being created
  // Display minimal information about old blocks, show detailed information for new blocks
  val now: Long = System.currentTimeMillis
  var count = 0
  web3j.catchUpToLatestAndSubscribeToNewBlocksObservable(EARLIEST, false).subscribe { ethBlock =>
    val block = ethBlock.getBlock
    if (block.getTimestamp.longValue<now) {
      count = count + 1
      print(s"\rSkipped $count blocks. ")
//      println(s"Skipping ${ block.getNumber }: ${ block.javaTime }")
    } else println(format(ethBlock))
  }

  // Topic Filter Demo
  // Filters are not supported on the Infura network.
  val contractAddress = "0x8888f1f195afa192cfee860698584c030f4c9db1" // todo relate this bogus contract to something in the demo

  // See https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_newfilter
  val ethFilter: request.EthFilter =
    new request
      .EthFilter(EARLIEST, LATEST, contractAddress)
      .addSingleTopic("0x000000000000000000000000a94f5374fce5edbc8e2a8697c15331677e6ebf0b") // todo relate this bogus topic to something in the demo
      .addOptionalTopics( // todo relate these bogus topics to something in the demo
        "0x000000000000000000000000a94f5374fce5edbc8e2a8697c15331677e6ebf0b",
        "0x0000000000000000000000000aff3454fce5edbc8cca8697c15331677e6ebccc"
      )

  web3j.ethLogObservable(ethFilter).subscribe { log =>
    println(format(log))
  }


  protected def format(ethBlock: response.EthBlock): String = {
    val block = ethBlock.getBlock
    s"""ETH block:
       |  Author               = ${ block.getAuthor }
       |  Bloom logs           = ${ block.getLogsBloom }
       |  Difficulty           = ${ block.getDifficulty }
       |  Extra data           = ${ block.getExtraData }
       |  Gas limit            = ${ block.getGasLimit }
       |  Gas used             = ${ block.getGasUsed }
       |  Hash                 = ${ block.getHash }
       |  Java time            = ${ block.javaTime }
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
}
