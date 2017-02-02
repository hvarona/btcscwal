package de.bitsharesmunich.cryptocoincore.bitcoin;

import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccount;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CrytpoCoinBalance;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;

/**
 *
 * @author Henry
 */
public class BitcoinAccount extends CryptoCoinAccount{
    
    
    public BitcoinAccount(CryptoCoinAccountSeed seed){
        this.seed = seed;
    }
    
    @Override
    public CrytpoCoinBalance getBalance(){
        return null;
    }
    
    public void sendCoin(Address to, Coin ammount){
    }
    
    public Address getAddress(){
        return null;
    }
    
}
