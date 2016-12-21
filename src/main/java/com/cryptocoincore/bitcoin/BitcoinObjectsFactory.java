/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cryptocoincore.bitcoin;

import com.cryptocoincore.base.CryptoCoinObjectsFactory;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;

/**
 *
 * @author javier
 */
public class BitcoinObjectsFactory extends CryptoCoinObjectsFactory{
    
    protected NetworkParameters netParams = TestNet3Params.get();
    
    public BitcoinObjectsFactory(NetworkParameters netParams){
        this.netParams = netParams;
    }
    
    @Override
    public BitcoinAccount newAccount(){
        Wallet wallet = new Wallet(this.netParams);
        BlockStore blockStore = new MemoryBlockStore(netParams);

        try {
            BlockChain chain = new BlockChain(this.netParams, wallet, blockStore);
            PeerGroup peerGroup = new PeerGroup(this.netParams, chain);
            peerGroup.addWallet(wallet);
            peerGroup.start();

            //Address walletCurrentAddress = wallet.currentReceiveAddress();
            //ECKey walletPrivateKey = wallet.currentReceiveKey();
            
            //assert walletPrivateKey.toAddress(wallet.getParams()).equals(walletCurrentAddress);
            //assert !c.equals(a);
            
            DeterministicSeed seed = wallet.getKeyChainSeed();
            peerGroup.stop();
            
            return new BitcoinAccount(new BitcoinAccountId(wallet, this.netParams));
            
        } catch (BlockStoreException ex){

        } finally {
        }   
        
        return null;
    }
    
    /*public BitcoinAccount getAccount(BitcoinAccountId id){
        return new BitcoinAccount(id);
    }*/
}
