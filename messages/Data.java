/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.io.Serializable;
import java.util.Arrays;

/**
 *  Rappresenta il messaggio DATA che i GDKC periodicamente genera ed invia ad i nodi del gruppo
 * 
 * @author domenico
 */


public class Data implements Serializable {
 
    private final byte[] cipherText;
    public int timeSlot;
   
    /**
     * 
     * Recupera il timeSlot nel messaggio DATA
     * @return 
     */
    public int getTimeSlot() {
        return timeSlot;
    }
    
    /**
     * Costruttore
     * 
     * @param cipher messaggio criptato
     * @param slot slot temporale relativo
     */
    public Data(byte[] cipher, int slot){
        this.timeSlot = slot;
        this.cipherText = cipher;
        
    }
    
    /**
     * 
     * Getter per cipherText
     * @return cipherText
     */
    public byte[] getCipherText() {
        return cipherText;
    }
  
    // forse c'è un problema nel caso in cui ^ venga letto come stringa del cipherText da ragionarci un attimo
    /**
     * 
     * @return Stringa che rappresenta il messaggio DATA
     */
    public String toStringato(){
        return this.timeSlot + "^" + Arrays.toString(this.getCipherText());
    }
    
}
