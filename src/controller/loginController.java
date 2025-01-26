package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import main.JDBC;
import DAO.userValidation;
import java.time.ZoneId;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
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
    private String errorMessage; //class level variable
    private String incorrectMessage; //class level variable

    /**
     * validates user inputs then logs-in, opens main view
     * <p>
     *     <b>Runtime Error</b></n>
     *     Trouble catching errors on this page due to the many ways you can miss input the fields.
     *     created user validate method do bump user inputs to existing users, much easier than other methods.
     * </p>
     * */
    public void loginButtonAction() {
        System.out.println("Login button pressed");
        try{
            int userId = userValidation.validateUser(usernameField.getText(), passwordField.getText());
            if (userId > 0) {
                System.out.println("User logged in");

                //Hides login view and shows the main View
                mainViewController.showMainView();

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR, incorrectMessage, ButtonType.OK);
                alert.setTitle(errorMessage);
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     *Prompts user to close the application.
     *changes language inline with detected system language.
     * <p>
     *     <b>Runtime Errors</b></n>
     *     I was having trouble when closing longin page and the database connection still open. figured out that the close command was in the main view, and that
     *     close connection command would execute if the use close the window using the corner x button, so I added the command here as well to solve that problem.
     * </p>
     */
    public void exitButtonAction() {
        System.out.println("Login exit button pressed");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, closeMessage, ButtonType.YES, ButtonType.NO);
        alert.setTitle(exitMessage);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){
            JDBC.closeConnection();
            System.exit(0);
        }
    }

    /**
     *Initialize executes upon longin view being opened.
     * sets the language of the view in line with system language setting
     * <p>
     *     <b>Future Improvements</b></n>
     *     adding a dropdown menu to manually select language would be nice
     *     and adding more languages.
     * </p>
     * @param url
     * file paths
     * @param resourceBundle
     * used to reassign FXML parameters to different languages
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Fetch user's system locale
            Locale userLocale = Locale.getDefault();

            //change zone ID here for troubleshooting
            //ZoneId newZoneId = ZoneId.of("Europe/Paris");
            ZoneId newZoneId = ZoneId.systemDefault();

            // Optional: Log default locale for debugging
            System.out.println("User Locale: " + userLocale.getDisplayLanguage());

            // Dynamically load the appropriate resource bundle for UI localization
            resourceBundle = ResourceBundle.getBundle("language/login", userLocale);

            // Update UI labels with localized strings
            locationLabel.setText(newZoneId.toString());
            usernameLabel.setText(resourceBundle.getString("username"));
            passwordLabel.setText(resourceBundle.getString("password"));
            loginButton.setText(resourceBundle.getString("login"));
            zoneIdLabel.setText(resourceBundle.getString("Location"));
            exitButton.setText(resourceBundle.getString("exit"));
            exitMessage = resourceBundle.getString("exit");
            closeMessage = resourceBundle.getString("close");
            errorMessage = resourceBundle.getString("Error");
            incorrectMessage = resourceBundle.getString("Incorrect");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // code below will initiate login attempt when ENTER is pressed on keyboard
        usernameField.setOnKeyPressed(event -> {
            if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER) {
                loginButtonAction();
            }
        });
        passwordField.setOnKeyPressed(event -> {
            if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER) {
                loginButtonAction();
            }
        });

        System.out.println("Login Controller initialized");
    }
}
