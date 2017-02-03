/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitsharesmunich.cryptocoincore.test;

/**
 *
 * @author javier
 */
public class CryptoCoreSQLiteContract{
    public static class CryptoCoinSeed /*implements BaseColumns*/ {
        public static final String TABLE_NAME = "crypto_coin_seed";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TYPE = "type";//bip44, bip32, brainkey
        public static final String COLUMN_MNEMONIC = "mnemonic";
        public static final String COLUMN_ADDITIONAL = "additional";
    }
    
    public static class CryptoCoinSeedCoin /*implements BaseColumns*/ {
        public static final String TABLE_NAME = "crypto_coin_seed_coin";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TYPE = "type";//bitcoin,litecoin,bitshares,openledger
        public static final String COLUMN_ACCOUNT_INDEX = "account_index";
        public static final String COLUMN_ID_SEED = "id_seed";
    }
}
