/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupkeydistribution.utilities;

import java.util.Random;

/**
 * classe nodo dell'albero ha come parametri una chiave 2 interi che rappresentano la posizione che occupa il nodo nell'albero e i nodi di sx e dx
 * @author D&D
 */
public class Node {
    
    
    private byte[] x00 = new byte[16];
    public int riga;
    public int pos;
    protected Node left;
    protected Node right;

    public void setX00(byte[] x00) {
        this.x00 = x00;
    }

    public byte[] getX00() {
        return x00;
    }
    
    
    public Node (){
        
        this.riga = 0;
        this.pos = 0;
        this.left = null;
        this.right = null;
       
    }

}
