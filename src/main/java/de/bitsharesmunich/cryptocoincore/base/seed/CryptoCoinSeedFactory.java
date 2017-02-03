/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitsharesmunich.cryptocoincore.base.seed;

import de.bitsharesmunich.cryptocoincore.base.Coin;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinManager;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinSeedType;
import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinManager;

/**
 *
 * @author javier
 */
public class CryptoCoinSeedFactory {
    public static CryptoCoinManager getSeed(CryptoCoinSeedType seedType){
        switch(seedType){
            case BIP39:
                return new CryptoCoinSeedBIP39();
        }
        
        return null;
    }
}
