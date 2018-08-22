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
        current.left =  newNode;
       
    }


    public Node traversePreOrder(Node node,int h, int p) {
        if (node == null) {
            return null;
        }
        else if ( !( h == node.riga && p == node.pos)){
            traversePreOrder(node.left,h,p);
            traversePreOrder(node.right,h,p);
        }
        System.out.println("  TROVATO NODO  riga : " + node.riga + " - " + " posizione: " + node.pos );
        return node;
    }
    
    
      
    public Node buildTree (Node root, int profondita) throws NoSuchAlgorithmException{
        
        Node current = root;
        
        for (int i = 1 ; i <= profondita; i++){
            
            int nNode = (int) Math.pow(2, i);
           
            for (int k = 0; k < nNode -1; k++){
                
                Node tmp = traversePreOrder(root, i-1 , k);
                
                if ( k%2 == 0 ){
                    f0(tmp,i,k*2);
                }
                else{
                    f1(tmp,i,k*2+1); 
                }
                
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
            System.out.print("riga:" + node.riga + "posizione:" + node.pos + "KEY: " + Arrays.toString(node.getX00()));
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
    
}
