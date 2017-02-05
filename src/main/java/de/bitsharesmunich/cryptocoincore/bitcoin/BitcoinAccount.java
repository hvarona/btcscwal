package de.bitsharesmunich.cryptocoincore.bitcoin;

import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.Balance;
import static de.bitsharesmunich.cryptocoincore.base.Coin.BITCOIN;
import de.bitsharesmunich.cryptocoincore.base.GeneralCoinAccount;
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
public class BitcoinAccount extends GeneralCoinAccount {

    private final static int ADDRESS_GAP = 20;
    private ArrayList<BitcoinAddress> externalKeys = new ArrayList();
    private ArrayList<BitcoinAddress> changeKeys = new ArrayList();
    
    private NetworkParameters param = NetworkParameters.fromID(NetworkParameters.ID_MAINNET);

    public BitcoinAccount(String id, String name, AccountSeed seed, int accountNumber, int lastExternalIndex, int lastChangeIndex) {
        super(id, name, BITCOIN, seed, accountNumber, lastExternalIndex, lastChangeIndex);
        calculateAddresses();
    }
    
    public BitcoinAccount(AccountSeed seed){
        super("", "", BITCOIN, seed, 0, 0, 0);
        calculateAddresses();
    }
    
    private void calculateAddresses(){
        //BIP44
        DeterministicKey masterKey = HDKeyDerivation.createMasterPrivateKey(seed.getSeed());
        DeterministicKey purposeKey = HDKeyDerivation.deriveChildKey(masterKey, new ChildNumber(44, true));
        DeterministicKey coinKey = HDKeyDerivation.deriveChildKey(purposeKey, new ChildNumber(0, true));
        DeterministicKey accountKey = HDKeyDerivation.deriveChildKey(coinKey, new ChildNumber(accountNumber, true));
        DeterministicKey external = HDKeyDerivation.deriveChildKey(accountKey, new ChildNumber(0, false));
        DeterministicKey change = HDKeyDerivation.deriveChildKey(accountKey, new ChildNumber(1, false));

        for (int i = 0; i < this.lastExternalIndex + ADDRESS_GAP; i++) {
            externalKeys.add(new BitcoinAddress(HDKeyDerivation.deriveChildKey(external, new ChildNumber(i, false)), param));
        }
        for (int i = 0; i < this.lastChangeIndex + ADDRESS_GAP; i++) {
            changeKeys.add(new BitcoinAddress(HDKeyDerivation.deriveChildKey(change, new ChildNumber(i, false)), param));
        }
    }

    @Override
    public Balance getBalance() {
        return null;
    }

    public String getNextAvaibleAddress() {
        return externalKeys.get(0).getAddress();
    }

    public void sendCoin(Address to, Coin ammount) {
    }

    public Address getAddress() {
        return null;
    }

}
