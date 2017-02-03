package de.bitsharesmunich.cryptocoincore.bitcoin;

import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinManager;
import de.bitsharesmunich.cryptocoincore.crypto.Random;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.params.TestNet3Params;

/**
 *
 * @author Henry
 */
public class BitcoinManager extends CryptoCoinManager<BitcoinAccount> {

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
    public BitcoinAccount newAccount() {
        Random.getSecureRandom().nextInt();

        return null;
    }

    @Override
    public BitcoinAccount getAccount(AccountSeed seed) {
        return new BitcoinAccount(seed);
    }

    @Override
    public BitcoinAccount getAccountFromJsonSeed(String jsonSeed) {
        return new BitcoinAccount(AccountSeed.loadFromJsonString(jsonSeed,""));
    }

}
