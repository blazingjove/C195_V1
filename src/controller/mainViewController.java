package controller;

import DAO.customerQuery;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import DAO.*;
import java.sql.SQLException;
import javafx.stage.Stage;
import model.appointments;
import model.customers;

/**mainViewController houses the appointment and customer tables as well as buttons that allows users to modify, and a reports tab that generates specific reports when prompted.*/
public class mainViewController {

    //appointment table tab table columns
    @FXML private TableView<appointments> appointmentTable;
    @FXML private TableColumn<? ,?> appointmentID;
    @FXML private TableColumn<? ,?> appointmentTitle;
    @FXML private TableColumn<? ,?> appointmentDescription;
    @FXML private TableColumn<? ,?> appointmentLocation;
    @FXML private TableColumn<? ,?> appointmentType;
    @FXML private TableColumn<? ,?> appointmentStart;
    @FXML private TableColumn<? ,?> appointmentEnd;
    @FXML private TableColumn<? ,?> appointmentCustomerID;
    @FXML private TableColumn<? ,?> appointmentContactID;
    @FXML private TableColumn<? ,?> appointmentUserID;

    //Customer Table Columns
    @FXML private TableView<customers> customerTable;
    @FXML private TableColumn<? ,?> customerID;
    @FXML private TableColumn<? ,?> customerName;
    @FXML private TableColumn<? ,?> customerAddress;
    @FXML private TableColumn<? ,?> customerPostalCode;
    @FXML private TableColumn<? ,?> customerPhone;
    @FXML private TableColumn<? ,?> customerDivisionID;

    //buttons
    @FXML private Button mainViewExit; //located on every tab in the mainView
    @FXML private Button deleteAppointment;


    /**Prompts user to select if they want to close the program.*/
    public void mainViewExitAction() {
        System.out.println("Login exit button pressed");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Close Program?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("EXIT");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){
            Stage thisStage = (Stage) mainViewExit.getScene().getWindow();
            thisStage.close();
        }
    }

    public void deleteAppointmentAction() {
        System.out.println("Delete Appointment button pressed");

        // Get the selected appointment
        appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

        // Check if no appointment is selected
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete Appointment");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
            return;
        }

        // Confirm deletion
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete the selected appointment?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.showAndWait();

        if (confirmationAlert.getResult() == ButtonType.YES) {
            try {
                // Delete appointment from database
                appointmentQuery.deleteAppointment(selectedAppointment.getAppointmentID());

                // Remove appointment from table
                appointmentTable.getItems().remove(selectedAppointment);

                // Refresh table
                appointmentTable.refresh();

                // Show success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Appointment Deleted");
                successAlert.setHeaderText(null);
                successAlert.setContentText("The appointment has been successfully deleted.");
                successAlert.showAndWait();
            } catch (SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Failed to Delete Appointment");
                errorAlert.setContentText("An error occurred while trying to delete the appointment.");
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }


    /** Initialize populates the customer and appointment table.
     * <p>
     *     <b>Runtime Errors</b></n>
     *     Had trouble figuring out the best way to present the information. I believe I have presented a good solution the data is presented statically and users modify the data
     *     in other views so you don't have to jump around to different views the Tables as large and will enable you to find what you need.
     * </p>
     *<p>
     *     <b>Future Improvements</b></n>
     *     I would like to add a search function that would come in handy when the table gets much larger, and you have to search for a specific item.
     *     possibly with a drop-down menu so you search a specific column and narrow your search that much more.
     *</p>
     * * @throws SQLException*/
    public void initialize() throws SQLException {

        System.out.println("Main Controller initialized");

        //uses method to get all appointments from sql database
        ObservableList<appointments> allAppointmentsList = appointmentQuery.getAllAppointments();
        //each column is stored one by one
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appointmentContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        appointmentUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        //appointment table is populated with the list
        appointmentTable.setItems(allAppointmentsList);


        //uses method to get all customers from sql database
        ObservableList<customers> allCustomersList = customerQuery.getAllCustomers();
        //each colum is stored
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerDivisionID.setCellValueFactory(new PropertyValueFactory<>("CustomerDivisionID"));
        //customer table populated with data that was stored above
        customerTable.setItems(allCustomersList);
    }
}
