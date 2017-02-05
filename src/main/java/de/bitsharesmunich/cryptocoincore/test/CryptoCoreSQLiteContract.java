package de.bitsharesmunich.cryptocoincore.test;

/**
 *
  */
public class CryptoCoreSQLiteContract{
    public static class Seeds /*implements BaseColumns*/ {
        public static final String TABLE_NAME = "seeds";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TYPE = "derived_seed_type";
        public static final String COLUMN_MNEMONIC = "mnemonic";
        public static final String COLUMN_ADDITIONAL = "additional";
    }
    
    public static class GeneralAccounts /*implements BaseColumns*/ {
        public static final String TABLE_NAME = "general_accounts";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "account_name"; //this is only to tag this account
        public static final String COLUMN_TYPE = "coin_type";//bitcoin,litecoin
        public static final String COLUMN_ID_SEED = "id_seed";
        public static final String COLUMN_ACCOUNT_INDEX = "account_index"; // the account index used
        public static final String COLUMN_EXTERNAL_INDEX = "external_index"; // the last external address index used
        public static final String COLUMN_CHANGE_INDEX = "change_index"; // the last change address index used
    }
    
    public static class GeneralOrphanKeys /*implements BaseColumns*/ {
        public static final String TABLE_NAME = "general_orphan_key";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name"; //this is only to tag this key
        public static final String COLUMN_TYPE = "coin_type";//bitcoin,litecoin,bitshares,openledger
        public static final String COLUMN_WIF = "wif";
    }
    
    /**
     * TODO SLIP-48 This must be implemented in future releases
     */
    public static class GrapheneAccounts /*implements BaseColumns*/ {
        public static final String TABLE_NAME = "graphene_accounts";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "account_name"; //The name of the account
        public static final String COLUMN_TYPE = "network_type";//Steem,BitShares,PeerPlays,Muse
        public static final String COLUMN_ID_SEED = "id_seed";
        public static final String COLUMN_ACCOUNT_INDEX = "account_index"; // the account used
        //TODO Each graphene network has its own role types, we need to design a way of indexing each address of each role
    }
}
