package de.bitsharesmunich.cryptocoincore.bitcoin;

import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccount;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CrytpoCoinBalance;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import static de.bitsharesmunich.cryptocoincore.base.Coin.BITCOIN;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;

/**
 *
 * @author Henry
 */
public class BitcoinAccount extends CryptoCoinAccount{
    
    
    public BitcoinAccount(CryptoCoinAccountSeed seed){
        this.seed = seed;
        this.coin = BITCOIN;
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
