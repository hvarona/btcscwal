package de.bitsharesmunich.cryptocoincore.test;

/**
 *
  */
public class CryptoCoreSQLiteContract{
    public static class CryptoCoinAccount /*implements BaseColumns*/ {
        public static final String TABLE_NAME = "crypto_coin_account";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_COIN = "coin";
        public static final String COLUMN_SEED = "seed";        
    }
}
