package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


/** main class that creates an application.*/
public class main extends Application {

    /** Initializes the mainView.fxml.
     * @param stage
     * @throws IOException
     * */
    @Override
    public void start(Stage stage) throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("language/login", Locale.getDefault());
        Parent root = FXMLLoader.load(getClass().getResource("/resource/view/loginView.fxml"), resourceBundle);
        stage.setTitle(resourceBundle.getString("title"));
        stage.setScene(new Scene(root, 400  ,320));
        stage.show();
    }

    /** main method that starts FXML and connects to database.
     * commented out code is to change locale for troubleshooting.
     * @param args*/
    public static void main(String[] args) {
        //Locale.setDefault(new Locale("fr"));
        //System.out.println("Locale set to: " + Locale.getDefault());

        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
