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

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_accounts eth_accounts]] JSON-RPC endpoint.
    * @return the list of addresses owned by the client */
  def accounts: List[String] = web3j.ethAccounts.send.getAccounts.asScala.toList

  def addToGroup(identityAddress: String): Boolean = web3j.shhAddToGroup(identityAddress).send.addedToGroup

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getbalance eth_getbalance]] JSON-RPC endpoint.
    * @param defaultBlockParameter either an integer block number, or the string "latest", "earliest" or "pending".
    * See the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#the-default-block-parameter specification]].
    * @return the balance of the account of given address */
  def balance(address: String, defaultBlockParameter: DefaultBlockParameter): BigInteger =
    web3j.ethGetBalance(address, defaultBlockParameter).send.getBalance

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getblockbyhash eth_getblockbyhash]] JSON-RPC endpoint.
    * @return Some(block object), or None if no block was found */
  def blockByHash(blockHash: String, returnFullTransactionObjects: Boolean): Option[EthBlock.Block] =
    Option(web3j.ethGetBlockByHash(blockHash, returnFullTransactionObjects).send.getBlock)

  def blockByNumber(
    defaultBlockParameter: DefaultBlockParameter,
    returnFullTransactionObjects: Boolean
  ): Option[EthBlock.Block] =
    Option(web3j.ethGetBlockByNumber(defaultBlockParameter, returnFullTransactionObjects).send.getBlock)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_blocknumber eth_blocknumber]] JSON-RPC endpoint.
    * @return the number of the most recent block */
  def blockNumber: BigInteger = web3j.ethBlockNumber.send.getBlockNumber

  def blockTransactionCountByHash(blockHash: String): BigInteger =
    web3j.ethGetBlockTransactionCountByHash(blockHash).send.getTransactionCount

  def blockTransactionCountByNumber(defaultBlockParameter: DefaultBlockParameter): BigInteger =
    web3j.ethGetBlockTransactionCountByNumber(defaultBlockParameter).send.getTransactionCount

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_call eth_call]] JSON-RPC endpoint.
    * @return value of executed contract, without creating a transaction on the block chain */
  def call(transaction: request.Transaction, defaultBlockParameter: DefaultBlockParameter): String =
    web3j.ethCall(transaction, defaultBlockParameter).send.getValue

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getcode eth_getcode]] JSON-RPC endpoint.
    * @return code at a given address */
  def code(address: String, defaultBlockParameter: DefaultBlockParameter): String =
    web3j.ethGetCode(address, defaultBlockParameter).send.getCode

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_coinbase eth_coinbase]] JSON-RPC endpoint.
    * @return the client coinbase address */
  def coinbaseAddress: String = web3j.ethCoinbase.send.getAddress

  def compileLLL(sourceCode: String): String = web3j.ethCompileLLL(sourceCode).send.getCompiledSourceCode

  def compileSerpent(sourceCode: String): String =
    web3j.ethCompileSerpent(sourceCode).send.getCompiledSourceCode

  def compileSolidity(sourceCode: String): Map[String, EthCompileSolidity.Code] =
    web3j.ethCompileSolidity(sourceCode).send.getCompiledSolidity.asScala.toMap

  def compilers: List[String] = web3j.ethGetCompilers.send.getCompilers.asScala.toList

  /** Makes a call or transaction, which won't be added to the blockchain and returns the used gas, which can be used
    * for estimating the used gas.
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_estimategas eth_estimategas]] JSON-RPC endpoint.
    * @return amount of gas estimated */
  def estimateGas(transaction: request.Transaction): BigInteger = web3j.ethEstimateGas(transaction).send.getAmountUsed

  def filterChangesEth(filterId: BigInteger): List[EthLog.LogResult[_]] =
    web3j.ethGetFilterChanges(filterId).send.getLogs.asScala.toList

  def filterChangesShh(filterId: BigInteger): List[ShhMessages.SshMessage] =
    web3j.shhGetFilterChanges(filterId).send.getMessages.asScala.toList

  def filterLogs(filterId: BigInteger): List[EthLog.LogResult[_]] =
    web3j.ethGetFilterLogs(filterId).send.getLogs.asScala.toList

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gasprice eth_gasprice]] JSON-RPC endpoint.
    * @return the current price per gas in wei */
  def gasPrice: BigInteger = web3j.ethGasPrice.send.getGasPrice

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_hashrate eth_hashrate]] JSON-RPC endpoint.
    * @return  number of hashes per second that the node is mining at */
  def hashRate: BigInteger = web3j.ethHashrate.send.getHashrate

  def hasIdentity(identityAddress: String): Boolean = web3j.shhHasIdentity(identityAddress).send.hasPrivateKeyForIdentity

  def hexFrom(databaseName: String, keyName: String): String =
    web3j.dbGetHex(databaseName, keyName).send.getStoredValue

  def hexTo(databaseName: String, keyName: String, dataToStore: String): Boolean =
    web3j.dbPutHex(databaseName, keyName, dataToStore).send.valueStored

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#net_listening net_listening]] JSON-RPC endpoint.
    * @return true if this client is actively listening for network connections */
  def isListening: Boolean = web3j.netListening.send.isListening

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_mining eth_mining]] JSON-RPC endpoint. */
  def isMining: Boolean    = web3j.ethMining.send.isMining

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_syncing eth_syncing]] JSON-RPC endpoint. */
  def isSyncing: Boolean   = web3j.ethSyncing.send.isSyncing

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

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#net_peercount net_peercount]] JSON-RPC endpoint.
    * @return number of peers currently connected to this client */
  def peerCount: BigInteger = web3j.netPeerCount.send.getQuantity

  def post(shhPost: request.ShhPost): Boolean = web3j.shhPost(shhPost).send.messageSent

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sendrawtransaction eth_sendrawtransaction]] JSON-RPC endpoint.
    * @return new message call transaction or a contract creation for signed transactions */
  def sendRawTransaction(signedTransactionData: String): String =
    web3j.ethSendRawTransaction(signedTransactionData).send.getTransactionHash

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sendtransaction eth_sendtransaction]] JSON-RPC endpoint.
    * @return a new contract if the {{{Transaction.data}}} field contains code, else return a new transaction */
  def sendTransaction(transaction: request.Transaction): String =
    web3j.ethSendTransaction(transaction).send.getTransactionHash

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#web3_sha3 web3_sha3]] JSON-RPC endpoint.
    * @param data the data to convert into an SHA3 hash
    * @return Keccak-256 hash (not the standardized SHA3-256 hash) of the given data */
  def sha3(data: String): String = web3j.web3Sha3(data).send.getResult

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
  def sign(address: String, sha3HashOfDataToSign: String): String =
    web3j.ethSign(address, sha3HashOfDataToSign).send.getSignature

  def stringFrom(databaseName: String, keyName: String): String =
    web3j.dbGetString(databaseName, keyName).send.getStoredValue

  def stringTo(databaseName: String, keyName: String, stringToStore: String): Boolean =
    web3j.dbPutString(databaseName, keyName, stringToStore).send.valueStored

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getstorageat eth_getstorageat]] JSON-RPC endpoint.
    * @return the value from a storage position at a given address */
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

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionbyhash eth_gettransactionbyhash]] JSON-RPC endpoint.
    * @return Future containing Some(transaction object), or None when no transaction was found */
  def transactionByHash(transactionHash: String): Optional[Transaction] =
    web3j.ethGetTransactionByHash(transactionHash).send.getTransaction

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactioncount eth_gettransactioncount]] JSON-RPC endpoint.
    * @return the number of transactions sent from an address */
  def transactionCount(address: String, defaultBlockParameter: DefaultBlockParameter): BigInteger =
    web3j.ethGetTransactionCount(address, defaultBlockParameter).send.getTransactionCount

  def transactionReceipt(transactionHash: String): Optional[TransactionReceipt] =
    web3j.ethGetTransactionReceipt(transactionHash).send.getTransactionReceipt

  def uncleByBlockNumberAndIndex(defaultBlockParameter: DefaultBlockParameter, transactionIndex: BigInteger): EthBlock.Block =
    web3j.ethGetUncleByBlockNumberAndIndex(defaultBlockParameter, transactionIndex).send.getBlock

  def uncleByBlockHashAndIndex(blockHash: String, transactionIndex: BigInteger): EthBlock.Block =
    web3j.ethGetUncleByBlockHashAndIndex(blockHash, transactionIndex).send.getBlock

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getunclecountbyblockhash eth_getunclecountbyblockhash]] JSON-RPC endpoint.
    * @return the number of uncles in a block from a block matching the given block hash */
  def uncleCountByBlockHash(blockHash: String): BigInteger =
    web3j.ethGetUncleCountByBlockHash(blockHash).send.getUncleCount

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getunclecountbyblocknumber eth_getunclecountbyblocknumber]] JSON-RPC endpoint.
    * @return the number of uncles in a block from a block matching the given block number */
  def uncleCountByBlockNumber(defaultBlockParameter: DefaultBlockParameter): BigInteger =
    web3j.ethGetUncleCountByBlockNumber(defaultBlockParameter).send.getUncleCount

  def uninstallFilter(filterId: BigInteger): Boolean = web3j.ethUninstallFilter(filterId).send.isUninstalled

  def uninstallShhFilter(filterId: BigInteger): Boolean = web3j.shhUninstallFilter(filterId).send.isUninstalled

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#web3_clientversion web3_clientversion]] JSON-RPC endpoint.
    * @return the Web3J client version used by this client */
  def versionWeb3J: String = web3j.web3ClientVersion.send.getWeb3ClientVersion

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#net_version net_version]] JSON-RPC endpoint.
    * @return the current network id */
  def versionNet: String = web3j.netVersion.send.getNetVersion

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_protocolversion eth_protocolversion]] JSON-RPC endpoint.
    * @return ethereum protocol version used by this client */
  def versionProtocol: String = web3j.ethProtocolVersion.send.getProtocolVersion

  def versionShh: String = web3j.shhVersion.send.getVersion

  def work: EthGetWork = web3j.ethGetWork.send
}
