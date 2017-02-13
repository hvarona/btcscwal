package de.bitsharesmunich.cryptocoincore.test;

import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.seed.BIP39;
import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinAccount;
import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinManager;
import de.bitsharesmunich.cyptocoincore.insightapi.GetAddressData;
import de.bitsharesmunich.cyptocoincore.insightapi.GetTransactionByAddress;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Henry
 */
public class MainTest {

    public void testBitcoinAccountCreation() {
        BitcoinManager bitcoinFactory = BitcoinManager.getInstance();
        BIP39 accountSeed = new BIP39("away rough beauty exist media curious labor recycle input riot produce rain series orphan exclude kit depend unfold still dizzy young girl emotion ahead", "");
        BitcoinAccount account = bitcoinFactory.newAccount(accountSeed, "test account");
        CryptoCoreSQLite db = new CryptoCoreSQLite();
        db.connect();
        db.putSeed(accountSeed);
        db.putGeneralAccount(account);
        System.out.println(account.toString());
    }

    public void testBip39SeedGeneration() {
        try {
            String current = new java.io.File(".").getCanonicalPath();
            File file = new File(current + "/src/main/java/de/bitsharesmunich/cryptocoincore/test/bip39dict.txt");

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String words = bufferedReader.readLine();
            BIP39 accountSeed = new BIP39(words.split(","));
            CryptoCoreSQLite db = new CryptoCoreSQLite();
            db.connect();
            db.putSeed(accountSeed);
        } catch (IOException ex) {
            Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void testBitcoinImportSeed(AccountSeed seed) {
        BitcoinManager bitcoinFactory = BitcoinManager.getInstance();
        BitcoinAccount account = bitcoinFactory.importAccount(seed, "test account import");
        System.out.println(account.toString());
    }

    public void testSocketConnection() {
        try {
            //BIP39 accountSeed = new BIP39("away rough beauty exist media curious labor recycle input riot produce rain series orphan exclude kit depend unfold still dizzy young girl emotion ahead", "");
            BIP39 accountSeed = new BIP39("BACHELOR BOARD ROUND RACCOON ELITE EDGE LAMP SQUIRREL UPSET REGION MIX NICE".toLowerCase(), "");
            final BitcoinAccount account = BitcoinManager.getInstance().newAccount(accountSeed, "test account");
            System.out.println("address " + account.getNextRecieveAddress());

            Socket socket = IO.socket("http://fr.blockpay.ch:3003/");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... os) {
                    try {
                        System.out.println("Connected ");
                        socket.emit("subscribe", "inv");
                        String[] toSend = new String[2];
                        toSend[0] = "bitcoind/addresstxid";
                        toSend[1] = account.getNextRecieveAddress();
                        socket.emit("subscribe", new Object[]{"bitcoind/addresstxid", new String[]{account.getNextRecieveAddress()}});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).on("bitcoind/addresstxid", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    try {
                        System.out.println("New addr transaction received: " + ((JSONObject) args[0]).toString());
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    System.out.println("Error " + os[0].toString());
                    socket.disconnect();
                }
            }).on("tx", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    try {
                        System.out.println("New transaction received: " + ((JSONObject) args[0]).toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            new Thread() {
                public void run() {
                    System.out.println("connecting...");
                    socket.connect();
                }
            }.start();
            /*new GetAddressData("fr.blockpay.ch", 3003,account.getAddressString(0, false)).start();
            new GetAddressData("fr.blockpay.ch", 3003,account.getAddressString(0, true)).start();
            new GetAddressData("fr.blockpay.ch", 3003,account.getAddressString(1, false)).start();
            new GetAddressData("fr.blockpay.ch", 3003,account.getAddressString(2, false)).start();
            new GetAddressData("fr.blockpay.ch", 3003,account.getAddressString(3, false)).start();
            new GetAddressData("fr.blockpay.ch", 3003,account.getAddressString(4, false)).start();
            new GetAddressData("fr.blockpay.ch", 3003,account.getAddressString(5, false)).start();*/
            GetTransactionByAddress request = new GetTransactionByAddress("fr.blockpay.ch", 3003, account.getNetworkParam());
            request.addAdress(account.getAddress(0, false));
            request.addAdress(account.getAddress(1, false));
            request.addAdress(account.getAddress(0, true));
            request.addAdress(account.getAddress(1, true));
            request.start();

            System.out.println("account balance : " + account.getBalance().get(0).getAmmount());
            Thread.sleep(60000);
            System.out.println("account balance : " + account.getBalance().get(0).getAmmount());
            System.out.println("address " + account.getNextRecieveAddress());
            socket.disconnect();
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
