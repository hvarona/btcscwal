package de.bitsharesmunich.cryptocoincore.base;

/**
 *
 * @author Henry
 */
public class CryptoCoinFactory {
    
    public static CryptoCoinManager getObjectFactory(Coin coin){
        switch(coin){
            case BITCOIN:
                //return new BitcoinObjectsFactory();
        }
        
        return null;
    }
    
}
