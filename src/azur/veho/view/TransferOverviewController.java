package azur.veho.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import azur.veho.Machine;
import azur.veho.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class TransferOverviewController implements Initializable {
	
	@FXML
	private ChoiceBox<Machine> chooseMachine;
//	@FXML
//	private ChoiceBox<String> chooseMachine;
    @FXML
    private Button refreshMachine;
    @FXML
    private Button openFiles;
    @FXML
    private Button sendFiles;
    @FXML
    private Label transferLabel;
    
    private Main main;
    
    public TransferOverviewController() {
    	
	}
    
    @FXML
    public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	showFileTransfer(null);
    	showMachineList();
//    	chooseMachine.getSelectionModel().selectedItemProperty().addListener(
//    			(observable, oldValue, newValue) -> showMachineList(newValue));
    	
    	
    }
    
    private void showMachineList() {
    	//Machine machine = new Machine(new SimpleStringProperty("Hello"), new SimpleStringProperty("0"));
    	//chooseMachine.getItems().add(machine);
    	//chooseMachine.setValue(machine);
    	//chooseMachine.getItems().add("Test");
    	chooseMachine.getItems().addAll(Machine.getMachineFromARP());
    }
    
//    private void showMachineList(Machine machine) {
//    	if (machine != null) {
//    		// ?
//    	}
//    	else {
//			
//		}
//    	
//    }
    
    private void showFileTransfer(File file) {
    	if(file != null)
    	{
    		transferLabel.setText("file transfer");    		
    	}
    	else {
    		transferLabel.setText("no file...");
		}
    }
    
    public void setMain(Main main) {
    	this.main = main;
    	
    	//chooseMachine.setItems(main.getMachineList());
    }
}
