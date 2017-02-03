package de.bitsharesmunich.cryptocoincore.base.seed;

import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinSeedType;
import java.util.Arrays;
import java.util.List;
import org.bitcoinj.crypto.MnemonicCode;

/**
 *
 * @author Henry
 */
public class CryptoCoinSeedBIP39 extends CryptoCoinAccountSeed {

    public CryptoCoinSeedBIP39(String id, CryptoCoinSeedType type, List<String> MnemonicCode, String additional) {
        super(id, type, MnemonicCode, additional);
    }
    
    public CryptoCoinSeedBIP39(String words, String passPhrase) {
        super("",CryptoCoinSeedType.BIP39, Arrays.asList(words.toLowerCase().split(" ")), passPhrase);
    }

    @Override
    public byte[] getSeed() {
        return MnemonicCode.toSeed(this.getMnemonicCode(), this.getAdditional());
    }
    
}
