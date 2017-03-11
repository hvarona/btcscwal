package de.bitsharesmunich.cryptocoincore.test;

import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.dash.DashAccount;
import de.bitsharesmunich.cryptocoincore.base.seed.BIP39;
import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinAccount;
import de.bitsharesmunich.cryptocoincore.bitcoin.BitcoinManager;
import de.bitsharesmunich.cryptocoincore.util.Util;
import de.bitsharesmunich.cyptocoincore.insightapi.GetAddressData;
import de.bitsharesmunich.cyptocoincore.insightapi.GetTransactionByAddress;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.UTXO;
import org.bitcoinj.script.Script;
import org.json.JSONObject;

/**
 *
 * @author Henry
 */
public class MainTest {
    
    private static final String TEST_SEED_WORDS = "away rough beauty exist media curious labor recycle input riot produce rain series orphan exclude kit depend unfold still dizzy young girl emotion ahead";

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
            BIP39 accountSeed = new BIP39(TEST_SEED_WORDS, "");
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
                        socket.emit("subscribe", new Object[]{"bitcoind/addresstxid", new String[]{"n1eheJRugaMeBCa2czHwPwk9kYGd41JQ7A"}});
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
            /*Thread.sleep(60000);
            System.out.println("account balance : " + account.getBalance().get(0).getAmmount());
            System.out.println("address " + account.getNextRecieveAddress());
            socket.disconnect();*/
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
        }/* catch (InterruptedException ex) {
            Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    public void testSendTransaction() {
        NetworkParameters params = NetworkParameters.fromID(NetworkParameters.ID_TESTNET);
        Transaction tx = new Transaction(params);
        BIP39 accountSeed = new BIP39(TEST_SEED_WORDS, "");
        final BitcoinAccount account = BitcoinManager.getInstance().newAccount(accountSeed, "test account");
        ECKey key = account.getAddress(0, false).getKey();
        System.out.println("address : " + key.toAddress(params).toBase58());
        // Variables from transaction
        String txid = "1885aff8fbb0044b54626384ad752d8379d7948dfe0e9bbdb6338c8f9857ac7f";
        byte[] scriptBytes = new byte[]{(byte) 0x76, (byte) 0xa9, (byte) 0x14, (byte) 0xde, (byte) 0x6a, (byte) 0x8f, (byte) 0x29, (byte) 0x0f, (byte) 0xe1, (byte) 0x2b, (byte) 0x7a, (byte) 0x60, (byte) 0x15, (byte) 0x86, (byte) 0xb7, (byte) 0xd9, (byte) 0x2c, (byte) 0x54, (byte) 0xdf, (byte) 0x36, (byte) 0xdd, (byte) 0x85, (byte) 0x5f, (byte) 0x88, (byte) 0xac};
        int blockheight = 1088396;
        long value = 100000;
        System.out.println("hex : " + Util.bytesToHex(org.bitcoinj.core.Utils.parseAsHexOrBase58(txid)));

        Transaction outputTransaction = new Transaction(params, Util.hexToBytes("01000000013069d4ebe5bcce4fac8951dd513b3b9ac11378f50131a0e930943dbac7cfe526000000006b483045022100deabd5cdeae7ff8636c8d6000033cf3bebe8a5947db3e3658bf1fc866d46a733022040c9324a5985da4f3efcf202a66e15e14dbd8385229074bac1e093741e69cab70121028b63c1ac4a8fe8c4e8cf804e589537320569b7196c6e6b38eca93d6002e70fc3ffffffff02a87a6400000000001976a9147cac011028424d349a7629cc0d2de8abbc2f35b988aca0860100000000001976a914de6a8f290fe12b7a601586b7d92c54df36dd855f88ac00000000"));
        System.out.println(outputTransaction.getOutput(1).getValue().getValue());
        System.out.println(outputTransaction.getOutput(1).getScriptPubKey().getToAddress(params));
        System.out.println(outputTransaction.getHash().toString());

        //String to an address
        Address address2 = Address.fromBase58(params, "mybW8qcbtBuW9LWyfEjxLirfruyZmMh9HA");

        //value is a sum of all inputs, fee is 4013
        tx.addOutput(Coin.valueOf(value - 10000), address2);
        Sha256Hash txHash = Sha256Hash.wrap(txid);
        System.out.println("txHash " + txHash.toString());
        Script script = new Script(scriptBytes);
        UTXO utxo = new UTXO(txHash, 1, Coin.valueOf(value), blockheight, true, script);
        TransactionOutPoint outPoint = new TransactionOutPoint(params, utxo.getIndex(), utxo.getHash());

        tx.addSignedInput(outPoint, utxo.getScript(), key, Transaction.SigHash.ALL, true);
        //tx.addSignedInput(outputTransaction.getOutput(1), key, Transaction.SigHash.ALL, true);

        //tx.getConfidence().setSource(TransactionConfidence.Source.SELF);
        tx.setPurpose(Transaction.Purpose.USER_PAYMENT);

        System.out.println(tx.getHashAsString());
        System.out.println(Util.bytesToHex(tx.bitcoinSerialize()));

        try {
            String urlParameters = "rawtx=" + Util.bytesToHex(tx.bitcoinSerialize()) + "";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            String request = "http://fr.blockpay.ch:3003/insight-api/tx/send";
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
                wr.flush();
            }
            
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            for (int c; (c = in.read()) >= 0;) {
                System.out.print((char) c);
            }

        } catch (IOException ex) {
            Logger.getLogger(GetAddressData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void testDashSendTransaction() {
        BIP39 accountSeed = new BIP39(TEST_SEED_WORDS, "");
        final DashAccount account = new DashAccount(accountSeed, " Test Dash Account");
        System.out.println(" dash next address " + account.getNextRecieveAddress());
        String toAddress ="XkysCa58ih9N5axycNfqVU7SsDA94CkbrT";
        account.send(toAddress, de.bitsharesmunich.cryptocoincore.base.Coin.DASH, 100000, "" , null);
    }
}
