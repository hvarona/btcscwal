package de.bitsharesmunich.cryptocoincore.crypto;

import java.security.SecureRandom;

/**
 * Created by nelson on 12/20/16.
 */
public class Random {

    public static SecureRandom getSecureRandom(){
        SecureRandomStrengthener randomStrengthener = SecureRandomStrengthener.getInstance();
        try{
        randomStrengthener.addEntropySource(new AndroidRandomSource());
        }catch(Exception e){
            
        }
        return randomStrengthener.generateAndSeedRandomNumberGenerator();
    }
}
