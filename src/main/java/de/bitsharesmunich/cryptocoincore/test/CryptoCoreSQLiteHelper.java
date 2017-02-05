package de.bitsharesmunich.cryptocoincore.test;

/**
 *
 */
public class CryptoCoreSQLiteHelper {

    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String TYPE_REAL = " REAL";

    public /*private*/ static final String SQL_CREATE_SEED_TABLE = "CREATE TABLE " + CryptoCoreSQLiteContract.Seeds.TABLE_NAME + " (" +
            CryptoCoreSQLiteContract.Seeds.COLUMN_ID + " TEXT PRIMARY KEY, " +
            CryptoCoreSQLiteContract.Seeds.COLUMN_TYPE + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.Seeds.COLUMN_MNEMONIC + TYPE_TEXT + ", "+
            CryptoCoreSQLiteContract.Seeds.COLUMN_ADDITIONAL + TYPE_TEXT +", " +
            "CONSTRAINT seedContraint UNIQUE ("+CryptoCoreSQLiteContract.Seeds.COLUMN_MNEMONIC+","+CryptoCoreSQLiteContract.Seeds.COLUMN_ADDITIONAL+") "+
            ") ";
    
    public /*private*/ static final String SQL_CREATE_ACCOUNT_TABLE = "CREATE TABLE " + CryptoCoreSQLiteContract.GeneralAccounts.TABLE_NAME + " (" +
            CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID + " TEXT PRIMARY KEY, " +
            CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_NAME + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_TYPE + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID_SEED + TYPE_TEXT + ", "+
            CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ACCOUNT_INDEX + TYPE_INTEGER + ", "+
            CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_EXTERNAL_INDEX + TYPE_INTEGER + ", "+
            CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_CHANGE_INDEX + TYPE_INTEGER + ", "+
            " FOREIGN KEY("+CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID_SEED+") REFERENCES "+CryptoCoreSQLiteContract.Seeds.TABLE_NAME+"("+CryptoCoreSQLiteContract.Seeds.COLUMN_ID+"),"+
            "CONSTRAINT accountContraint UNIQUE ("+CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID_SEED+","+CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_TYPE+","+CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ACCOUNT_INDEX+") "+
            ")";
    
    public /*private*/ static final String SQL_CREATE_GENERAL_ORPHAN_KEY_TABLE = "CREATE TABLE " + CryptoCoreSQLiteContract.GeneralOrphanKeys.TABLE_NAME + " (" +
            CryptoCoreSQLiteContract.GeneralOrphanKeys.COLUMN_ID + " TEXT PRIMARY KEY, " +
            CryptoCoreSQLiteContract.GeneralOrphanKeys.COLUMN_NAME + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.GeneralOrphanKeys.COLUMN_TYPE + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.GeneralOrphanKeys.COLUMN_WIF + TYPE_TEXT + ", "+
            "CONSTRAINT generalOprhanContraint UNIQUE ("+CryptoCoreSQLiteContract.GeneralOrphanKeys.COLUMN_WIF+"))";
}
