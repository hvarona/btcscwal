package de.bitsharesmunich.cryptocoincore.base;

/**
 *
 * @author Henry
 */
public abstract class CryptoCoinManager<T extends CryptoCoinAccount> {
    
    public abstract T newAccount();
    
    public abstract T getAccount(AccountSeed seed);
    
    public abstract T getAccountFromJsonSeed(String jsonSeed);
    
}
