package groupkeydistribution;

import groupkeydistribution.utilities.BinaryTree;
import groupkeydistribution.utilities.Encryption;
import groupkeydistribution.utilities.Singleton;
import java.io.IOException;
import java.net.DatagramPacket;
import it.unipr.netsec.ipstack.ip4.Ip4Address;
import it.unipr.netsec.ipstack.ip4.Ip4AddressPrefix;
import it.unipr.netsec.ipstack.ip4.Ip4Layer;
import it.unipr.netsec.ipstack.net.NetInterface;
import it.unipr.netsec.ipstack.udp.DatagramSocket;
import it.unipr.netsec.ipstack.udp.UdpLayer;
import it.unipr.netsec.nemo.ip.IpLink;
import it.unipr.netsec.nemo.ip.IpLinkInterface;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.Data;
import messages.JoinReq;
import messages.JoinResp;
import messages.UnpredictableLeave;
import org.apache.commons.lang.SerializationUtils;

/**
 * Classe che rappresenta un nodo generico della rete che richiede di entrare nella rete
 * @author domenico
 */

public class Node implements Serializable {
    
    /**
     *  Reset color
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     *  Red color
     */
    public static final String ANSI_RED = "\u001B[31m";

    /**
     *  Green Color
     */
    public static final String ANSI_GREEN = "\u001B[32m";

    /**
     *  Yellow color
     */
    public static final String ANSI_YELLOW = "\u001B[33m";

    /**
     *  Blue color
     */
    public static final String ANSI_BLUE = "\u001B[34m";
    
    /**
     *  ArrayList che rappresenta la lista di nodi dell'albero (chiavi) ricevute
     */
    public ArrayList<groupkeydistribution.utilities.Node> prova; 
    /**
     * Array di byte che rappresenta la chiave k2 in possesso del nodo
     */
    private byte [] k2 ;
    
    /**
     *  Boolean per fermare i thread del gkdc
     */
    private volatile boolean stop = false;
   
    /**
     * Il costruttore del nodo si occupa di inizializzarlo, avviare i suoi thread per  gestire i messaggi ricevuti, mandare richieste di join e leave
     * @author domenico
     * @param network NEMO
     * @param gkdc_addr indirizzo IP del GKDC
     * @throws java.io.IOException eccezione lanciata
     * @throws java.lang.InterruptedException eccezione lanciata
     * @throws java.lang.ClassNotFoundException eccezione lanciata
     */
    
