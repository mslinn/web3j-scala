package com.micronautics

/** <img src='https://docs.web3j.io/_static/web3j.png' align='right' height='100px'>
  * This project is an idiomatic Scala wrapper around [[https://www.web3j.io Web3J]] for Ethereum.
  * This project is built with Scala 2.12, and requires the Java 8 runtime; it is not yet compatible with Java 9.
  *
  * The [[https://github.com/ethereum/wiki/wiki/JSON-RPC Ethereum JSON-RPC documentation]]
  * was the source of the comments.
  *
  * The JSON-RPC [[https://github.com/ethereum/wiki/wiki/JSON-RPC#the-default-block-parameter default block parameter]]
  * can represent a `QUANTITY` or a `TAG`.
  * Quantities are expressed as integer block numbers, and tags are strings with one of the values "latest", "earliest" or "pending".
  * The [[https://jar-download.com/javaDoc/org.web3j/core/3.0.2/index.html?org/web3j/protocol/core/DefaultBlockParameter.html Java implementation]]
  * performs the same function as the JSON-RPC analog.
  *
  * [[https://github.com/ethereum/wiki/wiki/Whisper Whisper]]
  * is a communication protocol of the Ethereum P2P protocol suite that allows
  * for messaging between users' DApps (Ethereum Distributed Applications).
  * Whisper uses the same network that the blockchain runs on.
  * The protocol is separate from the blockchain, so smart contracts do not have access to whispers.
  *
  * [[https://jar-download.com/javaDoc/org.web3j/core/3.0.2/index.html?org/web3j/protocol/core/methods/response/EthBlock.html EthBlock]]
  * and its internal objects such as
  * [[https://jar-download.com/javaDoc/org.web3j/core/3.0.2/index.html?org/web3j/protocol/core/methods/response/EthBlock.Block.html Block]]
  * are returned by the following methods: {{{blockByHash}}}, {{{blockByNumber}}}, {{{uncleByBlockHashAndIndex}}}, {{{uncleByBlockNumberAndIndex}}}.
  */
package object web3j
