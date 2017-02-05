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
    
    public /*private*/ static final String SQL_CREATE_GENERAL_ADDRESS_TABLE = "CREATE TABLE " + CryptoCoreSQLiteContract.GeneralCoinAddress.TABLE_NAME + " (" +
            CryptoCoreSQLiteContract.GeneralCoinAddress.COLUMN_ID + " TEXT PRIMARY KEY, " +
            CryptoCoreSQLiteContract.GeneralCoinAddress.COLUMN_ID_ACCOUNT + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.GeneralCoinAddress.COLUMN_INDEX + TYPE_INTEGER + ", " +
            CryptoCoreSQLiteContract.GeneralCoinAddress.COLUMN_IS_CHANGE + TYPE_INTEGER + ", " +
            CryptoCoreSQLiteContract.GeneralCoinAddress.COLUMN_PUBLIC_KEY + TYPE_TEXT + ", " +
            " FOREIGN KEY("+CryptoCoreSQLiteContract.GeneralCoinAddress.COLUMN_ID_ACCOUNT+") REFERENCES "+CryptoCoreSQLiteContract.GeneralAccounts.TABLE_NAME+"("+CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID+"),"+
            "CONSTRAINT genAddressContraint UNIQUE ("+CryptoCoreSQLiteContract.GeneralCoinAddress.COLUMN_ID_ACCOUNT+","+CryptoCoreSQLiteContract.GeneralCoinAddress.COLUMN_INDEX+","+CryptoCoreSQLiteContract.GeneralCoinAddress.COLUMN_IS_CHANGE+") "+
            ")";
    
    public /*private*/ static final String SQL_CREATE_GENERAL_TRANSACTION_TABLE = "CREATE TABLE " + CryptoCoreSQLiteContract.GeneralTransaction.TABLE_NAME + " (" +
            CryptoCoreSQLiteContract.GeneralTransaction.COLUMN_ID + " TEXT PRIMARY KEY, " +
            CryptoCoreSQLiteContract.GeneralTransaction.COLUMN_TYPE + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.GeneralTransaction.COLUMN_ADDRESS + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.GeneralTransaction.COLUMN_DATE + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.GeneralTransaction.COLUMN_BLOCK + TYPE_INTEGER + ", " +
            CryptoCoreSQLiteContract.GeneralTransaction.COLUMN_CONFIRMS + TYPE_INTEGER + ", " +
            CryptoCoreSQLiteContract.GeneralTransaction.COLUMN_FEE + TYPE_INTEGER + ")";
    
    public /*private*/ static final String SQL_CREATE_INPUT_TX_TABLE = "CREATE TABLE " + CryptoCoreSQLiteContract.Inputs.TABLE_NAME + " (" +
            CryptoCoreSQLiteContract.Inputs.COLUMN_ID + " TEXT PRIMARY KEY, " +
            CryptoCoreSQLiteContract.Inputs.COLUMN_TYPE + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.Inputs.COLUMN_ID_ADDRESS + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.Inputs.COLUMN_ID_TRANSACTION + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.Inputs.COLUMN_AMOUNT + TYPE_TEXT + ", " +
            " FOREIGN KEY("+CryptoCoreSQLiteContract.Inputs.COLUMN_ID_ADDRESS+") REFERENCES "+CryptoCoreSQLiteContract.GeneralCoinAddress.TABLE_NAME+"("+CryptoCoreSQLiteContract.GeneralCoinAddress.COLUMN_ID+"),"+
            " FOREIGN KEY("+CryptoCoreSQLiteContract.Inputs.COLUMN_ID_TRANSACTION+") REFERENCES "+CryptoCoreSQLiteContract.GeneralTransaction.TABLE_NAME+"("+CryptoCoreSQLiteContract.GeneralTransaction.COLUMN_ID+"))";
    
    public /*private*/ static final String SQL_CREATE_OUTPUT_TX_TABLE = "CREATE TABLE " + CryptoCoreSQLiteContract.Outputs.TABLE_NAME + " (" +
            CryptoCoreSQLiteContract.Outputs.COLUMN_ID + " TEXT PRIMARY KEY, " +
            CryptoCoreSQLiteContract.Outputs.COLUMN_TYPE + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.Outputs.COLUMN_ID_ADDRESS + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.Outputs.COLUMN_ID_TRANSACTION + TYPE_TEXT + ", " +
            CryptoCoreSQLiteContract.Outputs.COLUMN_AMOUNT + TYPE_TEXT + ", " +
            " FOREIGN KEY("+CryptoCoreSQLiteContract.Outputs.COLUMN_ID_ADDRESS+") REFERENCES "+CryptoCoreSQLiteContract.GeneralCoinAddress.TABLE_NAME+"("+CryptoCoreSQLiteContract.GeneralCoinAddress.COLUMN_ID+"),"+
            " FOREIGN KEY("+CryptoCoreSQLiteContract.Outputs.COLUMN_ID_TRANSACTION+") REFERENCES "+CryptoCoreSQLiteContract.GeneralTransaction.TABLE_NAME+"("+CryptoCoreSQLiteContract.GeneralTransaction.COLUMN_ID+"))";
    
}
