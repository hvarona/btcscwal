/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitsharesmunich.cryptocoincore.test;

import de.bitsharesmunich.cryptocoincore.base.Coin;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccount;
import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinFactory;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinManager;
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
public class CryptoCoreSQLite{
    
    private Connection db = null;
    
    public void connect(){
        if (db == null){
            Statement stmt = null;
            Statement creationStmt = null;
            String sql = "";
            ResultSet rs;
            try {
                db = DriverManager.getConnection("jdbc:sqlite:test.db");

                stmt = db.createStatement();
                sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+CryptoCoreSQLiteContract.CryptoCoinSeed.TABLE_NAME+"';"; 
                rs = stmt.executeQuery(sql);
                if (rs.getFetchSize()<=0){
                    creationStmt = db.createStatement();
                    sql = CryptoCoreSQLiteHelper.SQL_CREATE_SEED_TABLE; 
                    creationStmt.execute(sql);
                    creationStmt.close();
                }
                stmt.close();

                stmt = db.createStatement();
                sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+CryptoCoreSQLiteContract.CryptoCoinSeedCoin.TABLE_NAME+"';"; 
                rs = stmt.executeQuery(sql);
                if (rs.getFetchSize()<=0){
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
    
    public void putCryptoCoinSeed(AccountSeed seed){
        Statement stmt = null;
        String sql = "";
            
        this.connect();
        if (db != null){
            try {            
                String newId = UUID.randomUUID().toString();
                stmt = db.createStatement();
                sql = "INSERT INTO "+CryptoCoreSQLiteContract.CryptoCoinSeed.TABLE_NAME+"("
                      +CryptoCoreSQLiteContract.CryptoCoinSeed.COLUMN_ID+","
                      +CryptoCoreSQLiteContract.CryptoCoinSeed.COLUMN_TYPE+","
                      +CryptoCoreSQLiteContract.CryptoCoinSeed.COLUMN_MNEMONIC+","
                      +CryptoCoreSQLiteContract.CryptoCoinSeed.COLUMN_ADDITIONAL+""
                      +") VALUES ("
                      +"'"+newId+"',"
                      +"'"+seed.getType().name()+"',"
                      +"'"+seed.getMnemonicCodeString()+"',"
                      +"'"+seed.getAdditional()+"'"                      
                      +")";
                if (stmt.execute(sql)){
                    seed.setId(newId);
                }
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public List<AccountSeed> getSeeds(){
        Statement stmt = null;
        String sql = "";
        List<AccountSeed> seeds = new ArrayList<AccountSeed>();    
        
        this.connect();
        if (db != null){
            try {            
                stmt = db.createStatement();
                sql = "SELECT s.*,sc.type AS coin_type FROM "+CryptoCoreSQLiteContract.CryptoCoinSeed.TABLE_NAME+" s"
                     +" LEFT JOIN "+CryptoCoreSQLiteContract.CryptoCoinSeedCoin.TABLE_NAME+" sc ON sc."+CryptoCoreSQLiteContract.CryptoCoinSeedCoin.COLUMN_ID_SEED+" = s."+CryptoCoreSQLiteContract.CryptoCoinSeed.COLUMN_ID+" ";
                ResultSet rs = stmt.executeQuery(sql);
                
                if (rs.getFetchSize() > 0){
                    AccountSeed seed;
                    List<String> mnemonic;
                    CryptoCoinManager manager;
                    Coin coin;
                    while(rs.next()){
                        coin = Coin.valueOf(rs.getString("coin_type"));
                        manager = CryptoCoinFactory.getObjectManager(coin);
                        
                        mnemonic = new ArrayList<String>(Arrays.asList(rs.getString("mnemonic").split(" ")));
                        seed = manager.getAccountFromJsonSeed() CryptoCoinAccountSeed(
                            rs.getString("id"),
                            mnemonic,
                            rs.getString("additional")    
                        );                       
                        
                        seeds.add(seed);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }                        
        }
        
        return seeds;
    }
    
    public void modifiyCryptoCoinAccount(CryptoCoinAccount account){ //TODO:Change this to modifyCryptoCoinSeed        
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
