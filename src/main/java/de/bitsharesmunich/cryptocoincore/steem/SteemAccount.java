package de.bitsharesmunich.cryptocoincore.steem;

import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.Coin;
import de.bitsharesmunich.cryptocoincore.base.GrapheneCoinAccount;
import de.bitsharesmunich.graphenej.Address;
import org.bitcoinj.crypto.DeterministicKey;

/**
 *
 * @author henry
 */
public class SteemAccount extends GrapheneCoinAccount{
    
    private int postingIndex = 0;
    private DeterministicKey postingKey = null;
    
    // Steem only role
    public static final int POSTING_ROLE = 3;

    public SteemAccount(int accountNumber, int ownerIndex, int activeIndex, int memoIndex, int netowrkNumber, long id, String name, Coin coin, AccountSeed seed) {
        super(accountNumber, ownerIndex, activeIndex, memoIndex, netowrkNumber, id, name, coin, seed);
    }

    public SteemAccount(int networkNumber, long id, String name, Coin coin, AccountSeed seed) {
        super(networkNumber, id, name, coin, seed);
    }

    @Override
    public Address getAddress(int role) {
        DeterministicKey answerKey = null;
        switch(role){
            case(OWNER_ROLE):
            {
                if(ownerKey == null){
                    ownerKey = calculateKey(OWNER_ROLE, ownerIndex);
                }
                answerKey = ownerKey;
                break;
            }
            case(ACTIVE_ROLE):
            {
                if(activeKey == null){
                    activeKey = calculateKey(ACTIVE_ROLE, activeIndex);
                }
                answerKey = activeKey;
                break;
            }
            case(MEMO_ROLE):
            {
                if(memoKey == null){
                    memoKey = calculateKey(MEMO_ROLE, memoIndex);
                }
                answerKey = memoKey;
                break;
            }
            case(POSTING_ROLE):
            {
                if(postingKey == null){
                    postingKey = calculateKey(POSTING_ROLE, postingIndex);
                }
                answerKey = postingKey;
                break;
            }
            default:
                //TODO Error
        }
        if(answerKey != null){
            return new Address(answerKey, "STM");
        }
        return null;
    }
    
    public DeterministicKey getKey(int role){
        DeterministicKey answerKey = null;
        switch(role){
            case(OWNER_ROLE):
            {
                if(ownerKey == null){
                    ownerKey = calculateKey(OWNER_ROLE, ownerIndex);
                }
                return ownerKey;
            }
            case(ACTIVE_ROLE):
            {
                if(activeKey == null){
                    activeKey = calculateKey(ACTIVE_ROLE, activeIndex);
                }
                return activeKey;
            }
            case(MEMO_ROLE):
            {
                if(memoKey == null){
                    memoKey = calculateKey(MEMO_ROLE, memoIndex);
                }
                return memoKey;
            }
            case(POSTING_ROLE):
            {
                if(postingKey == null){
                    postingKey = calculateKey(POSTING_ROLE, postingIndex);
                }
                return postingKey;
            }
            default:
                //TODO Error
                return null;
        }
    }
}
