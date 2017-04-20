package de.bitsharesmunich.cryptocoincore.steem;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedLong;
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
import de.bitsharesmunich.graphenej.Address;
import de.bitsharesmunich.graphenej.AssetAmount;
import de.bitsharesmunich.graphenej.BaseOperation;
import de.bitsharesmunich.graphenej.OperationType;
import de.bitsharesmunich.graphenej.UserAccount;
import de.bitsharesmunich.graphenej.Util;

import java.lang.reflect.Type;

import de.bitsharesmunich.graphenej.errors.MalformedAddressException;
import de.bitsharesmunich.graphenej.objects.Memo;

/**
 * Class used to encapsulate the TransferOperation operation related
 * functionalities. TODO: Add extensions support
 */
public class SteemTransferOperation extends BaseOperation {

    private static final String TAG = "TransferOperation";
    public static final String KEY_FEE = "fee";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_EXTENSIONS = "extensions";
    public static final String KEY_FROM = "from";
    public static final String KEY_TO = "to";
    public static final String KEY_MEMO = "memo";

    private AssetAmount fee;
    private AssetAmount amount;
    private UserAccount from;
    private UserAccount to;
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
     * Constructor used usually when we have all the transfer operation data,
     * including the fee.
     *
     * @param from
     * @param to
     * @param transferAmount
     * @param fee
     */
    public SteemTransferOperation(UserAccount from, UserAccount to, AssetAmount transferAmount, AssetAmount fee) {
        super(OperationType.transfer_operation);
        this.from = from;
        this.to = to;
        this.amount = transferAmount;
        this.fee = fee;
        this.memo = "";
    }

    /**
     * Constructor with the basic transfer operation. Use this if you will setup
     * the fee later.
     *
     * @param from
     * @param to
     * @param transferAmount
     */
    public SteemTransferOperation(UserAccount from, UserAccount to, AssetAmount transferAmount) {
        super(OperationType.transfer_operation);
        this.from = from;
        this.to = to;
        this.amount = transferAmount;
        this.fee = new AssetAmount(UnsignedLong.valueOf(0), transferAmount.getAsset());
        this.memo = "";
    }

    @Override
    public void setFee(AssetAmount newFee) {
        this.fee = newFee;
    }

    public UserAccount getFrom() {
        return this.from;
    }

    public UserAccount getTo() {
        return this.to;
    }

    public AssetAmount getTransferAmount() {
        return this.amount;
    }

    public AssetAmount getFee() {
        return this.fee;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setAmount(AssetAmount amount) {
        this.amount = amount;
    }

    public void setFrom(UserAccount from) {
        this.from = from;
    }

    public void setTo(UserAccount to) {
        this.to = to;
    }

    @Override
    public byte[] toBytes() {
        byte[] feeBytes = fee.toBytes();
        byte[] fromBytes = from.toBytes();
        byte[] toBytes = to.toBytes();
        byte[] amountBytes = amount.toBytes();

        byte[] memoBytes = new byte[memo.length() + 1];
        memoBytes[0] = (byte) memo.length();
        for (int i = 0; i < memo.length(); i++) {
            memoBytes[i + 1] = (byte) memo.charAt(i);
        }

        return Bytes.concat(feeBytes, fromBytes, toBytes, amountBytes, memoBytes);
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
        jsonObject.add(KEY_FEE, fee.toJsonObject());
        jsonObject.addProperty(KEY_FROM, from.toJsonString());
        jsonObject.addProperty(KEY_TO, to.toJsonString());
        jsonObject.add(KEY_AMOUNT, amount.toJsonObject());
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
     * 0, { "fee": { "amount": 264174, "asset_id": "1.3.0" }, "from":
     * "1.2.138632", "to": "1.2.129848", "amount": { "amount": 100, "asset_id":
     * "1.3.0" }, "extensions": [] } ]
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
                AssetAmount amount = context.deserialize(jsonObject.get(KEY_AMOUNT), AssetAmount.class);
                AssetAmount fee = context.deserialize(jsonObject.get(KEY_FEE), AssetAmount.class);

                // Deserializing UserAccount objects
                UserAccount from = new UserAccount(jsonObject.get(KEY_FROM).getAsString());
                UserAccount to = new UserAccount(jsonObject.get(KEY_TO).getAsString());
                SteemTransferOperation transfer = new SteemTransferOperation(from, to, amount, fee);

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
