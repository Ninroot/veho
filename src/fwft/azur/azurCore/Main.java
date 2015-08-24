package fwft.azur.azurCore;

import java.io.File;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		Machine m = new Machine("MyLocal", "127.0.0.1");
		ArrayList<File> fileList = new ArrayList<File>();
		
		//Files tests
		fileList.add(new  File("/Users/debec/Desktop/src/txt.txt"));
		fileList.add(new  File("/Users/debec/Desktop/src/big.html"));
		fileList.add(new  File("/Users/debec/Desktop/src/fire.png"));
		//fileList.add(new  File("/Users/debec/Desktop/src/txt2.txt"));
		//fileList.add(new  File("/Users/debec/Desktop/src/img.png"));
		//fileList.add(new  File("/Users/debec/Desktop/src/icon.gif"));
		//fileList.add(new  File("/Users/debec/Desktop/src/file.png"));
		//fileList.add(new  File("/Users/debec/Desktop/src/nouille.png"));
		
		Thread tServer = new Thread(new Server());
		tServer.start();
		
//		for (int i = 0; i < 3; i++) {
			m.send(m, fileList);
//			try {
//				Thread.sleep(1000);	//Long enough to finish the last one
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

}
