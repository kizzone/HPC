package groupkeydistribution;


import groupkeydistribution.utilities.BinaryTree;
import groupkeydistribution.utilities.Encryption;
import groupkeydistribution.utilities.Node;
import groupkeydistribution.utilities.timeSlots;
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
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.Data;


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
    public static final int MANAGEMENT_PORT=4001;
    public static final int DATA_PORT=4000;
    
    
	
    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
	public GKDC(IpLink network, Ip4AddressPrefix gkdc_addr) throws IOException, NoSuchAlgorithmException, Exception {
		// create virtual IP STACK
                //intero che rappresenta lo slot temporale
                timeSlots tS = new timeSlots();
                //gestito dalla classe per motivi di synch non so se si fa cosi
                
		Ip4Layer ip=new Ip4Layer(new NetInterface[]{new IpLinkInterface(network,gkdc_addr)});
		UdpLayer udp=new UdpLayer(ip);
		
		DatagramSocket management_sock=new DatagramSocket(udp,MANAGEMENT_PORT);
		DatagramSocket data_sock=new DatagramSocket(udp,DATA_PORT);

		InetAddress multicast_iaddr=Inet4Address.getByName(((Ip4Prefix)network.getPrefix()).getNetworkBroadcastAddress().toString());
		byte[] msg="hello world".getBytes();
		DatagramPacket pkt=new DatagramPacket(msg,msg.length,multicast_iaddr,DATA_PORT);	
		System.out.println( ANSI_GREEN + "GKDC["+gkdc_addr+"]: send to "+pkt.getAddress().getHostAddress()+":"+pkt.getPort()+": "+new String(pkt.getData(),0,pkt.getLength()) + ANSI_RESET );
		data_sock.send(pkt);

                BinaryTree bT = new BinaryTree();
                
                int profonditaAlbero = 3;
                groupkeydistribution.utilities.Node node = bT.buildTree(bT.getRoot(),profonditaAlbero);
                
                int slotTemporali = (int) Math.pow(2, profonditaAlbero);
                
                
                bT.traverseInOrder(node);
                
                //===============================ultima modifica con thread
                
                Thread managementThread = new Thread("management") {
                @Override
                public void run(){
                        
                        System.out.println(ANSI_YELLOW + " Thread  " + getName() + " is running "  + ANSI_RESET);
                        byte[] buf=new byte[1024];
                        DatagramPacket pktrcv =new DatagramPacket(buf,buf.length);
                        
                        while(true){
                            try {
                                //la receuve dovrebbe essere bloccante
                                management_sock.receive(pktrcv);
                            } catch (IOException ex) {
                                Logger.getLogger(GKDC.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println( ANSI_GREEN + "GKDC received a new join request : " + new String(pktrcv.getData(),0,pktrcv.getLength())+"  in time intervall "+ tS.getValue()  +"\n creating JOIN - RESPONSE " +  ANSI_RESET);
                            //TODO creating join response
                            String str = new String(pktrcv.getData(),0,pktrcv.getLength());
                            String [] arr = str.split("&");
                            int finalInt = Integer.parseInt(arr[1]);
                            int initInt = tS.getValue();
                            ArrayList<Node> nodeList = new ArrayList<Node>();
                            System.out.println("Sono stati richiesti chiavi dall'intevallo " + initInt +1  + "-" + (initInt + finalInt) );
                            nodeList =  bT.getKeySet(bT  ,   initInt +1 , initInt + finalInt );
                            
                            for ( Node e : nodeList) {
                                System.out.println( "Devo mandarti le chiavi di riga "+ e.riga +"e posizione: " + e.pos );
                                
                            }

                            //sending join response 
                            
                        }                                    
                    }
                };
                managementThread.start();
                
                
                //periodicamente in un iperperiodo slotTemporali il Gkdc invia dei messaggi data in chiaro contenti l'id dello slot temporale e criptato il messaggio verso i nodi
                // bisogna trovare un modo di far diventare la variabile i condivisa in modo che anche il manadgment thread sa che intervallo temporale è
                
                for ( tS.setValue(0); tS.getValue() < slotTemporali; tS.increment() ){
                    
                    byte[] messaggioSuperSegreto = "Domenico è troppo bello (capisci tu quale)".getBytes(StandardCharsets.UTF_8);
                    Encryption en = new Encryption( bT.search(bT.getRoot(),profonditaAlbero ,tS.getValue() ).getX00());
                    byte[] cipherText = en.encrypt(messaggioSuperSegreto);
                    Data msgData = new Data(cipherText, tS.getValue() );
                    System.out.println(msgData.toStringato());
                    //beccare la lungheza di msgData.toString()
                    DatagramPacket criptoPkt=new DatagramPacket(msgData.toStringato().getBytes("UTF-8"), msgData.toStringato().getBytes("UTF-8").length,multicast_iaddr,DATA_PORT);	
                    System.out.println( ANSI_GREEN + "GKDC["+gkdc_addr+"]: send to "+pkt.getAddress().getHostAddress()+":"+pkt.getPort()+": "+new String(criptoPkt.getData(),0,criptoPkt.getLength()) + ANSI_RESET );
                    data_sock.send(criptoPkt);
                    Thread.sleep(1000*10);

                }

                System.out.println("END OF GDKC CONSTRUCTOR ");
                
                
                
                
                
             
                //======================================== VELTRI=======================================
		/*msg="join".getBytes();
		pkt=new DatagramPacket(msg,msg.length,Inet4Address.getByName("10.1.1.1"),MANAGEMENT_PORT);
		System.out.println("GKDC["+gkdc_addr+"]: send to "+pkt.getAddress().getHostAddress()+":"+pkt.getPort()+": "+new String(pkt.getData(),0,pkt.getLength()));
		data_sock.send(pkt);*/
                
        }

}
