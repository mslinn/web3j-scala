package com.micronautics.web3j

import java.math.BigInteger
import org.web3j.protocol.core.methods.response.EthCompileSolidity

case class Address(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}

case class Compiler(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}

case class Digest(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}

// todo decide which type of hash this is; we already have Keccak256Hash (Keccak); maybe the usages should be separated into various HashTypes
// @see See [[https://github.com/ethereum/wiki/wiki/Ethash Ethash]] for proof of work
case class Hash(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}

case class FilterId(value: BigInteger) extends AnyVal {
  @inline override def toString: String = value.toString
}

case class LLLCompiled(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}

case class LLLSource(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}

/** An account nonce is a transaction counter, provided by Ethereum for each Account.
  * Nonces prevent replay attacks wherein a transaction sending Ether from A to B can be replayed by B over and over to continually drain A's balance
  * @see See [[https://github.com/ethereum/wiki/wiki/Glossary Glossary]] */
case class Nonce(value: BigInt) extends AnyVal {
  @inline def bigInteger: BigInteger = value.bigInteger

  @inline override def toString: String = value.toString
}

case class SerpentCompiled(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}

case class SerpentSource(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}

/** The SHA3 hash is more properly referred to as
  * [[https://ethereum.stackexchange.com/questions/550/which-cryptographic-hash-function-does-ethereum-use Keccak-256]] */
case class Keccak256Hash(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}

case class Signature(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}

/** Web3J already has [[EthCompileSolidity.Code]] to represent Solidity compiled code */
case class SoliditySource(value: String) extends AnyVal {
  @inline override def toString: String = value.toString
}
