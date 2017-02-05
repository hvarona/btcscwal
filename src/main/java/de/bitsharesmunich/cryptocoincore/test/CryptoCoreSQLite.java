package de.bitsharesmunich.cryptocoincore.test;

import de.bitsharesmunich.cryptocoincore.base.Coin;
import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinFactory;
import de.bitsharesmunich.cryptocoincore.base.GeneralCoinAccount;
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

    /**
     * Only for test outside of android
     */
    public void connect() {
        if (db == null) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
            Statement stmt = null;
            Statement creationStmt = null;
            String sql = "";
            ResultSet rs;
            try {
                db = DriverManager.getConnection("jdbc:sqlite:test.db");

                stmt = db.createStatement();
                sql = "SELECT COUNT(*) count FROM sqlite_master WHERE type='table' AND name='" + CryptoCoreSQLiteContract.Seeds.TABLE_NAME + "';";
                rs = stmt.executeQuery(sql);
                if (rs.getInt("count") <= 0) {
                    creationStmt = db.createStatement();
                    sql = CryptoCoreSQLiteHelper.SQL_CREATE_SEED_TABLE;
                    creationStmt.execute(sql);
                    creationStmt.close();
                }
                rs.close();
                stmt.close();

                stmt = db.createStatement();
                sql = "SELECT COUNT(*) count FROM sqlite_master WHERE type='table' AND name='" + CryptoCoreSQLiteContract.GeneralAccounts.TABLE_NAME + "';";
                rs = stmt.executeQuery(sql);
                if (rs.getInt("count") <= 0) {
                    creationStmt = db.createStatement();
                    sql = CryptoCoreSQLiteHelper.SQL_CREATE_ACCOUNT_TABLE;
                    creationStmt.execute(sql);
                    creationStmt.close();
                }
                rs.close();
                stmt.close();

            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void putSeed(final AccountSeed seed) {
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

    public void updateSeed(final AccountSeed seed) { //TODO:Change this to modifyCryptoCoinSeed        
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

    public AccountSeed getSeed(String idSeed) {

        Statement stmt = null;
        String sql = "";

        this.connect();
        if (db != null) {
            try {
                stmt = db.createStatement();
                sql = "SELECT * FROM " + CryptoCoreSQLiteContract.Seeds.TABLE_NAME + " WHERE " + CryptoCoreSQLiteContract.Seeds.COLUMN_ID + " = '" + idSeed + "'";
                ResultSet rs = stmt.executeQuery(sql);

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
                    rs.close();
                    stmt.close();
                    return seed;
                }
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    public String getIdSeed(AccountSeed seed) {
        Statement stmt = null;
        String sql = "";

        this.connect();
        if (db != null) {
            try {
                stmt = db.createStatement();
                sql = "SELECT * FROM " + CryptoCoreSQLiteContract.Seeds.TABLE_NAME + " WHERE " + CryptoCoreSQLiteContract.Seeds.COLUMN_MNEMONIC + " = '" + seed.getMnemonicCodeString() + "' AND " + CryptoCoreSQLiteContract.Seeds.COLUMN_ADDITIONAL + " = '" + seed.getAdditional() + "'";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String id = rs.getString(CryptoCoreSQLiteContract.Seeds.COLUMN_ID);
                    rs.close();
                    stmt.close();
                    return id;
                }
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
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
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return seeds;
    }

    public void putGeneralAccount(final GeneralCoinAccount account) {
        Statement stmt = null;
        String sql = "";

        this.connect();
        if (db != null) {
            try {
                String newId = UUID.randomUUID().toString();
                stmt = db.createStatement();
                String idSeed = account.getSeed().getId();
                if (idSeed == null || idSeed.isEmpty() || idSeed.equals("null")) {
                    idSeed = getIdSeed(account.getSeed());
                    if (idSeed == null || idSeed.isEmpty() || idSeed.equals("null")) {
                        putSeed(account.getSeed());
                        idSeed = getIdSeed(account.getSeed());
                    }
                }
                sql = "INSERT INTO " + CryptoCoreSQLiteContract.GeneralAccounts.TABLE_NAME + "("
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID + ","
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_NAME + ","
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_TYPE + ","
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID_SEED + ","
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ACCOUNT_INDEX + ","
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_EXTERNAL_INDEX + ","
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_CHANGE_INDEX + ""
                        + ") VALUES ("
                        + "'" + newId + "',"
                        + "'" + account.getName() + "',"
                        + "'" + account.getCoin().name() + "',"
                        + "'" + idSeed + "',"
                        + account.getAccountNumber() + ","
                        + account.getLastExternalIndex() + ","
                        + account.getLastChangeIndex()
                        + ")";
                System.out.println(sql);
                if (stmt.execute(sql)) {
                    account.setId(newId);
                }
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void updateGeneralAccount(final GeneralCoinAccount account) {
        Statement stmt = null;
        String sql = "";

        this.connect();
        if (db != null) {
            try {
                stmt = db.createStatement();
                sql = "UPDATE " + CryptoCoreSQLiteContract.GeneralAccounts.TABLE_NAME + " SET "
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_NAME + " = '" + account.getName() + "',"
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_TYPE + " = '" + account.getCoin().name() + "',"
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID_SEED + " = '" + account.getSeed().getId() + "',"
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ACCOUNT_INDEX + " = " + account.getAccountNumber() + ","
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_EXTERNAL_INDEX + " = " + account.getLastExternalIndex() + ","
                        + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_CHANGE_INDEX + " = " + account.getLastChangeIndex()
                        + " WHERE " + CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID + " = " + account.getId();
                stmt.execute(sql);
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<GeneralCoinAccount> getAccounts() {
        Statement stmt;
        String sql;
        List<GeneralCoinAccount> accounts = new ArrayList();

        this.connect();
        if (db != null) {
            try {
                stmt = db.createStatement();
                sql = "SELECT * FROM " + CryptoCoreSQLiteContract.GeneralAccounts.TABLE_NAME;
                ResultSet rs = stmt.executeQuery(sql);

                GeneralCoinAccount account;
                while (rs.next()) {
                    String id = rs.getString(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID);
                    String name = rs.getString(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_NAME);
                    Coin type = Coin.valueOf(rs.getString(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_TYPE));
                    String idSeed = rs.getString(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ID_SEED);
                    AccountSeed seed = getSeed(idSeed);
                    if (seed == null) {
                        continue;
                    }
                    int accountIndex = rs.getInt(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_ACCOUNT_INDEX);
                    int changeIndex = rs.getInt(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_CHANGE_INDEX);
                    int externalIndex = rs.getInt(CryptoCoreSQLiteContract.GeneralAccounts.COLUMN_EXTERNAL_INDEX);
                    account = CryptoCoinFactory
                            .getGeneralCoinManager(type)
                            .getAccount(id, name, seed, accountIndex,
                                    externalIndex, changeIndex);

                    accounts.add(account);
                }
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return accounts;
    }

    public List<GeneralCoinAccount> getAccounts(List<AccountSeed> seeds) {
        Statement stmt;
        String sql;
        List<GeneralCoinAccount> accounts = new ArrayList();
        this.connect();

        if (db != null) {
            try {
                stmt = db.createStatement();
                sql = "SELECT * FROM " + CryptoCoreSQLiteContract.GeneralAccounts.TABLE_NAME;
                ResultSet rs = stmt.executeQuery(sql);

                GeneralCoinAccount account;
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
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return accounts;
    }
}
