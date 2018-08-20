/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupkeydistribution.utilities;

/**
 *
 * @author domenico
 */
public class Node {
    
    
    protected Object value;
    protected Node left;
    protected Node right;
    
    
    public Node (Object value){
        
        this.value = value;
        this.left = null;
        this.right = null;
       
    }

}
