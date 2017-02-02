package de.bitsharesmunich.cryptocoincore.base;

/**
 *
 * @author Henry
 */
public abstract class CryptoCoinAccount {
    
    protected CryptoCoinAccountSeed seed;
    
    public CryptoCoinAccountSeed getSeed(){
        return this.seed;
    }
    
    //public abstract CryptoCoinAccount getFromJson(JSONObject json);
    
    //public abstract String toJsonString();   
    
    //public abstract CryptoCoinContactBook getContactBook();
    
    public abstract CrytpoCoinBalance getBalance();
    
    //public abstract CryptoCoinTransfer transfer(CryptoCoinAccount to, double ammount, String description, CryptoCoinTransferData additionalData);
}
