package azur.veho;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.beans.property.SimpleStringProperty;

public class ClientRequestPort implements Runnable{
	private int socketPort = 13267;
	private RequestPort requestPort;
	private RequestFile requestFile;

	public ClientRequestPort(RequestPort requestPort, RequestFile requestFile) {
		this.requestPort = requestPort;
		this.requestFile = requestFile;
		System.out.println("CLIENT REQUEST PORT");
	}

	@Override
	public void run() {
		Socket socketRequestPort = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;

		try {
			socketRequestPort = new Socket(requestPort.getMachineDst().getIpV3(), socketPort);
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
			System.out.println("WTF ????????????");
			e.printStackTrace();
		}

		System.out.println("Finding a socket.......................................................");
		Socket socketFile = getSocketFile(in, out);

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

		System.out.println("SOCKET FILE : "+socketFile.getPort());
		Thread tClient = new Thread(new Client(requestFile, socketFile));
		tClient.start();
	}

	public Socket getSocketFile(ObjectInputStream in, ObjectOutputStream out) {
		try {
			//Starting negotiation
			System.out.println("Starting negotiation");
			out.writeObject(requestPort);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Socket socketFile = null;
		
		do {
			try {
				requestPort = RequestPort.listenForRequestPort(in, out);
				socketFile = new Socket(requestPort.getMachineDst().getIpV3(), requestPort.getPort());
				requestPort.setAccepted(true);

			} catch (IOException e) {
				//requestPort.getAccepted() still false
				e.printStackTrace();
			} finally {
				try {
					out.writeObject(requestPort);
					out.flush();				
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} while(!requestPort.getAccepted());
		
		return socketFile;
	}

	public int getSocketPort() {
		return socketPort;
	}

	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}

	public RequestPort getRequestPort() {
		return requestPort;
	}

	public void setRequestPort(RequestPort requestPort) {
		this.requestPort = requestPort;
	}

}
