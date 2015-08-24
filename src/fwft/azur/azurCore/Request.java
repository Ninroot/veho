package fwft.azur.azurCore;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6705608236667722680L;
	private Machine machineDst;
	private Machine machineSrc;
	private ArrayList<File> fileList;
	private Boolean accepted;
	
	public Request(Machine machineSrc, Machine machineDst, ArrayList<File> fileList) {
		this.machineDst = machineDst;
		this.machineSrc = machineSrc;
		this.fileList = fileList;
		this.accepted = false;
		
	}
	
	protected static Request listenForRequest(ObjectInputStream in, ObjectOutputStream out) {
		boolean go = true;

		while(go) {
			try {
				//System.out.println("zZzz Waiting for request... ");
				Object objetRecu = in.readObject();
				Request request = (Request) objetRecu;
				if(request != null) {
					//System.out.println("Request received: " + request);
					return request;					
				}

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		return null;
	}

	public Machine getMachineDst() {
		return machineDst;
	}

	public void setMachineDst(Machine machineDst) {
		this.machineDst = machineDst;
	}

	public Machine getMachineSrc() {
		return machineSrc;
	}

	public void setMachineSrc(Machine machineSrc) {
		this.machineSrc = machineSrc;
	}

	public ArrayList<File> getFileList() {
		return fileList;
	}

	public void setFileList(ArrayList<File> fileList) {
		this.fileList = fileList;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

}
