package controller;

import DAO.firstLevelDivisionQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import DAO.countryQuery;
import model.firstLevelDivision;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class addCustomerViewController {

    @FXML private TextField addCustomerID;
    @FXML private  TextField addCustomerName;
    @FXML private  TextField addCustomerPhoneNumber;
    @FXML private  TextField addCustomerPostalCode;
    @FXML private  ComboBox<String> addCustomerCountry;
    @FXML private  ComboBox<String> addCustomerFirstLevel;
    @FXML private  TextField addCustomerAddress;
    @FXML private Button addCustomerSave;
    @FXML private Button addCustomerExit;


    public void addCustomerSaveAction(ActionEvent actionEvent) {
        System.out.println("Add Customer Save button pressed");
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

    private  ObservableList<String> countryCode1Divisions = FXCollections.observableArrayList();
    private  ObservableList<String> countryCode2Divisions = FXCollections.observableArrayList();
    private  ObservableList<String> countryCode3Divisions = FXCollections.observableArrayList();



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

                // Populate addCustomerFirstLevel based on the new country selected
                // Populate addCustomerFirstLevel using ObservableLists
                switch (newValue) {
                    case "U.S" -> addCustomerFirstLevel.setItems(countryCode1Divisions);
                    case "Canada" -> addCustomerFirstLevel.setItems(countryCode2Divisions);
                    case "UK" -> addCustomerFirstLevel.setItems(countryCode3Divisions);
                }
                    
                System.out.println(countryCode1Divisions);
                
            } else {
                addCustomerFirstLevel.setDisable(true); // Disable ComboBox if no country selected
                addCustomerFirstLevel.getItems().clear();
            }
        });
    }

}
