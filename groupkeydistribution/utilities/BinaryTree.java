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
    
    /**
     * 
     * @return root: testa dell'albero
     */
  
    public Node getRoot() {
        return root;
    }
    
    /**
     * 
     * Costruttore dell'albero binario crea un nodo radice con una chiave (hash di random byte)
     * @param k2 array di byte che serve per calcolare la chiave finale
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    
    public BinaryTree(byte [] k2) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        
        this.root =  new Node();
        byte[] array = new byte[16];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] encodedhash = digest.digest(generatedString.getBytes("UTF-8"));
        root.setX00( xor(encodedhash, k2) );
  
    }
    
    /**
     * 
     * @param n
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    public BinaryTree(Node n) throws NoSuchAlgorithmException, UnsupportedEncodingException{  
        this.root =  n;
        root.setX00(n.getX00());
        root.pos = n.pos;
        root.riga = n.riga;
        
    }
    
    /**
     * 
     * Funzione f0 utilizzata per collegare al ramo di sinistra di un nodo passato come parametro un nuovo nodo con chiave hash dell'array di byte del nodo passato
     * @param current
     * @param riga
     * @param pos
     * @param k2
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    
    public void f0 ( Node current, int riga , int pos , byte [] k2) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        
        String generatedString = new String(current.getX00(), Charset.forName("UTF-8"));
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] encodedhash = digest.digest(generatedString.getBytes("UTF-8"));  
        Node newNode = new Node();
        newNode.setX00( xor(encodedhash, k2) );
        newNode.riga = riga;
        newNode.pos  = pos;
        current.left =  newNode;
        
    }
    
    
    /**
     * Xor per due array di byte
     * @param a
     * @param b
     * @return 
     */
    
    public static byte[] xor(byte[] a, byte[] b) {
        
        byte[] result = new byte[Math.min(a.length, b.length)];
        for (int i = 0; i < result.length; i++) {
          result[i] = (byte) (((int) a[i]) ^ ((int) b[i]));
        }
        return result;
        
    }
    
    /**
     * 
     * Funzione f1 utilizzata per collegare al ramo di destra di un nodo passato come parametro un nuovo nodo con chiave hash dell'array di byte  incrementato
     * @param current
     * @param riga
     * @param pos
     * @param k2
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    public void f1 ( Node current, int riga , int pos , byte [] k2) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    	
        String generatedString = new String(current.getX00(), Charset.forName("UTF-8"));        
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] encodedhash = digest.digest(incrementAtIndex(generatedString.getBytes("UTF-8"),0));
        Node newNode = new Node();
        newNode.setX00( xor(encodedhash, k2) );
        newNode.riga = riga;
        newNode.pos  = pos;
        current.right =  newNode;
       
    }
    
   /**
     * 
     * Ricerca di un nodo di "righe" e "colonne" nell'albero
     * utilizzato in fase di costruzione dell'albero delle chiavi
     * @param node
     * @param h
     * @param p
     * @return 
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
    
    /**
     * 
     * @param node
     * @param h
     * @return 
     */
    public Node search(Node node,int h){
        if(node != null){
            if(node.riga == h){
              // System.out.println("  TROVATO NODO  riga : " + node.riga + " - " + " posizione: " + node.pos );
               return node;
            } else {
              // System.out.println("Non è il nodo che stai cercando semicit. RIGA: " +  node.riga + "POS "+ node.pos);
                Node foundNode = search(node.left,h);
                if(foundNode == null) {
                    foundNode = search(node.right,h);
                }
                return foundNode;
             }
        } else {
            return null;
        }
    } 

    /**
     * BuilTree prende in ingresso un nodo da considerare radice dell'albero e un intero che rappresenta la profondatità e costruisce un albero di chiavi con f0 e f1 
     * è brutta da vedere ma va sistemata ancora
     * @param root
     * @param profondita
     * @param k2
     * @return 
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException 
     */    
    public Node buildTree (Node root, int profondita, byte[] k2) throws NoSuchAlgorithmException, UnsupportedEncodingException{
     //* è brutta da vedere ma va sistemata ancora   
        Node current = root;
        int y = 0;
       // System.out.println("\n\n i" + y );
        int z = 0;
        Node tmp2 = search(current, y , z);
        //System.out.println("APPLICO F0 al nodo riga-colonna" + tmp2.riga+ "-"+ tmp2.pos);
        f0(tmp2,y+1,z*2,k2);  
        //System.out.println("APPLICO F1 riga-colonna" + tmp2.riga+ "-"+ tmp2.pos);
        f1(tmp2,y+1,z*2+1,k2); 
        
        for (int i = 1 ; i < profondita; i++){
            
            int nNode = (int) Math.pow(2, i);
            
            for (int k = 0; k < nNode ; k++){
                
                //System.out.println("\n\n i-j" + i +" "+ k);
                //System.out.println("riga da ricercare: " + i + " posizione da ricercare: " + k );
                Node tmp = search(current, i , k);
                //System.out.println("APPLICO F0 al nodo riga-colonna" + tmp.riga+ "-"+ tmp.pos);
                f0(tmp,i+1,k*2,k2);  
                //System.out.println("APPLICO F1 riga-colonna" + tmp.riga+ "-"+ tmp.pos);
                f1(tmp,i+1,k*2+1,k2); 
                
            }
      
        }
        return root;
    }
    
    /**
     * Generare un albero parziale a partire dal nodo
     * @param root
     * @param currDepth
     * @param maxDepth
     * @param k2
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    public Node buildTree (Node root, int currDepth, int maxDepth, byte[] k2) throws NoSuchAlgorithmException, UnsupportedEncodingException{ 
       Node current = root;
       
       for (int i = currDepth; i < maxDepth; i++){         
           int nNode = (int) Math.pow(2, i);
           
           for (int k = 0; k < nNode ; k++){
               Node tmp = search(current, i , k);
               
               if(tmp!=null) {
                   f0(tmp,i+1,k*2,k2);  
                   f1(tmp,i+1,k*2+1,k2); 
               }
               
           }
     
       }
       return root;
   }
    
    /**
     *  Stampa albero
     * @param node
     */
    public void traverseInOrder(Node node) {
        
        if (node != null) {
            traverseInOrder(node.left);
            System.out.println("        Stampa albero riga:" + node.riga + "posizione:" + node.pos + "KEY: " + Arrays.toString(node.getX00()));
            traverseInOrder(node.right);
        }
        
    }
    

    /**
     * 
     * @param hash
     * @return 
     */
    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xff & hash[i]);
        if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    
    
    /**
     * aumenta l'array di byte 
     * @param hash
     * @return array di byte aumentato
     */
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
     * @return ArrayList<>  che rappresenta i nodi contenti le chiavi per il dato intervallo
     */
    public ArrayList<Node> getKeySet(BinaryTree b, int initInt, int endInt) {
        
        //==========================andrà sostituito con get profondità albero
        int profonditaAlbero = b.maxDepth(b.getRoot()) ;
        //==========================andrà sostituito con get profondità albero
        
        int totNodes = (int) Math.pow(2, profonditaAlbero);
        //System.out.println("Numero di nodi sull'ultima riga: " + totNodes);
        
        
        ArrayList<Node> nodeList = new ArrayList<>();
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
        
        /*  ===========================================DEBUG========================
        for( Node elem : nodeList) {
         System.out.println("Nodo: "+ elem.riga + elem.pos);
        }
         ===========================================DEBUG======================== */
     
     return nodeList;
    }
    
    /**
     * 
     * 
     * 
     * @param n
     * @return 
     */
    private int getPrevPos(Node n) {
        
        int result = n.pos % 2 == 0 ? n.pos/2 :  (n.pos-1)/2;
        return result;
        
    }
    
    /**
     * becca la profondità dell'albero
     * 
     * @param node
     * @return 
     */
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
    
    /**
     * 
     * 
     * @param depth
     * @return 
     */
    private int getLastRowSize(int depth) {
    	return (int) Math.pow(2, depth);
    }
    
    /**
     * 
     * 
     * @param bt
     * @param depth
     * @return 
     */
    public ArrayList<Node> getIntervalKeys (BinaryTree bt, int depth){
    	
    	ArrayList<Node> tmpList = new ArrayList<> ();
    	
    	for( int i = 0; i < getLastRowSize(depth); i++ ) {
    		if( bt.search(bt.getRoot(), depth, i) != null ) {
    			tmpList.add( bt.search(bt.getRoot(), depth, i) );
    		}
    	}
    	
    	return null;
    }
    
    /**
     * 
     * 
     * @param kSet
     * @param depth
     * @param k2
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    public static ArrayList<Node> getKeysFromNodes(ArrayList<Node> kSet, int depth, byte[] k2) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    	
    	ArrayList<Node> derivedKeys = new ArrayList<>();
    	for(Node e : kSet) {
    		//System.out.println(" e.riga " + e.riga + "e.pos " + e.pos );
    		BinaryTree tmpTree = new BinaryTree(e);
    		tmpTree.buildTree(tmpTree.getRoot(), e.riga, depth, k2);

    		System.out.println("        Derivo le chiavi da: X" + e.riga + e.pos );
    		//System.out.println( (tmpTree.search( tmpTree.getRoot(), d )).pos );
    		
    		int i = (tmpTree.search( tmpTree.getRoot(), depth )).pos;
    		while( tmpTree.search( tmpTree.getRoot(), depth, i ) != null ) {
    			System.out.println( "           " + tmpTree.search( tmpTree.getRoot(), depth, i ).toString() );
    			derivedKeys.add(tmpTree.search( tmpTree.getRoot(), depth, i ) );
    			i++;
    		}
    		
    	}
    	
        return derivedKeys;
    }

   
}