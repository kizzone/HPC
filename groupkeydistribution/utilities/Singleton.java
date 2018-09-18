/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupkeydistribution.utilities;
/**
 * Classe utilizzata per simulare uno slot temporale in maniera protetta così 
 * da non avere interferenza tra i thread. È implementata tramite singleton
 * in modo tale che tutte le classi possano vedere la stessa linea del tempo
 * @author domenico
 */
public class Singleton {

    private static Singleton istance = null; 
    
    /**
     * Slot temporale corrente
     */
    private int slot;
    
    /**
     * numero di intervalli temporali totali
     */
    private int leafs;
    
    /**
     * profondità dell'albero
     */
    private int depth;

    /**
     *  Getter per la profondita dell'albero delle chiavi
     * @return depth: la profondità dell'albero
     */
    public int getDepth() {   
        return depth;
    }

    /**
     *  Setter per la profondita dell'albero delle chiavi
     * @param depth profondità dell'albero
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    /**
     *  Getter per la il numero di foglie nell'ultimo livello dell'albero
     * @return il numero di foglie sull'ultimo livello dell'albero
     */
    public int getLeafs() {
        return leafs;
    }

    /**
     *  Setter per la il numero di foglie nell'ultimo livello dell'albero
     * @param leafs numero di foglie nell'ultimo livello dell'albero
     */
    public void setLeafs(int leafs) {
        this.leafs = leafs;
    }
    
    private Singleton() {}
    
    /**
     * Roba del singleton
     * @return l'oggetto già instanziato oppure uno nuovo oggetto in caso non fosse già stato creato
     */
    public static Singleton getIstance() {
        if(istance == null)
            istance = new Singleton();
        return istance;
    }
    
    /**
     *  Setter per gli slot
     * @param n numero di slot temporali
     */
    public synchronized void setValue(int n){
        slot = n;
    }

    /**
     *  Funzione che incrementa slot in modo protetto
     * @return aumenta il campo slot di 1
     */
    public synchronized int increment(){
        return slot++;
    }
       
    /**
     *  Getter per slot
     * @return valore dello slot
     */
    public int getValue() { return slot; }
    
}
