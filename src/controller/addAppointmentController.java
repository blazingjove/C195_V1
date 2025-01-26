package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.JDBC;
import model.contacts;
import DAO.contactQuery;
import DAO.appointmentQuery;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static DAO.appointmentQuery.appointmentIDNext;

public class addAppointmentController {

    @FXML private Button appointmentSave;
    @FXML private TextField addAppointmentUserID;
    @FXML private TextField addAppointmentID;
    @FXML private TextField addAppointmentTitle;
    @FXML private TextField addAppointmentDescription;
    @FXML private TextField addAppointmentLocation;
    @FXML private TextField addAppointmentType;
    @FXML private ComboBox<String> addAppointmentContact;
    @FXML private TextField addAppointmentCustomerID;
    @FXML private Button appointmentExit;


    //TODO need to set the time saved to be based of menus

    //TODO populate the time selection 15 minute increments

    /**Captures all user inputs and saves the to the sql database
     * @throws RuntimeException error catching*/
    public void appointmentSaveAction() {
        System.out.println("Appointment save button pressed");

        try {
            // Extract input values
            int appointmentID = Integer.parseInt(addAppointmentID.getText());
            String title = addAppointmentTitle.getText();
            String description = addAppointmentDescription.getText();
            String location = addAppointmentLocation.getText();
            String type = addAppointmentType.getText();
            String contactName = addAppointmentContact.getSelectionModel().getSelectedItem();
            int customerID = Integer.parseInt(addAppointmentCustomerID.getText());
            int userID = Integer.parseInt(addAppointmentUserID.getText());

            // Validate required fields
            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty() || contactName == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "All fields must be filled!", ButtonType.OK);
                alert.setTitle("Validation Error");
                alert.showAndWait();
                return;
            }

            // Get contact ID based on contact name
            int contactID = 0;
            for (contacts contact : contactQuery.getContacts()) {
                if (contact.getContactName().equals(contactName)) {
                    contactID = contact.getID();
                    break;
                }
            }
            if (contactID == 0) {
                throw new SQLException("Contact not found");
            }

            // Insert into database
            boolean success = appointmentQuery.createAppointment(appointmentID, title, description, location, type, customerID, userID, contactID);

            // Notify the user
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment saved successfully!", ButtonType.OK);
                alert.setTitle("Success");
                alert.showAndWait();

                // Close current window (addAppointmentView)
                Stage thisStage = (Stage) appointmentSave.getScene().getWindow();
                thisStage.close();

                // Load and show the mainView.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resource/view/mainView.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("DB Client App");
                stage.setScene(scene);
                stage.show();


            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save the appointment.", ButtonType.OK);
                alert.setTitle("Error");
                alert.showAndWait();
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid number format in one or more fields.", ButtonType.OK);
            alert.setTitle("Input Error");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Database error occurred: " + e.getMessage(), ButtonType.OK);
            alert.setTitle("Database Error");
            alert.showAndWait();
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**Prompts user to select if they want to close the program.
     * <p><b>Future Improvements </b></n>
     * I would like to implement an exit button that checks if any of the fields have been changed and prompts user that
     * they will lose information if they choose to exit the view. Sort of a reminder that they will lose unsaved data.
     * </p>*/
    public void appointmentExitAction() {
        System.out.println("Appointment view exit button pressed");
        
        //notify user if the want to exit the window
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Close Window?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("EXIT");
        alert.showAndWait();
        
        //if user selects YES appointment view is closed and main view opened. if NO is selected nothing is done.
        if (alert.getResult() == ButtonType.YES) {
            try {
                Stage thisStage = (Stage) appointmentExit.getScene().getWindow();
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

    /**
     * Initialize populates the view with information such as dates and timeframes available for scheduling
     */
    public void initialize() throws SQLException {
        System.out.println("Add Appointment View initialized");

        // Populate next appointment ID
        addAppointmentID.setText(String.valueOf(appointmentIDNext()));

        //defining the two observable list that will be usd in lambda expression
        ObservableList<contacts> contactsObservableList = contactQuery.getContacts();
        ObservableList<String> allContactsNames = FXCollections.observableArrayList();

        // lambda #1
        contactsObservableList.forEach(contacts -> allContactsNames.add(contacts.getContactName()));

        //System.out.println(allContactsNames);
        addAppointmentContact.setItems(allContactsNames);

    }

}
