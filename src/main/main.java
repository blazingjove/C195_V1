package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.time.ZoneId;
import java.time.ZonedDateTime;


/** main class that creates an application.*/
public class main extends Application {

    /** Initializes the mainView.fxml.
     * @param stage sets the login view to define starting parameters */
    @Override
    public void start(Stage stage) throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("language/login", Locale.getDefault());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/view/loginView.fxml")), resourceBundle);
        stage.setTitle(resourceBundle.getString("title"));
        stage.setScene(new Scene(root, 400  ,320));
        stage.show();
    }

    /** main method that starts FXML and connects to database.
     * commented out code is to change locale for troubleshooting.
     * @param args launches main*/
    public static void main(String[] args) {

        //Locale.setDefault(new Locale("fr")); //sets system language to French

        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();

        System.out.println("Application closed");
    }
}
