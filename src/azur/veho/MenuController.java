package azur.veho;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class MenuController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
//	@FXML;
//	private ChoiceBox chooseMachine;
    @FXML
    private Button refreshMachineButton;
    @FXML
    private Button openFilesButton;
    @FXML
    private Button sendFiles;

    public void changeText(ActionEvent event) {
    	System.out.println("Nouille");
    }
}
