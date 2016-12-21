/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cryptocoincore.test;

import com.cryptocoincore.bitcoin.BitcoinAccount;
import com.cryptocoincore.bitcoin.BitcoinAccountId;
import com.cryptocoincore.bitcoin.BitcoinObjectsFactory;
import org.bitcoinj.params.TestNet3Params;
/**
 *
 * @author javier
 */
public class MainTest {
    
    
    public void testBitcoinAccountCreation(){
        BitcoinObjectsFactory bitcoinFactory = new BitcoinObjectsFactory(TestNet3Params.get());        
        BitcoinAccount account = bitcoinFactory.newAccount();
        System.out.println(account.getId().getJsonString());              
    }
    
    public void testBitcoinAccountCreationFromString(){
        BitcoinAccountId accountId = new BitcoinAccountId("{\"creation_time\":1482115724,\"deterministic_seed\":\"monitor swim symbol sadness illegal boring age language hand way carry amount\",\"net_id\":\"org.bitcoin.test\"}");
        BitcoinAccount account = new BitcoinAccount(accountId);
        
        System.out.println("address:"+account.getAddress()+" balance:"+account.getBalance());
        
    }
}
