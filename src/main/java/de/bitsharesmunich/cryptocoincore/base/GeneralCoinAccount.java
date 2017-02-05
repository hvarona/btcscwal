package de.bitsharesmunich.cryptocoincore.base;

/**
 *
 * @author Henry
 */
public abstract class GeneralCoinAccount extends CryptoCoinAccount{
    protected int accountNumber;
    protected int lastExternalIndex;
    protected int lastChangeIndex;

    public GeneralCoinAccount(String id, String name, Coin coin, final AccountSeed seed, int accountNumber, int lastExternalIndex, int lastChangeIndex) {
        super(id, name, coin, seed);
        this.accountNumber = accountNumber;
        this.lastExternalIndex = lastExternalIndex;
        this.lastChangeIndex = lastChangeIndex;
    }
    
    public int getAccountNumber() {
        return accountNumber;
    }

    public int getLastExternalIndex() {
        return lastExternalIndex;
    }

    public int getLastChangeIndex() {
        return lastChangeIndex;
    }
    
}
