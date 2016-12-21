/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cryptocoincore.base;

/**
 *
 * @author javier
 */
public abstract class CryptoCoinAccount {
    
    protected CryptoCoinAccountId id;
    
    public CryptoCoinAccountId getId(){
        return this.id;
    }
    
    //public abstract CryptoCoinAccount(JSONObject json);
    
    //public abstract JSONObject toJson();   
    
    //public abstract CryptoCoinContactBook getContactBook();
    
    public abstract String getBalance();
    
    //public abstract CryptoCoinTransfer transfer(CryptoCoinAccount to, double ammount, String description, CryptoCoinTransferData additionalData);
}
