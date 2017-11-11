package demo

import com.micronautics.web3j.{Address, Nonce, Password, Wallet}
import org.web3j.crypto.{Credentials, RawTransaction, TransactionEncoder}
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert.Unit.{ETHER, WEI}

/** Web3J provides support for both working with Ethereum wallet files (recommended) and Ethereum client admin commands
  * for sending transactions. */
class DemoTransaction(demo: Demo) {
  import Demo._, demo._

  //  Send Ether to another party using your Ethereum wallet file
  val password: Password = Password("bogus") // todo make this real
  val credentials: Credentials = Wallet.loadCredentials(password, walletDir)
  val transactionReceipt: TransactionReceipt =
    Transfer.sendFunds(web3j, credentials, "0x...", BigDecimal.valueOf(0.01).bigDecimal, ETHER).send
  println(format(transactionReceipt))

  // todo use a real value of address
  val address: Address = Address("address")

  // Before creating a custom transaction, first get the next available nonce
  val nonce: Nonce = demo.web3jScala.sync.nextNonce(address)

  // Create a custom transaction
  val rawTransaction: RawTransaction =
    RawTransaction.createEtherTransaction(nonce.bigInteger, gasPrice.bigInteger, gasLimit, "toAddress", BigInt(1).bigInteger)
  println(format(rawTransaction))

  // Sign & send the transaction
  val signedMessage: Array[Byte] = TransactionEncoder.signMessage(rawTransaction, credentials)
  val hexValue: String = javax.xml.bind.DatatypeConverter.printHexBinary(signedMessage)
  val transactionHash: String = web3j.ethSendRawTransaction(hexValue).send.getTransactionHash

  // Now let's transfer some funds. Be sure a wallet is available in the client’s keystore. TODO how?
  // One option is to use web3j’s `Transfer` class for transacting with Ether.
  Transfer.sendFunds(web3j, credentials, "toAddress", BigDecimal(1).bigDecimal, WEI)

  // Here is how to use the Ethereum client’s admin commands:
  val web3jAdmin: Admin = Admin.build(new HttpService)
  val personalUnlockAccount: PersonalUnlockAccount = web3jAdmin.personalUnlockAccount("0x000...", "a password").sendAsync.get
  if (personalUnlockAccount.accountUnlocked) {
       // todo send a transaction
  }

  // Todo demonstrate the use of Parity’s Personal, Trace, or geth’s personal client APIs, by using the org.web3j:parity and org.web3j:geth modules respectively.

  protected def format(tx: RawTransaction): String =
    s"""Raw transaction:
       |  Data         = ${ tx.getData }
       |  Gas limit    = ${ tx.getGasLimit }
       |  Gas price    = ${ tx.getGasPrice }
       |  Gas limit    = ${ tx.getGasLimit }
       |  Nonce        = ${ tx.getNonce }
       |  To           = ${ tx.getTo }
       |  Value        = ${ tx.getValue }
       |""".stripMargin

  protected def format(tx: TransactionReceipt): String =
    s"""Transaction receipt:
       |  Block hash              = ${ tx.getBlockHash }
       |  Block number            = ${ tx.getBlockNumber }
       |  Raw block number        = ${ tx.getBlockNumberRaw }
       |  Contract address        = ${ tx.getContractAddress }
       |  Cumulative gas used     = ${ tx.getCumulativeGasUsed }
       |  Raw cumulative gas used = ${ tx.getCumulativeGasUsedRaw }
       |  From                    = ${ tx.getFrom }
       |  Gas used                = ${ tx.getGasUsed }
       |  Raw gas used            = ${ tx.getGasUsedRaw }
       |  Logs                    = ${ tx.getLogs }
       |  Log bloom               = ${ tx.getLogsBloom }
       |  Root                    = ${ tx.getRoot }
       |  To                      = ${ tx.getTo }
       |  Transaction hash        = ${ tx.getTransactionHash }
       |  Transaction index       = ${ tx.getTransactionIndex }
       |  Raw transaction index   = ${ tx.getTransactionIndexRaw }
       |""".stripMargin
}
