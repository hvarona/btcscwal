package de.bitsharesmunich.cryptocoincore.bitcoin;

import de.bitsharesmunich.cryptocoincore.base.CryptoCoinObjectsManager;
import org.bitcoinj.core.BlockChain;
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
public class BitcoinObjectsManager extends CryptoCoinObjectsManager{
    
    protected NetworkParameters netParams = TestNet3Params.get();
    private BitcoinObjectsManager instance = null;
    
    private BitcoinObjectsManager(){
    }
    
    public BitcoinObjectsManager getInstance(){
        if (this.instance == null){
            this.instance = new BitcoinObjectsManager();
        }
        
        return this.instance;
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
            
            return new BitcoinAccount(new BitcoinAccountSeed(wallet, this.netParams));
            
        } catch (BlockStoreException ex){

        } finally {
        }   
        
        return null;
    }
    
    /*public BitcoinAccount getAccount(BitcoinAccountId id){
        return new BitcoinAccount(id);
    }*/
}
