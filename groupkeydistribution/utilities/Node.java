/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupkeydistribution.utilities;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Classe che rappresenta un nodo dell'albero delle chiavi
 * @author domenico;
 */
public class Node implements Serializable{
    
    /**
     * chiave segreta del nodo
     */
    private byte[] x00 = new byte[16];

    /**
     *  Profondità del nodo
     */
    public int riga;

    /**
     *  "Colonna" del nodo
     */
    public int pos;

    /**
     *  Nodo di sinistra
     */
    protected Node left;

    /**
     *  Nodo di destra 
     */
    protected Node right;

    /**
     *  Setter per la chiave del ndo
     * @param x00 chiave segreta del nodo
     */
    public void setX00(byte[] x00) {
        this.x00 = x00;
    }

    /**
     *  Getter per la chiave del nodo
     * @return array di byte rappresentanti la chiave del nodo
     */
    public byte[] getX00() {
        return x00;
    }
    
    /**
     *  Costruttore per nodo
     */
    public Node (){
        
        this.riga = 0;
        this.pos = 0;
        this.left = null;
        this.right = null;
       
    }
    
    /**
     *  To string override
     * @return stringa contenente la chiave e il numero di riga e colonna associate
     */
    @Override
    public String toString() {
    	return "Key: " + Arrays.toString(this.x00) + " row: " + this.riga  + " pos: " + this.pos;  
    }

}
