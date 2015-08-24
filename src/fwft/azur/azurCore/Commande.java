package fwft.azur.azurCore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
