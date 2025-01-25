package controller;

import DAO.firstLevelDivisionQuery;
import javafx.collections.FXCollections;
import java.sql.PreparedStatement;
import java.sql.Connection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import DAO.countryQuery;
import main.JDBC;
import model.firstLevelDivision;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import DAO.customerQuery;
import javafx.collections.ObservableList;

public class addCustomerViewController {
    private final Connection connection = JDBC.connection; // Ensure connection reference

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
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding customer to database", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void addCustomerExitAction(ActionEvent actionEvent) {
        System.out.println("Add Customer Exit button pressed");

        //notify user if the want to exit the window
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Close Window?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("EXIT");
        alert.showAndWait();

        //if user selects YES customer view is closed and main view opened. if NO is selected nothing is done.
        if (alert.getResult() == ButtonType.YES) {
            try {
                Stage thisStage = (Stage) addCustomerExit.getScene().getWindow();
                thisStage.close();

                // Load and show the mainView.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resource/view/mainView.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("DB Client App");
                stage.setScene(scene);
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    
    /**here is the code that is initialized upon view being opened
     * <p><b>Runtime Error</b></n>
     * I had a very hard time coding the division menu to change when a different country was selected took a very long time
     * the code can still be improved by not hardcoding the country name in the switch statement but as it is working right now
     * any change might break the code.
     * </p>*/
    public void initialize() throws SQLException {

        System.out.println("Add Customer View initialized");

        // Populate ObservableLists for each country
        firstLevelDivisionQuery.getAllFirstLevelDivisions().forEach(division -> {
            if (division.getCountryID() == 1) {
                System.out.println(division.getDivisionName());
                countryCode1Divisions.add(division.getDivisionName());
            } else if (division.getCountryID() == 2) {
                countryCode2Divisions.add(division.getDivisionName());
            } else if (division.getCountryID() == 3) {
                countryCode3Divisions.add(division.getDivisionName());
            }
        });

        // Populate addCustomerCountry ComboBox with country names
        addCustomerCountry.getItems().clear();
        countryQuery.getAllCountries().forEach(country -> addCustomerCountry.getItems().add(country.getCountryName()));

        // Add a listener to detect when a country is selected in addCustomerCountry
        addCustomerCountry.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                addCustomerFirstLevel.setDisable(false); // Enable ComboBox
                addCustomerFirstLevel.getItems().clear(); // Clear any previous data

                // Populate addCustomerFirstLevel based on the country being used
                switch (newValue) {
                    case "U.S" -> addCustomerFirstLevel.setItems(countryCode1Divisions);
                    case "UK" -> addCustomerFirstLevel.setItems(countryCode2Divisions);
                    case "Canada" -> addCustomerFirstLevel.setItems(countryCode3Divisions);
                }
                    
                System.out.println(countryCode1Divisions);
                
            } else {
                addCustomerFirstLevel.setDisable(true); // Disable ComboBox if no country selected
                addCustomerFirstLevel.getItems().clear();
            }
        });
    }

}
