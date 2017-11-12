package com.micronautics.web3j

import java.io.File
import java.time.format.DateTimeFormatter
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import org.web3j.crypto.{Credentials, ECKeyPair, Keys, WalletFile, WalletUtils, Wallet => WebWallet}

object Wallet {
  protected lazy val dateTimeFormat: DateTimeFormatter =
    DateTimeFormatter.ofPattern("'UTC--'yyyy-MM-dd'T'HH-mm-ss.nVV'--'")

  protected val objectMapper: ObjectMapper = new ObjectMapper()
    .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


  @inline def apply(password: String, destinationDirectory: File, isFullWallet: Boolean = true): Wallet =
    Wallet(password, destinationDirectory, isFullWallet)

  @inline def defaultKeyDirectory: String = WalletUtils.getDefaultKeyDirectory

  /** Cursory check to see if this is a plausible Address; does not actually verify the address */
  @inline def isValidAddress(address: Address): Boolean = WalletUtils.isValidAddress(address.value)

  @inline def loadCredentials(password: String, source: File): Credentials =
    WalletUtils.loadCredentials(password, source)

  @inline def loadCredentials(password: Password, source: String): Credentials =
    WalletUtils.loadCredentials(password.value, source)

  @inline def mainNetKeyDirectory: File = new File(WalletUtils.getMainnetKeyDirectory)

  @inline def rinkebyKeyDirectory: File =
     new File(s"$defaultKeyDirectory${ File.separator }rinkeby${ File.separator }keystore")

  @inline def ropstenKeyDirectory: File =
     new File(s"$defaultKeyDirectory${ File.separator }ropsten${ File.separator }keystore")

  @inline def testNetKeyDirectory: File = new File(WalletUtils.getTestnetKeyDirectory)
}

case class Wallet(password: String, destinationDirectory: File, isFullWallet: Boolean) {
  import Wallet._

  val ecKeyPair: ECKeyPair = Keys.createEcKeyPair

  val walletFile: WalletFile = if (isFullWallet) WebWallet.createStandard(password, ecKeyPair)
  else WebWallet.createLight(password, ecKeyPair)

  val walletPath: File = new File(destinationDirectory, fileName)

  Wallet.objectMapper.writeValue(walletPath, walletFile)

  // end of constructor, start of properties

  lazy val address: Address = Address(walletFile.getAddress)

  // todo what is this? Is it useful to expose?
  lazy val crypto: WalletFile.Crypto = walletFile.getCrypto

  /** fixme is this really an address? */
  lazy val id: Address = Address(walletFile.getId)

  lazy val version: Int = walletFile.getVersion


  @inline override def toString: String = s"Wallet at $walletPath"


  protected def fileName: String = {
    val now = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC)
    now.format(dateTimeFormat) + walletFile.getAddress + ".json"
  }
}
