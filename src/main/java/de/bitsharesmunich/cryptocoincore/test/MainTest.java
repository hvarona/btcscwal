package de.bitsharesmunich.cryptocoincore.test;

import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.GTxIO;
import de.bitsharesmunich.cryptocoincore.base.GeneralCoinAddress;
import de.bitsharesmunich.cryptocoincore.base.GeneralTransaction;
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
import java.util.ArrayList;
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
        account.calculateGapExternal();
        account.calculateGapChange();
        System.out.println(" dash next address " + account.getNextRecieveAddress());
        ArrayList<GeneralTransaction> transactions = new ArrayList<>();
        transactions.add(GetTransactionByAddress.parseTransaction("{\"txid\":\"e6b4dbfb0d846f097357f121eda90bab1804956874a05d83576345233148b4df\",\"version\":1,\"locktime\":635875,\"vin\":[{\"txid\":\"1b881b0f76d031ced756156fe7ebb68887bf3f06901ba535e66df7997e0cd88d\",\"vout\":1,\"sequence\":4294967294,\"n\":0,\"scriptSig\":{\"hex\":\"4730440220015d275280ea717de5112342b0e4d7f4d062173ef9fb038dcde6122a003aeb84022074399519c733a5d69f076ab20866fbdbf8f5218e576552d81b43d1458378d3920121035eeed9d54b5265ae65a8b3aebf289ecd54803e601a34a399734b4ffdde468996\",\"asm\":\"30440220015d275280ea717de5112342b0e4d7f4d062173ef9fb038dcde6122a003aeb84022074399519c733a5d69f076ab20866fbdbf8f5218e576552d81b43d1458378d392[ALL] 035eeed9d54b5265ae65a8b3aebf289ecd54803e601a34a399734b4ffdde468996\"},\"addr\":\"XmWENM5QAnrDk7tPgj7trcniSbeaJEcrQJ\",\"valueSat\":18111296,\"value\":0.18111296,\"doubleSpentTxID\":null}],\"vout\":[{\"value\":\"0.02116985\",\"n\":0,\"scriptPubKey\":{\"hex\":\"76a91487d0b2c4f6344c7d7f852f443216c8ec7864ecdd88ac\",\"asm\":\"OP_DUP OP_HASH160 87d0b2c4f6344c7d7f852f443216c8ec7864ecdd OP_EQUALVERIFY OP_CHECKSIG\",\"addresses\":[\"Xo4y4mG3JB8rzxMLwHa9qPFoQ46DiEL9Vq\"],\"type\":\"pubkeyhash\"},\"spentTxId\":null,\"spentIndex\":null,\"spentHeight\":null},{\"value\":\"0.15949153\",\"n\":1,\"scriptPubKey\":{\"hex\":\"76a91470e978837ddb31684e8033b0b96599f8a1ae9b7588ac\",\"asm\":\"OP_DUP OP_HASH160 70e978837ddb31684e8033b0b96599f8a1ae9b75 OP_EQUALVERIFY OP_CHECKSIG\",\"addresses\":[\"XkysCa58ih9N5axycNfqVU7SsDA94CkbrT\"],\"type\":\"pubkeyhash\"},\"spentTxId\":null,\"spentIndex\":null,\"spentHeight\":null}],\"blockhash\":\"0000000000004a233f492b80a08b86902bd64b51c47531be0987458ebf987bd9\",\"blockheight\":635876,\"confirmations\":6,\"time\":1489448344,\"blocktime\":1489448344,\"valueOut\":0.18066138,\"size\":225,\"valueIn\":0.18111296,\"fees\":0.00045158,\"txlock\":false}", de.bitsharesmunich.cryptocoincore.base.Coin.DASH));
        transactions.add(GetTransactionByAddress.parseTransaction("{\"txid\":\"f3818dc2379c4e6eaddca5ca5fea650000c3f3d45a4ee48429410ed9d6d51853\",\"version\":1,\"locktime\":0,\"vin\":[{\"txid\":\"e6b4dbfb0d846f097357f121eda90bab1804956874a05d83576345233148b4df\",\"vout\":1,\"sequence\":4294967295,\"n\":0,\"scriptSig\":{\"hex\":\"48304502210090c24533eb9c87aa0b5d6028b409281ca593ce2895624c784e55505429eafaba02204dcba6f945deb91a6a47cf59920112a082a4e5d5ebf4edbd7d23e06364bcdc05812102cc5e5ef416b0a80f4923ea56c16cb27ece4c2bc9ecd437b26b8a60980b6efb75\",\"asm\":\"304502210090c24533eb9c87aa0b5d6028b409281ca593ce2895624c784e55505429eafaba02204dcba6f945deb91a6a47cf59920112a082a4e5d5ebf4edbd7d23e06364bcdc05[ALL|ANYONECANPAY] 02cc5e5ef416b0a80f4923ea56c16cb27ece4c2bc9ecd437b26b8a60980b6efb75\"},\"addr\":\"XkysCa58ih9N5axycNfqVU7SsDA94CkbrT\",\"valueSat\":15949153,\"value\":0.15949153,\"doubleSpentTxID\":null}],\"vout\":[{\"value\":\"0.00100000\",\"n\":0,\"scriptPubKey\":{\"hex\":\"76a914a32d67d1b69f7a44752c6529c51d6d9a654d379888ac\",\"asm\":\"OP_DUP OP_HASH160 a32d67d1b69f7a44752c6529c51d6d9a654d3798 OP_EQUALVERIFY OP_CHECKSIG\",\"addresses\":[\"XqZeNMW8s29u7TdsYG96hZFULfP5chkP6B\"],\"type\":\"pubkeyhash\"},\"spentTxId\":null,\"spentIndex\":null,\"spentHeight\":null},{\"value\":\"0.15749153\",\"n\":1,\"scriptPubKey\":{\"hex\":\"76a914c24220e0b3aeba0660c8d0e7e6d11d2e012c52f288ac\",\"asm\":\"OP_DUP OP_HASH160 c24220e0b3aeba0660c8d0e7e6d11d2e012c52f2 OP_EQUALVERIFY OP_CHECKSIG\",\"addresses\":[\"XtPz99niVrzijLeg96xLa3VkBN18qMc24e\"],\"type\":\"pubkeyhash\"},\"spentTxId\":null,\"spentIndex\":null,\"spentHeight\":null}],\"blockhash\":\"000000000000459356b73ecdfb4f7757b1c9a31c3629523fd3b884c004a06fff\",\"blockheight\":635909,\"confirmations\":6,\"time\":1489453828,\"blocktime\":1489453828,\"valueOut\":0.15849153,\"size\":226,\"valueIn\":0.15949153,\"fees\":0.001,\"txlock\":false}", de.bitsharesmunich.cryptocoincore.base.Coin.DASH));
        for(GeneralTransaction trans : transactions){
         for(GeneralCoinAddress address : account.getAddresses()){
            for(GTxIO output : trans.getTxOutputs()){
                if(output.getAddressString().equalsIgnoreCase(address.getAddressString(account.getNetworkParam()))){
                    System.out.println("Se encontro output");
                    address.getTransactionInput().add(output);
                    output.setAddress(address);
                    break;
                }
            }
            for(GTxIO input : trans.getTxInputs()){
                if(input.getAddressString().equalsIgnoreCase(address.getAddressString(account.getNetworkParam()))){
                    System.out.println("Se encontro input");
                    address.getTransactionOutput().add(input);
                    input.setAddress(address);
                    break;
                }
            }
         }
        }
        System.out.println(" dash next address " + account.getNextRecieveAddress());
        String toAddress ="XgompkKkWQKtDWBR2C7V7Gc5aduy1j474X";
        System.out.println("balance " + account.getBalance().get(0).getAmmount()/100000000);
        System.out.println("balance conf " + account.getBalance().get(0).getConfirmedAmount()/100000000);
        System.out.println("balance unconf " + account.getBalance().get(0).getUnconfirmedAmount()/100000000);
        account.send(toAddress, de.bitsharesmunich.cryptocoincore.base.Coin.DASH, 5000000, "" , null);
        
        try {
            String urlParameters = "rawtx=01000000015318d5d6d90e412984e44e5ad4f3c3000065ea5fcaa5dcad6e4e9c37c28d81f3010000006a4730440220412add954680d851b3b3f103af896ba1cc35d686867759cddecfd7cddba9028e0220474aef855614cd753a49c3ddfe4d354b40e375ed0395b0ee29b2c03ab6890009812103114c7910b78bf3ed064e3e2e560360ed10bf554beb0da00e1d85e2c7c90edb95ffffffff02404b4c00000000001976a914432051f3ba52aeb8c838f5ca46376d61161fb0f788aca1c29400000000001976a914afca9d2c6da2bb969be3bf03a0ef512b48a02ad588ac00000000";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            String request = "http://fr.blockpay.ch:3005/insight-api-dash/tx/send";
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
        System.out.println("balance " + account.getBalance().get(0).getAmmount()/100000000);
        System.out.println("balance conf " + account.getBalance().get(0).getConfirmedAmount()/100000000);
        System.out.println("balance unconf " + account.getBalance().get(0).getUnconfirmedAmount()/100000000);
    }
}
