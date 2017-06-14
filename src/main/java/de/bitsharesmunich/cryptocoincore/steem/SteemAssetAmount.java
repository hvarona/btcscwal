package de.bitsharesmunich.cryptocoincore.steem;

import de.bitsharesmunich.graphenej.*;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedLong;
import com.google.gson.*;
import de.bitsharesmunich.graphenej.interfaces.ByteSerializable;
import de.bitsharesmunich.graphenej.interfaces.JsonSerializable;

import java.lang.reflect.Type;

/**
 * Created by nelson on 11/7/16.
 */
public class SteemAssetAmount implements ByteSerializable, JsonSerializable {

    /**
     * Constants used in the JSON serialization procedure.
     */
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_ASSET = "asset";

    private UnsignedLong amount;
    private String asset = "STEEM";

    public SteemAssetAmount(UnsignedLong amount) {
        this.amount = amount;
    }

    public void setAmount(UnsignedLong amount) {
        this.amount = amount;
    }

    public UnsignedLong getAmount() {
        return this.amount;
    }

    public String getAsset() {
        return this.asset;
    }

    @Override
    public byte[] toBytes() {
        byte[] value = Util.revertLong(this.amount.longValue());
        byte[] assetByte = new byte[6];
        assetByte[0] = 6; //precision
        assetByte[1] = (byte) '3';
        for (int i = 2; i < 6; i++) {
            assetByte[i] = (byte) asset.charAt(i - 1);
        }
        return Bytes.concat(value, assetByte);
    }

    @Override
    public String toJsonString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SteemAssetAmount.class, new AssetSerializer());
        return gsonBuilder.create().toJson(this);
    }

    @Override
    public JsonObject toJsonObject() {
        return new Gson().toJsonTree(amount + " " + asset).getAsJsonObject();
    }

    /**
     * Custom serializer used to translate this object into the JSON-formatted
     * entry we need for a transaction.
     */
    public static class AssetSerializer implements JsonSerializer<SteemAssetAmount> {

        @Override
        public JsonElement serialize(SteemAssetAmount assetAmount, Type type, JsonSerializationContext jsonSerializationContext) {
            return new Gson().toJsonTree(assetAmount.amount + " " + assetAmount.asset);
        }
    }

    public static class AssetDeserializer implements JsonDeserializer<SteemAssetAmount> {

        @Override
        public SteemAssetAmount deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String[] amountString = json.getAsString().split(" ");
            Long amount = Long.parseLong(amountString[0]);
            String asset = amountString[1].substring(1);
            SteemAssetAmount assetAmount = new SteemAssetAmount(UnsignedLong.valueOf(amount));
            return assetAmount;
        }
    }
}
