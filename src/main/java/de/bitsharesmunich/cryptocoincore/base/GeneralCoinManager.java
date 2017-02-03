package de.bitsharesmunich.cryptocoincore.base;

/**
 *
 * The manager for a general crypto coin
 * 
 * @author Henry
 */
public abstract class GeneralCoinManager<T extends CryptoCoinAccount> {
    
    public abstract T newAccount();
    
    public abstract T getAccount(String id, String name, AccountSeed seed, int accountIndex, int externalIndex, int changeIndex);
    
    public abstract T getAccountFromJsonSeed(String jsonSeed);
    
}
