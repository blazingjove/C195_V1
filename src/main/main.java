package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resource/view/mainView.fxml"));
        stage.setTitle("First Screen");
        stage.setScene(new Scene(root, 800,600));
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
