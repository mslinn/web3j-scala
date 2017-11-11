package com.micronautics.web3j

import java.io.File
import java.time.format.DateTimeFormatter
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import org.web3j.crypto.{Credentials, ECKeyPair, Keys, WalletFile, WalletUtils, Wallet => WebWallet}

object Wallet {
  protected lazy val dateTimeFormat: DateTimeFormatter =
    DateTimeFormatter.ofPattern("'UTC--'yyyy-MM-dd'T'HH-mm-ss.nVV'--'")

  protected val objectMapper = new ObjectMapper()
  objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
  objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


  @inline def apply(password: String, destinationDirectory: File, isFullWallet: Boolean = true): Wallet =
    Wallet(password, destinationDirectory, isFullWallet)

  @inline def defaultKeyDirectory: String = WalletUtils.getDefaultKeyDirectory

  @inline def isPrivateKey(key: PrivateKey): Boolean = WalletUtils.isValidPrivateKey(key.value)

  @inline def isValidAddress(address: Address): Boolean = WalletUtils.isValidAddress(address.value)

  @inline def loadCredentials(password: String, source: File): Credentials =
    WalletUtils.loadCredentials(password, source)

  @inline def loadCredentials(password: String, source: String): Credentials =
    WalletUtils.loadCredentials(password, source)

  @inline def mainnetKeyDirectory: File = new File(WalletUtils.getMainnetKeyDirectory)

  @inline def testnetKeyDirectory: File = new File(WalletUtils.getTestnetKeyDirectory)
}

case class Wallet(password: String, destinationDirectory: File, isFullWallet: Boolean) {
  import Wallet._

  val ecKeyPair: ECKeyPair = Keys.createEcKeyPair

  val walletFile: WalletFile = if (isFullWallet) WebWallet.createStandard(password, ecKeyPair)
  else WebWallet.createLight(password, ecKeyPair)

  val walletPath: File = new File(destinationDirectory, fileName)

  Wallet.objectMapper.writeValue(walletPath, walletFile)


  @inline override def toString: String = s"Wallet at $walletPath"

  protected def fileName: String = {
    val now = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC)
    now.format(dateTimeFormat) + walletFile.getAddress + ".json"
  }
}
