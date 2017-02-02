package de.bitsharesmunich.cryptocoincore.base;

/**
 *
 * @author Henry
 */
public abstract class CryptoCoinAccount {
    
    protected String id;
    protected Coin coin;
    protected CryptoCoinAccountSeed seed;
    
    public CryptoCoinAccountSeed getSeed(){
        return this.seed;
    }
    
    //public abstract CryptoCoinAccount getFromJson(JSONObject json);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }
    
    //public abstract String toJsonString();   
    
    //public abstract CryptoCoinContactBook getContactBook();
    
    public abstract CrytpoCoinBalance getBalance();
    
    //public abstract CryptoCoinTransfer transfer(CryptoCoinAccount to, double ammount, String description, CryptoCoinTransferData additionalData);
}
