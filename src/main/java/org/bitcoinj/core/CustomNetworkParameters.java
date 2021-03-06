package org.bitcoinj.core;

import de.bitsharesmunich.cryptocoincore.dogecoin.DogeCoinNetworkParameters;
import de.bitsharesmunich.cryptocoincore.base.Coin;
import de.bitsharesmunich.cryptocoincore.base.CoinDefinitions;
import de.bitsharesmunich.cryptocoincore.base.dash.DashNetworkParameters;
import de.bitsharesmunich.cryptocoincore.litecoin.LiteCoinNetworkParameters;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptOpCodes;

/**
 *
 * @author henry
 */
public abstract class CustomNetworkParameters extends NetworkParameters {

    protected CoinDefinitions coinDefinitions;

    //Dash Extra Parameters
    protected String strSporkKey;
    String strMasternodePaymentsPubKey;
    String strDarksendPoolDummyAddress;
    long nStartMasternodePayments;

    public String getSporkKey() {
        return strSporkKey;
    }

    protected CustomNetworkParameters(CoinDefinitions coinDefinitions) {
        super();
        alertSigningKey = Utils.HEX.decode(coinDefinitions.satoshiKey);
        this.coinDefinitions = coinDefinitions;
        genesisBlock = createGenesis(this, coinDefinitions);
    }

    //TODO:  put these bytes into the CoinDefinition
    private static Block createGenesis(NetworkParameters n, CoinDefinitions coinDefinitions) {
        Block genesisBlock = coinDefinitions.getCoinBlock(n);
        
        //Block genesisBlock = new Block(n, Block.BLOCK_VERSION_GENESIS,Sha256Hash.ZERO_HASH, Sha256Hash.ZERO_HASH, System.currentTimeMillis() / 1000, coinDefinitions.genesisBlockDifficultyTarget , 0, new ArrayList());
        Transaction t = new Transaction(n);
        try {
            // A script containing the difficulty bits and the following message:
            //
            //   coin dependent
            byte[] bytes = Utils.HEX.decode(coinDefinitions.genesisTxInBytes);

            t.addInput(new TransactionInput(n, t, bytes));
            ByteArrayOutputStream scriptPubKeyBytes = new ByteArrayOutputStream();
            Script.writeBytes(scriptPubKeyBytes, Utils.HEX.decode(coinDefinitions.genesisTxOutBytes));

            scriptPubKeyBytes.write(ScriptOpCodes.OP_CHECKSIG);
            t.addOutput(new TransactionOutput(n, t, org.bitcoinj.core.Coin.valueOf(coinDefinitions.genesisBlockValue, 0), scriptPubKeyBytes.toByteArray()));
        } catch (Exception e) {
            // Cannot happen.
            throw new RuntimeException(e);
        }
        genesisBlock.addTransaction(t);
        return genesisBlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return getId().equals(((NetworkParameters) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public static NetworkParameters fromCoin(Coin coin) {
        switch (coin) {
            case BITCOIN:
                return NetworkParameters.fromID(ID_MAINNET);
            case DASH:
                return new DashNetworkParameters();
            case LITECOIN:
                return new LiteCoinNetworkParameters();
            case DOGECOIN:
                return new DogeCoinNetworkParameters();
        };
        return null;
    }
}
