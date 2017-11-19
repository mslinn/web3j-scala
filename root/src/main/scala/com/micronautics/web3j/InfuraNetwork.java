package com.micronautics.web3j;

/** From the [[https://docs.web3j.io/transactions.html#ethereum-testnets web3j testnet docs]]:
 *  For development, its recommended you use the Rinkeby (geth only) or Kovan (Parity only) test networks.
 *  This is because they use a Proof of Authority (PoA) consensus mechanism,
 *  ensuring transactions and blocks are created in a consistent and timely manner.
 *  The Ropsten testnet, although closest to the Mainnet as it uses Proof of Work (PoW) consensus,
 *  has been subject to attacks in the past and tends to be more problematic for developers. */
public enum InfuraNetwork {
    MAINNET, KOVAN, RINKEBY, ROPSTEN
}
