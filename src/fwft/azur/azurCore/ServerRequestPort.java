package fwft.azur.azurCore;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRequestPort implements Runnable{
	private int socketPort = 13267;
	private RequestPort requestPort;
	//private String dstDirectory = "/Users/debec/Desktop/dst/";
	
	public ServerRequestPort() {
		this.requestPort = null;
	}
	
	@Override
	public void run() {
		ServerSocket serverSocketRequestPort = null;
		Socket socketRequestPort = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try {
			serverSocketRequestPort = new ServerSocket(socketPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			socketRequestPort = serverSocketRequestPort.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			out = new ObjectOutputStream(socketRequestPort.getOutputStream());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			in = new ObjectInputStream(socketRequestPort.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ServerSocket serverSocketFile = getServerSocketFile(in, out);
		
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
			socketRequestPort.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("SERVER SOCKET FILE : "+serverSocketFile);
	}
	
	public ServerSocket getServerSocketFile(ObjectInputStream in, ObjectOutputStream out) {
		//Starting negotiation
		requestPort = RequestPort.listenForRequestPort(in, out);
		
		requestPort.setPort(65535);
		ServerSocket serverSocketFile = null;
		
		do {
			do {				
				try {
					serverSocketFile = new ServerSocket(requestPort.getPort());
					
					out.writeObject(requestPort);
					out.flush();
					
					requestPort = RequestPort.listenForRequestPort(in, out);
					break;
				} catch (IOException e) {
					requestPort.setPort(requestPort.getPort()-1);
					e.printStackTrace();
				}
			} while(requestPort.getPort()>0);
		} while(!requestPort.getAccepted());
		
		return serverSocketFile;
	}

}
