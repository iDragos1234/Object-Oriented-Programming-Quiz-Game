package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class SplashScreenCtrl {

    @Inject
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    TextField usernameField;

    @FXML
    HBox hbox;

    @FXML
    TextField urlField;

    @FXML
    TextArea rules;

    @FXML
    Button helpButton;

    @Inject
    public SplashScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void clickHelp() {
        rules.setVisible(true);
    }

    public void releaseHelp() {
        rules.setVisible(false);
    }

    /**
     * Checks if a username is entered and if a server is entered
     * Check whether the IP is correct if yes it sets it, if not then an alert appears/
     * prompting the user to enter an active IP
     */
    public void toWaitingLobby() {
        if (usernameField.getText() == null || usernameField.getText().equals("")) {
            usernameField.setPromptText("First enter your username!!!");
        } else if (urlField.getText() == null || urlField.getText().equals("")) {
            urlField.setPromptText("ENTER a server: http://localhost:8080");
        } else {
            try {
                server.setServer(urlField.getText());
                Activity a = server.getRandomActivity();
                mainCtrl.setIP(urlField.getText());
                mainCtrl.toWaitingLobby();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("IP Error");
                alert.setHeaderText("You have entered an invalid IP address!");
                alert.setContentText("Please change it!");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if a username is set, if not it prompts the user
     * Check whether the IP is correct if yes it sets it, if not then an alert appears/
     * prompting the user to enter an active IP
     */
    public void toSoloGame() throws InterruptedException {
        if (usernameField.getText() == null || usernameField.getText().equals("")) {
            usernameField.setPromptText("First enter your username!!!");
        } else if (urlField.getText() == null || urlField.getText().equals("")) {
            urlField.setPromptText("ENTER a server: http://localhost:8080");
        } else {
            try {
                server.setServer(urlField.getText());
                Activity a = server.getRandomActivity();
                mainCtrl.setIP(urlField.getText());
                mainCtrl.toSoloGame();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("IP Error");
                alert.setHeaderText("You have entered an invalid IP address!");
                alert.setContentText("Please change it!");
                alert.showAndWait();
                e.printStackTrace();
            }

        }
    }

    /**
     * Go to the admin scene
     * Check whether the IP is correct if yes it sets it, if not then an alert appears/
     * prompting the user to enter an active IP
     */
    public void toAdmin() {
        if (urlField.getText() == null || urlField.getText().equals("")) {
            urlField.setPromptText("ENTER a server: http://localhost:8080");
        } else {
            try {
                server.setServer(urlField.getText());
                Activity a = server.getRandomActivity();
                mainCtrl.setIP(urlField.getText());
                mainCtrl.toAdmin();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("IP Error");
                alert.setHeaderText("You have entered an invalid IP address!");
                alert.setContentText("Please change it!");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    /**
     * initializes extra settings to the given scene
     * sets promtp text and creates the background colour
     */
    public void initialize() {
        urlField.setPromptText("Enter a server: http://localhost:8080");
        urlField.setFocusTraversable(false);
        usernameField.setFocusTraversable(false);
        usernameField.setPromptText("Enter your username");
        Stop[] stops = new Stop[]{new Stop(0, Color.CADETBLUE), new Stop(1, Color.LIGHTCYAN)};
        LinearGradient lgcolor = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        BackgroundFill bgfill = new BackgroundFill(lgcolor, CornerRadii.EMPTY, Insets.EMPTY);
        hbox.setBackground(new Background(bgfill));
        rules.setText("Type your name! \n If you want to play multiplayer, type which server!" +
                "\n\n Click the game type when you are ready, and have fun!");

    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public HBox getHbox() {
        return hbox;
    }

    public TextField getUrlField() {
        return urlField;
    }

}
