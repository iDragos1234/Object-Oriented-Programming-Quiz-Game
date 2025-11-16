package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;


public class LeaderboardHWScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    Label timeRemainingDisplay;

    @FXML
    Label pos1;

    @FXML
    Label pos2;

    @FXML
    Label pos3;

    @FXML
    Label pos4;

    @FXML
    Label pos5;

    @FXML
    Label pos6;

    @FXML
    Label pos7;

    @FXML
    Label pos8;

    @FXML
    Label pos9;

    @FXML
    Label pos10;

    @FXML
    Label posPlayer;

    @FXML
    HBox hBox;
    @Inject
    public LeaderboardHWScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }


    /**
     * This method can be used by the server to update the time remaining display in the leaderboard.
     *
     * @param timeLeft
     */
    public void updateTimeRemaining(int timeLeft) {
        timeRemainingDisplay.setText("Game will continue in:\n" +
                timeLeft + " seconds!");
    }

    /**
     * This method is for updating/inserting the Leaderboard Entries
     */
    public void updateLeaderboardEntries() {
    }


    public void initialize() {
        Stop[] stops = new Stop[]{new Stop(0, Color.CADETBLUE), new Stop(1, Color.LIGHTCYAN)};
        LinearGradient lgcolor = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        BackgroundFill bgfill = new BackgroundFill(lgcolor, CornerRadii.EMPTY, Insets.EMPTY);
        hBox.setBackground(new Background(bgfill));
    }

    public void setIP(String IP) {
        server.setServer(IP);
    }

}
