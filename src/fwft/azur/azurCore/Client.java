package fwft.azur.azurCore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
			Socket socket = new Socket(request.getMachineDst().getIpV3(), socketPort);
			System.out.println("[CLIENT] Socket : " + socket);

			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();

			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			out.writeObject(request);
			out.flush();
			System.out.println("[CLIENT] Request sent: "+request);
			
			Request requestBack = Request.listenForRequest(in, out);
			System.out.println("[CLIENT] Request back received: "+requestBack);
			
			if (requestBack.getAccepted()) {
				System.out.println("[CLIENT] Request back ACCEPTED: "+requestBack);				
				sendFile(request.getFileList(), in, out);				
			}
			else {
				System.out.println("[CLIENT] Request back DECLINED: "+requestBack);
			}
			

			in.close();
			out.close();
			socket.close();
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