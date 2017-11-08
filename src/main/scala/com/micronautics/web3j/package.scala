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
  *
  * [Whisper](https://github.com/ethereum/wiki/wiki/Whisper) is a communication protocol of the Ethereum P2P protocol suite that allows
  * for messaging between users' DApps (Ethereum Distributed Applications) to communicate with each other.
  * Whisper uses the same network that the blockchain runs on.
  * The protocol is separate from the blockchain, so smart contracts do not have access.
  *
  * <p style="clear: both">&nbsp;</p> */
package object web3j
