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
    
    private int currentTimeSlot;
    private String nodeIP;

    public UnpredictableLeave(int currentTimeSlot, String node) {
        this.currentTimeSlot = currentTimeSlot;
        this.nodeIP = node;
    }

    public int getCurrentTimeSlot() {
        return currentTimeSlot;
    }

    public void setCurrentTimeSlot(int currentTimeSlot) {
        this.currentTimeSlot = currentTimeSlot;
    }

    public String getNode() {
        return nodeIP;
    }

    public void setNode(String node) {
        this.nodeIP = node;
    }
    
}
