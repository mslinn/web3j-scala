package com.micronautics.web3j

import java.math.BigInteger
import java.util.Optional
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.methods.request
import org.web3j.protocol.core.methods.request.ShhFilter
import org.web3j.protocol.core.methods.response.{EthBlock, EthCompileSolidity, EthGetWork, EthLog, ShhMessages, Transaction, TransactionReceipt}
import scala.collection.JavaConverters._
import scala.compat.java8.FutureConverters._
import scala.concurrent.ExecutionContext.{global => defaultExecutionContext}
import scala.concurrent.{ExecutionContext, Future}

/** All of the methods in this class return a [[scala.concurrent.Future]] and do not block.
  * @param web3j can be shared with [[EthereumSynchronous]]
  * @param ec if no [[ExecutionContext]] is implicitly available, the default Scala
  *           [[scala.concurrent.ExecutionContext]] is used. */
class EthereumASynchronous(val web3j: Web3j)
                          (implicit val ec: ExecutionContext = defaultExecutionContext) {

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_accounts eth_accounts]] JSON-RPC endpoint.
    * @return the list of addresses owned by the client */
  def accounts: Future[List[String]] = web3j.ethAccounts.sendAsync.toScala.map(_.getAccounts.asScala.toList)

  def addToGroup(identityAddress: String): Future[Boolean] =
    web3j.shhAddToGroup(identityAddress).sendAsync.toScala.map(_.addedToGroup)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getbalance eth_getbalance]] JSON-RPC endpoint.
    * @param defaultBlockParameter either an integer block number, or the string "latest", "earliest" or "pending".
    * See the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#the-default-block-parameter specification]].
    * @return the balance of the account of given address */
  def balance(address: String, defaultBlockParameter: DefaultBlockParameter): Future[BigInteger] =
    web3j.ethGetBalance(address, defaultBlockParameter).sendAsync.toScala.map(_.getBalance)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getblockbyhash eth_getblockbyhash]] JSON-RPC endpoint.
    * @return Some(block object), or None if no block was found */
  def blockByHash(blockHash: String, returnFullTransactionObjects: Boolean): Future[Option[EthBlock.Block]] =
    web3j.ethGetBlockByHash(blockHash, returnFullTransactionObjects).sendAsync.toScala.map(x => Option(x.getBlock))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getblockbyhash eth_getblockbyhash]] JSON-RPC endpoint.
    * @return Some(block object), or None if no block was found */
  def blockByNumber(
    defaultBlockParameter: DefaultBlockParameter,
    returnFullTransactionObjects: Boolean
  ): Future[Option[EthBlock.Block]] =
    web3j
      .ethGetBlockByNumber(defaultBlockParameter, returnFullTransactionObjects)
      .sendAsync
      .toScala
      .map(x => Option(x.getBlock))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_blocknumber eth_blocknumber]] JSON-RPC endpoint.
    * @return the number of the most recent block */
  def blockNumber: Future[BigInteger] = web3j.ethBlockNumber.sendAsync.toScala.map(_.getBlockNumber)

  def blockTransactionCountByHash(blockHash: String): Future[BigInteger] =
    web3j.ethGetBlockTransactionCountByHash(blockHash).sendAsync.toScala.map(_.getTransactionCount)

  def blockTransactionCountByNumber(defaultBlockParameter: DefaultBlockParameter): Future[BigInteger] =
    web3j.ethGetBlockTransactionCountByNumber(defaultBlockParameter).sendAsync.toScala.map(_.getTransactionCount)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_call eth_call]] JSON-RPC endpoint.
    * @return value of executed contract, without creating a transaction on the block chain */
  def call(transaction: request.Transaction, defaultBlockParameter: DefaultBlockParameter): Future[String] =
    web3j.ethCall(transaction, defaultBlockParameter).sendAsync.toScala.map(_.getValue)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getcode eth_getcode]] JSON-RPC endpoint.
    * @return code at a given address */
  def code(address: String, defaultBlockParameter: DefaultBlockParameter): Future[String] =
    web3j.ethGetCode(address, defaultBlockParameter).sendAsync.toScala.map(_.getCode)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_coinbase eth_coinbase]] JSON-RPC endpoint.
    * @return the client coinbase address */
  def coinbaseAddress: Future[String] = web3j.ethCoinbase.sendAsync.toScala.map(_.getAddress)

  def compileLLL(sourceCode: String): Future[String] =
    web3j.ethCompileLLL(sourceCode).sendAsync.toScala.map(_.getCompiledSourceCode)

  def compileSerpent(sourceCode: String): Future[String] =
    web3j.ethCompileSerpent(sourceCode).sendAsync.toScala.map(_.getCompiledSourceCode)

  def compileSolidity(sourceCode: String): Future[Map[String, EthCompileSolidity.Code]] =
    web3j.ethCompileSolidity(sourceCode).sendAsync.toScala.map(_.getCompiledSolidity.asScala.toMap)

  def compilers: Future[List[String]] = web3j.ethGetCompilers.sendAsync.toScala.map(_.getCompilers.asScala.toList)

  /** Makes a call or transaction, which won't be added to the blockchain and returns the used gas, which can be used
    * for estimating the used gas.
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_estimategas eth_estimategas]] JSON-RPC endpoint.
    * @return amount of gas estimated */
  def estimateGas(transaction: request.Transaction): Future[BigInteger] =
    web3j.ethEstimateGas(transaction).sendAsync.toScala.map(_.getAmountUsed)

  def filterChangesEth(filterId: BigInteger): Future[List[EthLog.LogResult[_]]] =
    web3j.ethGetFilterChanges(filterId).sendAsync.toScala.map(_.getLogs.asScala.toList)

  def filterChangesShh(filterId: BigInteger): Future[List[ShhMessages.SshMessage]] =
    web3j.shhGetFilterChanges(filterId).sendAsync.toScala.map(_.getMessages.asScala.toList)

  def filterLogs(filterId: BigInteger): Future[List[EthLog.LogResult[_]]] =
    web3j.ethGetFilterLogs(filterId).sendAsync.toScala.map(_.getLogs.asScala.toList)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gasprice eth_gasprice]] JSON-RPC endpoint.
    * @return the current price per gas in wei */
  def gasPrice: Future[BigInteger] = web3j.ethGasPrice.sendAsync.toScala.map(_.getGasPrice)

  def hexFrom(databaseName: String, keyName: String): Future[String] =
    web3j.dbGetHex(databaseName, keyName).sendAsync.toScala.map(_.getStoredValue)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_hashrate eth_hashrate]] JSON-RPC endpoint.
    * @return the number of hashes per second that the node is mining at */
  def hashRate: Future[BigInteger] = web3j.ethHashrate.sendAsync.toScala.map(_.getHashrate)

  def hasIdentity(identityAddress: String): Future[Boolean] =
    web3j.shhHasIdentity(identityAddress).sendAsync.toScala.map(_.hasPrivateKeyForIdentity)

  def hexTo(databaseName: String, keyName: String, dataToStore: String): Future[Boolean] =
    web3j.dbPutHex(databaseName, keyName, dataToStore).sendAsync.toScala.map(_.valueStored)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#net_listening net_listening]] JSON-RPC endpoint.
    * @return true if this client is actively listening for network connections */
  def isListening: Future[Boolean] = web3j.netListening.sendAsync.toScala.map(_.isListening)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_mining eth_mining]] JSON-RPC endpoint. */
  def isMining: Future[Boolean] = web3j.ethMining.sendAsync.toScala.map(_.isMining)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_syncing eth_syncing]] JSON-RPC endpoint. */
  def isSyncing: Future[Boolean] = web3j.ethSyncing.sendAsync.toScala.map(_.isSyncing)

  def logs(ethFilter: request.EthFilter): Future[List[EthLog.LogResult[_]]] =
    web3j.ethGetLogs(ethFilter).sendAsync.toScala.map(_.getLogs.asScala.toList)

  def messages(filterId: BigInteger): Future[List[ShhMessages.SshMessage]] =
    web3j.shhGetMessages(filterId).sendAsync.toScala.map(_.getMessages.asScala.toList)

  def newBlockFilter: Future[BigInteger] = web3j.ethNewBlockFilter.sendAsync.toScala.map(_.getFilterId)

  def newFilter(ethFilter: request.EthFilter): Future[BigInteger] =
    web3j.ethNewFilter(ethFilter).sendAsync.toScala.map(_.getFilterId)

  def newFilter(shhFilter: ShhFilter): Future[BigInteger] =
    web3j.shhNewFilter(shhFilter).sendAsync.toScala.map(_.getFilterId)

  def newGroup: Future[String] = web3j.shhNewGroup.sendAsync.toScala.map(_.getAddress)

  /** @return new ssh identity */
  def newIdentity: Future[String] = web3j.shhNewIdentity.sendAsync.toScala.map(_.getAddress)

  def newPendingTransactionFilter: Future[BigInteger] =
    web3j.ethNewPendingTransactionFilter.sendAsync.toScala.map(_.getFilterId)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#net_peercount net_peercount]] JSON-RPC endpoint.
    * @return number of peers currently connected to this client */
  def peerCount: Future[BigInteger] = web3j.netPeerCount.sendAsync.toScala.map(_.getQuantity)

  def post(shhPost: request.ShhPost): Future[Boolean] = web3j.shhPost(shhPost).sendAsync.toScala.map(_.messageSent)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sendrawtransaction eth_sendrawtransaction]] JSON-RPC endpoint.
    * @return new message call transaction or a contract creation for signed transactions */
  def sendRawTransaction(signedTransactionData: String): Future[String] =
    web3j.ethSendRawTransaction(signedTransactionData).sendAsync.toScala.map(_.getTransactionHash)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sendtransaction eth_sendtransaction]] JSON-RPC endpoint.
    * @return a new contract if the {{{Transaction.data}}} field contains code, else return a new transaction */
  def sendTransaction(transaction: request.Transaction): Future[String] =
    web3j.ethSendTransaction(transaction).sendAsync.toScala.map(_.getTransactionHash)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#web3_sha3 web3_sha3]] JSON-RPC endpoint.
    * @param data the data to convert into an SHA3 hash
    * @return Keccak-256 hash (not the standardized SHA3-256 hash) of the given data */
  def sha3(data: String): Future[String] = web3j.web3Sha3(data).sendAsync.toScala.map(_.getResult)

  /** Calculates an Ethereum-specific signature with:
    * {{{sign(keccak256("\x19Ethereum Signed Message:\n" + len(message) + message)))}}}
    *
    * By adding a prefix to the message makes the calculated signature recognisable as an Ethereum-specific signature.
    * This prevents misuse where a malicious DApp can sign arbitrary data (e.g. transaction) and use the signature to impersonate the victim.
    *
    * Note: the address to sign with must be unlocked.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sign eth_sign]] JSON-RPC endpoint.
    * @return Signature */
  def sign(address: String, sha3HashOfDataToSign: String): Future[String] =
    web3j.ethSign(address, sha3HashOfDataToSign).sendAsync.toScala.map(_.getSignature)

  def stringFrom(databaseName: String, keyName: String): Future[String] =
    web3j.dbGetString(databaseName, keyName).sendAsync.toScala.map(_.getStoredValue)

  def stringTo(databaseName: String, keyName: String, stringToStore: String): Future[Boolean] =
    web3j.dbPutString(databaseName, keyName, stringToStore).sendAsync.toScala.map(_.valueStored)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getstorageat eth_getstorageat]] JSON-RPC endpoint.
    * @return the value from a storage position at a given address */
  def storageAt(address: String, position: BigInteger, defaultBlockParameter: DefaultBlockParameter): Future[String] =
    web3j.ethGetStorageAt(address, position, defaultBlockParameter).sendAsync.toScala.map(_.getData)

  def submitHashRate(hashRate: String, clientId: String): Future[Boolean] =
    web3j.ethSubmitHashrate(hashRate, clientId).sendAsync.toScala.map(_.submissionSuccessful)

  def submitWork(nonce: String, headerPowHash: String, mixDigest: String): Future[Boolean] =
    web3j.ethSubmitWork(nonce, headerPowHash, mixDigest).sendAsync.toScala.map(_.solutionValid)

  def transactionByBlockHashAndIndex(blockHash: String, transactionIndex: BigInteger): Future[Optional[Transaction]] =
    web3j.ethGetTransactionByBlockHashAndIndex(blockHash, transactionIndex).sendAsync.toScala.map(_.getTransaction)

  def transactionByBlockNumberAndIndex(
    defaultBlockParameter: DefaultBlockParameter,
    transactionIndex: BigInteger
  ): Future[Optional[Transaction]] =
    web3j
      .ethGetTransactionByBlockNumberAndIndex(defaultBlockParameter, transactionIndex)
      .sendAsync
      .toScala
      .map(_.getTransaction)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionbyhash eth_gettransactionbyhash]] JSON-RPC endpoint.
    * @return Future containing Some(transaction object), or None when no transaction was found */
  def transactionByHash(transactionHash: String): Future[Optional[Transaction]] =
    web3j.ethGetTransactionByHash(transactionHash).sendAsync.toScala.map(_.getTransaction)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactioncount eth_gettransactioncount]] JSON-RPC endpoint.
    * @return the number of transactions sent from an address */
  def transactionCount(address: String, defaultBlockParameter: DefaultBlockParameter): Future[BigInteger] =
    web3j.ethGetTransactionCount(address, defaultBlockParameter).sendAsync.toScala.map(_.getTransactionCount)

  def transactionReceipt(transactionHash: String): Future[Optional[TransactionReceipt]] =
    web3j.ethGetTransactionReceipt(transactionHash).sendAsync.toScala.map(_.getTransactionReceipt)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getunclecountbyblockhash eth_getunclecountbyblockhash]] JSON-RPC endpoint.
    * @return the number of uncles in a block from a block matching the given block hash */
  def uncleCountByBlockHash(blockHash: String): Future[BigInteger] =
    web3j.ethGetUncleCountByBlockHash(blockHash).sendAsync.toScala.map(_.getUncleCount)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getunclecountbyblocknumber eth_getunclecountbyblocknumber]] JSON-RPC endpoint.
    * @return the number of uncles in a block from a block matching the given block number */
  def uncleCountByBlockNumber(defaultBlockParameter: DefaultBlockParameter): Future[BigInteger] =
    web3j.ethGetUncleCountByBlockNumber(defaultBlockParameter).sendAsync.toScala.map(_.getUncleCount)

  def uncleByBlockNumberAndIndex(
    defaultBlockParameter: DefaultBlockParameter,
    transactionIndex: BigInteger
  ): Future[EthBlock.Block] =
    web3j.ethGetUncleByBlockNumberAndIndex(defaultBlockParameter, transactionIndex).sendAsync.toScala.map(_.getBlock)

  def uncleByBlockHashAndIndex(blockHash: String, transactionIndex: BigInteger): Future[EthBlock.Block] =
    web3j.ethGetUncleByBlockHashAndIndex(blockHash, transactionIndex).sendAsync.toScala.map(_.getBlock)

  def uninstallFilter(filterId: BigInteger): Future[Boolean] =
    web3j.ethUninstallFilter(filterId).sendAsync.toScala.map(_.isUninstalled)

  def uninstallShhFilter(filterId: BigInteger): Future[Boolean] =
    web3j.shhUninstallFilter(filterId).sendAsync.toScala.map(_.isUninstalled)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#web3_clientversion web3_clientversion]] JSON-RPC endpoint.
    * @return the Web3J client version used by this client */
  def versionWeb3J: Future[String] = web3j.web3ClientVersion.sendAsync.toScala.map(_.getWeb3ClientVersion)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#net_version net_version]] JSON-RPC endpoint.
    * @return the current network id */
  def versionNet: Future[String] = web3j.netVersion.sendAsync.toScala.map(_.getNetVersion)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_protocolversion eth_protocolversion]] JSON-RPC endpoint.
    * @return ethereum protocol version used by this client */
  def versionProtocol: Future[String] = web3j.ethProtocolVersion.sendAsync.toScala.map(_.getProtocolVersion)

  def versionShh: Future[String] = web3j.shhVersion.sendAsync.toScala.map(_.getVersion)

  def work: Future[EthGetWork] = web3j.ethGetWork.sendAsync.toScala
}
