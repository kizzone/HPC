package groupkeydistribution;


import java.io.IOException;

import it.unipr.netsec.ipstack.ip4.Ip4AddressPrefix;
import it.unipr.netsec.ipstack.ip4.Ip4Prefix;
import it.unipr.netsec.ipstack.util.IpAddressUtils;
import it.unipr.netsec.nemo.ip.IpLink;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

	public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException, Exception {
		
		IpLink network=new IpLink(new Ip4Prefix("10.1.1.0/24"));
		// GKDC address = 10.1.1.254
		Ip4AddressPrefix gkdc_addr=(Ip4AddressPrefix)IpAddressUtils.addressPrefix(network.getPrefix(),254);
		//modificato da 4 a 1 per motivi di "ordine"
		for (int i=0; i< 1; i++) {
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
		
		GKDC gkdc = new GKDC(network,gkdc_addr);
	}
}
