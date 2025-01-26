package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.JDBC;

import java.io.IOException;
import java.sql.SQLException;
import controller.mainViewController;

public class editAppointmentController {
    @FXML private TextField editAppointmentID;
    @FXML private TextField editAppointmentTitle;
    @FXML private TextField editAppointmentDescription;
    @FXML private TextField editAppointmentLocation;
    @FXML private TextField editAppointmentType;
    @FXML private ComboBox<String> editAppointmentContact;
    @FXML private TextField editAppointmentCustomerID;
    @FXML private TextField editAppointmentUserID;
    @FXML private Button editAppointmentExit;
    @FXML private Button editAppointmentSave;

    public void editAppointmentExitAction(ActionEvent actionEvent) throws IOException {
        System.out.println("Edit Appointment Exit button pressed");

        //notify user if they want to close the Edit Appointment view and open the Main view
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Close window?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("EXIT");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            mainViewController.showMainView();
        }

    }

    public void editAppointmentSaveAction(ActionEvent actionEvent) throws IOException {
        System.out.println("Edit Appointment Save button pressed");

        mainViewController.showMainView();
    }
}
