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
    
    /**
     * costruttore 
     * @param keySet 
     */
    public JoinResp(ArrayList<Node> keySet) {
        this.keySet = keySet;
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
