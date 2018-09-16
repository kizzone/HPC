package groupkeydistribution;


import groupkeydistribution.utilities.BinaryTree;
import groupkeydistribution.utilities.Encryption;
import groupkeydistribution.utilities.Node;
import groupkeydistribution.utilities.Singleton;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import it.unipr.netsec.ipstack.ip4.Ip4AddressPrefix;
import it.unipr.netsec.ipstack.ip4.Ip4Layer;
import it.unipr.netsec.ipstack.ip4.Ip4Prefix;
import it.unipr.netsec.ipstack.net.NetInterface;
import it.unipr.netsec.ipstack.udp.DatagramSocket;
import it.unipr.netsec.ipstack.udp.UdpLayer;
import it.unipr.netsec.nemo.ip.IpLink;
import it.unipr.netsec.nemo.ip.IpLinkInterface;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.Data;
import messages.JoinResp;
import messages.UnpredictableLeave;
import org.apache.commons.lang.SerializationUtils;


/**
 * 
 * @author domenico
 */

public class GKDC {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final int MANAGEMENT_PORT = 4001;
    public static final int DATA_PORT = 4000;
    public static final int LEAVE_PORT = 4002;

    //TODO aggiustare : non funziona lo volevo fare per fermare il cliclo nel thread managment
    protected volatile boolean stop = false;
    
