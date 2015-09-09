package azur.veho;

import java.io.File;
import java.io.IOException;
import java.rmi.server.LoaderHandler;
import java.util.ArrayList;

import azur.veho.view.TransferOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private ObservableList<Machine> machineList = FXCollections.observableArrayList();
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("veho");
		
		initRootLayout();
		
		showTransferOverview();
	}
	
	public MainApp() {
		//machineList.addAll(Machine.getMachineFromARP());
	}
	
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			primaryStage.setMaxHeight(primaryStage.getHeight());
			primaryStage.setMaxWidth(3*primaryStage.getWidth());
			primaryStage.setMinHeight(primaryStage.getHeight());
			primaryStage.setMinWidth(0.5*primaryStage.getWidth());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showTransferOverview() {
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TransferOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(personOverview);
            
            TransferOverviewController controller = loader.getController();
            controller.setMain(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public static void main(String[] args) {
		test();
		//launch(args);
	}
	
	public static void test() {
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

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public ObservableList<Machine> getMachineList() {
		return machineList;
	}
}
