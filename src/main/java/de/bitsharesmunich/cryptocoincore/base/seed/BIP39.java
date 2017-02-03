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

    public BIP39(String id, List<String> mnemonicCode, String additional) {
        this.id = id;
        this.type = SeedType.BIP39;
        this.mnemonicCode = mnemonicCode;
        this.additional = additional;
    }

    public BIP39(String words, String passPhrase) {
        this.id = "";
        this.type = SeedType.BIP39;
        this.mnemonicCode = Arrays.asList(words.split(" "));
        this.additional = passPhrase;
    }

    @Override
    public byte[] getSeed() {
        return MnemonicCode.toSeed(this.getMnemonicCode(), this.getAdditional());
    }

}
