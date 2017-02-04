/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitsharesmunich.cryptocoincore.base.seed;

import de.bitsharesmunich.cryptocoincore.base.Coin;
import de.bitsharesmunich.cryptocoincore.base.GeneralCoinManager;
import de.bitsharesmunich.cryptocoincore.base.SeedType;
import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinManager;

/**
 *
 * @author javier
 */
public class SeedFactory {
    public static GeneralCoinManager getSeed(SeedType seedType){
        switch(seedType){
            case BIP39:
                //return new BIP39();
        }
        
        return null;
    }
}
