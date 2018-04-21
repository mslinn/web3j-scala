package com.micronautics.web3j

import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.methods.request
import org.web3j.protocol.core.methods.response.{EthBlock, EthCompileSolidity, EthGetWork, EthLog, ShhMessages, Transaction, TransactionReceipt}
import scala.collection.JavaConverters._
import scala.collection.immutable.List
import scala.compat.java8.FutureConverters._
import scala.compat.java8.OptionConverters._
import scala.concurrent.ExecutionContext.{global => defaultExecutionContext}
import scala.concurrent.{ExecutionContext, Future}

/** All of the methods in this class return a [[scala.concurrent.Future]] and do not block.
  * @param web3j can be shared with [[EthereumSynchronous]]
  * @param ec if no [[concurrent.ExecutionContext]] is implicitly available, the default Scala [[concurrent.ExecutionContext]] is used. */
class EthereumASynchronous(val web3j: Web3j)
                          (implicit val ec: ExecutionContext = defaultExecutionContext) {

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_accounts eth_accounts]] JSON-RPC endpoint.
    * @return the list of addresses owned by the client */
  def accounts: Future[List[Address]] =
    web3j.ethAccounts.sendAsync.toScala.map(_.getAccounts.asScala.toList.map(Address.stringToAddress))

  /** Add the given identity address to the Whisper group.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_addtogroup shh_addtogroup]] JSON-RPC endpoint.
    * @return true if the identity was successfully added to the group */
  def addToGroup(identityAddress: Address): Future[Boolean] =
    web3j.shhAddToGroup(identityAddress.value).sendAsync.toScala.map(_.addedToGroup)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getbalance eth_getbalance]] JSON-RPC endpoint.
    * @param defaultBlockParameter either an integer block number, or the string "latest", "earliest" or "pending".
    * See the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#the-default-block-parameter specification]].
    * @return the balance of the account of given address */
  def balance(address: Address, defaultBlockParameter: DefaultBlockParameter): Future[Ether] =
    web3j.ethGetBalance(address.value, defaultBlockParameter).sendAsync.toScala.map(x => Ether(x.getBalance))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getblockbyhash eth_getblockbyhash]] JSON-RPC endpoint.
    * @return Option[EthBlock.Block] */
  def blockByHash(blockHash: BlockHash, returnFullTransactionObjects: Boolean): Future[Option[EthBlock.Block]] =
    web3j.ethGetBlockByHash(blockHash.value, returnFullTransactionObjects).sendAsync.toScala.map(x => Option(x.getBlock))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getblockbyhash eth_getblockbyhash]] JSON-RPC endpoint.
    * @return Some(block object), or None if no block was found */
  def blockByNumber(
    defaultBlockParameter: DefaultBlockParameter,
    returnFullTransactionObjects: Boolean = false
  ): Future[Option[EthBlock.Block]] =
    web3j
      .ethGetBlockByNumber(defaultBlockParameter, returnFullTransactionObjects)
      .sendAsync
      .toScala
      .map(x => Option(x.getBlock))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_blocknumber eth_blocknumber]] JSON-RPC endpoint.
    * @return the number of the most recent block */
  def blockNumber: Future[BigInt] = web3j.ethBlockNumber.sendAsync.toScala.map(_.getBlockNumber)

  def blockTransactionCountByHash(blockHash: String): Future[BigInt] =
    web3j.ethGetBlockTransactionCountByHash(blockHash).sendAsync.toScala.map(_.getTransactionCount)

  def blockTransactionCountByNumber(defaultBlockParameter: DefaultBlockParameter): Future[BigInt] =
    web3j.ethGetBlockTransactionCountByNumber(defaultBlockParameter).sendAsync.toScala.map(_.getTransactionCount)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_call eth_call]] JSON-RPC endpoint.
    * @return value of executed contract, without creating a transaction on the block chain */
  def call(transaction: request.Transaction, defaultBlockParameter: DefaultBlockParameter): Future[String] =
    web3j.ethCall(transaction, defaultBlockParameter).sendAsync.toScala.map(_.getValue)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getcode eth_getcode]] JSON-RPC endpoint.
    * @return code at a given address */
  def code(address: Address, defaultBlockParameter: DefaultBlockParameter): Future[String] =
    web3j.ethGetCode(address.value, defaultBlockParameter).sendAsync.toScala.map(_.getCode)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_coinbase eth_coinbase]] JSON-RPC endpoint.
    * @return the client coinbase address */
  def coinbaseAddress: Future[Address] = web3j.ethCoinbase.sendAsync.toScala.map(x => Address(x.getAddress))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_compilelll eth_compilelll]] JSON-RPC endpoint.
    * @return compiled LLL code */
  def compileLLL(sourceCode: LLLSource): Future[LLLCompiled] =
    web3j.ethCompileLLL(sourceCode.value).sendAsync.toScala.map(x => LLLCompiled(x.getCompiledSourceCode))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_compileserpent eth_compileserpent]] JSON-RPC endpoint.
    * @return compiled Serpent code */
  def compileSerpent(sourceCode: SerpentSource): Future[SerpentCompiled] =
    web3j.ethCompileSerpent(sourceCode.value).sendAsync.toScala.map(x => SerpentCompiled(x.getCompiledSourceCode))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_compilesolidity eth_compilesolidity]] JSON-RPC endpoint.
    * @return compiled Solidity code */
  def compileSolidity(sourceCode: SoliditySource): Future[Map[String, EthCompileSolidity.Code]] =
    web3j.ethCompileSolidity(sourceCode.value).sendAsync.toScala.map(_.getCompiledSolidity.asScala.toMap)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getcompilers eth_getcompilers]] JSON-RPC endpoint.
    * @return a list of available compilers found by the underlying Web3J library */
  def compilers: Future[List[Compiler]] =
    web3j.ethGetCompilers.sendAsync.toScala.map(_.getCompilers.asScala.toList.map(Compiler.stringToCompiler))

  /** Makes a call or transaction, which won't be added to the blockchain and returns the used gas, which can be used
    * for estimating the used gas.
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_estimategas eth_estimategas]] JSON-RPC endpoint.
    * @return amount of gas estimated */
  def estimateGas(transaction: request.Transaction): Future[Ether] =
    web3j.ethEstimateGas(transaction).sendAsync.toScala.map(x => Ether(x.getAmountUsed))

  /** Polling method for an eth filter.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getfilterchanges eth_getfilterchanges]] JSON-RPC endpoint.
    * @return List of log items since last poll, could be Nil if nothing changed since the last poll */
  def filterChangesEth(filterId: FilterId): Future[List[EthLog.LogResult[_]]] =
    web3j.ethGetFilterChanges(filterId.bigInteger).sendAsync.toScala.map(_.getLogs.asScala.toList)

  /** Polling method for a Whisper filter.
    *
    * Note: calling shh_getMessages will reset the buffer for this method to avoid duplicate messages.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_getfilterchanges shh_getfilterchanges]] JSON-RPC endpoint.
    * @return List of messages since the last poll; could be Nil if nothing changed since the last poll */
  def filterChangesShh(filterId: FilterId): Future[List[ShhMessages.SshMessage]] =
    web3j.shhGetFilterChanges(filterId.bigInteger).sendAsync.toScala.map(_.getMessages.asScala.toList)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gasprice eth_gasprice]] JSON-RPC endpoint.
    * @return the current price per gas in wei */
  def gasPrice: Future[Ether] = web3j.ethGasPrice.sendAsync.toScala.map(x => Ether(x.getGasPrice))

  /** Used for submitting mining hash rate
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_submithashrate eth_submithashrate]] JSON-RPC endpoint.
    * @return true if submitting successfully */
  def hashRate(hashRate: String, clientId: String): Future[Boolean] =
    web3j.ethSubmitHashrate(hashRate, clientId).sendAsync.toScala.map(_.submissionSuccessful)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_hashrate eth_hashrate]] JSON-RPC endpoint.
    * @return the number of hashes per second that the node is mining at */
  def hashRate: Future[BigInt] = web3j.ethHashrate.sendAsync.toScala.map(_.getHashrate)

  /** Checks if the client hold the private keys for a given identity.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_hasidentity shh_hasidentity]] JSON-RPC endpoint.
    * @return returns true if this client holds the private key for that identity */
  def hasIdentity(identityAddress: Address): Future[Boolean] =
    web3j.shhHasIdentity(identityAddress.value).sendAsync.toScala.map(_.hasPrivateKeyForIdentity)

  /** Retrieves binary data from the local database.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#db_gethex db_gethex]] JSON-RPC endpoint.
    * @return the retrieved value */
  @deprecated("deprecated", "")
  def hexFrom(databaseName: String, keyName: String): Future[String] =
    web3j.dbGetHex(databaseName, keyName).sendAsync.toScala.map(_.getStoredValue)

  /** Stores binary data in the local database.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#db_puthex db_puthex]] JSON-RPC endpoint.
    * @return true if the value was stored */
  @deprecated("deprecated", "")
  def hexTo(databaseName: String, keyName: String, dataToStore: String): Future[Boolean] =
    web3j.dbPutHex(databaseName, keyName, dataToStore).sendAsync.toScala.map(_.valueStored)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#net_listening net_listening]] JSON-RPC endpoint.
    * @return true if this client is actively listening for network connections */
  def isListening: Future[Boolean] = web3j.netListening.sendAsync.toScala.map(_.isListening)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_mining eth_mining]] JSON-RPC endpoint. */
  def isMining: Future[Boolean] = web3j.ethMining.sendAsync.toScala.map(_.isMining)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_syncing eth_syncing]] JSON-RPC endpoint. */
  def isSyncing: Future[Boolean] = web3j.ethSyncing.sendAsync.toScala.map(_.isSyncing)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getlogs eth_getlogs]] JSON-RPC endpoint.
    * @return List of all log items matching a given filter object */
  def logs(ethFilter: request.EthFilter): Future[List[EthLog.LogResult[_]]] =
    web3j.ethGetLogs(ethFilter).sendAsync.toScala.map(_.getLogs.asScala.toList)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getfilterlogs eth_getfilterlogs]] JSON-RPC endpoint.
    * @return List of all log items with the matching filter id */
  def logs(filterId: FilterId): Future[List[EthLog.LogResult[_]]] =
    web3j.ethGetFilterLogs(filterId.bigInteger).sendAsync.toScala.map(_.getLogs.asScala.toList)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_getmessages shh_getmessages]] JSON-RPC endpoint.
    * @return all Whisper messages matching a filter */
  def messages(filterId: FilterId): Future[List[ShhMessages.SshMessage]] =
    web3j.shhGetMessages(filterId.bigInteger).sendAsync.toScala.map(_.getMessages.asScala.toList)

  /** Creates a filter in the node, to notify when the state changes (logs).
    * To check if the state has changed, call `filterChanges`.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_newblockfilter eth_newblockfilter]] JSON-RPC endpoint.
    * @return filter id */
  def newBlockFilter: Future[FilterId] = web3j.ethNewBlockFilter.sendAsync.toScala.map(x => FilterId(x.getFilterId))

  /** Get the next available nonce before creating a transaction */
  // todo should a Nonce type be created?
  def nextNonce(address: Address): Future[Nonce] = nonce(address, LATEST)

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
  def newFilter(ethFilter: request.EthFilter): Future[FilterId] =
    web3j.ethNewFilter(ethFilter).sendAsync.toScala.map(x => FilterId(x.getFilterId))

  /** Create filter that notifies the client when whisper message is received that matches the filter options.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_newfilter shh_newfilter]] JSON-RPC endpoint.
    * @return The newly created filter as a BigInt */
  def newFilter(shhFilter: request.ShhFilter): Future[FilterId] =
    web3j.shhNewFilter(shhFilter).sendAsync.toScala.map(x => FilterId(x.getFilterId))

  /** New Whisper group.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_newgroup shh_newgroup]] JSON-RPC endpoint.
    * @return address of the new group */
  def newGroup: Future[Address] = web3j.shhNewGroup.sendAsync.toScala.map(x =>  Address(x.getAddress))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_newidentity shh_newidentity]] JSON-RPC endpoint.
    * @return address of the new whisper identity */
  def newIdentity: Future[Address] = web3j.shhNewIdentity.sendAsync.toScala.map(x => Address(x.getAddress))

  def newPendingTransactionFilter: Future[FilterId] =
    web3j.ethNewPendingTransactionFilter.sendAsync.toScala.map(x => FilterId(x.getFilterId))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactioncount eth_gettransactioncount]] JSON-RPC endpoint.
    * @see See [[https://github.com/ethereum/wiki/wiki/Glossary Glossary]]
    * @return the number of transactions sent from an address */
  def nonce(address: Address, defaultBlockParameter: DefaultBlockParameter): Future[Nonce] =
    web3j.ethGetTransactionCount(address.value, defaultBlockParameter).sendAsync.toScala.map(x => Nonce(x.getTransactionCount))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#net_peercount net_peercount]] JSON-RPC endpoint.
    * @return number of peers currently connected to this client */
  def peerCount: Future[BigInt] = web3j.netPeerCount.sendAsync.toScala.map(_.getQuantity)

  /** Sends a whisper message.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_post shh_post]] JSON-RPC endpoint.
    * @return true if the message was sent */
  def post(shhPost: request.ShhPost): Future[Boolean] = web3j.shhPost(shhPost).sendAsync.toScala.map(_.messageSent)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sendrawtransaction eth_sendrawtransaction]] JSON-RPC endpoint.
    * @return new message call transaction or a contract creation for signed transactions */
  def sendRawTransaction(signedTransactionData: SignedData): Future[TransactionHash] =
    web3j.ethSendRawTransaction(signedTransactionData.value).sendAsync.toScala.map(x => TransactionHash(x.getTransactionHash))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sendtransaction eth_sendtransaction]] JSON-RPC endpoint.
    * @return a new contract if the {{{Transaction.data}}} field contains code, else return a new transaction */
  def sendTransaction(transaction: request.Transaction): Future[TransactionHash] =
    web3j.ethSendTransaction(transaction).sendAsync.toScala.map(x => TransactionHash(x.getTransactionHash))

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#web3_sha3 web3_sha3]] JSON-RPC endpoint.
    * @param data the data to convert into an SHA3 hash
    * @return Keccak-256 hash (not the standardized SHA3-256 hash) of the given data */
  def sha3(data: String): Future[Keccak256Hash] = web3j.web3Sha3(data).sendAsync.toScala.map(x => Keccak256Hash(x.getResult))

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
  def sign(address: Address, sha3HashOfDataToSign: Keccak256Hash): Future[Signature] =
    web3j.ethSign(address.value, sha3HashOfDataToSign.value).sendAsync.toScala.map(x => Signature(x.getSignature))

  /** Obtains a string from the local database.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#db_getstring db_getstring]] JSON-RPC endpoint.
    * @return previously stored value */
  @deprecated("deprecated", "")
  def stringFrom(databaseName: String, keyName: String): Future[String] =
    web3j.dbGetString(databaseName, keyName).sendAsync.toScala.map(_.getStoredValue)

  /** Stores a string in the local database
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#db_putstring db_putstring]] JSON-RPC endpoint.
    * @return true if the value was stored */
  @deprecated("deprecated", "")
  def stringTo(databaseName: String, keyName: String, stringToStore: String): Future[Boolean] =
    web3j.dbPutString(databaseName, keyName, stringToStore).sendAsync.toScala.map(_.valueStored)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getstorageat eth_getstorageat]] JSON-RPC endpoint.
    * @return the value from a storage position at a given address */
  def storageAt(address: Address, position: BigInt, defaultBlockParameter: DefaultBlockParameter): Future[String] =
    web3j.ethGetStorageAt(address.value, position.bigInteger, defaultBlockParameter).sendAsync.toScala.map(_.getData)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionbyblockhashandindex eth_gettransactionbyblockhashandindex]] JSON-RPC endpoint.
    * @return Some containing transaction information by block hash and transaction index position, or None if no matching transaction was found */
  def transactionByBlockHashAndIndex(blockHash: BlockHash, transactionIndex: BigInt): Future[Option[Transaction]] =
    web3j.ethGetTransactionByBlockHashAndIndex(blockHash.value, transactionIndex.bigInteger).sendAsync.toScala.map(_.getTransaction.asScala)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionbyblocknumberandindex eth_gettransactionbyblocknumberandindex]] JSON-RPC endpoint.
    * @return Some containing transaction information by block hash and transaction index position, or None if no matching transaction was found */
  def transactionByBlockNumberAndIndex(
    defaultBlockParameter: DefaultBlockParameter,
    transactionIndex: BigInt
  ): Future[Option[Transaction]] =
    web3j
      .ethGetTransactionByBlockNumberAndIndex(defaultBlockParameter, transactionIndex.bigInteger)
      .sendAsync
      .toScala
      .map(_.getTransaction.asScala)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionbyhash eth_gettransactionbyhash]] JSON-RPC endpoint.
    * @return Future containing Some(transaction object), or None when no transaction was found */
  def transactionByHash(transactionHash: TransactionHash): Future[Option[Transaction]] =
    web3j.ethGetTransactionByHash(transactionHash.value).sendAsync.toScala.map(_.getTransaction.asScala)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionreceipt eth_gettransactionreceipt]] JSON-RPC endpoint.
    * @return the receipt of a transaction, identified by transaction hash. (Note: receipts are not available for pending transactions.) */
  def transactionReceipt(transactionHash: TransactionHash): Future[Option[TransactionReceipt]] =
    web3j.ethGetTransactionReceipt(transactionHash.value).sendAsync.toScala.map(_.getTransactionReceipt.asScala)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getunclecountbyblockhash eth_getunclecountbyblockhash]] JSON-RPC endpoint.
    * @return the number of uncles in a block from a block matching the given block hash */
  def uncleCountByBlockHash(blockHash: BlockHash): Future[BigInt] =
    web3j.ethGetUncleCountByBlockHash(blockHash.value).sendAsync.toScala.map(_.getUncleCount)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getunclecountbyblocknumber eth_getunclecountbyblocknumber]] JSON-RPC endpoint.
    * @return the number of uncles in a block from a block matching the given block number */
  def uncleCountByBlockNumber(defaultBlockParameter: DefaultBlockParameter): Future[BigInt] =
    web3j.ethGetUncleCountByBlockNumber(defaultBlockParameter).sendAsync.toScala.map(_.getUncleCount)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getunclebyblocknumberandindex eth_getunclebyblocknumberandindex]] JSON-RPC endpoint.
    * @return information about a uncle of a block by hash and uncle index position */
  def uncleByBlockNumberAndIndex(
    defaultBlockParameter: DefaultBlockParameter,
    transactionIndex: BigInt
  ): Future[EthBlock.Block] =
    web3j.ethGetUncleByBlockNumberAndIndex(defaultBlockParameter, transactionIndex.bigInteger).sendAsync.toScala.map(_.getBlock)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getunclebyblockhashandindex eth_getunclebyblockhashandindex]] JSON-RPC endpoint.
    * @return information about a uncle of a block by hash and uncle index position */
  def uncleByBlockHashAndIndex(blockHash: BlockHash, transactionIndex: BigInt): Future[EthBlock.Block] =
    web3j.ethGetUncleByBlockHashAndIndex(blockHash.value, transactionIndex.bigInteger).sendAsync.toScala.map(_.getBlock)

  /** Uninstalls a filter with the given id.
    * Should always be called when watch is no longer needed.
    *
    * Note: Filters time out when they aren't requested with filterChanges for a period of time.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_uninstallfilter eth_uninstallfilter]] JSON-RPC endpoint.
    * @return true if the filter was successfully uninstalled */
  def uninstallFilter(filterId: FilterId): Future[Boolean] =
    web3j.ethUninstallFilter(filterId.bigInteger).sendAsync.toScala.map(_.isUninstalled)

  /** Uninstalls a Whisper filter with the given id.
    * Should always be called when watch is no longer needed.
    *
    * Note: Filters time out when they aren't requested with filterChanges for a period of time.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_uninstallfilter shh_uninstallfilter]] JSON-RPC endpoint.
    * @return true if the filter was successfully uninstalled */
  def uninstallShhFilter(filterId: FilterId): Future[Boolean] =
    web3j.shhUninstallFilter(filterId.bigInteger).sendAsync.toScala.map(_.isUninstalled)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#web3_clientversion web3_clientversion]] JSON-RPC endpoint.
    * @return the Web3J client version used by this client */
  def versionWeb3J: Future[String] = web3j.web3ClientVersion.sendAsync.toScala.map(_.getWeb3ClientVersion)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#net_version net_version]] JSON-RPC endpoint.
    * @return the current network id */
  def versionNet: Future[String] = web3j.netVersion.sendAsync.toScala.map(_.getNetVersion)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_protocolversion eth_protocolversion]] JSON-RPC endpoint.
    * @return ethereum protocol version used by this client */
  def versionProtocol: Future[String] = web3j.ethProtocolVersion.sendAsync.toScala.map(_.getProtocolVersion)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_version shh_version]] JSON-RPC endpoint.
    * @return the current whisper protocol version. */
  def versionShh: Future[String] = web3j.shhVersion.sendAsync.toScala.map(_.getVersion)

  /** Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_getwork eth_getwork]] JSON-RPC endpoint.
    * @return the hash of the current block, the seedHash, and the boundary condition to be met ("target").
    * The Array with the following properties:
    *
    * DATA, 32 Bytes - current block header pow-hash
    * DATA, 32 Bytes - the seed hash used for the DAG.
    * DATA, 32 Bytes - the boundary condition ("target"), 2^^256 / difficulty. */
  def work: Future[EthGetWork] = web3j.ethGetWork.sendAsync.toScala

  /** Used for submitting a proof-of-work solution.
    *
    * Invokes the [[https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_submitwork eth_submitwork]] JSON-RPC endpoint.
    * @return true if the provided solution is valid */
  // fixme What type of hash is headerPowHash?
  def work(nonce: Nonce, headerPowHash: Keccak256Hash, mixDigest: Digest): Future[Boolean] =
    web3j.ethSubmitWork(nonce.toString, headerPowHash.value, mixDigest.value).sendAsync.toScala.map(_.solutionValid)
}
