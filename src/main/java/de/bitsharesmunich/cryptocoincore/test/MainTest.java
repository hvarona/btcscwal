package de.bitsharesmunich.cryptocoincore.test;

import de.bitsharesmunich.cryptocoincore.base.seed.CryptoCoinSeedBIP39;
import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinAccount;
import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinManager;
import org.bitcoinj.params.TestNet3Params;
/**
 *
 * @author Henry
 */
public class MainTest {
    
    
    public void testBitcoinAccountCreation(){
        BitcoinManager bitcoinFactory = new BitcoinManager(TestNet3Params.get());        
        BitcoinAccount account = bitcoinFactory.newAccount();
        //System.out.println(account.getId().getJsonString());              
    }
    
    public void testBitcoinAccountSendingCoins(){
        CryptoCoinSeedBIP39 accountSeed = new CryptoCoinSeedBIP39("{\"creation_time\":1482115724,\"deterministic_seed\":\"monitor swim symbol sadness illegal boring age language hand way carry amount\",\"net_id\":\"org.bitcoin.test\"}","");
        BitcoinAccount account = new BitcoinAccount(accountSeed);
    }
}
