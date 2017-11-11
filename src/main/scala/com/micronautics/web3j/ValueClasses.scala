package com.micronautics.web3j

import java.math.BigInteger
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.core.methods.response.EthCompileSolidity

object Address {
  implicit def stringToAddress(value: String): Address = Address(value)
}

case class Address(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object Compiler {
  implicit def stringToCompiler(value: String): Compiler = Compiler(value)
}

case class Compiler(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object Digest {
  implicit def stringToDigest(value: String): Digest = Digest(value)
}

case class Digest(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object EtHash {
  implicit def stringToEtHash(value: String): EtHash = EtHash(value)
}

// @see See [[https://github.com/ethereum/wiki/wiki/Ethash Ethash]] for proof of work
case class EtHash(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object BlockHash {
  implicit def stringToBlockHash(value: String): BlockHash = BlockHash(value)
}

case class BlockHash(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object FilterId {
  implicit def stringToFilterId(value: BigInteger): FilterId = FilterId(value)
}

case class FilterId(value: BigInteger) extends AnyVal {
  @inline override def toString: String = value.toString
}


object LLLCompiled {
  implicit def stringToLLLCompiled(value: String): LLLCompiled = LLLCompiled(value)
}

case class LLLCompiled(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object LLLSource {
  implicit def stringToLLLSource(value: String): LLLSource = LLLSource(value)
}

case class LLLSource(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object Nonce {
  implicit def stringToNonce(value: BigInt): Nonce = Nonce(value)
}

/** An account nonce is a transaction counter, provided by Ethereum for each Account.
  * Nonces prevent replay attacks wherein a transaction sending Ether from A to B can be replayed by B over and over to
  * continually drain A's balance.
  * @see See [[https://github.com/ethereum/wiki/wiki/Glossary Glossary]] */
case class Nonce(value: BigInt) extends AnyVal {
  @inline def bigInteger: BigInteger = value.bigInteger

  @inline override def toString: String = value.toString
}


object PrivateKey {
  implicit def stringToPrivateKey(value: String): PrivateKey = PrivateKey(value)
}

case class PrivateKey(value: String) extends AnyVal {
  @inline def isValid: Boolean = WalletUtils.isValidPrivateKey(value)

  @inline override def toString: String = value.toString
}


object PublicKey {
  implicit def stringToPublicKey(value: String): PublicKey = PublicKey(value)
}

case class PublicKey(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object SerpentCompiled {
  implicit def stringToSerpentCompiled(value: String): SerpentCompiled = SerpentCompiled(value)
}

case class SerpentCompiled(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object SerpentSource {
  implicit def stringToSerpentSource(value: String): SerpentSource = SerpentSource(value)
}

case class SerpentSource(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object Keccak256Hash {
  implicit def stringToKeccak256Hash(value: String): Keccak256Hash = Keccak256Hash(value)
}

/** The SHA3 hash is more properly referred to as
  * [[https://ethereum.stackexchange.com/questions/550/which-cryptographic-hash-function-does-ethereum-use Keccak-256]] */
/* todo better to use value classes to indicate functionality, not how something was created.
 * todo Delete this type and replace by one of the others, or rename it */
case class Keccak256Hash(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object Password {
  implicit def stringToPassword(value: String): Password = Password(value)
}

case class Password(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object Signature {
  implicit def stringToSignature(value: String): Signature = Signature(value)
}

case class Signature(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object SignedData {
  implicit def stringToSignedData(value: String): SignedData = SignedData(value)
}

case class SignedData(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object SoliditySource {
  implicit def stringToSoliditySource(value: String): SoliditySource = SoliditySource(value)
}

/** Web3J already has [[EthCompileSolidity.Code]] to represent Solidity compiled code */
case class SoliditySource(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}


object TransactionHash {
  implicit def stringToTransactionHash(value: String): TransactionHash = TransactionHash(value)
}

case class TransactionHash(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}
