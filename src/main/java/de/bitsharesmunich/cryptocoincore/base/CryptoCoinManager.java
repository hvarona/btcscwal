package de.bitsharesmunich.cryptocoincore.base;

/**
 *
 * @author Henry
 */
public abstract class CryptoCoinManager<T extends CryptoCoinAccount> {
    
    public abstract T newAccount();
    
    //public abstract T getAccount(CryptoCoinAccountId id);
    
}
