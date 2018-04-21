package com.micronautics.web3j

import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.methods.request
import org.web3j.protocol.core.methods.request.ShhFilter
import org.web3j.protocol.core.methods.response.{EthBlock, EthCompileSolidity, EthGetWork, EthLog, ShhMessages, Transaction, TransactionReceipt}
import scala.compat.java8.OptionConverters._

import scala.collection.JavaConverters._
import scala.collection.immutable.List

/** All of the methods in this class block until a value is ready to be returned to the caller.
  * @param web3j can be shared with [[EthereumASynchronous]] */
class EthereumSynchronous(val web3j: Web3j) {

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_accounts eth_accounts]] JSON-RPC endpoint.
    * @return the list of addresses owned by the client */
  def accounts: List[Address] = web3j.ethAccounts.send.getAccounts.asScala.toList.map(Address.stringToAddress)

  /** Add the given identity address to the Whisper group.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_addtogroup shh_addtogroup]] JSON-RPC endpoint.
    * @return true if the identity was successfully added to the group */
  def addToGroup(identityAddress: Address): Boolean = web3j.shhAddToGroup(identityAddress.value).send.addedToGroup

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getbalance eth_getbalance]] JSON-RPC endpoint.
    * @param defaultBlockParameter either an integer block number, or the string "latest", "earliest" or "pending".
    * See the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#the-default-block-parameter specification]].
    * @return the balance of the account at given address */
  def balance(address: Address, defaultBlockParameter: DefaultBlockParameter): Ether =
    Ether(web3j.ethGetBalance(address.value, defaultBlockParameter).send.getBalance)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getblockbyhash eth_getblockbyhash]] JSON-RPC endpoint.
    * @return Some(block object), or None if no block was found */
  def blockByHash(blockHash: BlockHash, returnFullTransactionObjects: Boolean): Option[EthBlock.Block] =
    Option(web3j.ethGetBlockByHash(blockHash.value, returnFullTransactionObjects).send.getBlock)

  def blockByNumber(
    defaultBlockParameter: DefaultBlockParameter,
    returnFullTransactionObjects: Boolean = false
  ): Option[EthBlock.Block] =
    Option(web3j.ethGetBlockByNumber(defaultBlockParameter, returnFullTransactionObjects).send.getBlock)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_blocknumber eth_blocknumber]] JSON-RPC endpoint.
    * @return the number of the most recent block */
  // todo define a type for BlockNumber?
  def blockNumber: BigInt = web3j.ethBlockNumber.send.getBlockNumber

  def blockTransactionCountByHash(blockHash: BlockHash): BigInt =
    web3j.ethGetBlockTransactionCountByHash(blockHash.value).send.getTransactionCount

  def blockTransactionCountByNumber(defaultBlockParameter: DefaultBlockParameter): BigInt =
    web3j.ethGetBlockTransactionCountByNumber(defaultBlockParameter).send.getTransactionCount

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_call eth_call]] JSON-RPC endpoint.
    * @return value of executed contract, without creating a transaction on the block chain */
  def call(transaction: request.Transaction, defaultBlockParameter: DefaultBlockParameter): String =
    web3j.ethCall(transaction, defaultBlockParameter).send.getValue

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getcode eth_getcode]] JSON-RPC endpoint.
    * @return code at a given address */
  def code(address: Address, defaultBlockParameter: DefaultBlockParameter): String =
    web3j.ethGetCode(address.value, defaultBlockParameter).send.getCode

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_coinbase eth_coinbase]] JSON-RPC endpoint.
    * @return the client coinbase address */
  def coinbaseAddress: Address = Address(web3j.ethCoinbase.send.getAddress)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_compilelll eth_compilelll]] JSON-RPC endpoint.
    * @return compiled LLL code */
  def compileLLL(sourceCode: LLLSource): LLLCompiled =
    LLLCompiled(web3j.ethCompileLLL(sourceCode.value).send.getCompiledSourceCode)

  def compileSerpent(sourceCode: SerpentSource): SerpentCompiled =
    SerpentCompiled(web3j.ethCompileSerpent(sourceCode.value).send.getCompiledSourceCode)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_compilesolidity eth_compilesolidity]] JSON-RPC endpoint.
    * @return compiled Solidity code */
  def compileSolidity(sourceCode: SoliditySource): Map[String, EthCompileSolidity.Code] =
    web3j.ethCompileSolidity(sourceCode.value).send.getCompiledSolidity.asScala.toMap

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getcompilers eth_getcompilers]] JSON-RPC endpoint.
    * @return a list of available compilers found by the underlying Web3J library */
  def compilers: List[Compiler] = web3j.ethGetCompilers.send.getCompilers.asScala.toList.map(Compiler.stringToCompiler)

  /** Makes a call or transaction, which won't be added to the blockchain and returns the used gas, which can be used
    * for estimating the used gas.
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_estimategas eth_estimategas]] JSON-RPC endpoint.
    * @return amount of gas estimated */
  def estimateGas(transaction: request.Transaction): Ether = Ether(web3j.ethEstimateGas(transaction).send.getAmountUsed)

  /** Polling method for an eth filter.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getfilterchanges eth_getfilterchanges]] JSON-RPC endpoint.
    * @return List of log items since last poll, could be an empty array if nothing has changed since last poll */
  def filterChangesEth(filterId: FilterId): List[EthLog.LogResult[_]] =
    web3j.ethGetFilterChanges(filterId.bigInteger).send.getLogs.asScala.toList

  /** Polling method for a Whisper filter.
    *
    * Note: calling shh_getMessages will reset the buffer for this method to avoid duplicate messages.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_getfilterchanges shh_getfilterchanges]] JSON-RPC endpoint.
    * @return List of messages since the last poll; could be Nil if nothing changed since the last poll */
  def filterChangesShh(filterId: FilterId): List[ShhMessages.SshMessage] =
    web3j.shhGetFilterChanges(filterId.bigInteger).send.getMessages.asScala.toList

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gasprice eth_gasprice]] JSON-RPC endpoint.
    * @return the current price per gas in wei */
  def gasPrice: Ether = Ether(web3j.ethGasPrice.send.getGasPrice)

  /** Query the hash rate.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_hashrate eth_hashrate]] JSON-RPC endpoint.
    * @return number of hashes per second that the node is mining at */
  def hashRate: BigInt = web3j.ethHashrate.send.getHashrate

  /** Used for submitting mining hash rate
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_submithashrate eth_submithashrate]] JSON-RPC endpoint.
    * @return true if submitting successfully */
  def hashRate(hashRate: String, clientId: String): Boolean =
    web3j.ethSubmitHashrate(hashRate, clientId).send.submissionSuccessful

  /** Checks if the client hold the private keys for a given identity.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_hasidentity shh_hasidentity]] JSON-RPC endpoint.
    * @return returns true if this client holds the private key for that identity */
  def hasIdentity(identityAddress: Address): Boolean =
    web3j.shhHasIdentity(identityAddress.value).send.hasPrivateKeyForIdentity

  /** Retrieves binary data from the local database.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#db_gethex db_gethex]] JSON-RPC endpoint.
    * @return the retrieved value */
  @deprecated("deprecated", "")
  def hexFrom(databaseName: String, keyName: String): String =
    web3j.dbGetHex(databaseName, keyName).send.getStoredValue

  /** Stores binary data in the local database.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#db_puthex db_puthex]] JSON-RPC endpoint.
    * @return true if the value was stored */
  @deprecated("deprecated", "")
  def hexTo(databaseName: String, keyName: String, dataToStore: String): Boolean =
    web3j.dbPutHex(databaseName, keyName, dataToStore).send.valueStored

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#net_listening net_listening]] JSON-RPC endpoint.
    * @return true if this client is actively listening for network connections */
  def isListening: Boolean = web3j.netListening.send.isListening

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_mining eth_mining]] JSON-RPC endpoint. */
  def isMining: Boolean    = web3j.ethMining.send.isMining

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_syncing eth_syncing]] JSON-RPC endpoint. */
  def isSyncing: Boolean   = web3j.ethSyncing.send.isSyncing

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getlogs eth_getlogs]] JSON-RPC endpoint.
    * @return List of all log items matching a given filter object */
  def logs(ethFilter: request.EthFilter): List[EthLog.LogResult[_]] =
    web3j.ethGetLogs(ethFilter).send.getLogs.asScala.toList

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getfilterlogs eth_getfilterlogs]] JSON-RPC endpoint.
    * @return List of all log items with the matching filter id */
  def logs(filterId: FilterId): List[EthLog.LogResult[_]] =
    web3j.ethGetFilterLogs(filterId.bigInteger).send.getLogs.asScala.toList

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_getmessages shh_getmessages]] JSON-RPC endpoint.
    * @return all Whisper messages matching a filter */
  def messages(filterId: FilterId): List[ShhMessages.SshMessage] =
    web3j.shhGetMessages(filterId.bigInteger).send.getMessages.asScala.toList

  /** Creates a filter in the node, to notify when the state changes (logs).
    * To check if the state has changed, call `filterChanges`.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_newblockfilter eth_newblockfilter]] JSON-RPC endpoint.
    * @return filter id */
  def newBlockFilter: FilterId = FilterId(web3j.ethNewBlockFilter.send.getFilterId)

  /** Creates a filter object, based on filter options, to notify when the state changes (logs).
    * To check if the state has changed, call `filterChanges`.
    *
    * Topics are order-dependent.
    * A transaction with a log with topics [A, B] will be matched by the following topic filters:
    *
    * - [] "anything"
    * - [A] "A in first position (and anything after)"
    * - [null, B] "anything in first position AND B in second position (and anything after)"
    * - [A, B] "A in first position AND B in second position (and anything after)"
    * - [ [A, B], [A, B] ] "(A OR B) in first position AND (A OR B) in second position (and anything after)"
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_newfilter eth_newfilter]] JSON-RPC endpoint.
    * @return filter id */
  def newFilter(ethFilter: request.EthFilter): FilterId = FilterId(web3j.ethNewFilter(ethFilter).send.getFilterId)

  /** Create filter that notifies the client when whisper message is received that matches the filter options.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_newfilter shh_newfilter]] JSON-RPC endpoint.
    * @return The newly created filter as a BigInt */
  def newFilter(shhFilter: ShhFilter): FilterId = FilterId(web3j.shhNewFilter(shhFilter).send.getFilterId)

  /** New Whisper group.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_newgroup shh_newgroup]] JSON-RPC endpoint.
    * @return address of the new group */
  def newGroup: Address = Address(web3j.shhNewGroup.send.getAddress)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_newidentity shh_newidentity]] JSON-RPC endpoint.
    * @return address of the new whisper identity */
  def newIdentity: Address = Address(web3j.shhNewIdentity.send.getAddress)

  def newPendingTransactionFilter: FilterId = FilterId(web3j.ethNewPendingTransactionFilter.send.getFilterId)

  /** Get the next available nonce before creating a transaction */
  def nextNonce(address: Address): Nonce = nonce(address, LATEST)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactioncount eth_gettransactioncount]] JSON-RPC endpoint.
    * @see See [[https://github.com/ethereum/wiki/wiki/Glossary Glossary]]
    * @return the number of transactions sent from an address */
  def nonce(address: Address, defaultBlockParameter: DefaultBlockParameter): Nonce =
    Nonce(web3j.ethGetTransactionCount(address.value, defaultBlockParameter).send.getTransactionCount)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#net_peercount net_peercount]] JSON-RPC endpoint.
    * @return number of peers currently connected to this client */
  def peerCount: BigInt = web3j.netPeerCount.send.getQuantity

  /** Sends a whisper message.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_post shh_post]] JSON-RPC endpoint.
    * @return true if the message was sent */
  def post(shhPost: request.ShhPost): Boolean = web3j.shhPost(shhPost).send.messageSent

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sendrawtransaction eth_sendrawtransaction]] JSON-RPC endpoint.
    * @return new message call transaction or a contract creation for signed transactions */
  def sendRawTransaction(signedTransactionData: SignedData): TransactionHash=
    TransactionHash(web3j.ethSendRawTransaction(signedTransactionData.value).send.getTransactionHash)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sendtransaction eth_sendtransaction]] JSON-RPC endpoint.
    * @return a new contract if the {{{Transaction.data}}} field contains code, else return a new transaction */
  def sendTransaction(transaction: request.Transaction): TransactionHash =
    TransactionHash(web3j.ethSendTransaction(transaction).send.getTransactionHash)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#web3_sha3 web3_sha3]] JSON-RPC endpoint.
    * @param data the data to convert into an SHA3 hash
    * @return Keccak-256 hash (not the standardized SHA3-256 hash) of the given data */
  def sha3(data: String): Keccak256Hash = Keccak256Hash(web3j.web3Sha3(data).send.getResult)

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
  def sign(address: Address, sha3HashOfDataToSign: String): Signature =
    Signature(web3j.ethSign(address.value, sha3HashOfDataToSign).send.getSignature)

  /** Obtains a string from the local database.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#db_getstring db_getstring]] JSON-RPC endpoint.
    * @return previously stored value */
  @deprecated("deprecated", "")
  def stringFrom(databaseName: String, keyName: String): String =
    web3j.dbGetString(databaseName, keyName).send.getStoredValue

  /** Stores a string in the local database
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#db_putstring db_putstring]] JSON-RPC endpoint.
    * @return true if the value was stored */
  @deprecated("deprecated", "")
  def stringTo(databaseName: String, keyName: String, stringToStore: String): Boolean =
    web3j.dbPutString(databaseName, keyName, stringToStore).send.valueStored

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getstorageat eth_getstorageat]] JSON-RPC endpoint.
    * @return the value from a storage position at a given address */
  def storageAt(address: Address, position: BigInt, defaultBlockParameter: DefaultBlockParameter): String =
    web3j.ethGetStorageAt(address.value, position.bigInteger, defaultBlockParameter).send.getData

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionbyblockhashandindex eth_gettransactionbyblockhashandindex]] JSON-RPC endpoint.
    * @return Some containing transaction information by block hash and transaction index position, or None if no matching transaction was found */
  def transactionByBlockHashAndIndex(blockHash: BlockHash, transactionIndex: BigInt): Option[Transaction] =
    web3j.ethGetTransactionByBlockHashAndIndex(blockHash.value, transactionIndex.bigInteger).send.getTransaction.asScala

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionbyblocknumberandindex eth_gettransactionbyblocknumberandindex]] JSON-RPC endpoint.
    * @return Some containing transaction information by block hash and transaction index position, or None if no matching transaction was found */
  def transactionByBlockNumberAndIndex(
    defaultBlockParameter: DefaultBlockParameter,
    transactionIndex: BigInt
  ): Option[Transaction] =
    web3j.ethGetTransactionByBlockNumberAndIndex(defaultBlockParameter, transactionIndex.bigInteger).send.getTransaction.asScala

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionbyhash eth_gettransactionbyhash]] JSON-RPC endpoint.
    * @return Future containing Some(transaction object), or None when no transaction was found */
  def transactionByHash(transactionHash: TransactionHash): Option[Transaction] =
    web3j.ethGetTransactionByHash(transactionHash.value).send.getTransaction.asScala

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionreceipt eth_gettransactionreceipt]] JSON-RPC endpoint.
    * @return the receipt of a transaction, identified by transaction hash. (Note: receipts are not available for pending transactions.) */
  def transactionReceipt(transactionHash: TransactionHash): Option[TransactionReceipt] =
    web3j.ethGetTransactionReceipt(transactionHash.value).send.getTransactionReceipt.asScala

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getunclebyblocknumberandindex eth_getunclebyblocknumberandindex]] JSON-RPC endpoint.
    * @return information about a uncle of a block by hash and uncle index position */
  def uncleByBlockNumberAndIndex(defaultBlockParameter: DefaultBlockParameter, transactionIndex: BigInt): EthBlock.Block =
    web3j.ethGetUncleByBlockNumberAndIndex(defaultBlockParameter, transactionIndex.bigInteger).send.getBlock

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getunclebyblockhashandindex eth_getunclebyblockhashandindex]] JSON-RPC endpoint.
    * @return information about a uncle of a block by hash and uncle index position */
  def uncleByBlockHashAndIndex(blockHash: BlockHash, transactionIndex: BigInt): EthBlock.Block =
    web3j.ethGetUncleByBlockHashAndIndex(blockHash.value, transactionIndex.bigInteger).send.getBlock

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getunclecountbyblockhash eth_getunclecountbyblockhash]] JSON-RPC endpoint.
    * @return the number of uncles in a block from a block matching the given block hash */
  def uncleCountByBlockHash(blockHash: BlockHash): BigInt =
    web3j.ethGetUncleCountByBlockHash(blockHash.value).send.getUncleCount

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getunclecountbyblocknumber eth_getunclecountbyblocknumber]] JSON-RPC endpoint.
    * @return the number of uncles in a block from a block matching the given block number */
  def uncleCountByBlockNumber(defaultBlockParameter: DefaultBlockParameter): BigInt =
    web3j.ethGetUncleCountByBlockNumber(defaultBlockParameter).send.getUncleCount

  /** Uninstalls a filter with given id.
    * Should always be called when watch is no longer needed.
    *
    * Note: Filters time out when they aren't requested with filterChanges for a period of time.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_uninstallfilter eth_uninstallfilter]] JSON-RPC endpoint.
    * @return true if the filter was successfully uninstalled */
  def uninstallFilter(filterId: FilterId): Boolean =
    web3j.ethUninstallFilter(filterId.bigInteger).send.isUninstalled

  /** Uninstalls a Whisper filter with the given id.
    * Should always be called when watch is no longer needed.
    *
    * Note: Filters time out when they aren't requested with filterChanges for a period of time.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_uninstallfilter shh_uninstallfilter]] JSON-RPC endpoint.
    * @return true if the filter was successfully uninstalled */
  def uninstallShhFilter(filterId: FilterId): Boolean =
    web3j.shhUninstallFilter(filterId.bigInteger).send.isUninstalled

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#web3_clientversion web3_clientversion]] JSON-RPC endpoint.
    * @return the Web3J client version used by this client */
  def versionWeb3J: String = web3j.web3ClientVersion.send.getWeb3ClientVersion

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#net_version net_version]] JSON-RPC endpoint.
    * @return the current network id */
  def versionNet: String = web3j.netVersion.send.getNetVersion

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_protocolversion eth_protocolversion]] JSON-RPC endpoint.
    * @return ethereum protocol version used by this client */
  def versionProtocol: String = web3j.ethProtocolVersion.send.getProtocolVersion

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_version shh_version]] JSON-RPC endpoint.
    * @return the current whisper protocol version. */
  def versionShh: String = web3j.shhVersion.send.getVersion

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getwork eth_getwork]] JSON-RPC endpoint.
    * @return the hash of the current block, the seedHash, and the boundary condition to be met ("target").
    * The Array with the following properties:
    *
    * DATA, 32 Bytes - current block header pow-hash
    * DATA, 32 Bytes - the seed hash used for the DAG.
    * DATA, 32 Bytes - the boundary condition ("target"), 2^^256 / difficulty. */
  def work: EthGetWork = web3j.ethGetWork.send

  /** Used for submitting a proof-of-work solution.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_submitwork eth_submitwork]] JSON-RPC endpoint.
    * @return true if the provided solution is valid */
  // todo what type of Hash should headerPowHash be?
  def work(nonce: Nonce, headerPowHash: Keccak256Hash, mixDigest: Digest): Boolean =
    web3j.ethSubmitWork(nonce.toString, headerPowHash.value, mixDigest.value).send.solutionValid
}
