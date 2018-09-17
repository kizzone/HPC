/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupkeydistribution.utilities;

/**
 * Classe utilizzata per criptare in maniera simmetrica un dato messaggio
 * @author domenico
 */

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author domenico e stackoverflow lol
 */
public class Encryption {
    
    /**
     * Chiave segreta
     */
    private byte[] key;

    /**
     * Getter per la chiave
     * @return chiave segreta
     */
    private byte[] getKey() {
        return key;
    }

    /**
     * Setter per la chiave
     * @param key 
     */
    private void setKey(byte[] key) {
        this.key = key;
    }

    /**
     * Tipo di algoritmo utilizzato
     */
    private static final String ALGORITHM = "AES";

    /**
     *  Costruttore dell'oggetto con la chiave segreta
     * @param key
     */
    public Encryption(byte[] key)
    {
        this.setKey(key);
    }

    /**
     * Encrypts the given plain text
     * @param plainText The plain text to encrypt
     * @return 
     * @throws java.lang.Exception
     */
    public byte[] encrypt(byte[] plainText) throws Exception
    {
        //System.out.println("\n        ENCRPIPT METHOD KEY: " + Arrays.toString(this.getKey()));
        SecretKeySpec secretKey = new SecretKeySpec(this.getKey(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(plainText);
    }

    /**
     * Decrypts the given byte array
     * @param cipherText The data to decrypt
     * @return cipherText
     * @throws java.lang.Exception
     */
    public byte[] decrypt(byte[] cipherText) throws Exception{
        //System.out.println("\n        DECRPIPT METHOD KEY: " + Arrays.toString(this.getKey()));
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(cipherText);
    }

}


