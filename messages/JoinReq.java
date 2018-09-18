/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

/**
 * Messaggio di join che un nodo manda al GKDC 
 * @author domenico;
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
     * @return il valore di timeslot
     */
    public int getTimeSlot() {
        return timeSlot;
    }
    
    /**
     * getter per indirizzo ip
     * @return ipAdress rappresenta l'indirizzo IP del nodo che ha effettuato la richiesta
     */
    public String getIpAdress() {
        return ipAdress;
    }
    
    /**
     * costruttore 
     * @param timeSlot timeslot relativo alla richiesta di join
     * @param ipAdress indirizzo IP del nodo che effettua la richiesta
     */
    public JoinReq(int timeSlot, String ipAdress) {
        this.timeSlot = timeSlot;
        this.ipAdress = ipAdress;
    }
    
    /**
     * Ritorna la stringa che rappresenta il messaggio di request 
     * @return Stringa  che rappresenta il messaggio di joinReq
     */
    @Override
    public String toString(){
        return this.getIpAdress() + "&" + this.getTimeSlot();
    }
    
}
