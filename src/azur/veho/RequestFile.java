package azur.veho;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class RequestFile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6705608236667722680L;
	private ArrayList<File> fileList;
	private Boolean accepted;
	
	public RequestFile(ArrayList<File> fileList) {
		this.fileList = fileList;
		this.accepted = false;
	}
	
	protected static RequestFile listenForRequest(ObjectInputStream in, ObjectOutputStream out) {
		boolean go = true;

		while(go) {
			try {
				System.out.println("Waiting for request...");
				Object objetRecu = in.readObject();
				RequestFile request = (RequestFile) objetRecu;
				if(request != null) {
					System.out.println("Request received: " + request);
					return request;					
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		return null;
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
