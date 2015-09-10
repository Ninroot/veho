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
		tServer.setDaemon(true);	//close the pss after closing the window...
		tServer.start();
	}

	@FXML
	public void initialize() {
		showFileTransfer(null);
		showMachineList();
		
		//Machine selection 
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

	@FXML
	private void handleRefreshMachine() {
		showMachineList();
	}

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
		chooseMachine.getItems().clear();
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
