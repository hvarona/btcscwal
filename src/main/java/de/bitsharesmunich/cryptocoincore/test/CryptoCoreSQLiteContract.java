package de.bitsharesmunich.cryptocoincore.test;

/**
 *
  */
public class CryptoCoreSQLiteContract{
    public static class CryptoCoinSeed /*implements BaseColumns*/ {
        public static final String TABLE_NAME = "seeds";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TYPE = "derived_seed_type";
        public static final String COLUMN_MNEMONIC = "mnemonic";
        public static final String COLUMN_ADDITIONAL = "additional";
    }
    
    public static class CryptoCoinSeedCoin /*implements BaseColumns*/ {
        public static final String TABLE_NAME = "general_accounts";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "account_name"; //this is only to tag this account
        public static final String COLUMN_TYPE = "coin_type";//bitcoin,litecoin,bitshares,openledger
        public static final String COLUMN_ID_SEED = "id_seed";
        public static final String COLUMN_ACCOUNT_INDEX = "account_index"; // the account index used
        public static final String COLUMN_EXTERNAL_INDEX = "external_index"; // the last external address index used
        public static final String COLUMN_CHANGE_INDEX = "change_index"; // the last change address index used
    }
}
