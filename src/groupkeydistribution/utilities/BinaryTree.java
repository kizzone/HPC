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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Rappresenta l'albero binario delle chiavi da utilizzare nei vari intervalli temporali
 * @author D&D
 * 
 */

public class BinaryTree {
    
    Node root;

    
    
    public Node getRoot() {
        return root;
    }
    
    
    /**
     * 
     * Costruttore dell'albero binario crea un nodo radice con una chiave (hash di random byte) 
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    
    public BinaryTree() throws NoSuchAlgorithmException, UnsupportedEncodingException{
        
        this.root =  new Node();
        byte[] array = new byte[16]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] encodedhash = digest.digest(generatedString.getBytes("UTF-8"));
        root.setX00(encodedhash);
        //System.out.println("ahhhhhhh " + bytesToHex(encodedhash));
        //System.out.println("ribalto " + bytesToHex(generatedString.getBytes("UTF-8")));
    }
    
    
    
    /**
     * 
     * Funzione f0 utilizzata per collegare al ramo di sinistra di un nodo passato come parametro un nuovo nodo con chiave hash dell'array di byte del nodo passato
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    
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
    
    /**
     * 
     * Funzione f1 utilizzata per collegare al ramo di destra di un nodo passato come parametro un nuovo nodo con chiave hash dell'array di byte  incrementato 
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    public void f1 ( Node current, int riga , int pos ) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    	
        String generatedString = new String(current.getX00(), Charset.forName("UTF-8"));        
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] encodedhash = digest.digest(incrementAtIndex(generatedString.getBytes("UTF-8"),0));
        Node newNode = new Node();
        newNode.setX00( encodedhash );
        newNode.riga = riga;
        newNode.pos  = pos;
        current.right =  newNode;
       
    }
    
   /**
     * 
     * Ricerca di un nodo di "righe" e "colonne" nell'albero
     * utilizzato in fase di costruzione dell'albero delle chiavi
     * 
     */
    public Node search(Node node,int h, int p){
        if(node != null){
            if(node.pos == p && node.riga == h){
              // System.out.println("  TROVATO NODO  riga : " + node.riga + " - " + " posizione: " + node.pos );
               return node;
            } else {
              // System.out.println("Non è il nodo che stai cercando semicit. RIGA: " +  node.riga + "POS "+ node.pos);
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
    
    //da sistemare
    
    /**
     * BuilTree prende in ingresso un nodo da considerare radice dell'albero e un intero che rappresenta la profondatità e costruisce un albero di chiavi con f0 e f1 
     * è brutta da vedere ma va sistemata ancora
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException 
     */
    
    
    public Node buildTree (Node root, int profondita) throws NoSuchAlgorithmException, UnsupportedEncodingException{
     //* è brutta da vedere ma va sistemata ancora   
        Node current = root;
        int y = 0;
       // System.out.println("\n\n i" + y );
        int z = 0;
        Node tmp2 = search(current, y , z);
        //System.out.println("APPLICO F0 al nodo riga-colonna" + tmp2.riga+ "-"+ tmp2.pos);
        f0(tmp2,y+1,z*2);  
        //System.out.println("APPLICO F1 riga-colonna" + tmp2.riga+ "-"+ tmp2.pos);
        f1(tmp2,y+1,z*2+1); 
        
        for (int i = 1 ; i < profondita; i++){
            
            int nNode = (int) Math.pow(2, i);
            
            for (int k = 0; k < nNode ; k++){
                
                //System.out.println("\n\n i-j" + i +" "+ k);
                //System.out.println("riga da ricercare: " + i + " posizione da ricercare: " + k );
                Node tmp = search(current, i , k);
                //System.out.println("APPLICO F0 al nodo riga-colonna" + tmp.riga+ "-"+ tmp.pos);
                f0(tmp,i+1,k*2);  
                //System.out.println("APPLICO F1 riga-colonna" + tmp.riga+ "-"+ tmp.pos);
                f1(tmp,i+1,k*2+1); 
                
            }
      
        }
        return root;
    }
       
    public void traverseInOrder(Node node) {
        
        if (node != null) {
            traverseInOrder(node.left);
            System.out.println(" Stampa albero riga:" + node.riga + "posizione:" + node.pos + "KEY: " + Arrays.toString(node.getX00()));
            traverseInOrder(node.right);
        }
        
    }
    
    public static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xff & hash[i]);
        if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    
    //aumenta l'array di byte 
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
    
    /**
     * Metodo utilizzato per restituire il miglior Key set per una data richiesta di un nodo per un certo intervallo
     * @param b albero di chiavi
     * @param initInt inizio intervallo
     * @param endInt    fine intervallo
     * @return ArrayList<Node>  che rappresenta i nodi contenti le chiavi per il dato intervallo
     */
    public ArrayList<Node> getKeySet(BinaryTree b, int initInt, int endInt) {
        
        //==========================andrà sostituito con get profondità albero
        int profonditaAlbero = b.maxDepth(b.getRoot()) ;
        //==========================andrà sostituito con get profondità albero
        
        int totNodes = (int) Math.pow(2, profonditaAlbero);
        //System.out.println("Numero di nodi sull'ultima riga: " + totNodes);
        
        
        ArrayList<Node> nodeList = new ArrayList<Node>();
        for(int i = initInt; i <= endInt; i++) {
         nodeList.add( b.search(b.getRoot(), profonditaAlbero, i) );
        }
        
        for( Node elem : nodeList) {
         //System.out.println("Nodo: "+ elem.riga + elem.pos);
        }
        
        int oldLength = 0;
        
        while(nodeList.size() != oldLength ) {
         //System.out.println("Giro di unione");
         oldLength = nodeList.size();
         
         for(int i = 0; i < nodeList.size()-1; i++) {
          
          Node curr = nodeList.get(i);
          Node next = nodeList.get(i+1);
          
          String prevCurr = String.valueOf(curr.riga) + String.valueOf(getPrevPos(curr)+1);
          String prevNext = String.valueOf(next.riga) + String.valueOf(getPrevPos(next)+1);
          
          
          //System.out.println("prevCurr " + prevCurr );
          //System.out.println("prevNext " + prevNext );
          
          if(prevCurr.equals(prevNext)) {
           nodeList.add(i, b.search(b.getRoot(), curr.riga-1, getPrevPos(curr)) );
           nodeList.remove(curr);
           nodeList.remove(next);
          }
         }
         
        }
        
        for( Node elem : nodeList) {
         System.out.println("Nodo: "+ elem.riga + elem.pos);
        }
     
     return nodeList;
    }
    
    private int getPrevPos(Node n) {
     
        int result = n.pos % 2 == 0 ? n.pos/2 :  (n.pos-1)/2;
        return result;
    }
     
    
    
    // pecca la profondità dell'albero
    public int maxDepth(Node node) {
       if (node == null) {
           return (-1); // an empty tree  has height −1
       } else {
           // compute the depth of each subtree
           int leftDepth = maxDepth(node.left);
           int rightDepth = maxDepth(node.right);
           // use the larger one
           if (leftDepth > rightDepth )
               return (leftDepth + 1);
           else
               return (rightDepth + 1);
       }
   }
   
}