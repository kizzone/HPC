/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

/**
 * messaggio di join che un nodo manda al kdc 
 * @author D&D
 */

public class JoinReq {
          
    private final int timeSlot;
    private final String ipAdress;
    
    public int getTimeSlot() {
        return timeSlot;
    }

    public String getIpAdress() {
        return ipAdress;
    }
    
    public JoinReq(int timeSlot, String ipAdress) {
        this.timeSlot = timeSlot;
        this.ipAdress = ipAdress;
    }
    
    //===========================
    @Override
    public String toString(){
        return this.getIpAdress() + "&" + this.getTimeSlot();
    }
    
}
