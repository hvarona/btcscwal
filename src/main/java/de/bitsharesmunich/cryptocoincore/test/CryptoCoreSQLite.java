/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitsharesmunich.cryptocoincore.test;

import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccount;
import java.sql.*;
import java.util.ArrayList;
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
            try {
                db = DriverManager.getConnection("jdbc:sqlite:test.db");

                stmt = db.createStatement();
                sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+CryptoCoreSQLiteContract.CryptoCoinAccount.TABLE_NAME+"';"; 
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.getFetchSize()<=0){
                    creationStmt = db.createStatement();
                    sql = CryptoCoreSQLiteHelper.SQL_CREATE_ACCOUNT_TABLE; 
                    creationStmt.execute(sql);
                    creationStmt.close();
                }
                stmt.close();



            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void putCryptoCoinAccount(CryptoCoinAccount account){
        Statement stmt = null;
        String sql = "";
            
        this.connect();
        if (db != null){
            try {            
                String newId = UUID.randomUUID().toString();
                stmt = db.createStatement();
                sql = "INSERT INTO "+CryptoCoreSQLiteContract.CryptoCoinAccount.TABLE_NAME+"("
                      +CryptoCoreSQLiteContract.CryptoCoinAccount.COLUMN_ID+","
                      +CryptoCoreSQLiteContract.CryptoCoinAccount.COLUMN_COIN+","
                      +CryptoCoreSQLiteContract.CryptoCoinAccount.COLUMN_SEED+""
                      +") VALUES ("
                      +"'"+newId+"',"
                      +"'"+account.getCoin().name()+"',"
                      +"'"+account.getSeed().getJsonString()+"'"
                      +")";
                if (stmt.execute(sql)){
                    stmt.close();                
                    account.setId(newId);
                }
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public List<CryptoCoinAccount> getAccounts(){
        Statement stmt = null;
        String sql = "";
        List<CryptoCoinAccount> accounts = new ArrayList<CryptoCoinAccount>();    
        
        this.connect();
        if (db != null){
            try {            
                stmt = db.createStatement();
                sql = "SELECT * FROM "+CryptoCoreSQLiteContract.CryptoCoinAccount.TABLE_NAME;
                ResultSet rs = stmt.executeQuery(sql);
                
                if (rs.getFetchSize() > 0){
                    
                    while(rs.next()){
                        CryptoCoreAccount account =
                        
                        rs.getString(CryptoCoreSQLiteContract.CryptoCoinAccount.COLUMN_ID);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(CryptoCoreSQLite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void modifiyCryptoCoinAccount(CryptoCoinAccount account){
        Statement stmt = null;
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
        }
    }
}
