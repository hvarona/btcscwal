package de.bitsharesmunich.cryptocoincore.bitcoin;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.DeterministicKey;


/**
 *
 * @author henry
 */
public class BitcoinAddress {
    private final DeterministicKey privateKey;
    private final NetworkParameters params;

    public BitcoinAddress(DeterministicKey privateKey, NetworkParameters params) {
        this.privateKey = privateKey;
        this.params = params;
    }
    
    public String getAddress(){
        return this.privateKey.toAddress(params).toString();
    }
    
    public ECKey getPrivateKey(){
        return privateKey;
    }
        
}
