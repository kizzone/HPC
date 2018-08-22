/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupkeydistribution.utilities;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author domenico
 */
public class BinaryTree {
    
    Node root;

    public Node getRoot() {
        return root;
    }
    
    public BinaryTree(){
        
        this.root =  new Node();
        new Random().nextBytes(this.root.getX00());
        
    }
    
    
    
    //
    public void f0 ( Node current, int riga , int pos ) throws NoSuchAlgorithmException{
        //hash di quello che ci passo
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(current.getX00());
        String encryptedString = new String(messageDigest.digest());
        Node newNode = new Node();
        newNode.setX00(encryptedString.getBytes());
        newNode.riga = riga;
        newNode.pos  = pos;
        current.left =  newNode;
       //return newNode;
        
    }
    public void f1 ( Node current, int riga , int pos ) throws NoSuchAlgorithmException{
        
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update( addOne(current.getX00() ) );
        String encryptedString = new String(messageDigest.digest());
        Node newNode = new Node();
        newNode.setX00( encryptedString.getBytes() );
        newNode.riga = riga;
        newNode.pos  = pos;
        current.right =  newNode;
       
    }
    
          
    private Node search(Node node,int h, int p){
    if(node != null){
        if(node.pos == p && node.riga == h){
           System.out.println("  TROVATO NODO  riga : " + node.riga + " - " + " posizione: " + node.pos );
           return node;
        } else {
            System.out.println("Non è il nodo che stai cercando semicit. RIGA: " +  node.riga + "POS "+ node.pos);
            Node foundNode = search(node.left,h,p);
            if(foundNode == null) {
                foundNode = search(node.right,h,p);
            }
            return foundNode;
         }
    } else {
        return null;
    }
}
    
    
    
    
    
    public Node buildTree (Node root, int profondita) throws NoSuchAlgorithmException{
        
        Node current = root;
        
        for (int i = 0 ; i < profondita; i++){
            

            if ( i == 0){
                
                System.out.println("\n\n i" + i );
                int k = 0;
                Node tmp = search(current, i , k);
                System.out.println("APPLICO F0 al nodo riga-colonna" + tmp.riga+ "-"+ tmp.pos);
                f0(tmp,i+1,k*2);  
                System.out.println("APPLICO F1 riga-colonna" + tmp.riga+ "-"+ tmp.pos);
                f1(tmp,i+1,k*2+1); 
                
            }
                
            
            int nNode = (int) Math.pow(2, i);
            
            for (int k = 0; k < nNode ; k++){
                
                System.out.println("\n\n i-j" + i +" "+ k);
                System.out.println("riga da ricercare: " + i + " posizione da ricercare: " + k );
                Node tmp = search(current, i , k);
                System.out.println("APPLICO F0 al nodo riga-colonna" + tmp.riga+ "-"+ tmp.pos);
                f0(tmp,i+1,k*2);  
                System.out.println("APPLICO F1 riga-colonna" + tmp.riga+ "-"+ tmp.pos);
                f1(tmp,i+1,k*2+1); 
                
                
            }
      
        }
        return root;
    }
   
    public static byte[] addOne(byte[] A) {
        for (int i = A.length - 1; i >= 0; i--) {
            if (A[i] == 0) {
                A[i] = 1;
                return A;
            }
            A[i] = 0;
            if (i == 0) {
                A = new byte[A.length + 1];
                Arrays.fill(A, (byte) 0); // Added cast to byte
                A[0] = 1;
            }
        }
        return A;
    }
    
    
    public void traverseInOrder(Node node) {
        
        if (node != null) {
            traverseInOrder(node.left);
            System.out.println(" Stampa albero riga:" + node.riga + "posizione:" + node.pos + "KEY: " + Arrays.toString(node.getX00()));
            traverseInOrder(node.right);
        }
        
    }
    
    public Node add (Node root, Object value ){
        /*
        
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
     */   
        return null;
    }
    
        public Node traversePreOrder(Node node,int h, int p) {
        
        //System.out.println("Da ricercare riga-posizione" + h +"-"+ p);
        if (node == null) {
            return null;
        }
        else{
            
            if ( h == node.riga && p == node.pos){
                System.out.println("  TROVATO NODO  riga : " + node.riga + " - " + " posizione: " + node.pos );
                return node;
            }
            else{
                 traversePreOrder(node.left,h+1,p);
                 traversePreOrder(node.right,h+1,p);
            }
       
        return node;
        }
    }
    
    
}
