package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class appointmentController {

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
}
