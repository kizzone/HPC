/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.io.Serializable;

/**
 * Rappresenta il messaggio che un nodo manda in caso dovesse abbandonare prima del previsto la rete
 * @author domenico
 */
public class UnpredictableLeave implements Serializable{
    
    /**
     *  Intervallo temporale corrente in cui il nodo manda la richiesta di leave
     */
    private int currentTimeSlot;
    /**
     * Indirizzo IP del nodo
     */
    private String nodeIP;

    /**
     *  Costruttore del messaggio
     * @param currentTimeSlot slot temporale attuale
     * @param node indirizzo IP del nodo che ha inviato la richiesta di "unpredictable leave"
     */
    public UnpredictableLeave(int currentTimeSlot, String node) {
        this.currentTimeSlot = currentTimeSlot;
        this.nodeIP = node;
    }

    /**
     * Getter per currentTimeSlot
     * @return currentTimeSlot: intervallo temporale corrente
     */
    public int getCurrentTimeSlot() {
        return currentTimeSlot;
    }

    /**
     *  Setter per currentTimeSlot
     * @param currentTimeSlot time slot corrente
     */
    public void setCurrentTimeSlot(int currentTimeSlot) {
        this.currentTimeSlot = currentTimeSlot;
    }

    /**
     *  Getter per indirizzo IP del nodo
     * @return stringa rappresentante l'indirizzo IP
     */ 
    public String getNode() {
        return nodeIP;
    }

    /**
     *  Setter per indirizzo IP del nodo
     * @param node indirizzo IP del nodo
     */
    public void setNode(String node) {
        this.nodeIP = node;
    }
    
}
