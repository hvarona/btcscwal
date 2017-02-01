package de.bitsharesmunich.cryptocoincore.base;

import java.util.List;

/**
 *
 * @author henry
 */
public abstract class CryptoCoinAccountSeed {
    
    private final CryptoCoinSeedType type;
    private final List<String> MnemonicCode;
    private final String additional;
        
    public abstract byte[] getSeed();

    public CryptoCoinSeedType getType() {
        return type;
    }

    public List<String> getMnemonicCode() {
        return MnemonicCode;
    }

    public String getAdditional() {
        return additional;
    }

    public CryptoCoinAccountSeed(CryptoCoinSeedType type, List<String> MnemonicCode, String additional) {
        this.type = type;
        this.MnemonicCode = MnemonicCode;
        this.additional = additional;
    }
    
    public boolean loadFromJsonString(String json){
        return this.loadFromJsonString(json, "");
    }
        
    public abstract boolean loadFromJsonString(String json, String password);
    
    public String getJsonString(){
        return this.getJsonString("");
    }
    
    public abstract String getJsonString(String password);
}
