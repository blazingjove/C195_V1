package controller;

import DAO.countryQuery;
import DAO.firstLevelDivisionQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.customers;

import java.io.IOException;
import java.sql.SQLException;

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
    @FXML private Button editCustomerExit;

    //initializing the list that will hold the First level divisions
    private final ObservableList<String> countryCode1Divisions = FXCollections.observableArrayList();
    private final ObservableList<String> countryCode2Divisions = FXCollections.observableArrayList();
    private final ObservableList<String> countryCode3Divisions = FXCollections.observableArrayList();
    private customers selectedCustomer;

    /** closes the view and doesn't retain any of the information */
    public void editCustomerExitAction() throws IOException {
        System.out.println("Edit Customer Exit button pressed");

        //close the edit customer view
        editCustomerExit.getScene().getWindow().hide();

        //closes the edit customer view and opens main view
        showMainView();

    }

    public void editCustomerSaveAction() throws IOException {
        System.out.println("Customer Save button pressed");

        //close the edit customer view
        editCustomerExit.getScene().getWindow().hide();

        //close edit customer view and opens main view
        showMainView();

    }

    public void initialize() throws SQLException {
        System.out.println("Edit Customer View initialized");

        // Populate ObservableLists for each country
        firstLevelDivisionQuery.getAllFirstLevelDivisions().forEach(division -> {
            if (division.getCountryID() == 1) {
                countryCode1Divisions.add(division.getDivisionName());
            } else if (division.getCountryID() == 2) {
                countryCode2Divisions.add(division.getDivisionName());
            } else if (division.getCountryID() == 3) {
                countryCode3Divisions.add(division.getDivisionName());
            }
        });

        // Populate addCustomerCountry ComboBox with country names
        countryQuery.getAllCountries().forEach(country -> editCustomerCountry.getItems().add(country.getCountryName()));

        // Add a listener to detect when a country is selected in addCustomerCountry
        editCustomerCountry.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                editCustomerFirstLevel.setDisable(false); // Enable ComboBox
                //addCustomerFirstLevel.getItems().clear(); // Clear any previous data

                // Populate addCustomerFirstLevel based on the country being used
                switch (newValue) {
                    case "U.S" -> {
                        editCustomerFirstLevel.setItems(countryCode1Divisions);
                        editCustomerAddressFormat.setText("Address Format: 123 ABC Street, White Plains");
                    }
                    case "UK" -> {
                        editCustomerFirstLevel.setItems(countryCode2Divisions);
                        editCustomerAddressFormat.setText("Address Format: 123 ABC Street, Greenwich, London");
                    }
                    case "Canada" -> {
                        editCustomerFirstLevel.setItems(countryCode3Divisions);
                        editCustomerAddressFormat.setText("Address Format: 123 ABC Street, Newmarket");
                    }
                }
                //System.out.println(countryCode1Divisions);

            }
        });

    }
    /**Uses method below to pull information from selected item into the edit customer view*/
    public void setCustomerData(customers selectedCustomer) {
        this.selectedCustomer = selectedCustomer;

        displaySelectedCustomerData();
    }

    private void displaySelectedCustomerData() {
        //populate the fields of view with selected customers data

        editCustomerID.setText(String.valueOf(selectedCustomer.getCustomerID()));
        editCustomerName.setText(selectedCustomer.getCustomerName());
        editCustomerPhoneNumber.setText(selectedCustomer.getCustomerPhone());
        editCustomerPostalCode.setText(selectedCustomer.getCustomerPostalCode());
        editCustomerAddress.setText(selectedCustomer.getCustomerAddress());

        //editCustomerCountry.setItems(selectedCustomer.getDivisionName());
        //editCustomerFirstLevel.setItems(selectedCustomer.getCustomerDivisionID());
    }
}
