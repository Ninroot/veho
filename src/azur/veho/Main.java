package azur.veho;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("veho");
		
		initRootLayout();
	}
	
	public void initRootLayout() {
		try {
			Parent rootLayout = FXMLLoader.load(Main.class.getResource("view/RootLayout.fxml"));
			Scene scene = new Scene(rootLayout);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//ARP broadcast pour choper les ips
		launch(args);
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
}
