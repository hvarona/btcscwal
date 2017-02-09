package de.bitsharesmunich.cryptocoincore.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.bitsharesmunich.cryptocoincore.base.AccountSeed;
import de.bitsharesmunich.cryptocoincore.base.CryptoCoinFactory;
import de.bitsharesmunich.cryptocoincore.base.GeneralCoinAccount;
import de.bitsharesmunich.cryptocoincore.test.CryptoCoreSQLite;
import java.util.List;

/**
 *
 * @author henry
 */
public class FileBin {

    public JsonObject DatabaseToJson(String password) {
        JsonObject answer = new JsonObject();
        CryptoCoreSQLite db = new CryptoCoreSQLite();
        List<AccountSeed> seeds = db.getSeeds();
        JsonArray seedsObject = new JsonArray();
        for (AccountSeed seed : seeds) {
            JsonObject seedObject = seed.toJson(password);
            List<GeneralCoinAccount> accounts = db.getAccounts(seed);
            JsonArray accountsObject = new JsonArray();
            for (GeneralCoinAccount account : accounts) {
                JsonObject accountObject = account.toJson();
                accountsObject.add(accountObject);
            }
            seedObject.add("accounts", accountsObject);
            seedsObject.add(seedObject);
        }
        answer.add("seeds", seedsObject);
        return answer;
    }

    public void JsontoDatabase(JsonObject in, String password) {
        CryptoCoreSQLite db = new CryptoCoreSQLite();

        JsonArray seedsObject = in.getAsJsonArray("seeds");
        for (int i = 0; i < seedsObject.size(); i++) {
            JsonObject seedObject = seedsObject.get(i).getAsJsonObject();
            AccountSeed seed = AccountSeed.fromJson(seedObject, password);
            String idSeed = db.putSeed(seed);
            seed.setId(idSeed);
            JsonArray accountsObject = seedObject.get("accounts").getAsJsonArray();
            for (int j = 0; j < accountsObject.size(); j++) {
                JsonObject accountObject = accountsObject.get(j).getAsJsonObject();
                GeneralCoinAccount account = (GeneralCoinAccount) CryptoCoinFactory.getAccountFromJson(accountObject, seed);
                db.putGeneralAccount(account);
            }
        }

    }

}
