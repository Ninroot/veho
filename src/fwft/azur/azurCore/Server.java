package fwft.azur.azurCore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 
 * @author abyx
 *
 */
public class Server implements Runnable {
	private ServerSocket serverSocketFile;
	private String dstDirectory;

	public Server(ServerSocket serverSocketFile) {
		this.serverSocketFile = serverSocketFile;
		this.dstDirectory = "/Users/debec/Desktop/dst/";
	}

	public void run() {		
		Socket socketFile = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try {
			socketFile = serverSocketFile.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		
		RequestFile requestFile = RequestFile.listenForRequest(in, out);
		//Ask user
		requestFile.setAccepted(true);
		
		try {
			out.writeObject(requestFile);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (requestFile.getAccepted()) {
			getFile(requestFile.getFileList(), in, out);
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
		
//		boolean go = true;
//		while(go) {
//			try {
//				ServerSocket serverSocketRequest = new ServerSocket(socketPort);
//				System.out.println("[SERVER] Socket : " + serverSocketRequest);
//
//				Socket socketRequest = serverSocketRequest.accept();
//				System.out.println("[SERVER] Connection accepted: " + socketRequest);
//
//				ObjectOutputStream out = new ObjectOutputStream(socketRequest.getOutputStream());
//				out.flush();
//
//				ObjectInputStream in = new ObjectInputStream(socketRequest.getInputStream());
//
//				System.out.println("[SERVER] zZzz Waiting for request... ");
//				RequestFile request = RequestFile.listenForRequest(in, out);
//				System.out.println("[SERVER] Request received: "+request);
//
//				//Ask the user for request back
//				request.setAccepted(true);
//				out.writeObject(request);
//				out.flush();
//				System.out.println("[SERVER] Request back ACCEPTED and sent: "+request);
//
//				if(request.getAccepted()) {
//					//Find a good port for files transfer
//					int port=65535;
//					ServerSocket serverSocketFile = null;
//					do {
//						do {
//							try {
//								System.out.println("[SERVER] Trying port n°: " + port);
//								serverSocketFile = new ServerSocket(port);
//								System.out.println("[SERVER] WORKS with port n°: " + port);
//								
//								request.setPort(port);
//								RequestFile requestCopy = new RequestFile(request);	//Why is the copy necessary ?
//								out.writeObject(requestCopy);
//								out.flush();
//								
//								request = requestCopy;
//								request = RequestFile.listenForRequest(in, out);
//								break;
//							} catch (IOException ex) {
//								port--;
//								continue;
//							}
//						}while(port>0);
//					} while(!request.isPortBool());
//
//					System.out.println("[SERVER] START download file(s)");
//					Socket socketFile = serverSocketFile.accept();
//					
//					ObjectOutputStream outFile = new ObjectOutputStream(socketFile.getOutputStream());
//					out.flush();
//
//					ObjectInputStream inFile = new ObjectInputStream(socketFile.getInputStream());
//					
//					getFile(request.getFileList(), inFile, outFile);
//					System.out.println("[SERVER] STOP upload file(s)");
//					
//					inFile.close();
//					outFile.close();
//					socketFile.close();
//				}
//				else {
//					System.out.println("[SERVER] Request back DECLINED and sent: "+request);				
//				}				
//
//				in.close();
//				out.close();
//				socketRequest.close();
//				serverSocketRequest.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			go = false;	//TESTING ONLY
//		}
	}

	/**
	 * Get files from fileList trough ObjectInputStream
	 * @param fileList : list of file 
	 * @param in : input of the socket
	 * @param out : output of the socket 
	 */
	private void getFile(ArrayList<File> fileList, ObjectInputStream in, ObjectOutputStream out) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		int buff = 1024; //why no more ?

		for (File file : fileList) {
			try {
				byte [] mybytearray = new byte [buff];
				//System.out.println("[SERVER] <--- DOWNLOAD START :" + file.getName() + " size:" + file.length());
				fos = new FileOutputStream(dstDirectory + file.getName());
				bos = new BufferedOutputStream(fos);

				int curPos = 0;
				while (curPos < file.length()) {
					int readThisTime = (int) Math.min(buff, file.length() - curPos);
					in.read(mybytearray, 0, readThisTime);
					bos.write(mybytearray, 0, readThisTime);
					bos.flush();

					curPos += readThisTime;
					//System.out.println("[SERVER] readThisTime:"+readThisTime+" curPos:"+curPos+ " file.length-curPos:"+(file.length() - curPos));
				}
				System.out.println("[SERVER] |<-- DOWNLOAD STOP :" + file.getName() + " size:" + file.length());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}