    //Chiave k2
    private byte[] k2 = new byte[16];
    private BinaryTree bT = null;
    //=====================================================================================================================================================================
    private Map<String, String> map = new HashMap<String, String>();
    

    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
    public GKDC(IpLink network, Ip4AddressPrefix gkdc_addr) throws IOException, NoSuchAlgorithmException, Exception {
        
        //riempi k2 di byte casuali---------------------------------------------
        new Random().nextBytes(k2); 
        //----------------------------------------------------------------------
        
        Singleton virtualTime = Singleton.getIstance();
        virtualTime.setDepth(3); //DA MODIFICARE IN BASEA ALLA SIMULAZIONE
        virtualTime.setLeafs( (int) Math.pow(2,virtualTime.getDepth()) );

        // create virtual IP STACK
        Ip4Layer ip=new Ip4Layer(new NetInterface[]{new IpLinkInterface(network,gkdc_addr)});
        UdpLayer udp=new UdpLayer(ip);

        DatagramSocket management_sock=new DatagramSocket(udp,MANAGEMENT_PORT);
        DatagramSocket data_sock=new DatagramSocket(udp,DATA_PORT);
        //===============================
        DatagramSocket leave_sock=new DatagramSocket(udp,LEAVE_PORT);
        

        InetAddress multicast_iaddr=Inet4Address.getByName(((Ip4Prefix)network.getPrefix()).getNetworkBroadcastAddress().toString());
        //  byte[] msg="hello world".getBytes();    //sostituito con k2
        //DatagramPacket pkt=new DatagramPacket(msg,msg.length,multicast_iaddr,DATA_PORT);	
        DatagramPacket pkt=new DatagramPacket(k2,k2.length,multicast_iaddr,DATA_PORT);	
        System.out.println( ANSI_GREEN + "GKDC["+gkdc_addr+"]: send K2 to "+pkt.getAddress().getHostAddress()+":"+pkt.getPort()+": "+ BinaryTree.bytesToHex(k2)  + ANSI_RESET );
        data_sock.send(pkt);

        //inizializzazione albero chiavi
        bT = new BinaryTree(k2);
        groupkeydistribution.utilities.Node node = bT.buildTree(bT.getRoot(),virtualTime.getDepth() ,k2);
        bT.traverseInOrder(node);

        //===============================il thread management si occupa di ricevere le richieste di JOIN e rispondere con eventuale messaggio di RESPONSE

        Thread managementThread = new Thread("management") {
        @Override
        @SuppressWarnings("empty-statement")
        public void run(){

                System.out.println(ANSI_YELLOW + "Thread  " + getName() + " is running "  + ANSI_RESET);
                byte[] buf=new byte[1024];
                DatagramPacket pktrcv =new DatagramPacket(buf,buf.length);
                while(!stop){
                    try {
                       //la receive dovrebbe essere bloccante
                        management_sock.receive(pktrcv);
                    } catch (IOException ex) {
                        Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
                    }
     
                    System.out.println( ANSI_YELLOW + "GKDC (managment) received a new join request : " + new String(pktrcv.getData(),0,pktrcv.getLength())+"  in time intervall "+ virtualTime.getValue()  +"creating JOIN - RESPONSE " +  ANSI_RESET);
                    String str = new String(pktrcv.getData(),0,pktrcv.getLength());
                    String [] arr = str.split("&");
                    int finalInt = Integer.parseInt(arr[1]);
                    int initInt = virtualTime.getValue();
                    ArrayList<Node> nodeList = new ArrayList<>();
                    
                    
                    //============================================================================================================================================================
                    map.put(getAddress(arr[0]),initInt + "-" + finalInt );
                    map.entrySet().forEach((entry) -> {
                        System.out.println(entry);
                    });
                    System.out.println();
                    //============================================================================================================================================================
                 
                    
                    System.out.println( ANSI_YELLOW + "GKDC (managment) : Sono stati richiesti chiavi dall'intevallo " + (initInt) + "-" + (initInt + finalInt - 1) + ANSI_RESET);
                    nodeList =  bT.getKeySet(bT  ,   initInt , initInt + finalInt - 1 );

                    for ( Node e : nodeList) {
                        System.out.println(  ANSI_YELLOW + "GKDC:  Devo mandare a " + arr[0] + " le chiavi di riga "+ e.riga +" e posizione: " + e.pos + " " + ANSI_RESET );
                    }

                    //sending join response to node arr[0]
                    JoinResp response = new JoinResp(  nodeList );
                    String addr = GKDC.getAddress( arr[0] );
                    //System.out.println("indirizzo : " + addr);//debug
                    try {

                        InetAddress resp_addr = Inet4Address.getByName(addr); 
                        byte[] data = SerializationUtils.serialize((Serializable) response); 
                        DatagramPacket pkt2 =new DatagramPacket( data , data.length, resp_addr ,GKDC.MANAGEMENT_PORT);
                        System.out.println( ANSI_YELLOW + "GKDC Send data message to : " + resp_addr.toString() + ANSI_RESET);
                        management_sock.send(pkt2);
                        //System.out.println( ANSI_RED + "Send ok " + ANSI_RESET); //DEBUG

                    } catch (UnknownHostException ex) {
                        Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
     
                System.out.println(ANSI_YELLOW + "Managment thread exit " + ANSI_RESET);
            }
        };
        managementThread.start();
        
        
        Thread leavingThread = new Thread("leaving") {
        @Override
        @SuppressWarnings("empty-statement")
        public void run(){

                System.out.println(ANSI_YELLOW + "Thread  " + getName() + " is running "  + ANSI_RESET);
                byte[] buf=new byte[1024];
                DatagramPacket pktrcv = new DatagramPacket(buf,buf.length);
                while(!stop){
                    try {
                       //la receive dovrebbe essere bloccante
                        leave_sock.receive(pktrcv);
                    } catch (IOException ex) {
                        Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally{
                        UnpredictableLeave uL = (UnpredictableLeave) SerializationUtils.deserialize(pktrcv.getData());
                        System.out.println("LEAVE MESSAGE RECEIVED from " + uL.getNode() + "  relativo al time  slot: " + uL.getCurrentTimeSlot());
                        //cambiare k2, riaggiornare l'albero e avvisare tutti i nodi
                        new Random().nextBytes(k2);
                        
                        //riscrive su bT? dovrebbe...
                        try {
                            
                            bT.buildTree( bT.getRoot() , virtualTime.getDepth() ,k2);
                        } catch (NoSuchAlgorithmException ex) {
                            Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
                        }finally{
                            System.out.println("IL NUOVO ALBERO E' DIVENTATO >> \n");
                            bT.traverseInOrder( bT.getRoot() );
                        }
                        //avvisare tutti i nodi
                        
                    }

                } 
                System.out.println(ANSI_YELLOW + "Leaving thread exit " + ANSI_RESET);
            }
        };
        leavingThread.start();
        
   
        //periodicamente in un iperperiodo di slot temporali il Gkdc invia dei messaggi data contenti (in chiaro) l'id dello slot temporale e criptato il messaggio verso i nodi
        
        for ( virtualTime.setValue(0); virtualTime.getValue() < virtualTime.getLeafs();){

            Thread.sleep(1000*10);
            byte[] messaggioSuperSegreto = "Domenico è troppo bello (capisci tu quale)".getBytes(StandardCharsets.UTF_8);
            System.out.println(ANSI_GREEN + "\nGKDC CHIAVE UTILIZZATA PER CRIPTARE IL MESSAGGIO DATA " + Arrays.toString(bT.search(bT.getRoot(),virtualTime.getDepth(), virtualTime.getValue() ).getX00()) + ANSI_RESET);
            Encryption en = new Encryption( bT.search(bT.getRoot(),virtualTime.getDepth(), virtualTime.getValue() ).getX00());
            byte[] cipherText = en.encrypt(messaggioSuperSegreto);
            Data msgData = new Data(cipherText, virtualTime.getValue() );
            DatagramPacket criptoPkt=new DatagramPacket(SerializationUtils.serialize((Serializable) msgData) , SerializationUtils.serialize((Serializable) msgData).length, multicast_iaddr,DATA_PORT);	
            System.out.println( ANSI_GREEN + "GKDC["+gkdc_addr+"]: send to "+ pkt.getAddress().getHostAddress() + ":" + pkt.getPort() + ": " +msgData.toStringato() + ANSI_RESET );
            data_sock.send(criptoPkt);
            
            synchronized(virtualTime){
                virtualTime.increment();
                virtualTime.notifyAll();
            }
            
        }
        //per stoppare il thread managment
        // NON FUNZIONA
        stop = true;
    

    }
        
    /**
     * Estrarre indirizzo da una stringa
     * @author domenico
     */

    private static String getAddress(String string){
        String [] arr = string.split("\\[");
        String [] arr2 =  arr[1].split("\\]");
        return arr2[0];
    }
    
}
