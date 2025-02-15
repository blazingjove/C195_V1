package controller;

import DAO.contactQuery;
import DAO.customerQuery;
import DAO.userQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.JDBC;
import model.appointments;
import model.contacts;
import model.users;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;


/**
 * Controller class for editing existing appointments in the application.
 *
 * <p>This class serves as the bridge between the UI and the database for editing
 * appointments. It includes functionality for populating fields based on a selected
 * appointment, validating user inputs, saving changes to the database, and handling
 * user interactions such as exiting the edit view.</p>
 */
public class editAppointmentController {

    @FXML private DatePicker editAppointmentDate;
    @FXML private ComboBox<String > editAppointmentEndTime;
    @FXML private ComboBox<String> editAppointmentStartTime;
    @FXML private Button editAppointmentSave;
    @FXML private ComboBox<String> editAppointmentUserID;
    @FXML private TextField editAppointmentID;
    @FXML private TextField editAppointmentTitle;
    @FXML private TextField editAppointmentDescription;
    @FXML private TextField editAppointmentLocation;
    @FXML private TextField editAppointmentType;
    @FXML private ComboBox<String> editAppointmentContact;
    @FXML private ComboBox<String> editAppointmentCustomerID;
    @FXML private Button editAppointmentExit;

    private appointments selectedAppointment;

