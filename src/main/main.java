package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import main.JDBC;
import java.util.Locale;


/** main class that creates an application.*/
public class main extends Application {
    @Override

    /**Initializes the mainView.fxml.
     * @param stage
     * throws Exception*/
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resource/view/mainView.fxml"));
        stage.setTitle("First Screen");
        stage.setScene(new Scene(root, 400  ,350));
        stage.show();
    }

    /** main method that starts FXML and connects to database.
     *
     * @param args */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
