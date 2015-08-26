package fwft.azur.azurCore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client implements Runnable {
	private int socketPort = 13267;
	private Request request;

	public Client(Request request) {
		this.request = request;
	}

	public void run() {
		try {
			Socket socketRequest = new Socket(request.getMachineDst().getIpV3(), socketPort);
			System.out.println("[CLIENT] Socket : " + socketRequest);

			ObjectOutputStream out = new ObjectOutputStream(socketRequest.getOutputStream());
			out.flush();

			ObjectInputStream in = new ObjectInputStream(socketRequest.getInputStream());

			out.writeObject(request);
			out.flush();
			System.out.println("[CLIENT] Request sent: "+request);
			
			System.out.println("[CLIENT] zZzz Waiting for request... ");
			request = Request.listenForRequest(in, out);
			System.out.println("[CLIENT] Request back received: "+request);
			if (request.getAccepted()) {
				System.out.println("[CLIENT] Request back ACCEPTED: "+request);
				
				int port=0;
				do {
					try {
						request = Request.listenForRequest(in, out);
						port = request.getPort();
						
						System.out.println("[CLIENT] Trying port n°: " + port);
						Socket socketFile = new Socket(request.getMachineDst().getIpV3(), port);
						System.out.println("[CLIENT] WORKS with port n°: " + port);
						
						request.setPortBool(true);
						out.writeObject(request);
						out.flush();
			        } catch (IOException ex) {
			        	System.out.println("[CLIENT] DOES NOT WORK port n°: " + port);
			        	out.writeObject(request);
						out.flush();
			            continue;
			        }
				} while(!request.isPortBool());
				
				sendFile(request.getFileList(), in, out);				
			}
			else {
				System.out.println("[CLIENT] Request back DECLINED: "+request);
			}
			

			in.close();
			out.close();
			socketRequest.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
				
				System.out.println("[CLIENT] ---> UPLOAD START :"+file.getName()+" size:"+file.length());

				int curPos = 0;
				while (curPos < file.length()) {
					int readThisTime = (int) Math.min(buff, file.length()-curPos);
					bis.read(mybytearray, 0, readThisTime);
					out.write(mybytearray, 0, readThisTime);
					out.flush();
					
					//System.out.println("[CLIENT] readThisTime:"+readThisTime+" curPos:"+curPos+ " file.length-curPos:"+(file.length() - curPos));

					curPos += readThisTime;
				}
				System.out.println("[CLIENT] -->| UPLOAD STOP :"+file.getName()+" size:"+file.length());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}