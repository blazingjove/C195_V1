package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.contacts;
import DAO.contactQuery;
import DAO.appointmentQuery;
import java.sql.SQLException;

import static DAO.appointmentQuery.appointmentIDNext;

public class addAppointmentController {


    @FXML private TextField addAppointmentID;
    @FXML private TextField addAppointmentTitle;
    @FXML private TextField addAppointmentDescription;
    @FXML private TextField addAppointmentLocation;
    @FXML private TextField addAppointmentType;
    @FXML private ComboBox<String> addAppointmentContact;
    @FXML private TextField addAppointmentCustomerID;

    @FXML private Button appointmentExit;




    /**Prompts user to select if they want to close the program.
     * <p><b>Future Improvements </b></n>
     * I would like to implement an exit button that checks if any of the fields have been changed and prompts user that
     * they will lose information if they choose to exit the view. Sort of a reminder that they will lose unsaved data.
     * </p>*/
    public void appointmentExitAction() {
        System.out.println("Appointment view exit button pressed");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Close Window?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("EXIT");
        alert.showAndWait();
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
