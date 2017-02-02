/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitsharesmunich.cryptocoincore.base;

/**
 *
 * @author javier
 */
public abstract class CryptoCoinAccount {
    
    protected String id;
    protected Coin coin;
    protected CryptoCoinAccountSeed seed;
    
    public CryptoCoinAccountSeed getSeed(){
        return this.seed;
    }
    
    //public abstract CryptoCoinAccount(JSONObject json);

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
    
    
    //public abstract JSONObject toJson();   
    
    //public abstract CryptoCoinContactBook getContactBook();
    
    public abstract String getBalance();
    
    //public abstract CryptoCoinTransfer transfer(CryptoCoinAccount to, double ammount, String description, CryptoCoinTransferData additionalData);
}
