package com.micronautics.solidity;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.0.2.
 */
public final class BasicInfoGetter extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b60008054600160a060020a033316600160a060020a031990911617905561038f8061003b6000396000f3006060604052600436106100cf5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166332a2c5d081146100d457806341c0e1b5146101105780636f9fb98a146101255780636fd902e11461014a57806377e5bf841461015d578063796b89b9146101705780637a6ce2e11461018357806392b7d5b9146101965780639d5c6061146101a9578063a1188e56146101bc578063a17042cc146101cf578063b8077e28146101d7578063c8e7ca2e146101ea578063f8f46b5f14610274575b600080fd5b34156100df57600080fd5b6100e7610287565b60405173ffffffffffffffffffffffffffffffffffffffff909116815260200160405180910390f35b341561011b57600080fd5b61012361028b565b005b341561013057600080fd5b6101386102cc565b60405190815260200160405180910390f35b341561015557600080fd5b6101386102e7565b341561016857600080fd5b6101386102eb565b341561017b57600080fd5b6101386102ef565b341561018e57600080fd5b6100e76102f3565b34156101a157600080fd5b6101386102f7565b34156101b457600080fd5b6101386102fb565b34156101c757600080fd5b610138610303565b610138610307565b34156101e257600080fd5b6100e761030b565b34156101f557600080fd5b6101fd61030f565b60405160208082528190810183818151815260200191508051906020019080838360005b83811015610239578082015183820152602001610221565b50505050905090810190601f1680156102665780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561027f57600080fd5b6100e761034d565b3090565b6000543373ffffffffffffffffffffffffffffffffffffffff908116911614156102ca5760005473ffffffffffffffffffffffffffffffffffffffff16ff5b565b73ffffffffffffffffffffffffffffffffffffffff30163190565b4390565b3a90565b4290565b3390565b4590565b60005a905090565b4490565b3490565b3290565b610317610351565b6000368080601f016020809104026020016040519081016040528181529291906020840183838082843750949550505050505090565b4190565b602060405190810160405260008152905600a165627a7a7230582013a79aa2701d45e426efdad37282ae3be07edda51eed0dbac13611b8053a3fc80029";

    private BasicInfoGetter(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private BasicInfoGetter(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    /*public RemoteCall<String> getContractAddress() {
        Function function = new Function("getContractAddress",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }*/

    public RemoteCall<TransactionReceipt> kill() {
        Function function = new Function(
                "kill",
                Collections.emptyList(),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getContractBalance() {
        Function function = new Function("getContractBalance",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getCurrentBlockNumber() {
        Function function = new Function("getCurrentBlockNumber",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getTxGasprice() {
        Function function = new Function("getTxGasprice",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getBlockTimestamp() {
        Function function = new Function("getBlockTimestamp",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> getMsgSender() {
        Function function = new Function("getMsgSender",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> getCurrentGaslimit() {
        Function function = new Function("getCurrentGaslimit",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getMsgGas() {
        Function function = new Function("getMsgGas",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getCurrentDifficulty() {
        Function function = new Function("getCurrentDifficulty",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> getMsgValue(BigInteger weiValue) {
        Function function = new Function(
                "getMsgValue",
                Collections.emptyList(),
                Collections.emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<String> getTxOrigin() {
        Function function = new Function("getTxOrigin",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<byte[]> getMsgData() {
        Function function = new Function("getMsgData",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<DynamicBytes>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<String> getCurrentMinerAddress() {
        Function function = new Function("getCurrentMinerAddress",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<BasicInfoGetter> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(BasicInfoGetter.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<BasicInfoGetter> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(BasicInfoGetter.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static BasicInfoGetter load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BasicInfoGetter(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static BasicInfoGetter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BasicInfoGetter(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
