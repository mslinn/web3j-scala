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
  * @param ec if no [[ExecutionContext]] is implicitly available, the default Scala [[ExecutionContext]] is used. */
class EthereumASynchronous(val web3j: Web3j)
                          (implicit val ec: ExecutionContext = defaultExecutionContext) {
  def accounts: Future[List[String]] = web3j.ethAccounts.sendAsync.toScala.map(_.getAccounts.asScala.toList)

  def addToGroup(identityAddress: String): Future[Boolean] =
    web3j.shhAddToGroup(identityAddress).sendAsync.toScala.map(_.addedToGroup)

  def balance(address: String, defaultBlockParameter: DefaultBlockParameter): Future[BigInteger] =
    web3j.ethGetBalance(address, defaultBlockParameter).sendAsync.toScala.map(_.getBalance)

  def blockByHash(blockHash: String, returnFullTransactionObjects: Boolean): Future[EthBlock.Block] =
    web3j.ethGetBlockByHash(blockHash, returnFullTransactionObjects).sendAsync.toScala.map(_.getBlock)

  def blockByNumber(
    defaultBlockParameter: DefaultBlockParameter,
    returnFullTransactionObjects: Boolean
  ): Future[EthBlock.Block] =
    web3j.ethGetBlockByNumber(defaultBlockParameter, returnFullTransactionObjects).sendAsync.toScala.map(_.getBlock)

  def blockNumber: Future[BigInteger] = web3j.ethBlockNumber.sendAsync.toScala.map(_.getBlockNumber)

  def blockTransactionCountByHash(blockHash: String): Future[BigInteger] =
    web3j.ethGetBlockTransactionCountByHash(blockHash).sendAsync.toScala.map(_.getTransactionCount)

  def blockTransactionCountByNumber(defaultBlockParameter: DefaultBlockParameter): Future[BigInteger] =
    web3j.ethGetBlockTransactionCountByNumber(defaultBlockParameter).sendAsync.toScala.map(_.getTransactionCount)

  def call(transaction: request.Transaction, defaultBlockParameter: DefaultBlockParameter): Future[String] =
    web3j.ethCall(transaction, defaultBlockParameter).sendAsync.toScala.map(_.getValue)

  def code(address: String, defaultBlockParameter: DefaultBlockParameter): Future[String] =
    web3j.ethGetCode(address, defaultBlockParameter).sendAsync.toScala.map(_.getCode)

  def coinbaseAddress: Future[String] = web3j.ethCoinbase.sendAsync.toScala.map(_.getAddress)

  def compileLLL(sourceCode: String): Future[String] =
    web3j.ethCompileLLL(sourceCode).sendAsync.toScala.map(_.getCompiledSourceCode)

  def compileSerpent(sourceCode: String): Future[String] =
    web3j.ethCompileSerpent(sourceCode).sendAsync.toScala.map(_.getCompiledSourceCode)

  def compileSolidity(sourceCode: String): Future[Map[String, EthCompileSolidity.Code]] =
    web3j.ethCompileSolidity(sourceCode).sendAsync.toScala.map(_.getCompiledSolidity.asScala.toMap)

  def compilers: Future[List[String]] = web3j.ethGetCompilers.sendAsync.toScala.map(_.getCompilers.asScala.toList)

  def estimateGas(transaction: request.Transaction): Future[BigInteger] =
    web3j.ethEstimateGas(transaction).sendAsync.toScala.map(_.getAmountUsed)

  def filterChangesEth(filterId: BigInteger): Future[List[EthLog.LogResult[_]]] =
    web3j.ethGetFilterChanges(filterId).sendAsync.toScala.map(_.getLogs.asScala.toList)

  def filterChangesShh(filterId: BigInteger): Future[List[ShhMessages.SshMessage]] =
    web3j.shhGetFilterChanges(filterId).sendAsync.toScala.map(_.getMessages.asScala.toList)

  def filterLogs(filterId: BigInteger): Future[List[EthLog.LogResult[_]]] =
    web3j.ethGetFilterLogs(filterId).sendAsync.toScala.map(_.getLogs.asScala.toList)

  def gasPrice: Future[BigInteger] = web3j.ethGasPrice.sendAsync.toScala.map(_.getGasPrice)

  def getHex(databaseName: String, keyName: String): Future[String] =
    web3j.dbGetHex(databaseName, keyName).sendAsync.toScala.map(_.getStoredValue)

  def getString(databaseName: String, keyName: String): Future[String] =
    web3j.dbGetString(databaseName, keyName).sendAsync.toScala.map(_.getStoredValue)

  def hashRate: Future[BigInteger] = web3j.ethHashrate.sendAsync.toScala.map(_.getHashrate)

  def hasIdentity(identityAddress: String): Future[Boolean] =
    web3j.shhHasIdentity(identityAddress).sendAsync.toScala.map(_.hasPrivateKeyForIdentity)

  def isSyncing: Future[Boolean] = web3j.ethSyncing.sendAsync.toScala.map(_.isSyncing)

  def isMining: Future[Boolean] = web3j.ethMining.sendAsync.toScala.map(_.isMining)

  def isListening: Future[Boolean] = web3j.netListening.sendAsync.toScala.map(_.isListening)

  def logs(ethFilter: request.EthFilter): Future[List[EthLog.LogResult[_]]] =
    web3j.ethGetLogs(ethFilter).sendAsync.toScala.map(_.getLogs.asScala.toList)

  def messages(filterId: BigInteger): Future[List[ShhMessages.SshMessage]] =
    web3j.shhGetMessages(filterId).sendAsync.toScala.map(_.getMessages.asScala.toList)

  def newBlockFilter: Future[BigInteger] = web3j.ethNewBlockFilter.sendAsync.toScala.map(_.getFilterId)

  def newFilter(ethFilter: request.EthFilter): Future[BigInteger] =
    web3j.ethNewFilter(ethFilter).sendAsync.toScala.map(_.getFilterId)

  def newFilter(shhFilter: ShhFilter): Future[BigInteger] =
    web3j.shhNewFilter(shhFilter).sendAsync.toScala.map(_.getFilterId)

  /** @return new ssh identity */
  def newIdentity: Future[String] = web3j.shhNewIdentity.sendAsync.toScala.map(_.getAddress)

  def newGroup: Future[String] = web3j.shhNewGroup.sendAsync.toScala.map(_.getAddress)

  def newPendingTransactionFilter: Future[BigInteger] =
    web3j.ethNewPendingTransactionFilter.sendAsync.toScala.map(_.getFilterId)

  def peerCount: Future[BigInteger] = web3j.netPeerCount.sendAsync.toScala.map(_.getQuantity)

  def post(shhPost: request.ShhPost): Future[Boolean] = web3j.shhPost(shhPost).sendAsync.toScala.map(_.messageSent)

  def putHex(databaseName: String, keyName: String, dataToStore: String): Future[Boolean] =
    web3j.dbPutHex(databaseName, keyName, dataToStore).sendAsync.toScala.map(_.valueStored)

  def putString(databaseName: String, keyName: String, stringToStore: String): Future[Boolean] =
    web3j.dbPutString(databaseName, keyName, stringToStore).sendAsync.toScala.map(_.valueStored)

  def sendRawTransaction(signedTransactionData: String): Future[String] =
    web3j.ethSendRawTransaction(signedTransactionData).sendAsync.toScala.map(_.getTransactionHash)

  def sendTransaction(transaction: request.Transaction): Future[String] =
    web3j.ethSendTransaction(transaction).sendAsync.toScala.map(_.getTransactionHash)

  def sha3(data: String): Future[String] = web3j.web3Sha3(data).sendAsync.toScala.map(_.getResult)

  def sign(address: String, sha3HashOfDataToSign: String): Future[String] =
    web3j.ethSign(address, sha3HashOfDataToSign).sendAsync.toScala.map(_.getSignature)

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

  def transactionByHash(transactionHash: String): Future[Optional[Transaction]] =
    web3j.ethGetTransactionByHash(transactionHash).sendAsync.toScala.map(_.getTransaction)

  def transactionCount(address: String, defaultBlockParameter: DefaultBlockParameter): Future[BigInteger] =
    web3j.ethGetTransactionCount(address, defaultBlockParameter).sendAsync.toScala.map(_.getTransactionCount)

  def transactionReceipt(transactionHash: String): Future[Optional[TransactionReceipt]] =
    web3j.ethGetTransactionReceipt(transactionHash).sendAsync.toScala.map(_.getTransactionReceipt)

  def uncleCountByBlockHash(blockHash: String): Future[BigInteger] =
    web3j.ethGetUncleCountByBlockHash(blockHash).sendAsync.toScala.map(_.getUncleCount)

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

  def versionWeb3J: Future[String] = web3j.web3ClientVersion.sendAsync.toScala.map(_.getWeb3ClientVersion)

  def versionNet: Future[String] = web3j.netVersion.sendAsync.toScala.map(_.getNetVersion)

  def versionProtocol: Future[String] = web3j.ethProtocolVersion.sendAsync.toScala.map(_.getProtocolVersion)

  def versionShh: Future[String] = web3j.shhVersion.sendAsync.toScala.map(_.getVersion)

  def work: Future[EthGetWork] = web3j.ethGetWork.sendAsync.toScala
}
