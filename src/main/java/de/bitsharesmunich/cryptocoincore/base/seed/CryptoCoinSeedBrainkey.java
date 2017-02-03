package de.bitsharesmunich.cryptocoincore.base.seed;

import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinSeedType;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Henry
 */
public class CryptoCoinSeedBrainkey extends CryptoCoinAccountSeed {
    
    public CryptoCoinSeedBrainkey(String words, int sequence) {
        super("", CryptoCoinSeedType.BRAINKEY, Arrays.asList(words.toLowerCase().split(" ")), Integer.toString(sequence));
    }

    public CryptoCoinSeedBrainkey(String id, CryptoCoinSeedType type, List<String> MnemonicCode, String additional) {
        super(id, type, MnemonicCode, additional);
    }

    @Override
    public byte[] getSeed() {
        StringBuilder encoded = new StringBuilder();
        for (String word : this.getMnemonicCode()) {
            encoded.append(word);
            encoded.append(" ");
        }
        encoded.append(this.getAdditional());
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(encoded.toString().getBytes("UTF-8"));
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] result = sha256.digest(bytes);
            return result;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgotithmException. Msg: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncodingException. Msg: " + e.getMessage());
        }
        return null;
    }
    
}
