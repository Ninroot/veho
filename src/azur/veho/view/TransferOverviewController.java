package azur.veho.view;

import java.net.URL;
import java.util.ResourceBundle;

import azur.veho.Machine;
import azur.veho.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn.CellDataFeatures;

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
    
    private Main main;
    
    public TransferOverviewController() {
    	
	}
    
    @FXML
    public void initialize(URL arg0, ResourceBundle arg1) {
    	
    }

//    public void changeText(ActionEvent event) {
//    	System.out.println("Nouille");
//    }
    
    public void setMain(Main main) {
    	this.main = main;
    	
    	chooseMachine.setItems(main.getMachineList());
    }
}
