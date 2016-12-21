/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cryptocoincore.base;

import com.cryptocoincore.bitcoin.BitcoinObjectsFactory;

/**
 *
 * @author javier
 */
public class CryptoCoinFactory {
    
    public static CryptoCoinObjectsFactory getObjectFactory(Coin coin){
        switch(coin){
            case BITCOIN:
                //return new BitcoinObjectsFactory();
        }
        
        return null;
    }
    
}
