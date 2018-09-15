/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupkeydistribution.utilities;
/**
 * Classe utilizzata per simulare uno slot temporale in maniera protetta così 
 * da non avere interferenza tra i thread. È implementata tramite un singleton
 * in modo tale che tutte le classi possano vedere la stessa linea del tempo
 * 
 * @author domenico
 */
public class Singleton {

    private static Singleton istance = null; 
    private int slot;
    private int leafs;
    private int depth;

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    public int getLeafs() {
        return leafs;
    }

    public void setLeafs(int leafs) {
        this.leafs = leafs;
    }
    
    private Singleton() {}
    
    public static Singleton getIstance() {
        if(istance == null)
            istance = new Singleton();
        return istance;
    }
    
    public synchronized void setValue(int n){
        slot = n;
    }
    public synchronized int increment(){
        return slot++;
    }
       
    public int getValue() { return slot; }
    
    
}
