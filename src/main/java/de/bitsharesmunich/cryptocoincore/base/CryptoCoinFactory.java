package de.bitsharesmunich.cryptocoincore.base;


import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinObjectsManager;
/**
 *
 * @author Henry
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
