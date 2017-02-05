package de.bitsharesmunich.cryptocoincore.bitcoin;

import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.GeneralCoinManager;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestNet3Params;

/**
 *
 * @author Henry
 */
public class BitcoinManager extends GeneralCoinManager<BitcoinAccount> {

    protected NetworkParameters netParams = TestNet3Params.get();
    static private BitcoinManager instance = null;

    private BitcoinManager() {

    }

    public static BitcoinManager getInstance() {
        if (BitcoinManager.instance == null) {
            BitcoinManager.instance = new BitcoinManager();
        }

        return BitcoinManager.instance;
    }

    @Override
    public BitcoinAccount newAccount(AccountSeed seed) {
        return new BitcoinAccount(seed);
    }

    @Override
    public BitcoinAccount getAccount(String id, String name, AccountSeed seed, int accountIndex, int externalIndex, int changeIndex) {
        return new BitcoinAccount(id, name, seed, accountIndex, externalIndex, changeIndex);
    }

}
