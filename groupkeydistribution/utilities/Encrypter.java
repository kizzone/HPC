package groupkeydistribution.utilities;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Davide Piccinini (282364)
 * @author Domenico Barretta (289802)
 */
@Deprecated
public class Encrypter {
    private String algo="AES";
    private Cipher cipher;
    private final SecretKey secret_key;
    private final IvParameterSpec iv_spec;
    
    /**
     * @param key
     */
    public Encrypter(byte[] key){
        try {
            cipher = Cipher.getInstance(algo);
        } catch(NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.err.println("Error in cipher instantiation: " +e+ ".");
        }
        
        byte[] iv = "0000000000000000".getBytes();
        secret_key  = new SecretKeySpec(key,"AES");
        iv_spec = (iv!=null) ? new IvParameterSpec(iv) : null;
    }
    
    /**
     * @param plaintext
     * @return chipertext
     * @throws IllegalBlockSizeException, BadPaddingExceptionException 
     */
    public byte[] encrypt(byte[] plaintext) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE,secret_key,iv_spec);
        return cipher.doFinal(plaintext);
    }
    
    public byte[] decrypt(byte[] ciphertext) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE,secret_key,iv_spec);
        return cipher.doFinal(Base64.getDecoder().decode(ciphertext));
    }
}
