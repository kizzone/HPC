package groupkeydistribution;


import groupkeydistribution.utilities.timeSlots;
import java.io.IOException;
import java.net.DatagramPacket;

import it.unipr.netsec.ipstack.ip4.Ip4Address;
import it.unipr.netsec.ipstack.ip4.Ip4AddressPrefix;
import it.unipr.netsec.ipstack.ip4.Ip4Layer;
import it.unipr.netsec.ipstack.ip4.Ip4Prefix;
import it.unipr.netsec.ipstack.net.NetInterface;
import it.unipr.netsec.ipstack.udp.DatagramSocket;
import it.unipr.netsec.ipstack.udp.UdpLayer;
import it.unipr.netsec.nemo.ip.IpLink;
import it.unipr.netsec.nemo.ip.IpLinkInterface;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import messages.JoinReq;


public class Node {
    
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BLACK = "\u001B[30m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE = "\u001B[34m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_CYAN = "\u001B[36m";
        public static final String ANSI_WHITE = "\u001B[37m";

    
	public Node(IpLink network, Ip4Address gkdc_addr) throws IOException, InterruptedException {
		Ip4AddressPrefix node_addr=(Ip4AddressPrefix)network.nextAddressPrefix();
		// create virtual IP STACK
		Ip4Layer ip = new Ip4Layer(new NetInterface[]{new IpLinkInterface(network,node_addr)});
		UdpLayer udp = new UdpLayer(ip);
		DatagramSocket management_sock=new DatagramSocket(udp,GKDC.MANAGEMENT_PORT);
		DatagramSocket data_sock=new DatagramSocket(udp,GKDC.DATA_PORT);
		
                //===================anche il nodo dovrebbe avere un thread che gestisce i messaggi DATA che riceve dal GKDC========================
                

                
                //=============================================================VELTRI=============================================
                /*
                byte[] msg="hello world".getBytes();
		DatagramPacket pkt=new DatagramPacket(msg,msg.length,multicast_iaddr,DATA_PORT);	
		System.out.println("GKDC["+gkdc_addr+"]: send to "+pkt.getAddress().getHostAddress()+":"+pkt.getPort()+": "+new String(pkt.getData(),0,pkt.getLength()));
		data_sock.send(pkt);
                */
                
                
		byte[] buf=new byte[1024];
		DatagramPacket pkt=new DatagramPacket(buf,buf.length);
		//System.out.println("Node["+ip_addr+"]: listening");
		data_sock.receive(pkt);
		System.out.println( ANSI_RED + "Node["+node_addr+"]: received: "+new String(pkt.getData(),0,pkt.getLength()) + ANSI_RESET);

		//management_sock.receive(pkt);
		//System.out.println("Node["+node_addr+"]: received: "+new String(pkt.getData(),0,pkt.getLength()));
                
                
                //-------------------------------------------Mandar la richiesta di join-----------------------------
                // per il momento tutti i nodi inviano dopo 15 secodni la stessa richiesta nello stesso slot temporale
                 Thread.sleep(15000);
                //System.out.println ("Ip4layer " + ip.toString());//DEBUG
                JoinReq richiesta = new JoinReq(4,ip.toString());
                byte[] msgToSend = richiesta.toString().getBytes();
                InetAddress point_addr = Inet4Address.getByName("10.1.1.254") ; 
                /*
                //================solo per prova da modificare=========================
                int randomNum = ThreadLocalRandom.current().nextInt(0, 5 + 1);
                Thread.sleep(randomNum * 1000);
                timeSlots tS = new timeSlots();
                //=====================================================================
                */             
                //messo pure qua lo slot temporale per provare
                DatagramPacket pkt2 =new DatagramPacket(msgToSend,msgToSend.length, point_addr ,GKDC.MANAGEMENT_PORT);
                System.out.println( ANSI_RED + "Send to : " + point_addr.toString() + ANSI_BLUE +"\nData " + new String(pkt2.getData())/*+ "in the time slot "+ tS.getValue()*/ + ANSI_RESET);
                management_sock.send(pkt2);
                
                //-----------------------------------------------------------------------------------
                
                
                
}

}
