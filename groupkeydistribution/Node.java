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
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.Data;
import messages.JoinReq;
import messages.JoinResp;
import org.apache.commons.lang.SerializationUtils;


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
    public ArrayList<groupkeydistribution.utilities.Node> prova; /*TODO*/
   
    public Node(IpLink network, Ip4Address gkdc_addr) throws IOException, InterruptedException, ClassNotFoundException {
        Ip4AddressPrefix node_addr=(Ip4AddressPrefix)network.nextAddressPrefix();
        // create virtual IP STACK
        Ip4Layer ip = new Ip4Layer(new NetInterface[]{new IpLinkInterface(network,node_addr)});
        UdpLayer udp = new UdpLayer(ip);
        DatagramSocket management_sock=new DatagramSocket(udp,GKDC.MANAGEMENT_PORT);
        DatagramSocket data_sock=new DatagramSocket(udp,GKDC.DATA_PORT);

        //===================anche il nodo dovrebbe avere un thread che gestisce i messaggi DATA che riceve dal GKDC========================
        //NON TESTATO

        Thread dataThread = new Thread("data") {
        @Override
        public void run(){

            System.out.println(ANSI_YELLOW + " Thread  " + getName() + " is running "  + ANSI_RESET);

            //Il primo messaggio è quello di helloworld poi da togliere, per adesso lo tengo per vedere se funziona
            byte[] buf=new byte[1024];
            DatagramPacket pkt = new DatagramPacket(buf,buf.length);
            try {
                //System.out.println("Node["+ip_addr+"]: listening");
                data_sock.receive(pkt);
            } catch (IOException ex) {
                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println( ANSI_RED + "Node[" + node_addr + "]: received: "+ new String(pkt.getData(),0,pkt.getLength()) + ANSI_RESET);

            while(true){
                try {
                    //la receive dovrebbe essere bloccante
                    data_sock.receive(pkt);
                } catch (IOException ex) {
                    Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
                }
                //errore lo da perchè il pacchetto non è stato serializzato con SerializationUtils.serialize
                Data dataRcv = (Data) SerializationUtils.deserialize( pkt.getData());
                System.out.println(System.identityHashCode(this) + " riceve: " + dataRcv.toStringato() + "relativo all'intervallo " + dataRcv.timeSlot );
                //get ciphertext , controllare se l'intervallo temporale rientra in quello che abbiamo noi di chiavi e poi decifrare
                
                if (prova != null) {
	            for (groupkeydistribution.utilities.Node e : prova) {
	             	if ( dataRcv.timeSlot == e.pos ) {
	               
                            try {
				byte[] messaggioSuperSegreto = Encryption.decrypt( dataRcv.getCipherText() );
                		System.out.println( new String( messaggioSuperSegreto, Charset.forName("UTF-8")) );
        		    } catch (Exception e1) {
                                e1.printStackTrace();
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
        //System.out.println(maxSlots);
        
        int nonChiamarloI = ThreadLocalRandom.current().nextInt( maxSlots );
        
        System.out.println("["+ node_addr + "] ha estratto: " + nonChiamarloI );
        Thread.sleep( nonChiamarloI * 10000);
        //System.out.println ("Ip4layer " + ip.toString());//DEBUG
        
        int rimanenza = 4;
        //TODO : estrarre tempo di rimanenza nel gruppo
        if( nonChiamarloI + rimanenza > maxSlots){
            rimanenza = maxSlots - nonChiamarloI;
        }
        
        JoinReq richiesta = new JoinReq( rimanenza,ip.toString() );
        System.out.println(ip.toString() + ": tempo che sto nel gruppo " + rimanenza);
        
        byte[] msgToSend = richiesta.toString().getBytes();
        InetAddress point_addr = Inet4Address.getByName("10.1.1.254") ; 

        //messo pure qua lo slot temporale per provare
        DatagramPacket pkt2 =new DatagramPacket(msgToSend,msgToSend.length, point_addr ,GKDC.MANAGEMENT_PORT);
        //System.out.println( ANSI_RED + "Send to : " + point_addr.toString() + ANSI_BLUE +"\nData " + new String(pkt2.getData()) + " in the time slot " + timeSlots.getValue() + " " + ANSI_RESET);
        management_sock.send(pkt2);

        //-----------------------------------------------------------------------------------

        byte[] bufResp =new byte[1024];
        DatagramPacket pktresp = new DatagramPacket(bufResp,bufResp.length);
        System.out.println("\n\n NODE IN ATTESA DI UNA RISPOSTA!!!\n\n");

        try {
            //la receive dovrebbe essere bloccante
            management_sock.receive(pktresp);
        } catch (IOException ex) {
            Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("\n\n NODE rivevuto il messaggio!!!\n\n");
        JoinResp jR = (JoinResp) SerializationUtils.deserialize(pktresp.getData());
        System.out.println( ANSI_RED + "Node["+node_addr+"]: result request : "+ jR.toStringato() + ANSI_RESET);
        // da stampare le chiavi per controllare se sono quelle
        jR.receivedKey(); 

        try {
            prova = BinaryTree.getKeysFromNodes(jR.getKeySet(), Singleton.getIstance().getDepth() );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
    }
    
}
