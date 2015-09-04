package fwft.azur.azurCore;

import java.net.URL;
import java.util.ResourceBundle;

import org.omg.CORBA.PUBLIC_MEMBER;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class MenuController implements Initializable {

    @FXML //  fx:id="refreshMachineButton"
    private Button refreshMachineButton; // Value injected by FXMLLoader


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert refreshMachineButton != null : "fx:id=\"refreshMachineButton\" was not injected: check your FXML file 'simple.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        refreshMachineButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("That was easy, wasn't it?");
            }
        });

    }
}
