package de.bitsharesmunich.cyptocoincore.insightapi;

import com.google.gson.Gson;
import de.bitsharesmunich.cryptocoincore.base.Coin;
import de.bitsharesmunich.cryptocoincore.base.GTxIO;
import de.bitsharesmunich.cryptocoincore.base.GeneralCoinAccount;
import de.bitsharesmunich.cryptocoincore.base.GeneralCoinAddress;
import de.bitsharesmunich.cryptocoincore.base.GeneralTransaction;
import de.bitsharesmunich.cryptocoincore.insightapi.models.AddressTxi;
import de.bitsharesmunich.cryptocoincore.insightapi.models.Txi;
import de.bitsharesmunich.cryptocoincore.insightapi.models.Vin;
import de.bitsharesmunich.cryptocoincore.insightapi.models.Vout;
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

    public static GeneralTransaction parseTransaction(String jsonTransaction, Coin coin) {

        Gson gson = new Gson();
        Txi txi  = gson.fromJson(jsonTransaction, Txi.class);

        if(txi != null){
            GeneralTransaction transaction = new GeneralTransaction();
            transaction.setTxid(txi.txid);
            transaction.setBlock(txi.blockheight);
            transaction.setDate(new Date(txi.time * 1000));
            transaction.setFee((long) (txi.fee * Math.pow(10, coin.getPrecision())));
            transaction.setConfirm(txi.confirmations);
            transaction.setType(coin);
            transaction.setBlockHeight(txi.blockheight);

            for (Vin vin : txi.vin) {
                GTxIO input = new GTxIO();
                input.setAmount((long) (vin.value * Math.pow(10, coin.getPrecision())));
                input.setTransaction(transaction);
                input.setOut(true);
                input.setType(coin);
                String addr = vin.addr;
                input.setAddressString(addr);
                input.setIndex(vin.n);
                input.setScriptHex(vin.scriptSig.hex);
                input.setOriginalTxid(vin.txid);
                transaction.getTxInputs().add(input);
            }

            for (Vout vout : txi.vout) {
                if (vout.scriptPubKey.addresses == null || vout.scriptPubKey.addresses.length <= 0) {
                    // The address is null, this must be a memo
                    String hex = vout.scriptPubKey.hex;
                    int opReturnIndex = hex.indexOf("6a");
                    if (opReturnIndex >= 0) {
                        byte[] memoBytes = new byte[Integer.parseInt(hex.substring(opReturnIndex + 2, opReturnIndex + 4), 16)];
                        for (int i = 0; i < memoBytes.length; i++) {
                            memoBytes[i] = Byte.parseByte(hex.substring(opReturnIndex + 4 + (i * 2), opReturnIndex + 6 + (i * 2)), 16);
                        }
                        transaction.setMemo(new String(memoBytes));
                    }
                } else {
                    GTxIO output = new GTxIO();
                    output.setAmount((long) (vout.value * Math.pow(10, coin.getPrecision())));
                    output.setTransaction(transaction);
                    output.setOut(false);
                    output.setType(coin);
                    String addr = vout.scriptPubKey.addresses[0];
                    output.setAddressString(addr);
                    output.setIndex(vout.n);
                    output.setScriptHex(vout.scriptPubKey.hex);
                    transaction.getTxOutputs().add(output);
                }
            }
            return transaction;
        }
        return null;
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
                        GTxIO input = new GTxIO();
                        input.setAmount(vin.getLong("valueSat"));
                        input.setTransaction(transaction);
                        input.setOut(true);
                        input.setType(Coin.BITCOIN);
                        String addr = vin.getString("addr");
                        input.setAddressString(addr);
                        for (GeneralCoinAddress address : addresses) {
                            if (address.getAddressString(param).equals(addr)) {
                                input.setAddress(address);
                                address.getTransactionOutput().add(input);
                            }
                        }
                    }

                    JSONArray vouts = transactionObject.getJSONArray("vout");
                    for (int j = 0; j < vouts.length(); j++) {
                        JSONObject vout = vouts.getJSONObject(j);
                        GTxIO input = new GTxIO();
                        System.out.println("value " + vout.getDouble("value"));
                        input.setAmount((long) (vout.getDouble("value") * 1000000000));
                        input.setTransaction(transaction);
                        input.setOut(false);
                        input.setType(Coin.BITCOIN);
                        String addr = vout.getJSONObject("scriptPubKey").getJSONArray("addresses").getString(0);
                        input.setAddressString(addr);
                        for (GeneralCoinAddress address : addresses) {
                            if (address.getAddressString(param).equals(addr)) {
                                input.setAddress(address);
                                address.getTransactionInput().add(input);
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
