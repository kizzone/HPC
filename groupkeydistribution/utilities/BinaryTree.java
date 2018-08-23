/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupkeydistribution.utilities;



import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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
    
    public BinaryTree() throws NoSuchAlgorithmException, UnsupportedEncodingException{
        
        this.root =  new Node();
/*      new Random().nextBytes(this.root.getX00());
        MessageDigest msg = MessageDigest.getInstance("MD5");
        msg.update(this.root.getX00());
        this.root.setX00(msg.digest().toString().getBytes("UTF-8"));*/
        
        byte[] array = new byte[16]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] encodedhash = digest.digest(generatedString.getBytes("UTF-8"));
        root.setX00(encodedhash);
        System.out.println("ahhhhhhh " + bytesToHex(encodedhash));
        System.out.println("ribalto " + bytesToHex(generatedString.getBytes("UTF-8")));
    }
    
    
    
    //
    public void f0 ( Node current, int riga , int pos ) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        String generatedString = new String(current.getX00(), Charset.forName("UTF-8"));
        
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] encodedhash = digest.digest(generatedString.getBytes("UTF-8"));
        
        Node newNode = new Node();
        newNode.setX00( encodedhash );
        newNode.riga = riga;
        newNode.pos  = pos;
        current.left =  newNode;
        
    }
    public void f1 ( Node current, int riga , int pos ) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        
        /*MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        
        messageDigest.update( addOne(current.getX00() ) );
        String encryptedString = messageDigest.digest().toString();
        Node newNode = new Node();
        newNode.setX00( encryptedString.getBytes("UTF-8") );
        newNode.riga = riga;
        newNode.pos  = pos;
        current.right =  newNode;*/
    	
        String generatedString = new String(current.getX00(), Charset.forName("UTF-8"));
        
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] encodedhash = digest.digest(incrementAtIndex(generatedString.getBytes("UTF-8"),0));
        
        Node newNode = new Node();
        newNode.setX00( encodedhash );
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
    
    
    
    
    
    public Node buildTree (Node root, int profondita) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        
        Node current = root;
     
        int y = 0;
        System.out.println("\n\n i" + y );
        int z = 0;
        Node tmp2 = search(current, y , z);
        System.out.println("APPLICO F0 al nodo riga-colonna" + tmp2.riga+ "-"+ tmp2.pos);
        f0(tmp2,y+1,z*2);  
        System.out.println("APPLICO F1 riga-colonna" + tmp2.riga+ "-"+ tmp2.pos);
        f1(tmp2,y+1,z*2+1); 

            
       
        
        for (int i = 1 ; i < profondita; i++){
            
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
/*        for (int i = A.length - 1; i >= 0; i--) {
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
        }*/
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
    
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xff & hash[i]);
        if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    private byte[] incrementAtIndex(byte[] array, int index) {
        if (array[index] == 127) {
            array[index] = 0;
            if(index > 0)
                incrementAtIndex(array, index - 1);
        }
        else {
            array[index]++;
        }
        return array;
    }
}