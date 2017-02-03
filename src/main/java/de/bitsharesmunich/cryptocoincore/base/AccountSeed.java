package de.bitsharesmunich.cryptocoincore.base;

import de.bitsharesmunich.cryptocoincore.base.seed.BIP39;
import de.bitsharesmunich.cryptocoincore.base.seed.Brainkey;
import de.bitsharesmunich.cryptocoincore.util.Util;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Henry
 */
public abstract class AccountSeed {

    private String id;
    protected SeedType type;
    protected List<String> mnemonicCode;
    protected String additional;
    
    public abstract byte[] getSeed();

    public SeedType getType() {
        return type;
    }

    public List<String> getMnemonicCode() {
        return mnemonicCode;
    }

    public String getMnemonicCodeString(){
        StringJoiner joiner = new StringJoiner(" ","","");
        this.mnemonicCode.forEach(joiner::add);
        return joiner.toString();        
    }
    
    public String getAdditional() {
        return additional;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public AccountSeed(String id, SeedType type, List<String> MnemonicCode, String additional) {
        this.id = id;
        this.type = type;
        this.mnemonicCode = MnemonicCode;
        this.additional = additional;
    }

    public static AccountSeed loadFromJsonString(String jsonString, String password) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String typeString = jsonObject.getString("type");
            String mnemonic;
            if (jsonObject.has("mnemonic")) {
                mnemonic = jsonObject.getString("mnemonic");
            } else {

                byte[] encKey_enc = new BigInteger(jsonObject.getString("encryption_key"), 16).toByteArray();
                byte[] temp = new byte[encKey_enc.length - (encKey_enc[0] == 0 ? 1 : 0)];
                System.arraycopy(encKey_enc, (encKey_enc[0] == 0 ? 1 : 0), temp, 0, temp.length);
                byte[] encKey = Util.decryptAES(temp, password.getBytes("UTF-8"));
                temp = new byte[encKey.length];
                System.arraycopy(encKey, 0, temp, 0, temp.length);

                byte[] encBrain = new BigInteger(jsonObject.getString("encrypted_brainkey"), 16).toByteArray();
                while (encBrain[0] == 0) {
                    byte[] temp2 = new byte[encBrain.length - 1];
                    System.arraycopy(encBrain, 1, temp2, 0, temp2.length);
                    encBrain = temp2;
                }
                mnemonic = new String((Util.decryptAES(encBrain, temp)), "UTF-8");

            }
            String additional = jsonObject.getString("additional");

            switch (typeString) {
                case "BIP39":
                    return new BIP39(mnemonic, additional);
                case "BrainKey":
                    return new Brainkey(mnemonic, Integer.parseInt(additional));
                default:
                    break;
            }
        } catch (JSONException | UnsupportedEncodingException | NumberFormatException e) {
        }
        return null;
    }

    public String getJsonString() {
        return this.getJsonString("");
    }

    public String getJsonString(String password) {
        try {
            JSONObject answer = new JSONObject();
            answer.append("type", this.type);
            StringBuilder mnemonic = new StringBuilder();
            for (String word : this.mnemonicCode) {
                mnemonic.append(word);
                mnemonic.append(" ");
            }
            mnemonic.deleteCharAt(mnemonic.length() - 1);
            if (password.isEmpty()) {
                answer.append("mnemonic", mnemonic);
            } else {
                byte[] encKey = new byte[32];
                new SecureRandom().nextBytes(encKey);
                byte[] encKey_enc = Util.encryptAES(encKey, password.getBytes("UTF-8"));
                byte[] encMnem = Util.encryptAES(mnemonic.toString().getBytes("ASCII"), encKey);
                answer.append("encryption_key", Util.byteToString(encKey_enc));
                answer.append("encrypted_mnemonic", Util.byteToString(encMnem));
            }
            answer.append("additional", this.additional);
            return answer.toString();
        } catch (JSONException | UnsupportedEncodingException ex) {
            Logger.getLogger(AccountSeed.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
