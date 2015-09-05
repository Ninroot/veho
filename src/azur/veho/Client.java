package azur.veho;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 
 * @author abyx
 *
 */
public class Client implements Runnable {
	private RequestFile requestFile;
	private Socket socketFile;

	public Client(RequestFile requestFile, Socket socketFile) {
		this.requestFile = requestFile;
		this.socketFile = socketFile;
	}

	public void run() {
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try {
			out = new ObjectOutputStream(socketFile.getOutputStream());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			in = new ObjectInputStream(socketFile.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			out.writeObject(requestFile);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		requestFile = RequestFile.listenForRequest(in, out);
		if(requestFile.getAccepted()) {
			sendFile(requestFile.getFileList(), in, out);
		}
		
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socketFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send files from fileList trough ObjectInputStream
	 * @param fileList : list of file 
	 * @param in : input of the socket
	 * @param out : output of the socket 
	 */
	private void sendFile(ArrayList<File> fileList, ObjectInputStream in, ObjectOutputStream out) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		int buff = 1024;

		for (File file : fileList) {
			try {				
				byte[] mybytearray = new byte[buff];
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				
				//System.out.println("[CLIENT] ---> UPLOAD START :"+file.getName()+" size:"+file.length());

				int curPos = 0;
				while (curPos < file.length()) {
					int readThisTime = (int) Math.min(buff, file.length()-curPos);
					bis.read(mybytearray, 0, readThisTime);
					out.write(mybytearray, 0, readThisTime);
					out.flush();
					
					//System.out.println("[CLIENT] readThisTime:"+readThisTime+" curPos:"+curPos+ " file.length-curPos:"+(file.length() - curPos));

					curPos += readThisTime;
				}
				//System.out.println("[CLIENT] -->| UPLOAD STOP :"+file.getName()+" size:"+file.length());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}