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
import DAO.*;
import model.customers;
import model.users;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class addAppointmentController {

    @FXML private DatePicker addAppointmentDate;
    @FXML private ComboBox<String > addAppointmentEndTime;
    @FXML private ComboBox<String> addAppointmentStartTime;
    @FXML private Button appointmentSave;
    @FXML private ComboBox<String> addAppointmentUserID;
    @FXML private TextField addAppointmentID;
    @FXML private TextField addAppointmentTitle;
    @FXML private TextField addAppointmentDescription;
    @FXML private TextField addAppointmentLocation;
    @FXML private TextField addAppointmentType;
    @FXML private ComboBox<String> addAppointmentContact;
    @FXML private ComboBox<String> addAppointmentCustomerID;
    @FXML private Button appointmentExit;

    /**Captures all user inputs and saves the to the sql database
     * @throws RuntimeException error catching*/
    public void appointmentSaveAction() {
        System.out.println("Appointment save button pressed");

        try {
            // Extract input values
            String title = addAppointmentTitle.getText();
            String description = addAppointmentDescription.getText();
            String location = addAppointmentLocation.getText();
            String type = addAppointmentType.getText();
            String contactName = addAppointmentContact.getSelectionModel().getSelectedItem();

            //streams to filter appropriate ID from input selected in combobox
            int customerID = customerQuery.getAllCustomers().stream()
                    .filter(customer -> customer.getCustomerName().equals(addAppointmentCustomerID.getSelectionModel().getSelectedItem()))
                    .findFirst()
                    .map(customers::getCustomerID)
                    .orElseThrow(() -> new RuntimeException("Customer not found in the database"));;
            int userID = userQuery.getAllUsers().stream()
                    .filter(user -> user.getUserName().equals(addAppointmentUserID.getSelectionModel().getSelectedItem()))
                    .findFirst()
                    .map(users::getUserID)
                    .orElseThrow(() -> new RuntimeException("User not found in the database"));;

            // Retrieve and parse start and end times from ComboBoxes
            String selectedStartTime = addAppointmentStartTime.getSelectionModel().getSelectedItem();
            String selectedEndTime = addAppointmentEndTime.getSelectionModel().getSelectedItem();

            // Validate time selections
            if (selectedStartTime == null || selectedEndTime == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Start time and end time must be selected.", ButtonType.OK);
                alert.setTitle("Time Validation Error");
                alert.showAndWait();
                return;
            }

            // join selected appointment date components
            LocalDateTime start = LocalDateTime.of(addAppointmentDate.getValue(),
                    java.time.LocalTime.of(
                            Integer.parseInt(selectedStartTime.split(":")[0]),
                            Integer.parseInt(selectedStartTime.split(":")[1])));
            LocalDateTime end = LocalDateTime.of(addAppointmentDate.getValue(),
                    java.time.LocalTime.of(
                            Integer.parseInt(selectedEndTime.split(":")[0]),
                            Integer.parseInt(selectedEndTime.split(":")[1])));

            // Ensure start and end times are in the future
            if (start.isBefore(LocalDateTime.now()) || end.isBefore(LocalDateTime.now())) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Date, start and end times must be in the future.", ButtonType.OK);
                alert.setTitle("Time Validation Error");
                alert.showAndWait();
                return;
            }

            // Validate that end time is after start time
            if (!end.isAfter(start)) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "End time must be greater than start time.", ButtonType.OK);
                alert.setTitle("Time Validation Error");
                alert.showAndWait();
                return;
            }

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

            System.out.println("Start time " + start);
            System.out.println("end time " + end);


            // Check for overlapping appointments
            String overlapQuery = "SELECT Appointment_ID FROM appointments WHERE Customer_ID = ? AND ((Start <= ? AND End > ?) OR (Start < ? AND End >= ?))";
            try (PreparedStatement psOverlap = JDBC.connection.prepareStatement(overlapQuery)) {
                psOverlap.setInt(1, customerID);
                psOverlap.setTimestamp(2, java.sql.Timestamp.valueOf(start));
                psOverlap.setTimestamp(3, java.sql.Timestamp.valueOf(start));
                psOverlap.setTimestamp(4, java.sql.Timestamp.valueOf(end));
                psOverlap.setTimestamp(5, java.sql.Timestamp.valueOf(end));
                if (psOverlap.executeQuery().next()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment time overlaps with selected customers existing appointment. \n please select a different time", ButtonType.OK);
                    alert.setTitle("Time Conflict");
                    alert.showAndWait();
                    return;
                }
            }

            // Insert into database
            boolean success;
            String sqlInsert = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = JDBC.connection.prepareStatement(sqlInsert)) {
                ps.setString(1, title);
                ps.setString(2, description);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setTimestamp(5, java.sql.Timestamp.valueOf(start));
                ps.setTimestamp(6, java.sql.Timestamp.valueOf(end));
                ps.setInt(7, customerID);
                ps.setInt(8, userID);
                ps.setInt(9, contactID);
                success = ps.executeUpdate() > 0;
            }

            // Notify the user upon success
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment saved successfully!", ButtonType.OK);
                alert.setTitle("Success");
                alert.showAndWait();

                //close current view
                //appointmentSave.getScene().getWindow().hide();

                // Load and show the mainView.fxml
                mainViewController.showMainView();
                
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save the appointment.", ButtonType.OK);
                alert.setTitle("Error");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid format in one or more fields.", ButtonType.OK);
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
                // Open the main view
                mainViewController.showMainView();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Initialize populates the view with information such as dates and timeframes available for scheduling.
     *
     * <p><b>Purpose of Lambda Expressions:</b></p>
     * <ul>
     *     <li>The first lambda expression extracts contact names and adds them to the {@code allContactsNames} list.</li>
     *     <li>The second lambda expression retrieves customer names and adds them to the {@code customerNames} list.</li>
     *     <li>The third lambda expression retrieves usernames and adds them to the {@code userNames} list.</li>
     * </ul>
     */
    public void initialize() throws SQLException {
        System.out.println("Add Appointment View initialized");

        //defining the two observable list that will be usd in lambda expression populating combo box
        ObservableList<contacts> contactsObservableList = contactQuery.getContacts();
        ObservableList<String> allContactsNames = FXCollections.observableArrayList();
        // lambda #1
        contactsObservableList.forEach(contacts -> allContactsNames.add(contacts.getContactName()));
        //System.out.println(allContactsNames);
        addAppointmentContact.setItems(allContactsNames);

        //lambda #2
        //defining the list to hold customer Names
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        // retrieves customer names
        customerQuery.getAllCustomers().forEach(customer -> customerNames.add(customer.getCustomerName()));
        addAppointmentCustomerID.setItems(customerNames);

        //lambda #3
        //defining the list to hold user's names
        ObservableList<String> userNames = FXCollections.observableArrayList();
        userQuery.getAllUsers().forEach(users -> userNames.add(users.getUserName()));
        addAppointmentUserID.setItems(userNames);

        //defining start and end time combo boxes

        // Populate the start time ComboBox with 15-minute increments
        ObservableList<String> timeSlots = FXCollections.observableArrayList();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String formattedTime = String.format("%02d:%02d", hour, minute);
                timeSlots.add(formattedTime);
            }
        }

        // assigns the time list above to the time slots
        addAppointmentStartTime.setItems(timeSlots);
        addAppointmentEndTime.setItems(timeSlots);
        
    }

}
