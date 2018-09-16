/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupkeydistribution.utilities;

/**
 * classe utilizzata per criptare in maniera simmetrica un dato messaggio
 * @author domenico
 */

import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class Encryption {
    
    private byte[] key;

    private byte[] getKey() {
        return key;
    }

    private void setKey(byte[] key) {
        this.key = key;
    }

    private static final String ALGORITHM = "AES";

    public Encryption(byte[] key)
    {
        this.setKey(key);
    }

    /**
     * Encrypts the given plain text
     *
     * @param plainText The plain text to encrypt
     * @return 
     * @throws java.lang.Exception
     */
    public byte[] encrypt(byte[] plainText) throws Exception
    {
        System.out.println("\n        ENCRPIPT METHOD KEY: " + Arrays.toString(this.getKey()));
        SecretKeySpec secretKey = new SecretKeySpec(this.getKey(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(plainText);
    }

    /**
     * Decrypts the given byte array
     *
     * @param cipherText The data to decrypt
     * @return 
     * @throws java.lang.Exception
     */
    public byte[] decrypt(byte[] cipherText) throws Exception{
        System.out.println("\n        DECRPIPT METHOD KEY: " + Arrays.toString(this.getKey()));
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(cipherText);
    }

}


