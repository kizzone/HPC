/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/*

JOIN-RESP invece dovrebbe contenere i valori x_{h,i} necessari per coprire l'intervallo complessivo in cui il nodo e' presente nel gruppo. 
--Questo messaggio dovrebbe essere critpato con la chiave simmetrica (SK_i) del nodo (i). Questo criptaggio potreste aggiungerlo anche in un secondo memento e iniziare ad implementarlo in chiaro.

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

    public JoinResp(ArrayList<Node> keySet) {
        this.keySet = keySet;
    }
    
    public ArrayList<Node> getKeySet() {
        return keySet;
    }

    public void setKeySet(ArrayList<Node> keySet) {
        this.keySet = keySet;
    }
    
    
    //solo per debug
    public String toStringato(){
        return "messaggio ricevuto";
    }
    
    //stampa le chiavi ricevute
    public void receivedKey(){
        for ( Node e : this.getKeySet()) {
            System.out.println( "chiavi di riga "+ e.riga +" e posizione: " + e.pos + "valore : " +  Arrays.toString(e.getX00()));  
        }
    }
    
}
