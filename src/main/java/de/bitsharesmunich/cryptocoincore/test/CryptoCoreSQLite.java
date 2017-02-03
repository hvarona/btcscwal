package de.bitsharesmunich.cryptocoincore.test;

import de.bitsharesmunich.cryptocoincore.base.Coin;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccount;
import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinFactory;
import de.bitsharesmunich.cryptocoincore.base.seed.BIP39;
import de.bitsharesmunich.cryptocoincore.base.seed.Brainkey;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author javier
 */
public class CryptoCoreSQLite {

    private Connection db = null;

    public void connect() {
        if (db == null) {
            Statement stmt = null;
            Statement creationStmt = null;
            String sql = "";
            ResultSet rs;
            try {
                db = DriverManager.getConnection("jdbc:sqlite:test.db");

                stmt = db.createStatement();
                sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + CryptoCoreSQLiteContract.Seeds.TABLE_NAME + "';";
                rs = stmt.executeQuery(sql);
                if (rs.getFetchSize() <= 0) {
                    creationStmt = db.createStatement();
                    sql = CryptoCoreSQLiteHelper.SQL_CREATE_SEED_TABLE;
                    creationStmt.execute(sql);
                    creationStmt.close();
                }
                stmt.close();

                stmt = db.createStatement();
                sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + CryptoCoreSQLiteContract.GeneralAccounts.TABLE_NAME + "';";
                rs = stmt.executeQuery(sql);
                if (rs.getFetchSize() <= 0) {
                    creationStmt = db.createStatement();
                    sql = CryptoCoreSQLiteHelper.SQL_CREATE_SEED_COIN_TABLE;
                    creationStmt.execute(sql);
                    creationStmt.close();
                }
                stmt.close();

            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void putCryptoCoinSeed(AccountSeed seed) {
        Statement stmt = null;
        String sql = "";

        this.connect();
        if (db != null) {
            try {
                String newId = UUID.randomUUID().toString();
                stmt = db.createStatement();
                sql = "INSERT INTO " + CryptoCoreSQLiteContract.Seeds.TABLE_NAME + "("
                        + CryptoCoreSQLiteContract.Seeds.COLUMN_ID + ","
                        + CryptoCoreSQLiteContract.Seeds.COLUMN_TYPE + ","
                        + CryptoCoreSQLiteContract.Seeds.COLUMN_MNEMONIC + ","
                        + CryptoCoreSQLiteContract.Seeds.COLUMN_ADDITIONAL + ""
                        + ") VALUES ("
                        + "'" + newId + "',"
                        + "'" + seed.getType().name() + "',"
                        + "'" + seed.getMnemonicCodeString() + "',"
                        + "'" + seed.getAdditional() + "'"
                        + ")";
                if (stmt.execute(sql)) {
                    seed.setId(newId);
                }
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<AccountSeed> getSeeds() {
        Statement stmt;
        String sql;
        List<AccountSeed> seeds = new ArrayList();

        this.connect();
        if (db != null) {
            try {
                stmt = db.createStatement();
                sql = "SELECT * FROM " + CryptoCoreSQLiteContract.Seeds.TABLE_NAME;
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.getFetchSize() > 0) {
                    AccountSeed seed;
                    List<String> mnemonic;
                    while (rs.next()) {
                        String id = rs.getString(CryptoCoreSQLiteContract.Seeds.COLUMN_ID);
                        mnemonic = Arrays.asList(rs.getString(CryptoCoreSQLiteContract.Seeds.COLUMN_MNEMONIC).split(" "));
                        String additional = rs.getString(CryptoCoreSQLiteContract.Seeds.COLUMN_ADDITIONAL);
                        String type = rs.getString(CryptoCoreSQLiteContract.Seeds.COLUMN_TYPE);
                        switch (type) {
                            case "BIP39":
                                seed = new BIP39(id, mnemonic, additional);
                                break;
                            case "BrainKey":
                                seed = new Brainkey(id, mnemonic, additional);
                                break;
                            default:
                                seed = null;
                        }

                        seeds.add(seed);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return seeds;
    }

    public List<CryptoCoinAccount> getAccounts(List<AccountSeed> seeds) {
        Statement stmt;
        String sql;
        List<CryptoCoinAccount> accounts = new ArrayList();
        this.connect();

        if (db != null) {
            try {
                stmt = db.createStatement();
                sql = "SELECT * FROM " + CryptoCoreSQLiteContract.GeneralAccounts.TABLE_NAME;
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.getFetchSize() > 0) {
                    CryptoCoinAccount account;
                    while (rs.next()) {
                        String id = rs.getString(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID);
                        String name = rs.getString(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_NAME);
                        String type = rs.getString(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_TYPE);
                        String idSeed = rs.getString(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID_SEED);
                        AccountSeed seed = null;
                        for (AccountSeed search : seeds) {
                            if (search.getId().equals(idSeed)) {
                                seed = search;
                                break;
                            }
                        }
                        if (seed == null) {
                            continue;
                        }
                        int accountIndex = rs.getInt(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ACCOUNT_INDEX);
                        int changeIndex = rs.getInt(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_CHANGE_INDEX);
                        int externalIndex = rs.getInt(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_EXTERNAL_INDEX);
                        switch (type) {
                            case "Bitcoin":
                                account = CryptoCoinFactory
                                        .getGeneralCoinManager(Coin.BITCOIN)
                                        .getAccount(id, name, seed, accountIndex,
                                                externalIndex, changeIndex);
                                break;
                            default:
                                account = null;
                        }

                        accounts.add(account);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return accounts;
    }

    public void modifiyCryptoCoinAccount(CryptoCoinAccount account) { //TODO:Change this to modifyCryptoCoinSeed        
        /*Statement stmt = null;
        String sql = "";
            
        this.connect();
        if (db != null){
            try {            
                stmt = db.createStatement();
                sql = "UPDATE "+CryptoCoreSQLiteContract.CryptoCoinAccount.TABLE_NAME+" SET "
                      +CryptoCoreSQLiteContract.CryptoCoinAccount.COLUMN_COIN+" = '"+account.getCoin().toString()+"',"
                      +CryptoCoreSQLiteContract.CryptoCoinAccount.COLUMN_SEED+" = '"+account.getSeed().getJsonString()+"'"
                      +" WHERE "
                      +CryptoCoreSQLiteContract.CryptoCoinAccount.COLUMN_ID+" = '"+account.getId()+"'";
                stmt.execute(sql);
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
    }
}
