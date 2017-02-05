package de.bitsharesmunich.cryptocoincore.base;

/**
 *
 * The manager for a general crypto coin
 * 
 * @author Henry
 */
public abstract class GeneralCoinManager<T extends GeneralCoinAccount> {
    
    public abstract T newAccount(AccountSeed seed);
    
    public abstract T getAccount(String id, String name, AccountSeed seed, int accountIndex, int externalIndex, int changeIndex);
   
}
