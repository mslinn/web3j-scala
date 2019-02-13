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
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.1.1.
 */
public class BasicInfoGetter extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060008054600160a060020a0319163317905561035d806100326000396000f3006080604052600436106100cf5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166332a2c5d081146100d457806341c0e1b5146101125780636f9fb98a146101295780636fd902e11461015057806377e5bf8414610165578063796b89b91461017a5780637a6ce2e11461018f57806392b7d5b9146101a45780639d5c6061146101b9578063a1188e56146101ce578063a17042cc146101e3578063b8077e281461018f578063c8e7ca2e146101eb578063f8f46b5f14610275575b600080fd5b3480156100e057600080fd5b506100e961028a565b6040805173ffffffffffffffffffffffffffffffffffffffff9092168252519081900360200190f35b34801561011e57600080fd5b5061012761028e565b005b34801561013557600080fd5b5061013e6102cb565b60408051918252519081900360200190f35b34801561015c57600080fd5b5061013e6102d0565b34801561017157600080fd5b5061013e6102d4565b34801561018657600080fd5b5061013e6102d8565b34801561019b57600080fd5b506100e96102dc565b3480156101b057600080fd5b5061013e6102e0565b3480156101c557600080fd5b5061013e6102e4565b3480156101da57600080fd5b5061013e6102ec565b61013e6102f0565b3480156101f757600080fd5b506102006102f4565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561023a578181015183820152602001610222565b50505050905090810190601f1680156102675780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561028157600080fd5b506100e961032d565b3090565b60005473ffffffffffffffffffffffffffffffffffffffff163314156102c95760005473ffffffffffffffffffffffffffffffffffffffff16ff5b565b303190565b4390565b3a90565b4290565b3390565b4590565b60005a905090565b4490565b3490565b60606000368080601f01602080910402602001604051908101604052809392919081815260200183838082843750949550505050505090565b41905600a165627a7a723058202010521da62f4a1983b79b72d096c0cd1d4d5f1ac77ce588b7f7729ede9202d50029";

    public static final String FUNC_GETCONTRACTADDRESS = "getContractAddress";

    public static final String FUNC_KILL = "kill";

    public static final String FUNC_GETCONTRACTBALANCE = "getContractBalance";

    public static final String FUNC_GETCURRENTBLOCKNUMBER = "getCurrentBlockNumber";

    public static final String FUNC_GETTXGASPRICE = "getTxGasprice";

    public static final String FUNC_GETBLOCKTIMESTAMP = "getBlockTimestamp";

    public static final String FUNC_GETMSGSENDER = "getMsgSender";

    public static final String FUNC_GETCURRENTGASLIMIT = "getCurrentGaslimit";

    public static final String FUNC_GETMSGGAS = "getMsgGas";

    public static final String FUNC_GETCURRENTDIFFICULTY = "getCurrentDifficulty";

    public static final String FUNC_GETMSGVALUE = "getMsgValue";

    public static final String FUNC_GETTXORIGIN = "getTxOrigin";

    public static final String FUNC_GETMSGDATA = "getMsgData";

    public static final String FUNC_GETCURRENTMINERADDRESS = "getCurrentMinerAddress";

    @Deprecated
    protected BasicInfoGetter(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BasicInfoGetter(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected BasicInfoGetter(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected BasicInfoGetter(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    // Clashes with super. Renamed getContractAddress2
    public RemoteCall<String> getContractAddress2() {
        final Function function = new Function(FUNC_GETCONTRACTADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> kill() {
        final Function function = new Function(
                FUNC_KILL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getContractBalance() {
        final Function function = new Function(FUNC_GETCONTRACTBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getCurrentBlockNumber() {
        final Function function = new Function(FUNC_GETCURRENTBLOCKNUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getTxGasprice() {
        final Function function = new Function(FUNC_GETTXGASPRICE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getBlockTimestamp() {
        final Function function = new Function(FUNC_GETBLOCKTIMESTAMP, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> getMsgSender() {
        final Function function = new Function(FUNC_GETMSGSENDER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> getCurrentGaslimit() {
        final Function function = new Function(FUNC_GETCURRENTGASLIMIT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getMsgGas() {
        final Function function = new Function(FUNC_GETMSGGAS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getCurrentDifficulty() {
        final Function function = new Function(FUNC_GETCURRENTDIFFICULTY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> getMsgValue(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_GETMSGVALUE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<String> getTxOrigin() {
        final Function function = new Function(FUNC_GETTXORIGIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<byte[]> getMsgData() {
        final Function function = new Function(FUNC_GETMSGDATA, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<String> getCurrentMinerAddress() {
        final Function function = new Function(FUNC_GETCURRENTMINERADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static BasicInfoGetter load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BasicInfoGetter(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static BasicInfoGetter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BasicInfoGetter(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static BasicInfoGetter load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new BasicInfoGetter(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static BasicInfoGetter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new BasicInfoGetter(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<BasicInfoGetter> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(BasicInfoGetter.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<BasicInfoGetter> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(BasicInfoGetter.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<BasicInfoGetter> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(BasicInfoGetter.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<BasicInfoGetter> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(BasicInfoGetter.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
