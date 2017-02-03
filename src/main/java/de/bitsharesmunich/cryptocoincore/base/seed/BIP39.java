package de.bitsharesmunich.cryptocoincore.base.seed;

import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.SeedType;
import java.util.Arrays;
import java.util.List;
import org.bitcoinj.crypto.MnemonicCode;

/**
 *
 * @author Henry
 */
public class BIP39 extends AccountSeed {

    public BIP39(String id, SeedType type, List<String> MnemonicCode, String additional) {
        super(id, type, MnemonicCode, additional);
    }
    
    public BIP39(String words, String passPhrase) {
        super("",SeedType.BIP39, Arrays.asList(words.toLowerCase().split(" ")), passPhrase);
    }

    @Override
    public byte[] getSeed() {
        return MnemonicCode.toSeed(this.getMnemonicCode(), this.getAdditional());
    }
    
}
