/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitsharesmunich.cryptocoincore.bitcoin;

import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccount;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.FilteredBlock;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.net.discovery.DnsDiscovery;
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
    PeerGroup peerGroup;
    
    
    public BitcoinAccount(BitcoinAccountSeed seed){
        this.seed = seed;
    }
    
    public void connect(){
        if (this.peerGroup == null){
            try{
                Wallet wallet = ((BitcoinAccountSeed)this.id).getWallet();
                NetworkParameters netParams = wallet.getNetworkParameters();
                File file = new File("bc.bs");
                BlockStore blockStore = new SPVBlockStore(netParams, file);
                BlockChain chain = new BlockChain(netParams, wallet, blockStore);
                this.peerGroup = new PeerGroup(netParams, chain);
                this.peerGroup.addPeerDiscovery(new DnsDiscovery(netParams));
                this.peerGroup.addWallet(wallet);                
            } catch(BlockStoreException e){
            
            }
        }
    }
    
    public String getBalance(){
        Wallet wallet = ((BitcoinAccountSeed)this.id).getWallet();

        connect();
        this.peerGroup.start();            

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

        this.peerGroup.startBlockChainDownload(bListener);

        try{
            bListener.await();
        } catch(InterruptedException e){

        }
        System.out.println("("+dateFormat.format(now)+") Block chain downloaded");            

        String balance = (wallet.getBalance(AVAILABLE)).toFriendlyString();

        this.peerGroup.stop();
        return balance;
    }
    
    public void sendCoin(Address to, Coin ammount){
        connect();
        Wallet wallet = ((BitcoinAccountSeed)this.id).getWallet();
        Coin satoshiAndFee = Coin.SATOSHI.add(Transaction.REFERENCE_DEFAULT_MIN_TX_FEE);
        
        try{
            System.out.println("Sending ...");
            final Wallet.SendResult sendResult = wallet.sendCoins(this.peerGroup, to, ammount);
            
            
            Futures.addCallback(sendResult.broadcastComplete, new FutureCallback<Transaction>() {
                public void onSuccess(Transaction transaction) {
                    System.out.println("Sent coins back! Transaction hash is " + sendResult.tx.getHashAsString());
                    // The wallet has changed now, it'll get auto saved shortly or when the app shuts down.
                }

                public void onFailure(Throwable throwable) {
                    System.err.println("Failed to send coins :(");
                    throwable.printStackTrace();
                }
            });
        } catch(InsufficientMoneyException e){
            System.out.println("There's no funds");
        }
    }
    
    public Address getAddress(){
        return ((BitcoinAccountSeed)this.id).getWallet().currentReceiveAddress();
    }
    
}
