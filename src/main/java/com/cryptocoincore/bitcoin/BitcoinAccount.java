/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cryptocoincore.bitcoin;

import com.cryptocoincore.base.CryptoCoinAccount;
import java.io.File;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.Wallet;
import static org.bitcoinj.wallet.Wallet.BalanceType.*;

/**
 *
 * @author javier
 */
public class BitcoinAccount extends CryptoCoinAccount{
    
    //BitcoinAccountId id;
    
    public BitcoinAccount(BitcoinAccountId id){
        this.id = id;
    }
    
    public String getBalance(){
        try {

            Wallet wallet = ((BitcoinAccountId)this.id).getWallet();
            NetworkParameters netParams = wallet.getNetworkParameters();

            //BlockStore blockStore = new MemoryBlockStore(netParams);
            File file = new File("bc.bs");
            BlockStore blockStore = new SPVBlockStore(netParams, file);
            BlockChain chain = new BlockChain(netParams, wallet, blockStore);
            PeerGroup peerGroup = new PeerGroup(netParams, chain);
            peerGroup.addWallet(wallet);
            peerGroup.start();            
            peerGroup.downloadBlockChain();
            
            String balance = (wallet.getBalance(AVAILABLE)).toFriendlyString();

            peerGroup.stop();
            return balance;
        } catch(BlockStoreException e){
            
        }
        
        return "";
    }
    
    public Address getAddress(){
        return ((BitcoinAccountId)this.id).getWallet().currentReceiveAddress();
    }
    
}
