package de.bitsharesmunich.cryptocoincore.base.seed;

import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.SeedType;
import de.bitsharesmunich.cryptocoincore.crypto.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import org.bitcoinj.crypto.MnemonicCode;

/**
 *
 * @author Henry
 */
public class BIP39 extends AccountSeed {

    private final int WORDNUMBER = 12;

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

    public BIP39(String[] wordList) {
        try {
            this.id = "";
            this.type = SeedType.BIP39;
            this.additional = "";
            int entropySize = ((WORDNUMBER * 11) / 8) * 8;
            SecureRandom secureRandom = Random.getSecureRandom();
            byte[] entropy = new byte[entropySize / 8];
            secureRandom.nextBytes(entropy);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] shaResult = md.digest(entropy);
            int mask = 0x80;
            int cheksum = 0;
            for (int i = 0; i < entropySize / 32; i++) {
                cheksum = cheksum ^ (shaResult[0] & mask);
                mask = mask / 2;
            }
            int[] wordsIndex = new int[(entropySize / 11) + 1];
            for (int i = 0; i < wordsIndex.length; i++) {
                wordsIndex[i] = 0;
            }

            int lastIndex = 0;
            int lastBit = 0;
            for (int i = 0; i < entropy.length; i++) {
                for (int j = 7; j >= 0; j--) {
                    if (lastBit == 11) {
                        lastBit = 0;
                        ++lastIndex;
                    }
                    wordsIndex[lastIndex] = wordsIndex[lastIndex] ^ ((int) (Math.pow(2, 11 - (lastBit + 1))) * (entropy[i] & ((int) Math.pow(2, j))) >> j);
                    ++lastBit;
                }
            }
            for (int j = 7; j >= 0; j--) {
                if (lastBit == 11) {
                    break;
                }
                wordsIndex[lastIndex] = wordsIndex[lastIndex] ^ ((int) (Math.pow(2, 11 - (lastBit + 1))) * (cheksum & ((int) Math.pow(2, j))) >> j);
                ++lastBit;
            }
            StringBuilder words = new StringBuilder();
            for (int windex : wordsIndex) {
                words.append(wordList[windex]).append(" ");
            }
            words.deleteCharAt(words.length() - 1);
            this.mnemonicCode = Arrays.asList(words.toString().split(" "));
        } catch (NoSuchAlgorithmException ex) {
        }
    }

    @Override
    public byte[] getSeed() {
        return MnemonicCode.toSeed(this.getMnemonicCode(), this.getAdditional());
    }

}