    public Node(IpLink network, Ip4Address gkdc_addr) throws IOException, InterruptedException, ClassNotFoundException {
        
        Ip4AddressPrefix node_addr=(Ip4AddressPrefix)network.nextAddressPrefix();
        // create virtual IP STACK
        Ip4Layer ip = new Ip4Layer(new NetInterface[]{new IpLinkInterface(network,node_addr)});
        UdpLayer udp = new UdpLayer(ip);
        DatagramSocket management_sock = new DatagramSocket(udp,GKDC.MANAGEMENT_PORT);
        DatagramSocket data_sock=new DatagramSocket(udp,GKDC.DATA_PORT);
        DatagramSocket leave_sock = new DatagramSocket(udp,GKDC.LEAVE_PORT);
        
        //Il nodo ha un thread che gestisce i messaggi DATA che riceve dal GKDC
        Thread dataThread = new Thread("data") {
        @Override
        public void run(){

            System.out.println(ANSI_RED + "NODE: "+ node_addr + "  Thread  " + getName() + " is running "  + ANSI_RESET);
            byte[] buf=new byte[16];
            DatagramPacket pkt = new DatagramPacket(buf,buf.length);
            try {
                //System.out.println("Node["+ip_addr+"]: listening");  //DEBUG
                data_sock.receive(pkt);
            } catch (IOException ex) {
                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            k2 = pkt.getData();
            System.out.println( ANSI_RED + "Node[" + node_addr + "]: k2 received: " + BinaryTree.bytesToHex(k2) + ANSI_RESET);
            
            while(!stop){
                buf = new byte[1024];
                pkt = new DatagramPacket(buf,buf.length);
                try {
                    data_sock.receive(pkt);
                } catch (IOException ex) {
                    Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Data dataRcv = (Data) SerializationUtils.deserialize( pkt.getData());
                System.out.println(ANSI_RED + "NODE Thread DATA n " + node_addr + " ricevuto dato : " + dataRcv.toStringato() + ANSI_RESET);
                
                if (prova != null) {
                    prova.stream().filter((e) -> ( dataRcv.timeSlot == e.pos )).forEachOrdered(( _item ) -> {
                        try {
                            byte[] messaggioSuperSegreto = new Encryption(_item.getX00()).decrypt( dataRcv.getCipherText());
                            System.out.println(ANSI_RED + "NODE "+ ANSI_RESET + node_addr + ANSI_RED + " Thread DATA : decriptato " + ANSI_RESET + new String( messaggioSuperSegreto, Charset.forName("UTF-8")) + " chiave utilizzata per decript: " + Arrays.toString(_item.getX00()) );
                        } catch (Exception e1) {
                        }
                           
                    });
                }
            }   
            this.interrupt();
        }};

        dataThread.start();
        
        Thread.sleep(1000);
        
        //Mandar la richiesta di join
        int maxSlots = Singleton.getIstance().getLeafs() - 1;
        //System.out.println(maxSlots); //DEBUG
        int nonChiamarloI = ThreadLocalRandom.current().nextInt( maxSlots );
        
        System.out.println("NODE ["+ node_addr + "] ha estratto: " + nonChiamarloI  );
        Thread.sleep( nonChiamarloI * 10000);
        //System.out.println ("Ip4layer " + ip.toString());//DEBUG
        int rimanenza = 4; //TODO: da rendere dinamico anche questo
       
        if( nonChiamarloI + rimanenza > maxSlots){
            rimanenza = maxSlots - nonChiamarloI + 1;
        }
        
        JoinReq richiesta = new JoinReq( rimanenza,ip.toString() );
        //System.out.println(" \nNODE " + ip.toString() + ": tempo che sto nel gruppo " + rimanenza);//DEBUG 
        
        byte[] msgToSend = richiesta.toString().getBytes();
        InetAddress point_addr = Inet4Address.getByName("10.1.1.254") ; 

        DatagramPacket pkt2 =new DatagramPacket(msgToSend,msgToSend.length, point_addr ,GKDC.MANAGEMENT_PORT);
        //System.out.println( ANSI_RED + "Send to : " + point_addr.toString() + ANSI_BLUE +"\nData " + new String(pkt2.getData()) + " in the time slot " + timeSlots.getValue() + " " + ANSI_RESET); //DEBUG
        management_sock.send(pkt2);

        //Thread in attesa di eventuali messaggi di JOIN-RESP
        Thread joinResp = new Thread("joinResp") {
        @Override
        public void run(){

            while(!stop){
                
                byte[] bufResp =new byte[1024];
                DatagramPacket pktresp = new DatagramPacket(bufResp,bufResp.length);
                //System.out.println("\n\n NODE IN ATTESA DI UNA RISPOSTA!!!\n\n");

                try {
                    management_sock.receive(pktresp);
                } catch (IOException ex) {
                    Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
                }

                //System.out.println("\n NODE:" + node_addr + " rivevuto un  messaggio di RESPONSE");//DEBUG
                JoinResp jR = (JoinResp) SerializationUtils.deserialize(pktresp.getData());
                System.out.println( ANSI_RED + "Node["+node_addr+"]: result request : "+ jR.toStringato() + ANSI_RESET);//DEBUG
                jR.receivedKey(); // DEBUG  stampa le chiavi ricevute

                try {
                    prova = BinaryTree.getKeysFromNodes(jR.getKeySet(), Singleton.getIstance().getDepth(),jR.getK2());
                } catch (NoSuchAlgorithmException e) {
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.interrupt();
        }};
        
        joinResp.start();
        
        //SENDING LEAVE MESSAGE
        char aa = ( char  )node_addr.toString().toCharArray()[node_addr.toString().length() -1 ];
        int a =  Integer.parseInt(String.valueOf(aa));
        
        int ts = Singleton.getIstance().getValue();
        if( ( a % 2 ) != 0 ){
            //se non sforiamo l'intervallo allora lo mando
            if (ts + 2 <  Singleton.getIstance().getLeafs() ){            
                UnpredictableLeave uL = new UnpredictableLeave ((ts + 2) ,node_addr.toString());
                byte[] leaveMessage = SerializationUtils.serialize(uL); 
                DatagramPacket pkt3 = new DatagramPacket(leaveMessage,leaveMessage.length, point_addr ,GKDC.LEAVE_PORT);
                System.out.println( ANSI_RED + "Send to : " + point_addr.toString() + ANSI_BLUE +"\nUNPREDICTABLE LEAVE in the time slot " + (ts + 2)  + " " + ANSI_RESET); //DEBUG
                //WAITING....
                synchronized( Singleton.getIstance() ){
                    while(Singleton.getIstance().getValue() != ts + 2 ){
                        Singleton.getIstance().wait();
                    }
                }
                
                leave_sock.send(pkt3);
        
            }
        }
        else{
            synchronized( Singleton.getIstance() ){
                while(Singleton.getIstance().getValue() != ts + 4 ){
                    Singleton.getIstance().wait();
                }
            }
        }
        stop = true;
        management_sock.close();
        data_sock.close();
        leave_sock.close();
        Thread.sleep(1000);
        System.out.println("NODE "+ node_addr + " exit." );
        
        Thread.currentThread().interrupt();
        
    }
}
