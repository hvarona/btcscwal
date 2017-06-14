package de.bitsharesmunich.cryptocoincore.base;

import de.bitsharesmunich.graphenej.Address;
import java.util.List;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;

/**
 *
 * @author henry
 */
public abstract class GrapheneCoinAccount extends CryptoCoinAccount {

    protected int accountNumber;
    protected int ownerIndex = 0;
    protected int activeIndex = 0;
    protected int memoIndex = 0;
    protected DeterministicKey accountKey;
    protected DeterministicKey ownerKey = null;
    protected DeterministicKey activeKey = null;
    protected DeterministicKey memoKey = null;

    private final int networkNumber;

    public static final int STEEM_NETWORK = 0;
    public static final int BITSHARES_NETWORK = 1;
    public static final int PEERPLAYS_NETWORK = 2;
    public static final int MUSE_NETWORK = 3;
    
    public static final int OWNER_ROLE = 0;
    public static final int ACTIVE_ROLE = 1;
    public static final int MEMO_ROLE = 2;
    
    public GrapheneCoinAccount(int accountNumber, int ownerIndex, int activeIndex, int memoIndex, int networkNumber, long id, String name, Coin coin, AccountSeed seed) {
        super(id, name, coin, seed);
        this.accountNumber = accountNumber;
        this.ownerIndex = ownerIndex;
        this.activeIndex = activeIndex;
        this.memoIndex = memoIndex;
        this.networkNumber = networkNumber;
        calculateAddresses();
    }

    public GrapheneCoinAccount(int networkNumber, long id, String name, Coin coin, AccountSeed seed) {
        this(0, 0, 0, 0, networkNumber, id, name, coin, seed);
    }
    

    private void calculateAddresses() {
        //SLIP48
        DeterministicKey masterKey = HDKeyDerivation.createMasterPrivateKey(seed.getSeed());
        DeterministicKey purposeKey = HDKeyDerivation.deriveChildKey(masterKey, new ChildNumber(48, true));
        DeterministicKey networkKey = HDKeyDerivation.deriveChildKey(purposeKey, new ChildNumber(networkNumber, true));
        accountKey = HDKeyDerivation.deriveChildKey(networkKey, new ChildNumber(accountNumber, true));
    }

    protected DeterministicKey calculateKey(int roleNumber, int addressIndex) {
        DeterministicKey roleKey = HDKeyDerivation.deriveChildKey(accountKey, new ChildNumber(roleNumber, true));
        return HDKeyDerivation.deriveChildKey(roleKey, new ChildNumber(addressIndex, true));
    }

    @Override
    public List<Balance> getBalance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public abstract Address getAddress(int role);

}
