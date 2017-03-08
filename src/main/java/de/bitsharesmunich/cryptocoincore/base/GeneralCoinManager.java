package de.bitsharesmunich.cryptocoincore.base;

/**
 *
 * The manager for a general crypto coin
 *
 * @author Henry
 * @param <T> The class that holds and manage the account of the CryptoCoin
 */
public abstract class GeneralCoinManager<T extends GeneralCoinAccount> {

    public abstract T newAccount(AccountSeed seed, String name);

    public abstract T importAccount(AccountSeed seed, String name);

    public abstract T getAccount(long id, String name, AccountSeed seed, int accountIndex, int externalIndex, int changeIndex);

}
