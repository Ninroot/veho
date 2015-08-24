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

public class Server implements Runnable {
	private int socketPort;
	private String dstDirectory;

	public Server() {
		this.socketPort = 13267;
		this.dstDirectory = "/Users/debec/Desktop/dst/";
	}

	public Server(int socketPort) {
		this.socketPort = socketPort;
	}

	public void run() {
		try {
			ServerSocket s = new ServerSocket(socketPort);
			System.out.println("[SERVER] Socket serveur: " + s);

			Socket soc = s.accept();
			System.out.println("[SERVER] Connexion accepted: " + soc);

			ObjectOutputStream out = new ObjectOutputStream(
					soc.getOutputStream());
			out.flush();

			ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
			System.out.println("[SERVER] Creation du flux");

			// int[] tableauAEmettre = {7, 8, 9};
			//
			// out.writeObject(tableauAEmettre);
			// out.flush();
			//
			// System.out.println("Serveur: donnees emises");

			Object objetRecu = in.readObject();
			Request request = (Request) objetRecu;

			System.out.println("[SERVER] Recoit la request: " + request);
			getFile(request.getFileList(), in, out);

			in.close();
			out.close();
			soc.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getFile(ArrayList<File> fileList, ObjectInputStream in, ObjectOutputStream out) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		int buff = 1024; //why no more ?

		for (File file : fileList) {
			try {
				byte [] mybytearray = new byte [buff];
				System.out.println("[SERVER] <-- download file :" + file.getName() + " size:" + file.length() + " with buffer:" + mybytearray.length);
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// public void receive() {
	// String FILE_TO_RECEIVED = dstDirectory;
	// int bytesRead;
	// int current = 0;
	// FileOutputStream fos = null;
	// BufferedOutputStream bos = null;
	// ServerSocket servsock = null;
	// Socket sock = null;
	//
	// try {
	// servsock = new ServerSocket(socketPort);
	//
	// while (true) {
	// System.out.println("[RECEIVE] Waiting...");
	// sock = servsock.accept();
	// System.out.println("[RECEIVE] Accepted connection : " + sock);
	//
	// // receive file
	// byte [] mybytearray = new byte [200000];
	// InputStream is = sock.getInputStream();
	// fos = new FileOutputStream(FILE_TO_RECEIVED);
	// bos = new BufferedOutputStream(fos);
	// bytesRead = is.read(mybytearray,0,mybytearray.length);
	// current = bytesRead;
	//
	// do {
	// bytesRead =
	// is.read(mybytearray, current, (mybytearray.length-current));
	// if(bytesRead >= 0) current += bytesRead;
	// } while(bytesRead > -1);
	//
	// bos.write(mybytearray, 0 , current);
	// bos.flush();
	// System.out.println("File " + FILE_TO_RECEIVED + " downloaded (" + current
	// + " bytes read)");
	//
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// finally {
	// try {
	// if (fos != null) fos.close();
	// if (bos != null) bos.close();
	// if (sock!=null) sock.close();
	// if (servsock != null) servsock.close();
	// } catch (IOException e) {
	// e.printStackTrace(); }
	// }
	//
	// }
}