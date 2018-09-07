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
import org.apache.commons.lang.SerializationUtils;

/**
 * 
 * @author domenico
 */

public class Node implements Serializable {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public ArrayList<groupkeydistribution.utilities.Node> prova; 
   
    /**
     * 
     * @author domenico
     * @param network
     * @param gkdc_addr
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     * @throws java.lang.ClassNotFoundException
     */
    
    public Node(IpLink network, Ip4Address gkdc_addr) throws IOException, InterruptedException, ClassNotFoundException {
        
        Ip4AddressPrefix node_addr=(Ip4AddressPrefix)network.nextAddressPrefix();
        // create virtual IP STACK
        Ip4Layer ip = new Ip4Layer(new NetInterface[]{new IpLinkInterface(network,node_addr)});
        UdpLayer udp = new UdpLayer(ip);
        DatagramSocket management_sock=new DatagramSocket(udp,GKDC.MANAGEMENT_PORT);
        DatagramSocket data_sock=new DatagramSocket(udp,GKDC.DATA_PORT);

        //===================Il nodo ha un thread che gestisce i messaggi DATA che riceve dal GKDC==================================================

        Thread dataThread = new Thread("data") {
        @Override
        public void run(){

            System.out.println(ANSI_RED + "NODE:  Thread  " + getName() + " is running "  + ANSI_RESET);

            //Il primo messaggio è quello di helloworld poi da togliere, per adesso lo tengo per vedere se funziona
            byte[] buf=new byte[1024];
            DatagramPacket pkt = new DatagramPacket(buf,buf.length);
            try {
                //System.out.println("Node["+ip_addr+"]: listening");  //DEBUG
                data_sock.receive(pkt);
            } catch (IOException ex) {
                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //==================================================================
            try {
                byte [] k2 = new String(pkt.getData(),0,pkt.getLength()).getBytes("UTF-8");
                System.out.println( ANSI_RED + "Node[" + node_addr + "]: k2 received: " + Arrays.toString(k2) + ANSI_RESET); 
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            }
            //==================================================================
            

            while(true){
                
                try {
                    data_sock.receive(pkt);
                } catch (IOException ex) {
                    Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Data dataRcv = (Data) SerializationUtils.deserialize( pkt.getData());
                System.out.println(ANSI_RED + "NODE Thread DATA n " + System.identityHashCode(this) + " ricevuto dato : " + dataRcv.toStringato() + ANSI_RESET);
                
                if (prova != null) {
	            for (groupkeydistribution.utilities.Node e : prova) {
	             	if ( dataRcv.timeSlot == e.pos ) {
                            try {
				byte[] messaggioSuperSegreto = Encryption.decrypt( dataRcv.getCipherText() );
                		System.out.println(ANSI_RED + "NODE Thread DATA : ricevuto " + ANSI_RESET + new String( messaggioSuperSegreto, Charset.forName("UTF-8")) );
        		    } catch (Exception e1) {
        		    }
	               	}
	            }
                }
            }                                    
        }};

        dataThread.start();
        
        Thread.sleep(1000);
        //-------------------------------------------Mandar la richiesta di join----------------------------- 
        int maxSlots = Singleton.getIstance().getLeafs() - 1;
        //System.out.println(maxSlots); //DEBUG
        
        int nonChiamarloI = ThreadLocalRandom.current().nextInt( maxSlots );
        
        System.out.println("NODE ["+ node_addr + "] ha estratto: " + nonChiamarloI );
        Thread.sleep( nonChiamarloI * 10000);
        //System.out.println ("Ip4layer " + ip.toString());//DEBUG
        
        int rimanenza = 4; //TODO: da rendere dinamico anche questo
       
        if( nonChiamarloI + rimanenza > maxSlots){
            rimanenza = maxSlots - nonChiamarloI;
        }
        
        JoinReq richiesta = new JoinReq( rimanenza,ip.toString() );
        System.out.println("NODE " + ip.toString() + ": tempo che sto nel gruppo " + rimanenza);//DEBUG 
        
        byte[] msgToSend = richiesta.toString().getBytes();
        InetAddress point_addr = Inet4Address.getByName("10.1.1.254") ; 

        DatagramPacket pkt2 =new DatagramPacket(msgToSend,msgToSend.length, point_addr ,GKDC.MANAGEMENT_PORT);
        //System.out.println( ANSI_RED + "Send to : " + point_addr.toString() + ANSI_BLUE +"\nData " + new String(pkt2.getData()) + " in the time slot " + timeSlots.getValue() + " " + ANSI_RESET); //DEBUG
        management_sock.send(pkt2);

        //-----------------------------------------------------------------------------------

        byte[] bufResp =new byte[1024];
        DatagramPacket pktresp = new DatagramPacket(bufResp,bufResp.length);
        System.out.println("\n\n NODE IN ATTESA DI UNA RISPOSTA!!!\n\n");

        try {
            management_sock.receive(pktresp);
        } catch (IOException ex) {
            Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("\n\n NODE: rivevuto il messaggio!!!\n\n");//DEBUG
        JoinResp jR = (JoinResp) SerializationUtils.deserialize(pktresp.getData());
        System.out.println( ANSI_RED + "Node["+node_addr+"]: result request : "+ jR.toStringato() + ANSI_RESET);//DEBUG
        jR.receivedKey(); // DEBUG  stampa le chiavi ricevute

        
        try {
            prova = BinaryTree.getKeysFromNodes(jR.getKeySet(), Singleton.getIstance().getDepth() );
        } catch (NoSuchAlgorithmException e) {
        }
        
    }
    
}
