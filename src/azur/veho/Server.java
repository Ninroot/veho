package azur.veho;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

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
		
		System.out.println("SERVER : Waiting for request...");
		RequestFile requestFile = RequestFile.listenForRequest(in, out);
		
		//Ask user. Redo it in a better way
//		requestFile.setAccepted(true);
		ThreadUtils.runAndWait(new Runnable(){
			public void run() {				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Request Confirmation");
				alert.setHeaderText("Would you like those file(s) ?");
				alert.setContentText(requestFile.getFileList().toString());
				
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					requestFile.setAccepted(true);
				} else {
					requestFile.setAccepted(false);
				}
			}
			
		});		
		
		//wait for response
		
		System.out.println("SERVER : Sending acceptation... : "+requestFile.getAccepted());
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
//				System.out.println("[SERVER] <--- DOWNLOAD START :" + file.getName() + " size:" + file.length());
				fos = new FileOutputStream(dstDirectory + file.getName());
				bos = new BufferedOutputStream(fos);

				int curPos = 0;
				while (curPos < file.length()) {
					int readThisTime = (int) Math.min(buff, file.length() - curPos);
					in.read(mybytearray, 0, readThisTime);
					bos.write(mybytearray, 0, readThisTime);
					bos.flush();

					curPos += readThisTime;
//					System.out.println("[SERVER] readThisTime:"+readThisTime+" curPos:"+curPos+ " file.length-curPos:"+(file.length() - curPos));
				}
				System.out.println("[SERVER] |<-- DOWNLOAD STOP :" + file.getName() + " size:" + file.length());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}