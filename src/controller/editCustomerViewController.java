package controller;

import DAO.countryQuery;
import DAO.firstLevelDivisionQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.JDBC;
import model.customers;

import java.io.IOException;
import java.sql.PreparedStatement;
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
    @FXML private Button editCustomerSave;

    //initializing the list that will hold the First level divisions
    private final ObservableList<String> countryCode1Divisions = FXCollections.observableArrayList();
    private final ObservableList<String> countryCode2Divisions = FXCollections.observableArrayList();
    private final ObservableList<String> countryCode3Divisions = FXCollections.observableArrayList();
    private customers selectedCustomer;

    /** closes the view and doesn't retain any of the information */
    public void editCustomerExitAction() throws IOException {
        System.out.println("Edit Customer Exit button pressed");

        //close the edit customer view after confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to close without saving?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirm Exit");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            editCustomerExit.getScene().getWindow().hide();

            //close edit customer view and opens main view
            showMainView();
        }

    }

    public void editCustomerSaveAction() throws IOException {
        System.out.println("Customer Save button pressed");

        try {
            // Retrieve form data
            int customerID = Integer.parseInt(editCustomerID.getText());
            String customerName = editCustomerName.getText();
            String customerPhone = editCustomerPhoneNumber.getText();
            String customerPostalCode = editCustomerPostalCode.getText();
            String customerAddress = editCustomerAddress.getText();
            String customerDivision = editCustomerFirstLevel.getValue();

            // Validate inputs (example: you can add validation logic here if needed)
            if (customerName.isBlank() || customerPhone.isBlank() || customerPostalCode.isBlank() || customerAddress.isBlank() || customerDivision == null) {
                throw new IllegalArgumentException("All fields must be filled in before saving.");
            }

            // Update the database
            boolean isUpdated;
            String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = (SELECT Division_ID FROM first_level_divisions WHERE Division = ?) WHERE Customer_ID = ?";
            try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
                ps.setString(1, customerName);
                ps.setString(2, customerAddress);
                ps.setString(3, customerPostalCode);
                ps.setString(4, customerPhone);
                ps.setString(5, customerDivision);
                ps.setInt(6, customerID);
                isUpdated = ps.executeUpdate() > 0;
            }

            // Log success or failure
            if (isUpdated) {
                System.out.println("The customer has been successfully updated in the database.");
            } else {
                System.out.println("Failed to update the customer in the database.");
            }

        } catch (SQLException e) {
            System.err.println("Error occurred while updating the customer in the database: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Input Validation Error: " + e.getMessage());
        }

        // Close the edit customer view
        editCustomerSave.getScene().getWindow().hide();

        // Close edit customer view and open the main view
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
    public void setCustomerData(customers selectedCustomer) throws SQLException {
        this.selectedCustomer = selectedCustomer;

        displaySelectedCustomerData();
    }

    private void displaySelectedCustomerData() throws SQLException {
        //populate the fields of view with selected customers data

        editCustomerID.setText(String.valueOf(selectedCustomer.getCustomerID()));
        editCustomerName.setText(selectedCustomer.getCustomerName());
        editCustomerPhoneNumber.setText(selectedCustomer.getCustomerPhone());
        editCustomerPostalCode.setText(selectedCustomer.getCustomerPostalCode());
        editCustomerAddress.setText(selectedCustomer.getCustomerAddress());

        // Set country and division based on division ID
        int divisionID = selectedCustomer.getCustomerDivisionID();
        String countryName = firstLevelDivisionQuery.getCountryNameByDivisionID(divisionID);
        editCustomerCountry.setValue(countryName);

        // Set the divisions for the selected country
        switch (countryName) {
            case "U.S" -> editCustomerFirstLevel.setItems(countryCode1Divisions);
            case "UK" -> editCustomerFirstLevel.setItems(countryCode2Divisions);
            case "Canada" -> editCustomerFirstLevel.setItems(countryCode3Divisions);
        }

        // Set the customer's specific division
        String divisionName = firstLevelDivisionQuery.getDivisionNameByDivisionID(divisionID);
        editCustomerFirstLevel.setValue(divisionName);

        //System.out.println("Country: "+editCustomerCountry.getValue());

        //System.out.println("State: "+editCustomerFirstLevel.getValue());
    }
}
