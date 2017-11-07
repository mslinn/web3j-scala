package com.micronautics.web3j

import java.math.BigInteger
import java.util.Optional
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.methods.request
import org.web3j.protocol.core.methods.request.ShhFilter
import org.web3j.protocol.core.methods.response.{EthBlock, EthCompileSolidity, EthGetWork, EthLog, ShhMessages, Transaction, TransactionReceipt}
import scala.collection.JavaConverters._

/** All of the methods in this class block until a value is ready to be returned to the caller.
  * @param web3j can be shared with [[EthereumASynchronous]] */
class EthereumSynchronous(val web3j: Web3j) {
  def accounts: List[String] = web3j.ethAccounts.send.getAccounts.asScala.toList

  def addToGroup(identityAddress: String): Boolean = web3j.shhAddToGroup(identityAddress).send.addedToGroup

  def balance(address: String, defaultBlockParameter: DefaultBlockParameter): BigInteger =
    web3j.ethGetBalance(address, defaultBlockParameter).send.getBalance

  def blockByHash(blockHash: String, returnFullTransactionObjects: Boolean): EthBlock.Block =
    web3j.ethGetBlockByHash(blockHash, returnFullTransactionObjects).send.getBlock

  def blockByNumber(defaultBlockParameter: DefaultBlockParameter, returnFullTransactionObjects: Boolean): EthBlock.Block =
    web3j.ethGetBlockByNumber(defaultBlockParameter, returnFullTransactionObjects).send.getBlock

  def blockNumber: BigInteger = web3j.ethBlockNumber.send.getBlockNumber

  def blockTransactionCountByHash(blockHash: String): BigInteger =
    web3j.ethGetBlockTransactionCountByHash(blockHash).send.getTransactionCount

  def blockTransactionCountByNumber(defaultBlockParameter: DefaultBlockParameter): BigInteger =
    web3j.ethGetBlockTransactionCountByNumber(defaultBlockParameter).send.getTransactionCount

  def call(transaction: request.Transaction, defaultBlockParameter: DefaultBlockParameter): String =
    web3j.ethCall(transaction, defaultBlockParameter).send.getValue

  def code(address: String, defaultBlockParameter: DefaultBlockParameter): String =
    web3j.ethGetCode(address, defaultBlockParameter).send.getCode

  def coinbaseAddress: String = web3j.ethCoinbase.send.getAddress

  def compileLLL(sourceCode: String): String = web3j.ethCompileLLL(sourceCode).send.getCompiledSourceCode

  def compileSerpent(sourceCode: String): String =
    web3j.ethCompileSerpent(sourceCode).send.getCompiledSourceCode

  def compileSolidity(sourceCode: String): Map[String, EthCompileSolidity.Code] =
    web3j.ethCompileSolidity(sourceCode).send.getCompiledSolidity.asScala.toMap

  def compilers: List[String] = web3j.ethGetCompilers.send.getCompilers.asScala.toList

  def estimateGas(transaction: request.Transaction): BigInteger = web3j.ethEstimateGas(transaction).send.getAmountUsed

  def filterChangesEth(filterId: BigInteger): List[EthLog.LogResult[_]] =
    web3j.ethGetFilterChanges(filterId).send.getLogs.asScala.toList

  def filterChangesShh(filterId: BigInteger): List[ShhMessages.SshMessage] =
    web3j.shhGetFilterChanges(filterId).send.getMessages.asScala.toList

  def filterLogs(filterId: BigInteger): List[EthLog.LogResult[_]] =
    web3j.ethGetFilterLogs(filterId).send.getLogs.asScala.toList

  def gasPrice: BigInteger = web3j.ethGasPrice.send.getGasPrice

  def getHex(databaseName: String, keyName: String): String =
    web3j.dbGetHex(databaseName, keyName).send.getStoredValue

  def getString(databaseName: String, keyName: String): String =
    web3j.dbGetString(databaseName, keyName).send.getStoredValue

  def hashRate: BigInteger = web3j.ethHashrate.send.getHashrate

  def hasIdentity(identityAddress: String): Boolean = web3j.shhHasIdentity(identityAddress).send.hasPrivateKeyForIdentity

  def isSyncing: Boolean   = web3j.ethSyncing.send.isSyncing

  def isMining: Boolean    = web3j.ethMining.send.isMining

  def isListening: Boolean = web3j.netListening.send.isListening

  def logs(ethFilter: request.EthFilter): List[EthLog.LogResult[_]] =
    web3j.ethGetLogs(ethFilter).send.getLogs.asScala.toList

  def messages(filterId: BigInteger): List[ShhMessages.SshMessage] =
    web3j.shhGetMessages(filterId).send.getMessages.asScala.toList

  def newBlockFilter: BigInteger = web3j.ethNewBlockFilter.send.getFilterId

