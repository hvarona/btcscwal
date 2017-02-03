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
public class CryptoCoreSQLiteHelper {

    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String TYPE_REAL = " REAL";

    public /*private*/ static final String SQL_CREATE_SEED_TABLE = "CREATE TABLE " + CryptoCoreSQLiteContract.CryptoCoinSeed.TABLE_NAME + " (" +
            CryptoCoreSQLiteContract.CryptoCoinSeed.COLUMN_ID + " TEXT PRIMARY KEY, " +
            CryptoCoreSQLiteContract.CryptoCoinSeed.COLUMN_TYPE + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.CryptoCoinSeed.COLUMN_MNEMONIC + TYPE_TEXT + ", "+
            CryptoCoreSQLiteContract.CryptoCoinSeed.COLUMN_ADDITIONAL + TYPE_TEXT + ")";
    
    public /*private*/ static final String SQL_CREATE_SEED_COIN_TABLE = "CREATE TABLE " + CryptoCoreSQLiteContract.CryptoCoinSeedCoin.TABLE_NAME + " (" +
            CryptoCoreSQLiteContract.CryptoCoinSeedCoin.COLUMN_ID + " TEXT PRIMARY KEY, " +
            CryptoCoreSQLiteContract.CryptoCoinSeedCoin.COLUMN_TYPE + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.CryptoCoinSeedCoin.COLUMN_ACCOUNT_INDEX + TYPE_TEXT + ", "+
            CryptoCoreSQLiteContract.CryptoCoinSeedCoin.COLUMN_ID_SEED + TYPE_TEXT + ") "+
            " FOREIGN KEY("+CryptoCoreSQLiteContract.CryptoCoinSeedCoin.COLUMN_ID_SEED+") REFERENCES "+CryptoCoreSQLiteContract.CryptoCoinSeed.TABLE_NAME+"("+CryptoCoreSQLiteContract.CryptoCoinSeed.COLUMN_ID+")";
}
