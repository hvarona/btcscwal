package de.bitsharesmunich.cryptocoincore.steem;

import com.google.common.primitives.Bytes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.bitsharesmunich.graphenej.AssetAmount;
import de.bitsharesmunich.graphenej.BaseOperation;
import de.bitsharesmunich.graphenej.OperationType;

import java.lang.reflect.Type;

/**
 * Class used to encapsulate the TransferOperation operation related
 * functionalities. TODO: Add extensions support
 */
public class SteemTransferOperation extends BaseOperation {

    private static final String TAG = "SteemTransferOperation";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_EXTENSIONS = "extensions";
    public static final String KEY_FROM = "from";
    public static final String KEY_TO = "to";
    public static final String KEY_MEMO = "memo";

    private SteemAssetAmount amount;
    private String from;
    private String to;
    private String memo;
    private String[] extensions;

    /**
     * Empty constructor is needed in some situations where only part of the
     * transfer operation data is relevant.
     */
    public SteemTransferOperation() {
        super(OperationType.transfer_operation);
    }

    /**
     * Constructor with the basic transfer operation. Use this if you will setup
     * the fee later.
     *
     * @param from
     * @param to
     * @param transferAmount
     */
    public SteemTransferOperation(String from, String to, SteemAssetAmount transferAmount) {
        super(OperationType.transfer_operation);
        this.from = from;
        this.to = to;
        this.amount = transferAmount;
        this.memo = "";
    }

    @Override
    public void setFee(AssetAmount newFee) {
        
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public SteemAssetAmount getTransferAmount() {
        return this.amount;
    }

    public AssetAmount getFee() {
        return null;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setAmount(SteemAssetAmount amount) {
        this.amount = amount;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public byte[] serializeFixedString(String toSerial) {
        byte[] result = new byte[toSerial.length() + 1];
        result[0] = (byte) toSerial.length();
        for (int i = 0; i < toSerial.length(); i++) {
            result[i + 1] = (byte) toSerial.charAt(i);
        }
        return result;
    }

    @Override
    public byte[] toBytes() {
        byte[] fromBytes = serializeFixedString(from);
        byte[] toBytes = serializeFixedString(to);
        byte[] amountBytes = amount.toBytes();
        byte[] memoBytes = serializeFixedString(memo);

        return Bytes.concat(fromBytes, toBytes, amountBytes, memoBytes);
    }

    @Override
    public String toJsonString() {
        //TODO: Evaluate using simple Gson class to return a simple string representation and drop the TransferSerializer class
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SteemTransferOperation.class, new TransferSerializer());
        return gsonBuilder.create().toJson(this);
    }

    @Override
    public JsonElement toJsonObject() {
        JsonArray array = new JsonArray();
        array.add(this.getId());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KEY_FROM, from);
        jsonObject.addProperty(KEY_TO, to);
        jsonObject.addProperty(KEY_AMOUNT, amount.toString());
        jsonObject.add(KEY_MEMO, new Gson().toJsonTree(memo));
        jsonObject.add(KEY_EXTENSIONS, new JsonArray());
        array.add(jsonObject);
        return array;
    }

    public static class TransferSerializer implements JsonSerializer<SteemTransferOperation> {

        @Override
        public JsonElement serialize(SteemTransferOperation transfer, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonArray arrayRep = new JsonArray();
            arrayRep.add(transfer.getId());
            arrayRep.add(transfer.toJsonObject());
            return arrayRep;
        }
    }

    /**
     * This deserializer will work on any transfer operation serialized in the
     * 'array form' used a lot in the Graphene Blockchain API.
     *
     * An example of this serialized form is the following:
     *
     * [
     * 0, { "from": "ACCOUNTNAME1", "to": "ACCOUNTNAME2", "amount": "0.1 STEEM"} ]
     *
     * It will convert this data into a nice TransferOperation object.
     */
    public static class TransferDeserializer implements JsonDeserializer<SteemTransferOperation> {

        @Override
        public SteemTransferOperation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonArray()) {
                // This block is used just to check if we are in the first step of the deserialization
                // when we are dealing with an array.
                JsonArray serializedTransfer = json.getAsJsonArray();
                if (serializedTransfer.get(0).getAsInt() != OperationType.transfer_operation.ordinal()) {
                    // If the operation type does not correspond to a transfer operation, we return null
                    return null;
                } else {
                    // Calling itself recursively, this is only done once, so there will be no problems.
                    return context.deserialize(serializedTransfer.get(1), SteemTransferOperation.class);
                }
            } else {
                // This block is called in the second recursion and takes care of deserializing the
                // transfer data itself.
                JsonObject jsonObject = json.getAsJsonObject();

                // Deserializing AssetAmount objects
                SteemAssetAmount amount = context.deserialize(jsonObject.get(KEY_AMOUNT), SteemAssetAmount.class);

                // Deserializing UserAccount objects
                String from = jsonObject.get(KEY_FROM).getAsString();
                String to = jsonObject.get(KEY_TO).getAsString();
                SteemTransferOperation transfer = new SteemTransferOperation(from, to, amount);

                // Deserializing Memo if it exists
                if (jsonObject.get(KEY_MEMO) != null) {
                    JsonObject memoObj = jsonObject.get(KEY_MEMO).getAsJsonObject();
                    transfer.setMemo(memoObj.getAsString());
                }
                return transfer;
            }
        }
    }
}
