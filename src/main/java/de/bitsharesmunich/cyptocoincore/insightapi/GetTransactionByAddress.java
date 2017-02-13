package de.bitsharesmunich.cyptocoincore.insightapi;

import de.bitsharesmunich.cryptocoincore.base.Coin;
import de.bitsharesmunich.cryptocoincore.base.GIOTx;
import de.bitsharesmunich.cryptocoincore.base.GeneralCoinAddress;
import de.bitsharesmunich.cryptocoincore.base.GeneralTransaction;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bitcoinj.core.NetworkParameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author henry
 */
public class GetTransactionByAddress extends Thread {

    private final String urlQuery = "/insight-api/addrs/";
    private final String urlPostQuery = "/txs/";
    private NetworkParameters param;
    List<GeneralCoinAddress> addresses = new ArrayList();

    private String serverUrl;

    public GetTransactionByAddress(String server, int port, NetworkParameters param) {
        serverUrl = "http://" + server + ":" + port + urlQuery;
        this.param = param;
    }

    public void addAdress(GeneralCoinAddress address) {
        addresses.add(address);
    }

    @Override
    public void run() {
        if (addresses.size() > 0) {
            try {
                for (GeneralCoinAddress address : addresses) {
                    serverUrl += address.getAddressString(param) + ",";

                }
                serverUrl = serverUrl.substring(0, serverUrl.length() - 1) + urlPostQuery;
                URLConnection connection = new URL(serverUrl).openConnection();
                InputStream response = connection.getInputStream();
                Scanner scanner = new Scanner(response);
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println(responseBody);
                JSONObject responseObject = new JSONObject(responseBody);
                JSONArray items = responseObject.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject transactionObject = items.getJSONObject(i);
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
                        GIOTx input = new GIOTx();
                        input.setAmount(vin.getLong("valueSat"));
                        input.setTransaction(transaction);
                        input.setIsOut(true);
                        input.setType(Coin.BITCOIN);
                        String addr = vin.getString("addr");
                        input.setAddressString(addr);
                        for (GeneralCoinAddress address : addresses) {
                            if (address.getAddressString(param).equals(addr)) {
                                input.setAddress(address);
                                address.getOutputTransaction().add(input);
                            }
                        }
                    }

                    JSONArray vouts = transactionObject.getJSONArray("vout");
                    for (int j = 0; j < vouts.length(); j++) {
                        JSONObject vout = vouts.getJSONObject(j);
                        GIOTx input = new GIOTx();
                        System.out.println("value " + vout.getDouble("value"));
                        input.setAmount((long)(vout.getDouble("value")*1000000000));
                        input.setTransaction(transaction);
                        input.setIsOut(false);
                        input.setType(Coin.BITCOIN);
                        String addr = vout.getJSONObject("scriptPubKey").getJSONArray("addresses").getString(0);
                        input.setAddressString(addr);
                        for (GeneralCoinAddress address : addresses) {
                            if (address.getAddressString(param).equals(addr)) {
                                input.setAddress(address);
                                address.getInputTransaction().add(input);
                            }
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

}
