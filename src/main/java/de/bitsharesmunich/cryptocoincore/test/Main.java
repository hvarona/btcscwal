package de.bitsharesmunich.cryptocoincore.test;

public class Main {

    public static void main(String[] args) {
        //CryptoCoreSQLite db = new CryptoCoreSQLite();
        //db.connect();
        
        MainTest test = new MainTest();
        //test.testBitcoinAccountCreation();
        test.testBip39SeedGeneration();
    }
}
