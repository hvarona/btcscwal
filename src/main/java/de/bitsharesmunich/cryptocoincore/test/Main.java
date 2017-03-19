package de.bitsharesmunich.cryptocoincore.test;

import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccount;
import de.bitsharesmunich.cryptocoincore.base.GeneralCoinAccount;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        MainTest test = new MainTest();
        /*CryptoCoreSQLite db = new CryptoCoreSQLite();
        db.connect();
        
        List<AccountSeed> seeds = db.getSeeds();
        System.out.println("DB seeds: \r\n");
        for(AccountSeed seed : seeds){
            System.out.println(seed.getMnemonicCodeString());
            test.testBitcoinImportSeed(seed);
        }
        List<GeneralCoinAccount> accounts = db.getAccounts();
        System.out.println("\r\n\r\nDB accounts: \r\n");
        for(CryptoCoinAccount account : accounts){
            System.out.println(account.toString());
        }*/
        
        
        
        //test.testBitcoinAccountCreation();
        /*for(int i = 0; i< 20; i++){
            test.testBip39SeedGeneration();
        }*/
        //test.testSocketConnection();
        //test.testSendTransaction();
        //test.testDashSendTransaction();
        test.testSendLiteCoinAccount();
        test.testSendDogeCoinAccount();
    }
}
