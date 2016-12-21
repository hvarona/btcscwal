/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cryptocoincore.base;

import com.google.gson.JsonObject;

/**
 *
 * @author javier
 */
public abstract class CryptoCoinAccountId {
    
    //public abstract CryptoCoinAccountId(JSONObject json);
    
    public abstract boolean loadFromJsonString(String json);
    
    public abstract String getJsonString();
}
