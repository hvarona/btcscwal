package de.bitsharesmunich.cryptocoincore.steem;

import de.bitsharesmunich.graphenej.*;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedLong;
import com.google.gson.*;
import de.bitsharesmunich.graphenej.interfaces.ByteSerializable;
import de.bitsharesmunich.graphenej.interfaces.JsonSerializable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by nelson on 11/7/16.
 */
public class SteemAssetAmount implements ByteSerializable, JsonSerializable {

    /**
     * Constants used in the JSON serialization procedure.
     */
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_ASSET_ID = "asset_id";

    private UnsignedLong amount;
    private String asset;

    public SteemAssetAmount(UnsignedLong amount, String asset) {
        this.amount = amount;
        this.asset = asset;
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
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutput out = new DataOutputStream(byteArrayOutputStream);
        try {
            Varint.writeUnsignedVarLong(asset.instance, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] assetId = byteArrayOutputStream.toByteArray();
        byte[] value = Util.revertLong(this.amount.longValue());

        return Bytes.concat(value, assetId);
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
            Long amount = json.getAsJsonObject().get(KEY_AMOUNT).getAsLong();
            String assetId = json.getAsJsonObject().get(KEY_ASSET_ID).getAsString();
            //SteemAssetAmount assetAmount = new SteemAssetAmount(UnsignedLong.valueOf(amount), new Asset(assetId));
            //return assetAmount;
            return null;
        }
    }
}