  def newFilter(ethFilter: request.EthFilter): BigInteger = web3j.ethNewFilter(ethFilter).send.getFilterId

  def newFilter(shhFilter: ShhFilter): BigInteger = web3j.shhNewFilter(shhFilter).send.getFilterId

  def newGroup: String = web3j.shhNewGroup.send.getAddress

  /** @return new ssh identity */
  def newIdentity: String = web3j.shhNewIdentity.send.getAddress

  def newPendingTransactionFilter: BigInteger = web3j.ethNewPendingTransactionFilter.send.getFilterId

  def peerCount: BigInteger = web3j.netPeerCount.send.getQuantity

  def post(shhPost: request.ShhPost): Boolean = web3j.shhPost(shhPost).send.messageSent

  def putHex(databaseName: String, keyName: String, dataToStore: String): Boolean =
    web3j.dbPutHex(databaseName, keyName, dataToStore).send.valueStored

  def putString(databaseName: String, keyName: String, stringToStore: String): Boolean =
    web3j.dbPutString(databaseName, keyName, stringToStore).send.valueStored

  def sendRawTransaction(signedTransactionData: String): String =
    web3j.ethSendRawTransaction(signedTransactionData).send.getTransactionHash

  def sendTransaction(transaction: request.Transaction): String =
    web3j.ethSendTransaction(transaction).send.getTransactionHash

  def sha3(data: String): String = web3j.web3Sha3(data).send.getResult

  def sign(address: String, sha3HashOfDataToSign: String): String =
    web3j.ethSign(address, sha3HashOfDataToSign).send.getSignature

  def storageAt(address: String, position: BigInteger, defaultBlockParameter: DefaultBlockParameter): String =
    web3j.ethGetStorageAt(address, position, defaultBlockParameter).send.getData

  def submitHashrate(hashRate: String, clientId: String): Boolean =
    web3j.ethSubmitHashrate(hashRate, clientId).send.submissionSuccessful

  def submitWork(nonce: String, headerPowHash: String, mixDigest: String): Boolean =
    web3j.ethSubmitWork(nonce, headerPowHash, mixDigest).send.solutionValid

  def transactionByBlockHashAndIndex(blockHash: String, transactionIndex: BigInteger): Optional[Transaction] =
    web3j.ethGetTransactionByBlockHashAndIndex(blockHash, transactionIndex).send.getTransaction

  def transactionByBlockNumberAndIndex(
    defaultBlockParameter: DefaultBlockParameter,
    transactionIndex: BigInteger
  ): Optional[Transaction] =
    web3j.ethGetTransactionByBlockNumberAndIndex(defaultBlockParameter, transactionIndex).send.getTransaction

  def transactionByHash(transactionHash: String): Optional[Transaction] =
    web3j.ethGetTransactionByHash(transactionHash).send.getTransaction

  def transactionCount(address: String, defaultBlockParameter: DefaultBlockParameter): BigInteger =
    web3j.ethGetTransactionCount(address, defaultBlockParameter).send.getTransactionCount

  def transactionReceipt(transactionHash: String): Optional[TransactionReceipt] =
    web3j.ethGetTransactionReceipt(transactionHash).send.getTransactionReceipt

  def uncleByBlockNumberAndIndex(defaultBlockParameter: DefaultBlockParameter, transactionIndex: BigInteger): EthBlock.Block =
    web3j.ethGetUncleByBlockNumberAndIndex(defaultBlockParameter, transactionIndex).send.getBlock

  def uncleByBlockHashAndIndex(blockHash: String, transactionIndex: BigInteger): EthBlock.Block =
    web3j.ethGetUncleByBlockHashAndIndex(blockHash, transactionIndex).send.getBlock

  def uncleCountByBlockHash(blockHash: String): BigInteger =
    web3j.ethGetUncleCountByBlockHash(blockHash).send.getUncleCount

  def uncleCountByBlockNumber(defaultBlockParameter: DefaultBlockParameter): BigInteger =
    web3j.ethGetUncleCountByBlockNumber(defaultBlockParameter).send.getUncleCount

  def uninstallFilter(filterId: BigInteger): Boolean = web3j.ethUninstallFilter(filterId).send.isUninstalled

  def uninstallShhFilter(filterId: BigInteger): Boolean = web3j.shhUninstallFilter(filterId).send.isUninstalled

  def versionWeb3J: String = web3j.web3ClientVersion.send.getWeb3ClientVersion

  def versionNet: String = web3j.netVersion.send.getNetVersion

  def versionProtocol: String = web3j.ethProtocolVersion.send.getProtocolVersion

  def versionShh: String = web3j.shhVersion.send.getVersion

  def work: EthGetWork = web3j.ethGetWork.send
}
