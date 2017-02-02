package de.bitsharesmunich.cryptocoincore.base.seed;

import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinSeedType;
import java.util.Arrays;
import org.bitcoinj.crypto.MnemonicCode;

/**
 *
 * @author Henry
 */
public class CryptoCoinSeedBIP39 extends CryptoCoinAccountSeed {
    
    public CryptoCoinSeedBIP39(String words, String passPhrase) {
        super(CryptoCoinSeedType.BIP39, Arrays.asList(words.toLowerCase().split(" ")), passPhrase);
    }

    @Override
    public byte[] getSeed() {
        return MnemonicCode.toSeed(this.getMnemonicCode(), this.getAdditional());
    }
    
}
