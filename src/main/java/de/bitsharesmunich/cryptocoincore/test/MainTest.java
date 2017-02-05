package de.bitsharesmunich.cryptocoincore.test;

import de.bitsharesmunich.cryptocoincore.base.seed.BIP39;
import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinAccount;
import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bitcoinj.params.TestNet3Params;
/**
 *
 * @author Henry
 */
public class MainTest {
    
    public void testBitcoinAccountCreation(){
        BitcoinManager bitcoinFactory = BitcoinManager.getInstance();
        BitcoinAccount account = bitcoinFactory.newAccount();
        //System.out.println(account.getId().getJsonString());              
    }
    
    public void testBitcoinAccountSendingCoins(){
        try {
            //BIP39 accountSeed = new BIP39("{\"creation_time\":1482115724,\"deterministic_seed\":\"monitor swim symbol sadness illegal boring age language hand way carry amount\",\"net_id\":\"org.bitcoin.test\"}","");
            String current = new java.io.File(".").getCanonicalPath();
            File file = new File(current + "/src/main/java/de/bitsharesmunich/cryptocoincore/test/bip39dict.txt");
            
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String words = bufferedReader.readLine();
            BIP39 accountSeed = new BIP39(words.split(","));
            BitcoinAccount account = new BitcoinAccount(accountSeed);
            System.out.println(account.getNextAvaibleAddress());
        } catch (IOException ex) {
            Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
