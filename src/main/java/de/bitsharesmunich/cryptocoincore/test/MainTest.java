package de.bitsharesmunich.cryptocoincore.test;

import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.seed.BIP39;
import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinAccount;
import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
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
            Socket socket = IO.socket("http://fr.blockpay.ch:3002/");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... os) {
                    System.out.println("Connected");
                    socket.emit("subscribe", "inv");
                    //socket.disconnect();
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
            System.out.println("connecting...");
            socket.connect();
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
