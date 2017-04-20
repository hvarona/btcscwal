package de.bitsharesmunich.cryptocoincore.steem;

import de.bitsharesmunich.graphenej.AssetAmount;
import de.bitsharesmunich.graphenej.BaseOperation;
import de.bitsharesmunich.graphenej.BlockData;
import de.bitsharesmunich.graphenej.Transaction;
import de.bitsharesmunich.graphenej.TransactionBuilder;
import de.bitsharesmunich.graphenej.TransferOperation;
import de.bitsharesmunich.graphenej.UserAccount;
import de.bitsharesmunich.graphenej.errors.MalformedTransactionException;

import org.bitcoinj.core.ECKey;

import java.util.ArrayList;
import java.util.List;



/**
 * Class used to build a transaction containing a transfer operation.
 */
public class SteemTransferTransactionBuilder extends TransactionBuilder {
    private List<BaseOperation> operations;
    private UserAccount sourceAccount;
    private UserAccount destinationAccount;
    private AssetAmount transferAmount;
    private AssetAmount feeAmount;
    private String memo;

    public SteemTransferTransactionBuilder(){}

    public SteemTransferTransactionBuilder(ECKey privKey) {
        super(privKey);
    }

    public SteemTransferTransactionBuilder setPrivateKey(ECKey key){
        this.privateKey = key;
        return this;
    }

    public SteemTransferTransactionBuilder setBlockData(BlockData blockData){
        this.blockData = blockData;
        return this;
    }

    public SteemTransferTransactionBuilder setSource(UserAccount source){
        this.sourceAccount = source;
        return this;
    }

    public SteemTransferTransactionBuilder setDestination(UserAccount destination){
        this.destinationAccount = destination;
        return this;
    }

    public SteemTransferTransactionBuilder setAmount(AssetAmount amount){
        this.transferAmount = amount;
        return this;
    }

    public SteemTransferTransactionBuilder setFee(AssetAmount amount){
        this.feeAmount = amount;
        return this;
    }

    public SteemTransferTransactionBuilder setMemo(String memo){
        this.memo = memo;
        return this;
    }

    //TODO: Add support for multiple transfer operations in a single transaction
    public SteemTransferTransactionBuilder addOperation(TransferOperation transferOperation){
        if(operations == null){
            operations = new ArrayList<BaseOperation>();
        }
        return this;
    }

    @Override
    public Transaction build() throws MalformedTransactionException {
        if(privateKey == null){
            throw new MalformedTransactionException("Missing private key information");
        }else if(operations == null){
            // If the operations list has not been set, we might be able to build one with the
            // previously provided data. But in order for this to work we have to have all
            // source, destination and transfer amount data.
            operations = new ArrayList<>();
            if(sourceAccount == null){
                throw new MalformedTransactionException("Missing source account information");
            }
            if(destinationAccount == null){
                throw new MalformedTransactionException("Missing destination account information");
            }
            if(transferAmount == null){
                throw new MalformedTransactionException("Missing transfer amount information");
            }
            SteemTransferOperation transferOperation;
            if(feeAmount == null){
                transferOperation = new SteemTransferOperation(sourceAccount, destinationAccount, transferAmount);
            }else{
                transferOperation = new SteemTransferOperation(sourceAccount, destinationAccount, transferAmount, feeAmount);
            }
            if(memo != null){
                transferOperation.setMemo(this.memo);
            }else{
                transferOperation.setMemo("");
            }
            operations.add(transferOperation);
        }
        return new Transaction(privateKey, blockData, operations);
    }
}
