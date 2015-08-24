package fwft.azur.azurCore;

import java.io.File;
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
	
	public Request(Machine machineSrc, Machine machineDst, ArrayList<File> fileList) {
		this.machineDst = machineDst;
		this.machineSrc = machineSrc;
		this.fileList = fileList;
		
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

}
