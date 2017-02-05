package de.bitsharesmunich.cryptocoincore.bitcoin;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.DeterministicKey;

/**
 *
 * @author Henry
 */
public class BitcoinAddress {

    private final DeterministicKey privateKey;
    private final NetworkParameters params;

    public BitcoinAddress(DeterministicKey privateKey, NetworkParameters params) {
        this.privateKey = privateKey;
        this.params = params;
    }

    public String getAddressString() {
        return this.privateKey.toAddress(params).toString();
    }

    public Address getAddress() {
        return this.privateKey.toAddress(params);
    }

    public ECKey getPrivateKey() {
        return privateKey;
    }

}
