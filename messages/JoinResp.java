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
 *
 * @author domenico & domenico
 */

public class JoinResp implements java.io.Serializable{
    
    private ArrayList<Node> keySet;
    
    private byte[] k2;

    public byte[] getK2() {
        return k2;
    }

    public void setK2(byte[] k2) {
        this.k2 = k2;
    }
    
    /**
     * costruttore 
     * @param keySet 
     * @param k2 
     */
    public JoinResp(ArrayList<Node> keySet,byte[] k2) {
        this.keySet = keySet;
        this.k2 = k2;
    }
    /**
     * 
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
     * DEBUG 
     * @return stringa di debug
     */
    public String toStringato(){
        return "messaggio ricevuto";
    }

    /**
     * stampa le chiavi ricevute
     */
    public void receivedKey(){
        this.getKeySet().forEach((e) -> {
            System.out.println( "       chiavi di riga "+ e.riga +" e posizione:" + e.pos + "valore:" +  Arrays.toString(e.getX00()));
        });
    }
    
}
