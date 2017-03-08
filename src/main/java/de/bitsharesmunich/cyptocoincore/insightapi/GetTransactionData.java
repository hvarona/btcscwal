package de.bitsharesmunich.cyptocoincore.insightapi;

import de.bitsharesmunich.cryptocoincore.base.Coin;
import de.bitsharesmunich.cryptocoincore.base.GTxIO;
import de.bitsharesmunich.cryptocoincore.base.GeneralCoinAccount;
import de.bitsharesmunich.cryptocoincore.base.GeneralCoinAddress;
import de.bitsharesmunich.cryptocoincore.base.GeneralTransaction;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author henry
 */
public class GetTransactionData extends Thread {

    private final String urlQuery = "/insight-api/tx/";

    private final GeneralCoinAccount account;
    private final String serverUrl;

    public GetTransactionData(String server, int port, String txi, GeneralCoinAccount account) {
        serverUrl = "http://" + server + ":" + port + urlQuery + txi;
        this.account = account;
    }

    @Override
    public void run() {
        try {
            URLConnection connection = new URL(serverUrl).openConnection();
            InputStream response = connection.getInputStream();
            Scanner scanner = new Scanner(response);
            String responseBody = scanner.useDelimiter("\\A").next();

            JSONObject transactionObject = new JSONObject(responseBody);
            GeneralTransaction transaction = new GeneralTransaction();
            transaction.setTxid(transactionObject.getString("txid"));
            transaction.setBlock(transactionObject.getLong("blockheight"));
            transaction.setDate(new Date(1486850318));
            transaction.setFee((long) (transactionObject.getDouble("fees") * 1000000000));
            transaction.setConfirm(transactionObject.getInt("confirmations"));
            transaction.setType(Coin.BITCOIN);//TODO

            JSONArray vins = transactionObject.getJSONArray("vin");
            for (int j = 0; j < vins.length(); j++) {
                JSONObject vin = vins.getJSONObject(j);
                GTxIO input = new GTxIO();
                input.setAmount(vin.getLong("valueSat"));
                input.setTransaction(transaction);
                input.setOut(true);
                input.setType(Coin.BITCOIN);
                String addr = vin.getString("addr");
                input.setAddressString(addr);
                for (GeneralCoinAddress address : account.getAddresses()) {
                    if (address.getAddressString(account.getNetworkParam()).equals(addr)) {
                        input.setAddress(address);
                        address.getTransactionOutput().add(input);
                    }
                }
            }

            JSONArray vouts = transactionObject.getJSONArray("vout");
            for (int j = 0; j < vouts.length(); j++) {
                JSONObject vout = vouts.getJSONObject(j);
                GTxIO input = new GTxIO();
                input.setAmount((long) (vout.getDouble("value") * 1000000000));
                input.setTransaction(transaction);
                input.setOut(false);
                input.setType(Coin.BITCOIN);
                String addr = vout.getJSONObject("scriptPubKey").getJSONArray("addresses").getString(0);
                input.setAddressString(addr);
                for (GeneralCoinAddress address : account.getAddresses()) {
                    if (address.getAddressString(account.getNetworkParam()).equals(addr)) {
                        input.setAddress(address);
                        address.getTransactionInput().add(input);
                    }
                }
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(GetTransactionByAddress.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GetTransactionByAddress.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetTransactionByAddress.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
