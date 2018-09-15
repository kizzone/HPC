/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupkeydistribution.utilities;

/**
 * 
 *  Classe utilizzata per simulare uno slot temporale in maniera protetta così da non avere interferenza tra i thread
 * @author domenico
 */

@Deprecated
public class timeSlots {
    // Employs the cheap read-write lock trick
    // All mutative operations MUST be done with the 'this' lock held
    /*@GuardedBy("this") */
    private volatile int slot;

    public int getValue() { return slot; }
    
    public synchronized void setValue (int n) {
        slot = n;
    }
    public synchronized int increment() {
        return slot++;
    }
}
