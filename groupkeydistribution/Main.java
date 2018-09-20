package groupkeydistribution;


import java.io.IOException;
import it.unipr.netsec.ipstack.ip4.Ip4AddressPrefix;
import it.unipr.netsec.ipstack.ip4.Ip4Prefix;
import it.unipr.netsec.ipstack.util.IpAddressUtils;
import it.unipr.netsec.nemo.ip.IpLink;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 * Avvia la simulazione 
 * @author Luca Veltri
 */

public class Main {

	/**
         * 
         * @param args standars java args
         * @throws IOException eccezione lanciata
         * @throws InterruptedException eccezione lanciata
         * @throws NoSuchAlgorithmException eccezione lanciata
         * @throws Exception eccezione lanciata
         */
            public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException, Exception {
		
		IpLink network=new IpLink(new Ip4Prefix("10.1.1.0/24"));
		// GKDC address = 10.1.1.254
		Ip4AddressPrefix gkdc_addr=(Ip4AddressPrefix)IpAddressUtils.addressPrefix(network.getPrefix(),254);
	
                
                Scanner in = new Scanner(System.in);
                
                System.out.println("Ineserisci la profondità dell'albero da simulare: ");
                int depth = in.nextInt();
                System.out.println("Ineserisci in numero di nodi da simulare: ");
                int nodi = in.nextInt();
                
                
		for (int i = 0; i < nodi; i++) {
                    new Thread() {
                        @Override
			public void run() {
                            try {  
				new Node(network,gkdc_addr);
	   		    }
			    catch (IOException e) {
			} catch (InterruptedException | ClassNotFoundException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
				}
			}.start();
			Thread.sleep(100);
		}	
		
		GKDC gkdc = new GKDC(network,gkdc_addr,depth);
	}
}