    /**
     * Captures all user inputs, validates them, and saves the changes to the SQL database.
     *
     * <p>This method ensures that the user-provided details, such as timings, contact, and
     * customer information, are valid before updating the database. It checks for overlapping
     * appointments, verifies required fields, and performs the save operation if all conditions
     * are met. Additionally, it handles any errors that might occur during the process.</p>
     *
     * @throws RuntimeException if an input or database-related error occurs that cannot be managed gracefully.
     */
    public void editAppointmentSaveAction() {
        System.out.println("Appointment save button pressed");

        try {
            // Extract input values
            String title = editAppointmentTitle.getText();
            String description = editAppointmentDescription.getText();
            String location = editAppointmentLocation.getText();
            String type = editAppointmentType.getText();
            String contactName = editAppointmentContact.getSelectionModel().getSelectedItem();

            //streams to filter appropriate ID from input selected in combobox
            int customerID = customerQuery.getAllCustomers().stream()
                    .filter(customer -> customer.getCustomerName().equals(editAppointmentCustomerID.getSelectionModel().getSelectedItem()))
                    .findFirst()
                    .map(customer -> customer.getCustomerID())
                    .orElseThrow(() -> new RuntimeException("Customer not found in the database"));;
            int userID = userQuery.getAllUsers().stream()
                    .filter(user -> user.getUserName().equals(editAppointmentUserID.getSelectionModel().getSelectedItem()))
                    .findFirst()
                    .map(users::getUserID)
                    .orElseThrow(() -> new RuntimeException("User not found in the database"));;

            // Retrieve and parse start and end times from ComboBoxes
            String selectedStartTime = editAppointmentStartTime.getSelectionModel().getSelectedItem();
            String selectedEndTime = editAppointmentEndTime.getSelectionModel().getSelectedItem();

            // Validate time selections
            if (selectedStartTime == null || selectedEndTime == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Start time and end time must be selected.", ButtonType.OK);
                alert.setTitle("Time Validation Error");
                alert.showAndWait();
                return;
            }

            // Convert selected times and appointment date to LocalDateTime objects
            LocalDateTime start = LocalDateTime.of(editAppointmentDate.getValue(),
                    java.time.LocalTime.of(
                            Integer.parseInt(selectedStartTime.split(":")[0]),
                            Integer.parseInt(selectedStartTime.split(":")[1])));
            LocalDateTime end = LocalDateTime.of(editAppointmentDate.getValue(),
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

            // Validate start and end times are within business hours (8:00 AM to 10:00 PM EST). Convert the business hours to local time for comparison
            java.time.ZoneId estZoneId = java.time.ZoneId.of("America/New_York");
            java.time.ZoneId localZoneId = java.time.ZoneId.systemDefault();

            java.time.LocalTime startLocal = start.toLocalTime();
            java.time.LocalTime endLocal = end.toLocalTime();

            java.time.LocalTime businessStart = java.time.ZonedDateTime.of(start.toLocalDate(), java.time.LocalTime.of(8, 0), estZoneId)
                    .withZoneSameInstant(localZoneId).toLocalTime();
            java.time.LocalTime businessEnd = java.time.ZonedDateTime.of(end.toLocalDate(), java.time.LocalTime.of(22, 0), estZoneId)
                    .withZoneSameInstant(localZoneId).toLocalTime();

            System.out.println("startEST: " + startLocal + " endEST: " + endLocal + " businessStart: " + businessStart + " businessEnd: " + businessEnd);

            if (startLocal.isBefore(businessStart) || endLocal.isAfter(businessEnd)) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Start and end times must be within business hours " + businessStart + " and " + businessEnd, ButtonType.OK);
                alert.setTitle("Time Validation Error");
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

            // Check for overlapping appointments ignore the appointment ID that matched the selected appointment being edited
            String overlapQuery = "SELECT Appointment_ID FROM appointments WHERE Customer_ID = ? AND Appointment_ID != ? AND ((Start <= ? AND End > ?) OR (Start < ? AND End >= ?))";
            try (PreparedStatement psOverlap = JDBC.connection.prepareStatement(overlapQuery)) {
                psOverlap.setInt(1, customerID);
                psOverlap.setInt(2, selectedAppointment.getAppointmentID());
                psOverlap.setTimestamp(3, java.sql.Timestamp.valueOf(start));
                psOverlap.setTimestamp(4, java.sql.Timestamp.valueOf(start));
                psOverlap.setTimestamp(5, java.sql.Timestamp.valueOf(end));
                psOverlap.setTimestamp(6, java.sql.Timestamp.valueOf(end));
                if (psOverlap.executeQuery().next()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment time overlaps with selected customers existing appointment. \n please select a different time", ButtonType.OK);
                    alert.setTitle("Time Conflict");
                    alert.showAndWait();
                    return;
                }
            }
            
            // Insert into database
            boolean success;
            String sqlInsert = "REPLACE INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = JDBC.connection.prepareStatement(sqlInsert)) {
                ps.setInt(1, selectedAppointment.getAppointmentID());
                ps.setString(2, title);
                ps.setString(3, description);
                ps.setString(4, location);
                ps.setString(5, type);
                ps.setTimestamp(6, java.sql.Timestamp.valueOf(start));
                ps.setTimestamp(7, java.sql.Timestamp.valueOf(end));
                ps.setInt(8, customerID);
                ps.setInt(9, userID);
                ps.setInt(10, contactID);
                success = ps.executeUpdate() > 0;
            }

            // Notify the user upon success
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment saved successfully!", ButtonType.OK);
                alert.setTitle("Success");
                alert.showAndWait();

                //closes the edit appointment scene after saving the information in the database
                //editAppointmentSave.getScene().getWindow().hide();

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

    /**
     * Handles the action for exiting the Edit Appointment view.
     *
     * <p>This method prompts the user with a confirmation dialog to ensure they want to close
     * the current window. If the user confirms, the method closes the Edit Appointment view
     * and navigates back to the main view. Future improvements may include implementing a
     * warning for unsaved changes before exiting.</p>
     */
    public void editAppointmentExitAction() {
        System.out.println("Appointment view exit button pressed");
        
        //notify user if the want to exit the window
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Close Window?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("EXIT");
        alert.showAndWait();
        
        //if user selects YES appointment view is closed and main view opened. if NO is selected nothing is done.
        if (alert.getResult() == ButtonType.YES) {
            try {
                // Close the current view
                //editAppointmentExit.getScene().getWindow().hide();
                // Open the main view
                mainViewController.showMainView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Initializes and populates the Edit Appointment view with necessary data.
     *
     * <p>This method prepares the ComboBoxes and other UI elements by loading
     * contacts, customers, and users from the database. Time-related fields are initialized
     * with 15-minute increments to simplify scheduling. Lambda expressions are used for
     * efficient data population into the ComboBoxes.</p>
     *
     * @throws SQLException if an SQL error occurs while fetching data for initialization.
     */
    public void initialize() throws SQLException {
        System.out.println("Add Appointment View initialized");

        //defining the two observable list that will be usd in lambda expression populating combo box
        ObservableList<contacts> contactsObservableList = contactQuery.getContacts();
        ObservableList<String> allContactsNames = FXCollections.observableArrayList();
        // lambda #1
        contactsObservableList.forEach(contacts -> allContactsNames.add(contacts.getContactName()));
        //System.out.println(allContactsNames);
        editAppointmentContact.setItems(allContactsNames);

        //lambda #2
        //defining the list to hold customer Names
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        // retrieves customer names
        customerQuery.getAllCustomers().forEach(customer -> customerNames.add(customer.getCustomerName()));
        editAppointmentCustomerID.setItems(customerNames);

        //defining the list to hold user's names
        ObservableList<String> userNames = FXCollections.observableArrayList();
        userQuery.getAllUsers().forEach(users -> userNames.add(users.getUserName()));
        editAppointmentUserID.setItems(userNames);

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
        editAppointmentStartTime.setItems(timeSlots);
        editAppointmentEndTime.setItems(timeSlots);
        
    }

    /**
     * Sets the selected appointment to be edited and prepopulates the fields with its details.
     *
     * <p>This method assigns the selected appointment and uses it to populate
     * the input fields with its existing data, making it ready for editing.</p>
     *
     * @param selectedAppointment the appointment to be edited in this view.
     */
    public void setAppointmentDate(appointments selectedAppointment) {
        //defining selected appointment as appointment object
        this.selectedAppointment = selectedAppointment;
        //populate fields with selected appointment
        displaySelectedAppointmentData();
    }

    /**
     * Populates the Edit Appointment view with the details of the selected appointment.
     *
     * <p>This method retrieves data from the selected appointment and fills the
     * corresponding input fields in the view. It also ensures that the correct
     * items are selected in the ComboBoxes.</p>
     */
    private void displaySelectedAppointmentData() {

        editAppointmentID.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        editAppointmentTitle.setText(selectedAppointment.getAppointmentTitle());
        editAppointmentDescription.setText(selectedAppointment.getAppointmentDescription());
        editAppointmentLocation.setText(selectedAppointment.getAppointmentLocation());
        editAppointmentType.setText(selectedAppointment.getAppointmentType());

        // Select the matching contact in the ComboBox
        String appointmentContact = contactQuery.getContactByContactID(selectedAppointment.getContactID());
        editAppointmentContact.setValue(appointmentContact);

        // select the matching customer in combobox
        String appointmentCustomer = customerQuery.getCustomerByCustomerID(selectedAppointment.getCustomerID());
        editAppointmentCustomerID.setValue(appointmentCustomer);

        // select matching user in combo box
        String appointmentUser = userQuery.getUserByUserID(selectedAppointment.getUserID());
        editAppointmentUserID.setValue(appointmentUser);

        // Set the value of editAppointmentDate to the start date
        editAppointmentDate.setValue(selectedAppointment.getStart().toLocalDate() );

        //set the values of start and end times
        editAppointmentStartTime.setValue(selectedAppointment.getStart().toLocalTime().toString().substring(0, 5));
        editAppointmentEndTime.setValue(selectedAppointment.getEnd().toLocalTime().toString().substring(0, 5));
    }
}
