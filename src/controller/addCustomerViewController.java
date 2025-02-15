package controller;

import DAO.firstLevelDivisionQuery;
import javafx.collections.FXCollections;
import java.sql.PreparedStatement;
import java.sql.Connection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import DAO.countryQuery;
import main.JDBC;
import model.firstLevelDivision;
import java.sql.SQLException;
import static controller.mainViewController.showMainView;


/**
 * Controller class for managing the "Add Customer" view.
 * Provides functionality to add new customers, validate input, and interact with the database.
 */
public class addCustomerViewController {
    private final Connection connection = JDBC.connection; // Ensure connection reference

    @FXML private Label addCustomerAddressFormat;
    @FXML private TextField addCustomerID;
    @FXML private  TextField addCustomerName;
    @FXML private  TextField addCustomerPhoneNumber;
    @FXML private  TextField addCustomerPostalCode;
    @FXML private  ComboBox<String> addCustomerCountry;
    @FXML private  ComboBox<String> addCustomerFirstLevel;
    @FXML private  TextField addCustomerAddress;
    @FXML private Button addCustomerSave;
    @FXML private Button addCustomerExit;

    //initializing the list that will hold the First level divisions
    private final ObservableList<String> countryCode1Divisions = FXCollections.observableArrayList();
    private final ObservableList<String> countryCode2Divisions = FXCollections.observableArrayList();
    private final ObservableList<String> countryCode3Divisions = FXCollections.observableArrayList();

    /**
     * Handles the Save action on the "Add Customer" form.
     * Collects user input, validates it, and inserts a new customer record into the database.
     * Shows an error alert if any fields are empty or the database insertion fails.
     *
     * @param actionEvent The action event triggered when the Save button is clicked.
     */
    public void addCustomerSaveAction(ActionEvent actionEvent) {
        System.out.println("Add Customer Save button pressed");

        // Validate inputs, check for non-null/ empty inputs
        if (addCustomerName.getText().isEmpty() || addCustomerPhoneNumber.getText().isEmpty() ||
                addCustomerPostalCode.getText().isEmpty() || addCustomerAddress.getText().isEmpty() ||
                addCustomerCountry.getValue() == null || addCustomerFirstLevel.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "All fields must be filled out!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try {
            // Create new customers object from form fields
            String name = addCustomerName.getText();
            String phone = addCustomerPhoneNumber.getText();
            String postalCode = addCustomerPostalCode.getText();
            String address = addCustomerAddress.getText();

            //loops through first level division data, Note: not very efficient
            int divisionID = -1;
            for (firstLevelDivision division : firstLevelDivisionQuery.getAllFirstLevelDivisions()) {
                if (division.getDivisionName().equals(addCustomerFirstLevel.getValue())) {
                    divisionID = division.getDivisionID();
                    break;
                }
            }
            if (divisionID == -1) {
                throw new Exception("Division not found");
            }

            // SQL query to insert customer record
            String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, divisionID);
            ps.executeUpdate();

            // Display success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer successfully added!", ButtonType.OK);
            alert.showAndWait();

            //method to show main view
            showMainView();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding customer to database", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Handles the Exit action for the "Add Customer" form.
     * Prompts the user with a confirmation dialog to confirm exiting.
     * If confirmed, the current view is closed, and the main view is displayed.
     */
    public void addCustomerExitAction() {
        System.out.println("Add Customer Exit button pressed");

        //notify user if the want to exit the window
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Close Window?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("EXIT");
        alert.showAndWait();

        //if user selects YES customer view is closed and main view opened. if NO is selected nothing is done.
        if (alert.getResult() == ButtonType.YES) {
            try {

                //method to show main view
                mainViewController.showMainView();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Initializes the "Add Customer" form when the view is opened.
     * Populates the country and division lists and sets up a listener for country selection.
     * <p><b>Runtime Error:</b></p>
     * Encountered issues with updating division lists on new country selection;
     * resolved by removing problematic clear() lines for ComboBox values.
     * <p><b>Note:</b> Further optimization could reduce the dependence on hardcoded country names.</p>
     *
     * lambda expression for Listener is selected in country ComboBox to populate division ID combobox with appropriate
     * first level divisions
     *
     * @throws SQLException If fetching data from the database fails.
     */
    public void initialize() throws SQLException {

        System.out.println("Add Customer View initialized");

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
        //addCustomerCountry.getItems().clear();
        countryQuery.getAllCountries().forEach(country -> addCustomerCountry.getItems().add(country.getCountryName()));

        // lambda expression number #2 Add a listener to detect when a country is selected in addCustomerCountry
        addCustomerCountry.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                addCustomerFirstLevel.setDisable(false); // Enable ComboBox
                //addCustomerFirstLevel.getItems().clear(); // Clear any previous data

                // Populate addCustomerFirstLevel based on the country being used
                switch (newValue) {
                    case "U.S" -> {
                        addCustomerFirstLevel.setItems(countryCode1Divisions);
                        addCustomerAddressFormat.setText("Address Format: 123 ABC Street, White Plains");
                    }
                    case "UK" -> {
                        addCustomerFirstLevel.setItems(countryCode2Divisions);
                        addCustomerAddressFormat.setText("Address Format: 123 ABC Street, Greenwich, London");
                    }
                    case "Canada" -> {
                        addCustomerFirstLevel.setItems(countryCode3Divisions);
                        addCustomerAddressFormat.setText("Address Format: 123 ABC Street, Newmarket");
                    }
                }
                //System.out.println(countryCode1Divisions);

            } else {
                //addCustomerFirstLevel.setDisable(true); // Disable ComboBox if no country selected
                addCustomerFirstLevel.getItems().clear();
            }
        });
    }
}
