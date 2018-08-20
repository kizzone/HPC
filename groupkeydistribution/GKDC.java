package groupkeydistribution;


import groupkeydistribution.utilities.BinaryTree;
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
import java.util.logging.Level;
import java.util.logging.Logger;


public class GKDC {
    
    
    //usato per tenere traccia dell'albero
    BinaryTree bT = new BinaryTree();
    
    
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
	public GKDC(IpLink network, Ip4AddressPrefix gkdc_addr) throws IOException {
		// create virtual IP STACK
		Ip4Layer ip=new Ip4Layer(new NetInterface[]{new IpLinkInterface(network,gkdc_addr)});
		UdpLayer udp=new UdpLayer(ip);
		
		DatagramSocket management_sock=new DatagramSocket(udp,MANAGEMENT_PORT);
		DatagramSocket data_sock=new DatagramSocket(udp,DATA_PORT);

		InetAddress multicast_iaddr=Inet4Address.getByName(((Ip4Prefix)network.getPrefix()).getNetworkBroadcastAddress().toString());
		byte[] msg="hello world".getBytes();
		DatagramPacket pkt=new DatagramPacket(msg,msg.length,multicast_iaddr,DATA_PORT);	
		System.out.println( ANSI_GREEN + "GKDC["+gkdc_addr+"]: send to "+pkt.getAddress().getHostAddress()+":"+pkt.getPort()+": "+new String(pkt.getData(),0,pkt.getLength()) + ANSI_RESET );
		data_sock.send(pkt);

                
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
                            System.out.println( ANSI_GREEN + "GKDC received a new join request : " + new String(pktrcv.getData(),0,pktrcv.getLength())+ "\n creating JOIN - RESPONSE " +  ANSI_RESET);
                            //creating join response
                            
                            //sending join response 
                            
                        }
                                    
                    }
                };
                managementThread.start();
        
                System.out.println("END OF GDKC CONSTRUCTOR ");
                
                /*=============================per adesso ho commentato spero funzioni con il thread
                
		byte[] buf=new byte[1024];
		DatagramPacket pktrcv =new DatagramPacket(buf,buf.length);
		//System.out.println("Node["+ip_addr+"]: listening");
                
                
                // ===============================  QUA MI SA CHE CI VOGLIONO I THREAD ===============================================
                for (int i = 0; i < 4 ; i++){
                    	management_sock.receive(pktrcv);
                        System.out.println( ANSI_GREEN + "GKDC received: " + new String(pktrcv.getData(),0,pktrcv.getLength())+ ANSI_RESET);
 
                        
                }
	        */
                
                //======================================== VELTRI=======================================
		/*msg="join".getBytes();
		pkt=new DatagramPacket(msg,msg.length,Inet4Address.getByName("10.1.1.1"),MANAGEMENT_PORT);
		System.out.println("GKDC["+gkdc_addr+"]: send to "+pkt.getAddress().getHostAddress()+":"+pkt.getPort()+": "+new String(pkt.getData(),0,pkt.getLength()));
		data_sock.send(pkt);*/
                
        }

}
