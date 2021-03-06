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
 * @author domenico
 * 
 */

public class BinaryTree {

    
    /**
     * Radice dell'albero
     */
    Node root;
    
    /**
     * Getter per la radice dell'albero
     * @return root radice dell'albero
     */
    public Node getRoot() {
        return root;
    }
    
    /**
     * Costruttore dell'albero binario crea un nodo radice con una chiave (hash di random byte)
     * @param k2 array di byte che serve per calcolare la chiave finale
     * @throws NoSuchAlgorithmException eccezione lanciata
     * @throws UnsupportedEncodingException  eccezione lanciata
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
     * Costruttore per albero parziale
     * @param n  nodo inizile
     * @throws NoSuchAlgorithmException eccezione lanciata
     * @throws UnsupportedEncodingException  eccezione lanciata
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
     * @param current nodo a cui applicare la funzione
     * @param riga riga dell'albero
     * @param pos colonna  dell'albero
     * @param k2 chiave k2
     * @throws NoSuchAlgorithmException eccezione lanciata
     * @throws UnsupportedEncodingException  eccezione lanciata
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
     * @param a array di byte
     * @param b array di byte
     * @return  xor dei due array
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
     * @param current nodo a cui applicare la funzione
     * @param riga riga dell'albero
     * @param pos colonna  dell'albero
     * @param k2 chiave k2
     * @throws NoSuchAlgorithmException eccezione lanciata
     * @throws UnsupportedEncodingException eccezione lanciata
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
     * Ricerca di un nodo di "righe" e "colonne" nell'albero utilizzato in fase di costruzione dell'albero delle chiavi
     * @param node nodo da cui partire per la ricerca
     * @param h riga 
     * @param p colonna
     * @return nodo trovato oppure null
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
     * Ricerca di un nodo nell'albero ad una data profondà
     * @param node nodo da ricercare
     * @param h profondità
     * @return nodo trovato o null
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
     * @param root nodo radice dell'albero
     * @param profondita la profondità dell'albero da generare
     * @param k2 chiave k2  con cui calcolare le chiavi successive
     * @return radice dellàalbero
     * @throws UnsupportedEncodingException eccezione lanciata
     * @throws NoSuchAlgorithmException  eccezione lanciata
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
     * @param root Radice da cui partire
     * @param currDepth profondità attuale
     * @param maxDepth massima profondità dell'albero
     * @param k2 chiave k2
     * @return nodo di partenza a cui sono stati collegati i nodi successivi
     * @throws NoSuchAlgorithmException eccezione lanciata
     * @throws UnsupportedEncodingException eccezione lanciata
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
     * @param node Radice dell'albero da stampare
     */
    public void traverseInOrder(Node node) {
        
        if (node != null) {
            traverseInOrder(node.left);
            System.out.println("        Stampa albero riga:" + node.riga + "posizione:" + node.pos + "KEY: " + Arrays.toString(node.getX00()));
            traverseInOrder(node.right);
        }
        
    }
    

    /**
     * Converte byte in hex
     * @param hash array di byte da stampare in HEX
     * @return Stringa rappresentante l'array in HEX
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
     * @param array array da incrementare
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
     * @return ArrayList  che rappresenta i nodi contenti le chiavi per il dato intervallo
     */
    public ArrayList<Node> getKeySet(BinaryTree b, int initInt, int endInt) {
        
        int profonditaAlbero = b.maxDepth(b.getRoot()) ;
        int totNodes = (int) Math.pow(2, profonditaAlbero);
        //System.out.println("Numero di nodi sull'ultima riga: " + totNodes);
        
        ArrayList<Node> nodeList = new ArrayList<>();
        for(int i = initInt; i <= endInt; i++) {
        	nodeList.add( b.search(b.getRoot(), profonditaAlbero, i) );
        }
        
        /*
        for( Node elem : nodeList) {
         System.out.println("Nodo: "+ elem.riga + elem.pos);
        }
        */
        
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
     * Getter per la posizione superiore del nodo
     * @param n nodo da considerare
     * @return 
     */
    private int getPrevPos(Node n) {
        
        int result = n.pos % 2 == 0 ? n.pos/2 :  (n.pos-1)/2;
        return result;
        
    }
    
    /**
     * Calcola la profondità dell'albero
     * @param node nodo da cui calcolare la profondità
     * @return  profondità dell'albero binario
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
     * Calcola il numero di nodi ad una data profondita
     * @param depth profondità dell'albero
     * @return numero di elementi nella riga di profondità depth
     */
    private int getLastRowSize(int depth) {
    	return (int) Math.pow(2, depth);
    }
    
    /**
     * Cerca i nodi minimi per il calcolo delle chiavi
     * @param bt Albero binario delle chiavi
     * @param depth profondità dell'albero
     * @return nodi minimi che ti permettono di ottenere le chiavi per ogni intervallo
     */
    public ArrayList<Node> getIntervalKeys (BinaryTree bt, int depth){
    	
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    	ArrayList<Node> tmpList = new ArrayList<> ();
    	
    	for( int i = 0; i < getLastRowSize(depth); i++ ) {
    		if( bt.search(bt.getRoot(), depth, i) != null ) {
    			tmpList.add( bt.search(bt.getRoot(), depth, i) );
    		}
    	}
    	
    	return null;
    }
    
    /**
     * Metodo che deriva le chiavi dal key set ricevuto
     * @param kSet il keySet ricevuto dal nodo
     * @param depth profondità dell'albero
     * @param k2 array di byte che rappresenta la chiave k2
     * @return arraylist dei nodi dell'albero calcolati al partire dal kSet
     * @throws NoSuchAlgorithmException eccezione lanciata
     * @throws UnsupportedEncodingException eccezione lanciata
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