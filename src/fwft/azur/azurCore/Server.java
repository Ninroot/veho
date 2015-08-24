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

			ObjectOutputStream out = new ObjectOutputStream(soc.getOutputStream());
			out.flush();

			ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
			System.out.println("[SERVER] Creation du flux");

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
				System.out.println("[SERVER] <--- DOWNLOAD START :" + file.getName() + " size:" + file.length());
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