package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import DAO.appointmentQuery;
import java.sql.SQLException;
import model.appointments;


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
    @FXML private TableColumn<? ,?> customerID;
    @FXML private TableColumn<? ,?> customerName;
    @FXML private TableColumn<? ,?> customerAddress;
    @FXML private TableColumn<? ,?> customerPostalCode;
    @FXML private TableColumn<? ,?> customerPhone;
    @FXML private TableColumn<? ,?> customerDivisionID;













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
    }
}
