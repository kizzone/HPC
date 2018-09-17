/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package messages;

import groupkeydistribution.utilities.Node;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Rappresenta il messaggio di risposta che il GKDC manda ad un nodo
 * @author domenico
 */

public class JoinResp implements java.io.Serializable{
    
    /**
     * Rappresenta il più piccolo keySet delle chiavi che il nodo riceve e da cui si ricava le restanti
     */
    private ArrayList<Node> keySet;
    
    /**
     * Rappresenta l'array di byte k2 con cui il nodo calcola le chiavi successive
     */
    private byte[] k2;

    /**
     * Getter per k2
     * @return k2
     */
    public byte[] getK2() {
        return k2;
    }

    /**
     * Setter per k2
     * @param k2 
     */
    public void setK2(byte[] k2) {
        this.k2 = k2;
    }
    
    /**
     * Costruttore  del messaggio
     * @param keySet 
     * @param k2 
     */
    public JoinResp(ArrayList<Node> keySet,byte[] k2) {
        this.keySet = keySet;
        this.k2 = k2;
    }
    /**
     * Getter per il keySet
     * @return ArrayList<> di nodi dell'albero che rappresenta il keySet 
     */
    public ArrayList<Node> getKeySet() {
        return keySet;
    }
    /**
     * Setter per keySet
     * @param keySet 
     */
    public void setKeySet(ArrayList<Node> keySet) {
        this.keySet = keySet;
    }
    
    
    /**
     *  Metodo di debug 
     * @return stringa di debug
     */
    public String toStringato(){
        return "messaggio ricevuto";
    }

    /**
     * Stampa le chiavi ricevute
     */
    public void receivedKey(){
        this.getKeySet().forEach((e) -> {
            System.out.println( " Ho Ricevuto le chiavi di riga "+ e.riga +" e posizione: " + e.pos + " valore:" +  Arrays.toString(e.getX00()));
        });
    }
    
}
