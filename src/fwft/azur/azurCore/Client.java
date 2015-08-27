package fwft.azur.azurCore;

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
//		
//		try {
//			Socket socketRequest = new Socket(request.getMachineDst().getIpV3(), socketPort);
//			System.out.println("[CLIENT] Socket : " + socketRequest);
//
//			ObjectOutputStream out = new ObjectOutputStream(socketRequest.getOutputStream());
//			out.flush();
//
//			ObjectInputStream in = new ObjectInputStream(socketRequest.getInputStream());
//
//			out.writeObject(request);
//			out.flush();
//			System.out.println("[CLIENT] Request sent: "+request);
//			
//			System.out.println("[CLIENT] zZzz Waiting for request... ");
//			request = RequestFile.listenForRequest(in, out);
//			System.out.println("[CLIENT] Request back received: "+request);
//			
//			if (request.getAccepted()) {
//				System.out.println("[CLIENT] Request back ACCEPTED: "+request);
//				
//				//Find a good port for files transfer
//				int port=0;
//				Socket socketFile = null;
//				do {
//					try {
//						request = RequestFile.listenForRequest(in, out);
//						port = request.getPort();
//						
//						System.out.println("[CLIENT] Trying port n°: " + port);
//						socketFile = new Socket(request.getMachineDst().getIpV3(), port);
//						System.out.println("[CLIENT] WORKS with port n°: " + port);
//						
//						request.setPortBool(true);
//						out.writeObject(request);
//						out.flush();
//			        } catch (IOException ex) {
//			        	System.out.println("[CLIENT] DOES NOT WORK port n°: " + port);
//			        	out.writeObject(request);
//						out.flush();
//			            continue;
//			        }
//				} while(!request.isPortBool());
//				
//				System.out.println("[CLIENT] START upload file(s)");
//				ObjectOutputStream outFile = new ObjectOutputStream(socketFile.getOutputStream());
//				outFile.flush();
//				
//				ObjectInputStream inFile = new ObjectInputStream(socketFile.getInputStream());
//				sendFile(request.getFileList(), inFile, outFile);
//				System.out.println("[CLIENT] STOP upload file(s)");
//				
//				inFile.close();
//				outFile.close();
//				socketFile.close();
//			}
//			else {
//				System.out.println("[CLIENT] Request back DECLINED: "+request);
//			}
//			
//
//			in.close();
//			out.close();
//			socketRequest.close();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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