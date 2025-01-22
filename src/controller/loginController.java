package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.ZoneId;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * controller class mainController provides logic for main view of the application.
 */
public class loginController implements Initializable{

    @FXML private Label zoneIdLabel;
    @FXML private Label locationLabel;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Button loginButton;
    @FXML private Button exitButton;

    private String exitMessage; //class level variable
    private String closeMessage; //class level variable






    /**
     * Prompts user to close the application.
     *changes language inline with detected system language.
     */
    public void exitButtonAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, closeMessage, ButtonType.YES, ButtonType.NO);
        alert.setTitle(exitMessage);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){
            System.exit(0);
        }
    }

    /**
     *Initialize executes upon longin view being opened.
     * sets the language of the view in line with system language setting
     * @param url
     * @param resourceBundle
     * used to reassign FXML parameters to different languages
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            //defining user location
            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);
            ZoneId systemZone = ZoneId.systemDefault();

            //displays current locale being used by program
            System.out.println(locale.getDisplayLanguage());
            //changing javafx components to English/French depending on language settings
            locationLabel.setText(String.valueOf(systemZone));
            resourceBundle = ResourceBundle.getBundle("language/login", Locale.getDefault());
            usernameLabel.setText(resourceBundle.getString("username"));
            passwordLabel.setText(resourceBundle.getString("password"));
            loginButton.setText(resourceBundle.getString("login"));
            zoneIdLabel.setText(resourceBundle.getString("Location"));
            exitButton.setText(resourceBundle.getString("exit"));

            exitMessage = resourceBundle.getString("exit");
            closeMessage = resourceBundle.getString("close");
            System.out.println(exitMessage);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Login Controller initialized");
    }
}
