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
public class BinaryTree {
    
    Node root;
    
    
    public BinaryTree(){
        
       this.root =  new Node(null);
       
    }
    
    public Node add (Node root, Object value ){
        
        if ( root == null )
            return new Node(value);
        // da errore per adesso si aggiusta quando capisco che ci va dentro l'albero, lol
        if( value < root.value ){
            root.left = add(root.left, value);
        }
        else if( value > root.value ){
            root.right = add(root.right, value);
        }else{
            return root;
        }
       
        return root; 
        
    }
    
}
