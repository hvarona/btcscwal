/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cryptocoincore.bitcoin;

import com.cryptocoincore.base.CryptoCoinAccount;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.FilteredBlock;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.Wallet;
import static org.bitcoinj.wallet.Wallet.BalanceType.*;
import static org.spongycastle.asn1.ua.DSTU4145NamedCurves.params;

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
            peerGroup.addPeerDiscovery(new DnsDiscovery(netParams));
            peerGroup.addWallet(wallet);
            peerGroup.start();            
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date now = new Date();
            System.out.println("("+dateFormat.format(now)+") Starting to download block chain...");
            
            DownloadProgressTracker bListener = new DownloadProgressTracker() {
                @Override
                public void doneDownload() {
                    System.out.println("blockchain downloaded");
                }
                
                @Override                
                public void onBlocksDownloaded(Peer peer, Block block, FilteredBlock filteredBlock, int blocksLeft){
                    System.out.println(blocksLeft+" blocks remains...");
                }
                
                @Override                
                public void progress(double pct, int blocksSoFar, Date date){
                    System.out.println(blocksSoFar+" blocks so far...");
                }
            };
                        
            peerGroup.startBlockChainDownload(bListener);
            
            try{
                bListener.await();
            } catch(InterruptedException e){
                
            }
            System.out.println("("+dateFormat.format(now)+") Block chain downloaded");            
            
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
