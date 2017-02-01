/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cryptocoincore.test;

import com.cryptocoincore.bitcoin.BitcoinAccount;
import com.cryptocoincore.bitcoin.BitcoinAccountSeed;
import com.cryptocoincore.bitcoin.BitcoinObjectsFactory;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
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
        BitcoinAccountSeed accountId = new BitcoinAccountSeed("{\"creation_time\":1482115724,\"deterministic_seed\":\"monitor swim symbol sadness illegal boring age language hand way carry amount\",\"net_id\":\"org.bitcoin.test\"}");
        BitcoinAccount account = new BitcoinAccount(accountId);
        
        System.out.println("address:"+account.getAddress()+" balance:"+account.getBalance());
        
    }
    
    public void testBitcoinAccountSendingCoins(){
        BitcoinAccountSeed accountId = new BitcoinAccountSeed("{\"creation_time\":1482115724,\"deterministic_seed\":\"monitor swim symbol sadness illegal boring age language hand way carry amount\",\"net_id\":\"org.bitcoin.test\"}");
        BitcoinAccount account = new BitcoinAccount(accountId);
        NetworkParameters netParams = accountId.getWallet().getNetworkParameters();
        
        Address to = Address.fromBase58(netParams,"mkFrBPyadfMdPagjL9hu3Hd7TQZJGD4S3T");
        Coin ammount = Coin.parseCoin("0.00000546");
        
        account.sendCoin(to, ammount);        
    }
}
