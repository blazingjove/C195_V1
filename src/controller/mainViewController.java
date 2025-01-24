package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.util.ResourceBundle;


public class mainViewController implements Initializable {


    @FXML private TableColumn appointmentID;
    @FXML private TableColumn appointmentTitle;
    @FXML private TableColumn appointmentDescription;
    @FXML private TableColumn appointmentLocation;
    @FXML private TableColumn appointmentType;
    @FXML private TableColumn appointmentStart;
    @FXML private TableColumn appointmentEnd;
    @FXML private TableColumn appointmentCustomerID;
    @FXML private TableColumn appointmentContactID;
    @FXML private TableColumn appointmentUserID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Main Controller initialized");


    }
}
