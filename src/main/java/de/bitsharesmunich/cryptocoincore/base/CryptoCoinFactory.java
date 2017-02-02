/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitsharesmunich.cryptocoincore.base;

import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinObjectsManager;

/**
 *
 * @author javier
 */
public class CryptoCoinFactory {
    
    public static CryptoCoinObjectsManager getObjectManager(Coin coin){
        switch(coin){
            case BITCOIN:
                return new BitcoinObjectsManager();
        }
        
        return null;
    }
    
}
