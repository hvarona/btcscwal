/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cryptocoincore.base;

import com.google.gson.JsonObject;
import java.util.List;

/**
 *
 * @author henry
 */
public abstract class CryptoCoinAccountSeed {
    
    private CryptoCoinSeedType type;
    private List<String> MnemonicCode;
    private String passphrase;
        
    public abstract byte[] getSeed();
    
    public boolean loadFromJsonString(String json){
        return this.loadFromJsonString(json, "");
    }
        
    public abstract boolean loadFromJsonString(String json, String password);
    
    public String getJsonString(){
        return this.getJsonString("");        
    }
    
    public abstract String getJsonString(String password);
}
