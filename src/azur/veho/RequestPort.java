package azur.veho;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class RequestPort implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3677758931078147195L;
	private int port;
	private Boolean accepted;
	private Machine machineSrc;
	private Machine machineDst;
	
	public RequestPort(Machine machineSrc, Machine machineDst) {
		this.accepted = false;
		this.machineSrc = machineSrc;
		this.machineDst = machineDst;
	}
	
	protected static RequestPort listenForRequestPort(ObjectInputStream in, ObjectOutputStream out) {
		boolean go = true;

		while(go) {
			try {
				System.out.println("Waiting for request port...");
				Object objetRecu = in.readObject();
				RequestPort requestPort = (RequestPort) objetRecu;
				
				if(requestPort != null) {
					System.out.println("Request port received: " + requestPort);
					return requestPort;					
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		return null;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public Machine getMachineSrc() {
		return machineSrc;
	}

	public void setMachineSrc(Machine machineSrc) {
		this.machineSrc = machineSrc;
	}

	public Machine getMachineDst() {
		return machineDst;
	}

	public void setMachineDst(Machine machineDst) {
		this.machineDst = machineDst;
	}
}
