package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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

        mainViewController.showMainView();

    }

    public void editAppointmentSaveAction(ActionEvent actionEvent) throws IOException {
        System.out.println("Edit Appointment Save button pressed");

        mainViewController.showMainView();
    }
}
