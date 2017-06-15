/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitsharesmunich.cryptocoincore.steem;

import com.google.common.primitives.Bytes;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.bitsharesmunich.graphenej.BaseOperation;
import de.bitsharesmunich.graphenej.BlockData;
import de.bitsharesmunich.graphenej.Chains;
import de.bitsharesmunich.graphenej.Extensions;
import de.bitsharesmunich.graphenej.Transaction;
import static de.bitsharesmunich.graphenej.Transaction.KEY_EXPIRATION;
import static de.bitsharesmunich.graphenej.Transaction.KEY_EXTENSIONS;
import static de.bitsharesmunich.graphenej.Transaction.KEY_OPERATIONS;
import static de.bitsharesmunich.graphenej.Transaction.KEY_REF_BLOCK_NUM;
import static de.bitsharesmunich.graphenej.Transaction.KEY_REF_BLOCK_PREFIX;
import static de.bitsharesmunich.graphenej.Transaction.KEY_SIGNATURES;
import de.bitsharesmunich.graphenej.Util;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.bitcoinj.core.ECKey;

/**
 *
 * @author henry
 */
public class SteemTransaction extends Transaction {

    private final BlockData blockData;
    private final List<BaseOperation> operations;
    private final List<Extensions> extensions = new ArrayList();

    public SteemTransaction(ECKey privateKey, BlockData blockData, List<BaseOperation> operationList) {
        super(privateKey, blockData, operationList);
        this.blockData = blockData;
        this.operations = operationList;
    }

    public SteemTransaction(String wif, BlockData block_data, List<BaseOperation> operation_list) {
        super(wif, block_data, operation_list);
        this.blockData = block_data;
        this.operations = operation_list;
    }

    @Override
    public List<BaseOperation> getOperations() {
        return this.operations;
    }

    @Override
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();

        // Getting the signature before anything else,
        // since this might change the transaction expiration data slightly
        byte[] signature = getGrapheneSignature();

        // Formatting expiration time
        Date expirationTime = new Date(blockData.getRelativeExpiration() * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Adding expiration
        obj.addProperty(KEY_EXPIRATION, dateFormat.format(expirationTime));

        // Adding signatures
        JsonArray operationsArray = new JsonArray();
        for (BaseOperation operation : operations) {
            operationsArray.add(operation.toJsonObject());
        }
        // Adding operations
        obj.add(KEY_OPERATIONS, operationsArray);

        // Adding extensions
        obj.add(KEY_EXTENSIONS, new JsonArray());

        // Adding block data
        obj.addProperty(KEY_REF_BLOCK_NUM, blockData.getRefBlockNum());
        obj.addProperty(KEY_REF_BLOCK_PREFIX, blockData.getRefBlockPrefix());
        JsonArray signatureArray = new JsonArray();
        signatureArray.add(Util.bytesToHex(signature));
        obj.add(KEY_SIGNATURES, signatureArray);

        return obj;

    }
    
    @Override
    public byte[] toBytes(){
        // Creating a List of Bytes and adding the first bytes from the chain apiId
        List<Byte> byteArray = new ArrayList<>();
        byteArray.addAll(Bytes.asList(Util.hexToBytes("0000000000000000000000000000000000000000000000000000000000000000")));

        // Adding the block data
        byteArray.addAll(Bytes.asList(this.blockData.toBytes()));

        // Adding the number of operations
        byteArray.add((byte) this.operations.size());

        // Adding all the operations
        for(BaseOperation operation : operations){
            byteArray.add(operation.getId());
            byteArray.addAll(Bytes.asList(operation.toBytes()));
        }

        //Adding the number of extensions
        byteArray.add((byte) this.extensions.size());

        for(Extensions extension : this.extensions){
            //TODO: Implement the extensions serialization
        }
        // Adding a last zero byte to match the result obtained by the python-graphenelib code
        // I'm not exactly sure what's the meaning of this last zero byte, but for now I'll just
        // leave it here and work on signing the transaction.
        //TODO: Investigate the origin and meaning of this last byte.
        byteArray.add((byte) 0 );

        return Bytes.toArray(byteArray);
    }
    
    @Override
    public String toJsonString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SteemTransaction.class, new SteemTransactionSerializer());
        return gsonBuilder.create().toJson(this);
    }
    
    class SteemTransactionSerializer implements JsonSerializer<SteemTransaction> {

        @Override
        public JsonElement serialize(SteemTransaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {
            return transaction.toJsonObject();
        }
    }

}
