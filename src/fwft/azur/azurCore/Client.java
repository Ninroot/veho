package fwft.azur.azurCore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;

public class Client implements Runnable {
	private int socketPort = 13267;
	private Request request;

	public Client(Request request) {
		this.request = request;
	}

	public void run() {
		try {
			Socket socket = new Socket(request.getMachineDst().getIpV3(),
					socketPort);
			System.out.println("[CLIENT] Socket client: " + socket);

			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
			out.flush();

			ObjectInputStream in = new ObjectInputStream(
					socket.getInputStream());
			System.out.println("[CLIENT] Client a cree les flux");

			out.writeObject(request);
			out.flush();
			System.out.println("[CLIENT] Donnees emises");

			sendFile(request.getFileList(), in, out);

			// Object objetRecu = in.readObject();
			// int[] tableauRecu = (int[]) objetRecu;
			//
			// System.out.println("Client recoit: " +
			// Arrays.toString(tableauRecu));

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

	private void sendFile(ArrayList<File> fileList, ObjectInputStream in,
			ObjectOutputStream out) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		for (File file : fileList) {
			try {
				// byte [] mybytearray = new byte [(int)file.length()];
				byte[] mybytearray = new byte[300000];
				// byte [] mybytearray = Files.readAllBytes(file.toPath());
				System.out.println("[CLIENT] --> upload :" + file.getName()
						+ " size:" + file.length() + " with buffer:"
						+ mybytearray.length);
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);

				int curPos = 0;
				while (curPos < file.length()) {
					int readThisTime = (int) Math.min(1024, file.length()-curPos);
					bis.read(mybytearray, curPos, readThisTime);
					out.write(mybytearray, curPos, readThisTime);
					out.flush();

					curPos += readThisTime;
				}
				System.out.println("[CLIENT] uploaded");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// public void send() {
	// //ECHANGER SERVEUR ET CLIENT
	// int FILE_SIZE = 20000; // file size temporary hard coded, should bigger
	// than the file to be downloaded
	//
	// FileInputStream fis = null;
	// BufferedInputStream bis = null;
	// OutputStream os = null;
	//
	// Socket sock = null;
	// try {
	// sock = new Socket(machineDst.getIpV3(), socketPort);
	// System.out.println("[SEND] Connecting...");
	//
	// byte [] mybytearray = new byte [(int)fileList.get(0).length()];
	// System.out.println("Sending " + fileList.get(0).getName() + "(" +
	// mybytearray.length + " bytes)");
	// fis = new FileInputStream(fileList.get(0));
	// bis = new BufferedInputStream(fis);
	// bis.read(mybytearray,0,mybytearray.length);
	// os = sock.getOutputStream();
	// os.write(mybytearray,0,mybytearray.length);
	// os.flush();
	// System.out.println("Done.");
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// finally {
	// try {
	// if (bis != null) bis.close();
	// if (os != null) os.close();
	// if (sock != null) sock.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }

}