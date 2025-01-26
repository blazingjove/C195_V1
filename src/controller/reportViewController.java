package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class reportViewController {
    
    @FXML private Button reportViewExit;


    public void reportViewExitAction() {
        Stage stage = (Stage) reportViewExit.getScene().getWindow();
        stage.close();
    }
}
