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
     * @param currentTimeSlot
     * @param node
     */
    public UnpredictableLeave(int currentTimeSlot, String node) {
        this.currentTimeSlot = currentTimeSlot;
        this.nodeIP = node;
    }

    /**
     *  Getter per currentTimeSlot
     * @return currentTimeSlot: intervallo temporale corrente
     */
    public int getCurrentTimeSlot() {
        return currentTimeSlot;
    }

    /**
     *  Setter per currentTimeSlot
     * @param currentTimeSlot
     */
    public void setCurrentTimeSlot(int currentTimeSlot) {
        this.currentTimeSlot = currentTimeSlot;
    }

    /**
     *  Getter per indirizzo IP del nodo
     * @return
     */
    public String getNode() {
        return nodeIP;
    }

    /**
     *  Setter per indirizzo IP del nodo
     * @param node
     */
    public void setNode(String node) {
        this.nodeIP = node;
    }
    
}
