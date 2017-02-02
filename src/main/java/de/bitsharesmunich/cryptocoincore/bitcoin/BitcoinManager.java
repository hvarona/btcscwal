package de.bitsharesmunich.cryptocoincore.bitcoin;

import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinManager;
import org.bitcoinj.core.NetworkParameters;
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

        return null;
    }

    @Override
    public BitcoinAccount getAccount(CryptoCoinAccountSeed seed) {
        return new BitcoinAccount(seed);
    }

    @Override
    public BitcoinAccount getAccountFromJsonSeed(String jsonSeed) {
        return new BitcoinAccount(CryptoCoinAccountSeed.loadFromJsonString(jsonSeed,""));
    }

}
