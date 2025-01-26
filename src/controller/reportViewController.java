package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.contacts;
import model.appointments; // Import appointments model
import DAO.contactQuery;
import DAO.appointmentQuery;

import java.io.IOException;
import java.sql.SQLException;

public class reportViewController {

    @FXML private TableColumn<?, ?> reportViewAppointmentID;
    @FXML private TableColumn<?, ?> reportViewType;
    @FXML private TableColumn<?, ?> reportViewTitle;
    @FXML private TableColumn<?, ?> reportViewDescription;
    @FXML private TableColumn<?, ?> reportViewStart;
    @FXML private TableColumn<?, ?> reportViewEnd;
    @FXML private TableColumn<?, ?> reportViewCustomerID;

    @FXML
    private Button reportViewExit;

    @FXML
    private TableView<appointments> reportViewTable; // Add TableView for binding appointments


    public void reportViewExitAction() throws IOException {

        mainViewController.showMainView();
    }


    public void setSelectedContact(String selectedContact) {
        try {
            int contactID = contactQuery.getContactIDByName(selectedContact);
            if (contactID > 0) {
                System.out.println("Contact ID for " + selectedContact + " is: " + contactID);
                populateAppointmentsByContactID(contactID); // Call to populate table for contact ID
            } else {
                System.out.println("No contact found for name: " + selectedContact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while fetching contact ID for: " + selectedContact);
        }
    }

    public void populateAppointmentsByContactID(int contactID) {
        var appointments = appointmentQuery.getAppointmentsByContactID(contactID); // Fetch appointments for contact
        if (appointments.isEmpty()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("No Appointments Found");
            alert.setHeaderText(null);
            alert.setContentText("No appointments are available for the selected contact.");
            alert.showAndWait(); // Display alert
            return; // Exit the method
        }

        reportViewTable.setItems(appointments); // Set table data from query
        reportViewAppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        reportViewType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        reportViewTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        reportViewDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        reportViewStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        reportViewEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        reportViewCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    }


}
