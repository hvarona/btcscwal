package de.bitsharesmunich.cryptocoincore.bitcoin;

import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinManager;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestNet3Params;

/**
 *
 * @author Henry
 */
public class BitcoinManager extends CryptoCoinManager<BitcoinAccount>{
    
    protected NetworkParameters netParams = TestNet3Params.get();
    
    public BitcoinManager(NetworkParameters netParams){
        this.netParams = netParams;
    }
    
    @Override
    public BitcoinAccount newAccount(){
        
        return null;
    }
    
    public BitcoinAccount getAccount(CryptoCoinAccountSeed seed){
        return new BitcoinAccount(seed);
    }
}
