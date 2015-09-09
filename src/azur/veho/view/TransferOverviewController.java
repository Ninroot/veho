package azur.veho.view;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import azur.veho.Machine;
import azur.veho.MainApp;
import azur.veho.ServerRequestPort;

public class TransferOverviewController implements Initializable {

	@FXML
	private ChoiceBox<Machine> chooseMachine;
	@FXML
	private Button refreshMachine;
	@FXML
	private Button openFiles;
	@FXML
	private Button sendFiles;
	@FXML
	private Label transferLabel;

	private MainApp main;
	private ArrayList<File> fileList = new ArrayList<File>();
	private Machine myMachine, machineDst;

	public TransferOverviewController() {
		myMachine = Machine.getMyLocalMachine();
		Thread tServer = new Thread(new ServerRequestPort());
		tServer.start();
	}

	@FXML
	public void initialize(URL arg0, ResourceBundle arg1) {
		showFileTransfer(null);
		showMachineList();
		chooseMachine.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<Machine>() {
					@Override
					public void changed(ObservableValue<? extends Machine> om, Machine m, Machine nm) {
						machineDst = nm;
						if(nm != null)
							openFiles.setDisable(false);
					}
				});
	}

//	@FXML
//	private void handleChooseMachine() {
//		System.out.println("Noodle");
//		machineDst = chooseMachine.getValue();
//		if(machineDst != null) {
//			System.out.println(machineDst.getIpV3());
//			openFiles.setDisable(false);
//		}
//		else {
//			System.out.println("null ?");
//		}
//	}

	@FXML
	private void handleChooseFiles() {
		fileList.clear();	//against resending
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		List<File> fL = fileChooser.showOpenMultipleDialog(null);
		fileList.addAll(fL);	//cast List into ArrayList
		if(fileList != null) {
			sendFiles.setDisable(false);
		}
	}

	@FXML
	private void handleSendFiles() {
		if(fileList != null) {
			myMachine.send(machineDst, fileList);
		}
	}

	private void showMachineList() {
		chooseMachine.getItems().addAll(Machine.getMachineFromARP());
	}

	private void showFileTransfer(File file) {
		if(file != null)
		{
			transferLabel.setText("file transfer");    		
		}
		else {
			transferLabel.setText("no file...");
		}
	}

	public void setMain(MainApp main) {
		this.main = main;

		//chooseMachine.setItems(main.getMachineList());
	}
}
