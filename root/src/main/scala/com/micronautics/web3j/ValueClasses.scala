package com.micronautics.web3j

import java.math.BigInteger
import org.web3j.crypto.WalletUtils

object Address {
  /** This implicit conversion allows for a convenient way of constructing an `Address` instance from a [[java.lang.String]].
    * {{{val address: Address = "0xdeadbeef"}}} */
  implicit def stringToAddress(value: String): Address = Address(value)
}

case class Address(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object BlockHash {
  /** This implicit conversion allows for a convenient way of constructing a `BlockHash` instance from a [[java.lang.String]].
    * {{{val blockHash: BlockHash = "0xdeadbeef"}}} */
  implicit def stringToBlockHash(value: String): BlockHash = BlockHash(value)
}

case class BlockHash(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object Compiler {
  /** This implicit conversion allows for a convenient way of constructing a `Compiler` instance from a [[java.lang.String]].
    * {{{val compiler: Compiler = "solc"}}} */
  implicit def stringToCompiler(value: String): Compiler = Compiler(value)
}

case class Compiler(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object Digest {
  /** This implicit conversion allows for a convenient way of constructing a `Digest` instance from a [[java.lang.String]].
    * {{{val digest: Digest = "0xdeadbeef"}}} */
  implicit def stringToDigest(value: String): Digest = Digest(value)
}

case class Digest(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object EtHash {
  /** This implicit conversion allows for a convenient way of constructing an `EtHash` instance from a [[java.lang.String]].
    * {{{val etHash: EtHash = "0xdeadbeef"}}} */
  implicit def stringToEtHash(value: String): EtHash = EtHash(value)
}

// @see See [[https://github.com/ethereum/wiki/wiki/Ethash Ethash]] for proof of work
case class EtHash(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object FilterId {
  /** This implicit conversion allows for a convenient way of constructing a `FilterId` instance from a [[scala.BigInt]].
    * {{{val filterId: FilterId = "0xdeadbeef"}}} */
  implicit def bigIntToFilterId(value: BigInt): FilterId = FilterId(value)
}

case class FilterId(value: BigInt) extends AnyVal {
  @inline def bigInteger: BigInteger = value.bigInteger

  @inline override def toString: String = value.toString
}


object Keccak256Hash {
  /** This implicit conversion allows for a convenient way of constructing a `Keccak256Hash` instance from a [[java.lang.String]].
    * {{{val keccak256Hash: Keccak256Hash = "0xdeadbeef"}}} */
  implicit def stringToKeccak256Hash(value: String): Keccak256Hash = Keccak256Hash(value)
}

/** The SHA3 hash is more properly referred to as
  * [[https://ethereum.stackexchange.com/questions/550/which-cryptographic-hash-function-does-ethereum-use Keccak-256]] */
/* todo better to use value classes to indicate functionality, not how something was created.
 * todo Delete this type and replace by one of the others, or rename it */
case class Keccak256Hash(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object LLLCompiled {
  /** This implicit conversion allows for a convenient way of constructing a `LLLCompiled` instance from a [[java.lang.String]].
    * {{{val lLLCompiled: LLLCompiled = "{hello, world}"}}} */
  implicit def stringToLLLCompiled(value: String): LLLCompiled = LLLCompiled(value)
}

case class LLLCompiled(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object LLLSource {
  /** This implicit conversion allows for a convenient way of constructing a `LLLSource` instance from a [[java.lang.String]].
    * {{{val lLLSource: LLLSource = "{hello, world}"}}} */
  implicit def stringToLLLSource(value: String): LLLSource = LLLSource(value)
}

case class LLLSource(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object Nonce {
  /** This implicit conversion allows for a convenient way of constructing a `Nonce` instance from a [[scala.BigInt]].
    * {{{val nonce: Nonce = BigInt("123456789")}}} */
  implicit def bigIntToNonce(value: BigInt): Nonce = Nonce(value)
}

/** An account nonce is a transaction counter, provided by Ethereum for each Account.
  * Nonces prevent replay attacks wherein a transaction sending Ether from A to B can be replayed by B over and over to
  * continually drain A's balance.
  * @see See [[https://github.com/ethereum/wiki/wiki/Glossary Glossary]] */
case class Nonce(value: BigInt) extends AnyVal {
  @inline def bigInteger: BigInteger = value.bigInteger

  @inline override def toString: String = value.toString
}


object Password {
  /** This implicit conversion allows for a convenient way of constructing a `Password` instance from a [[java.lang.String]].
    * {{{val password: Password = "secret"}}} */
  implicit def stringToPassword(value: String): Password = Password(value)
}

case class Password(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object PrivateKey {
  /** This implicit conversion allows for a convenient way of constructing a `PrivateKey` instance from a [[java.lang.String]].
    * {{{val privateKey: PrivateKey = "blah blah blah"}}} */
  implicit def stringToPrivateKey(value: String): PrivateKey = PrivateKey(value)
}

case class PrivateKey(value: String) extends AnyVal {
  @inline def isValid: Boolean = WalletUtils.isValidPrivateKey(value)

  @inline override def toString: String = value.toString
}


object PublicKey {
  /** This implicit conversion allows for a convenient way of constructing a `PublicKey` instance from a [[java.lang.String]].
    * {{{val publicKey: PublicKey = "blah blah blah"}}} */
  implicit def stringToPublicKey(value: String): PublicKey = PublicKey(value)
}

case class PublicKey(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object SerpentCompiled {
  /** This implicit conversion allows for a convenient way of constructing a `SerpentCompiled` instance from a [[java.lang.String]].
    * {{{val serpentCompiled: SerpentCompiled = "{hello, world}"}}} */
  implicit def stringToSerpentCompiled(value: String): SerpentCompiled = SerpentCompiled(value)
}

case class SerpentCompiled(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object SerpentSource {
  /** This implicit conversion allows for a convenient way of constructing a `SerpentSource` instance from a [[java.lang.String]].
    * {{{val serpentSource: SerpentSource = "{hello, world}"}}} */
  implicit def stringToSerpentSource(value: String): SerpentSource = SerpentSource(value)
}

case class SerpentSource(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object Signature {
  /** This implicit conversion allows for a convenient way of constructing a `Signature` instance from a [[java.lang.String]].
    * {{{val signature: Signature = "0xdeadbeef"}}} */
  implicit def stringToSignature(value: String): Signature = Signature(value)
}

case class Signature(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object SignedData {
  /** This implicit conversion allows for a convenient way of constructing a `SignedData` instance from a [[java.lang.String]].
    * {{{val signedData: SignedData = "0xdeadbeef"}}} */
  implicit def stringToSignedData(value: String): SignedData = SignedData(value)
}

case class SignedData(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object SoliditySource {
  /** This implicit conversion allows for a convenient way of constructing a `SoliditySource` instance from a [[java.lang.String]].
    * {{{val soliditySource: SoliditySource = "{hello world}"}}} */
  implicit def stringToSoliditySource(value: String): SoliditySource = SoliditySource(value)
}

/** Web3J already has [[org.web3j.protocol.core.methods.response.EthCompileSolidity.Code]] to represent Solidity compiled code */
case class SoliditySource(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object TransactionHash {
  /** This implicit conversion allows for a convenient way of constructing a `TransactionHash` instance from a [[java.lang.String]].
    * {{{val transactionHash: TransactionHash = "0xdeadbeef"}}} */
  implicit def stringToTransactionHash(value: String): TransactionHash = TransactionHash(value)
}

case class TransactionHash(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}
