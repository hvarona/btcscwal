package de.bitsharesmunich.cryptocoincore.bitcoin;

import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccount;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CrytpoCoinBalance;
import static de.bitsharesmunich.cryptocoincore.base.Coin.BITCOIN;
import java.util.ArrayList;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;

/**
 *
 * @author Henry
 */
public class BitcoinAccount extends CryptoCoinAccount {
    
    private final static int ADDRESS_GAP = 20;
    private int accountNumber = 0;
    private ArrayList<BitcoinAddress> externalKeys;
    private ArrayList<BitcoinAddress> changeKeys;
    private NetworkParameters param = NetworkParameters.fromID(NetworkParameters.ID_TESTNET);
    

    public BitcoinAccount(CryptoCoinAccountSeed seed) {
        this.seed = seed;
        this.coin = BITCOIN;
        //BIP44
        DeterministicKey masterKey = HDKeyDerivation.createMasterPrivateKey(seed.getSeed());
        DeterministicKey purposeKey = HDKeyDerivation.deriveChildKey(masterKey, new ChildNumber(44, true));
        DeterministicKey coinKey = HDKeyDerivation.deriveChildKey(purposeKey, new ChildNumber(0, true));
        //TODO account number
        DeterministicKey accountKey = HDKeyDerivation.deriveChildKey(coinKey, new ChildNumber(accountNumber, true));
        DeterministicKey external = HDKeyDerivation.deriveChildKey(accountKey, new ChildNumber(0, false));
        DeterministicKey change = HDKeyDerivation.deriveChildKey(accountKey, new ChildNumber(1, false));
        
        for(int i = 0; i < ADDRESS_GAP; i++){
            externalKeys.add(new BitcoinAddress(HDKeyDerivation.deriveChildKey(external, new ChildNumber(i, false)), param));
            changeKeys.add(new BitcoinAddress(HDKeyDerivation.deriveChildKey(change, new ChildNumber(i, false)), param));
        }
    }

    @Override
    public CrytpoCoinBalance getBalance() {
        return null;
    }
    
    public String getNextAvaibleAddress(){
        return externalKeys.get(0).getAddress();
    }

    public void sendCoin(Address to, Coin ammount) {
    }

    public Address getAddress() {
        return null;
    }

}
