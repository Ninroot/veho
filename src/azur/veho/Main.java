package azur.veho;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		//ARP broadcast pour choper les ips
		//Application.launch(Main.class, (java.lang.String[])null);
		launch(args);
		//test1();
	}
	
	public static void test1() {
//		try {
//			ServerSocket socketTest = new ServerSocket(65535);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		Machine m = Machine.getMyMachine();
		System.out.println(m.getIpV3());
		System.out.println(m.getName());
		
		//Files tests
		ArrayList<File> fileList = new ArrayList<File>();
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

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent page = FXMLLoader.load(Main.class.getResource("menu.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("FXML is Simple");
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
		
	}
}
