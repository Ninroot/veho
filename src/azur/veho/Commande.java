package azur.veho;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public abstract class Commande {

	/**
	 * Execute a ARP request
	 * @return raw ARP result
	 */
	public static String arp() {
    	Runtime rt = Runtime.getRuntime();
		try {
			Process process = rt.exec("arp -a");
			InputStreamReader is = new InputStreamReader(process.getInputStream());
			return inputStreamReader2String(is);
			
		} catch (IOException e) {
			return e.getMessage();
		}
		
    }
	
	public static void getBroadcastAddress() {
		Enumeration<NetworkInterface> en;
		try {
			en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface ni = en.nextElement();
				System.out.println("Display Name = " + ni.getDisplayName());
				
				List<InterfaceAddress> list = ni.getInterfaceAddresses();
				Iterator<InterfaceAddress> it = list.iterator();
				
				while (it.hasNext()) {
					InterfaceAddress ia = it.next();
					System.out.println("Broadcast = " + ia.getBroadcast().getHostAddress());
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String pigAll() {
    	Runtime rt = Runtime.getRuntime();
		try {
			Process process = rt.exec("arp -a");
			InputStreamReader is = new InputStreamReader(process.getInputStream());
			return inputStreamReader2String(is);
			
		} catch (IOException e) {
			return e.getMessage();
		}
		
    }
	
	/**
     * Transform InputStreamReader to String
	 * src: http://www.java2s.com/Code/Java/File-Input-Output/ConvertInputStreamtoString.htm
	 * @param isr InputStreamReader
	 * @return String
	 * @throws IOException
	 */
	private static String inputStreamReader2String(InputStreamReader isr) throws IOException {
		BufferedReader reader = new BufferedReader(isr);
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
	    }
        
	    isr.close();
	    return sb.toString();
	}
}
