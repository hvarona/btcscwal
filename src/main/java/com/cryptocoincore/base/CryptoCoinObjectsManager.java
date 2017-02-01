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
public abstract class CryptoCoinObjectsManager<T extends CryptoCoinAccount> {
    
    public abstract T newAccount();
    
    //public abstract T getAccount(CryptoCoinAccountId id);
    
}
