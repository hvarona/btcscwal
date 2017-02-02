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
    public static class CryptoCoinAccount /*implements BaseColumns*/ {
        public static final String TABLE_NAME = "crypto_coin_account";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_COIN = "coin";
        public static final String COLUMN_SEED = "seed";        
    }
}
