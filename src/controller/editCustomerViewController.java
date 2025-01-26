package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

import static controller.mainViewController.showMainView;

public class editCustomerViewController {
    @FXML private TextField editCustomerID;
    @FXML private TextField editCustomerName;
    @FXML private TextField editCustomerPhoneNumber;
    @FXML private TextField editCustomerPostalCode;
    @FXML private ComboBox<String> editCustomerCountry;
    @FXML private ComboBox<String> editCustomerFirstLevel;
    @FXML private TextField editCustomerAddress;
    @FXML private Label editCustomerAddressFormat;
    @FXML private Button editCustomerSave;
    @FXML private Button editCustomerExit;

    public void editCustomerExitAction(ActionEvent actionEvent) throws IOException {
        System.out.println("Edit Customer Exit button pressed");

        //closes the edit customer view and opens main view
        showMainView();

    }

    public void editCustomerSaveAction(ActionEvent actionEvent) throws IOException {
        System.out.println("Customer Save button pressed");

        //close edit customer view and opens main view
        showMainView();

    }
}
