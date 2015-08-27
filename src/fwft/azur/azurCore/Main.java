package fwft.azur.azurCore;

import java.io.File;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		test1();
	}
	
	public static void test1() {
//		try {
//			ServerSocket socketTest = new ServerSocket(65535);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		Machine m = Machine.getMyMachine();
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
		
		Thread tServer = new Thread(new ServerRequestPort());
		tServer.start();
		
		for (int i=0; i<5; i++)
			m.send(m, fileList);
		
	}
}
