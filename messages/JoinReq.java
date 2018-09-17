/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

/**
 * Messaggio di join che un nodo manda al GKDC 
 * @author D&D
 */

public class JoinReq {
          
    /**
     * Slot temporale in cui arriva la richiesta di JOIN
     */
    private final int timeSlot;
    
    /**
     * Indirizzo IP del nodo
     */
    private final String ipAdress;
    
    /**
     * getter per il timeslot
     * @return 
     */
    public int getTimeSlot() {
        return timeSlot;
    }
    
    /**
     * getter per indirizzo ip
     * @return ipAdress
     */
    public String getIpAdress() {
        return ipAdress;
    }
    
    /**
     * costruttore 
     * @param timeSlot
     * @param ipAdress 
     */
    public JoinReq(int timeSlot, String ipAdress) {
        this.timeSlot = timeSlot;
        this.ipAdress = ipAdress;
    }
    
    /**
     * Ritorna la stringa che rappresenta il messaggio di request 
     * @return Stringa 
     */
    @Override
    public String toString(){
        return this.getIpAdress() + "&" + this.getTimeSlot();
    }
    
}
