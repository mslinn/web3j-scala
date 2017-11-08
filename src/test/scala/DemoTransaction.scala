import java.math.BigInteger
import com.micronautics.web3j.Ethereum
import org.web3j.crypto.{Credentials, RawTransaction, TransactionEncoder, WalletUtils}
import org.web3j.protocol.Web3j
import org.web3j.protocol.admin.Admin
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.{EthGetTransactionCount, EthSendTransaction, TransactionReceipt}
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert

class DemoTransaction(demo: Demo) {
  import demo._

  //  web3j provides support for both working with Ethereum wallet files (recommended) and Ethereum client admin commands for sending transactions.
  //  To send Ether to another party using your Ethereum wallet file:
  val credentials: Credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile")
  val transactionReceipt: TransactionReceipt =
    Transfer.sendFunds(web3j, credentials, "0x...", BigDecimal.valueOf(1.0).bigDecimal, Convert.Unit.ETHER).send()

  //  Or if you wish to create your own custom transaction, get the next available nonce
  val ethGetTransactionCount: EthGetTransactionCount = web3j.ethGetTransactionCount("address", DefaultBlockParameterName.LATEST).send()
  val nonce: BigInteger = ethGetTransactionCount.getTransactionCount

  // create our transaction
  val rawTransaction: RawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, "toAddress", BigInt(1).bigInteger)

  // sign & send our transaction
  val signedMessage: Array[Byte] = TransactionEncoder.signMessage(rawTransaction, credentials)
  val hexValue: String = javax.xml.bind.DatatypeConverter.printHexBinary(signedMessage)
  val ethSendTransaction: EthSendTransaction = web3j.ethSendRawTransaction(hexValue).send()

  //  Although it’s far simpler using web3j’s Transfer for transacting with Ether.
  // Using an Ethereum client’s admin commands (make sure you have your wallet in the client’s keystore):

//  val personalUnlockAccount: PersonalUnlockAccount = web3j.personalUnlockAccount("0x000...", "a password").sendAsync().get()
//  if (personalUnlockAccount.accountUnlocked()) {
//       send a transaction
//  }

  //  If you want to make use of Parity’s Personal or Trace, or Geth’s Personal client APIs, you can use the org.web3j:parity and org.web3j:geth modules respectively.
}
