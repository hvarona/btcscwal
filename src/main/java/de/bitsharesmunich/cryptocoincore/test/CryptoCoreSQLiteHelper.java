package de.bitsharesmunich.cryptocoincore.test;

/**
 *
 */
public class CryptoCoreSQLiteHelper {

    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String TYPE_REAL = " REAL";

    public /*private*/ static final String SQL_CREATE_ACCOUNT_TABLE = "CREATE TABLE " + CryptoCoreSQLiteContract.CryptoCoinAccount.TABLE_NAME + " (" +
            CryptoCoreSQLiteContract.CryptoCoinAccount.COLUMN_ID + " TEXT PRIMARY KEY, " +
            CryptoCoreSQLiteContract.CryptoCoinAccount.COLUMN_COIN + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.CryptoCoinAccount.COLUMN_SEED + TYPE_TEXT + ")";
}
