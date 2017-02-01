/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitsharesmunich.cryptocoincore.bitcoin;

import de.bitsharesmunich.cryptocoincore.base.CryptoCoinAccountSeed;
import com.google.common.base.Joiner;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.json.*;

/**
 *
 * @author javier
 */
public class BitcoinAccountSeed extends CryptoCoinAccountSeed {
    protected Wallet wallet;
    protected NetworkParameters netParams;
    
    public BitcoinAccountSeed(Wallet wallet, NetworkParameters netParams){
        this.wallet = wallet;
        this.netParams = netParams;        
    }
    
    public Wallet getWallet(){
       return this.wallet; 
    }
    
    public BitcoinAccountSeed(String json){
        this.loadFromJsonString(json);   
    }
    
    public String getJsonString(){
        DeterministicSeed  seed = this.wallet.getKeyChainSeed();
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("net_id",this.netParams.getId());
            jsonObject.put("deterministic_seed",Joiner.on(" ").join(seed.getMnemonicCode()));
            jsonObject.put("creation_time",seed.getCreationTimeSeconds());
            return jsonObject.toString();
        } catch (JSONException e){
            
        }
        return null;
    }
    
    public boolean loadFromJsonString(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            
            NetworkParameters params = NetworkParameters.fromID(jsonObject.getString("net_id"));
            DeterministicSeed seed = new DeterministicSeed(jsonObject.getString("deterministic_seed"), null, "", jsonObject.getLong("creation_time"));
            this.wallet = Wallet.fromSeed(params, seed);
            this.netParams = params;
            
            return true;
        } catch (JSONException e){            
        } catch (UnreadableWalletException e){            
        }
        
        return false;
    }    
}
