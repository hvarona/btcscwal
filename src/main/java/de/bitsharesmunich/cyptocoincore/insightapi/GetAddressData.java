package de.bitsharesmunich.cyptocoincore.insightapi;

import de.bitsharesmunich.cryptocoincore.base.GeneralCoinAddress;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bitcoinj.core.NetworkParameters;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author henry
 */
public class GetAddressData extends Thread {

    private final String urlQuery = "/insight-api/addr/";
    
    private GeneralCoinAddress address;

    private String serverUrl;

    public GetAddressData(String server, int port, GeneralCoinAddress address, NetworkParameters param) {
        serverUrl = "http://" + server + ":" + port + urlQuery + address.getAddressString(param);
        this.address = address;
    }
    
    public GetAddressData(String server, int port, String addressString) {
        serverUrl = "http://" + server + ":" + port + "/insight-api/" + urlQuery + addressString;
    }

    public void run() {
        InputStream response = null;
        try {
            
            URLConnection connection = new URL(serverUrl).openConnection();
            response = connection.getInputStream();
            Scanner scanner = new Scanner(response);
            String responseBody = scanner.useDelimiter("\\A").next();
            System.out.println(responseBody);
            JSONObject responseObject = new JSONObject(responseBody);
            System.out.println("balance " + responseObject.getString("balance"));

        } catch (IOException ex) {
            Logger.getLogger(GetAddressData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GetAddressData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                response.close();
            } catch (IOException ex) {
                Logger.getLogger(GetAddressData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
