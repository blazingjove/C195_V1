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


/**
 * Controller class for the report view. It manages the display of appointment reports
 * based on selected contacts and provides functionality for navigating back to the main view.
 */
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

    /**
     * Closes the report view and navigates back to the main view.
     * <p>
     * This method uses the {@link mainViewController#showMainView()} to display
     * the main application view and ensures that any necessary cleanup is performed.
     *
     * @throws IOException if an error occurs while loading the main view.
     */
    public void reportViewExitAction() throws IOException {
        mainViewController.showMainView();
    }

    /**
     * Sets the selected contact from the reports tab and retrieves its associated data.
     * <p>
     * This method determines the ID of the provided contact name by querying the database
     * and updates the table view with appointments of the selected contact.
     *
     * @param selectedContact the name of the contact selected in the reports tab.
     * @throws SQLException if a database access error occurs during the contact query.
     */
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

    /**
     * Populates the table view with appointments for the specified contact ID.
     * <p>
     * This method retrieves all appointments associated with the given contact ID,
     * binds the data to the table view, and maps table columns to the appropriate properties
     * of the {@link appointments} model.
     *
     * @param contactID the ID of the contact for which appointments need to be displayed.
     */
    public void populateAppointmentsByContactID(int contactID) {
        var appointments = appointmentQuery.getAppointmentsByContactID(contactID); // Fetch appointments for contact

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
