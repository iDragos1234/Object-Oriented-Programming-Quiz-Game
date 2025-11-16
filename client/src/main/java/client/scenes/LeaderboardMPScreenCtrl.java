package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardMPScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

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
    Button mainMenu;

    @FXML
    Button rejoin;

    @FXML
    Label[] labels;

    @FXML
    BarChart leaderbar;

    @FXML
    NumberAxis yAxis;

    @FXML
    HBox hBox;

    @Inject
    public LeaderboardMPScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }



    /**
     * This method is for updating/inserting the Leaderboard Entries
     */
    public void updateLeaderboardEntries() {
        Map<String, Integer> leaderboard =  mainCtrl.getMultiPlayerSession().getMultiplayerScores();
        System.out.println(leaderboard);

        List<Map.Entry<String, Integer>> list = sortLeaderboard(leaderboard);

        for (int i = 0; i < list.size(); i++) {
            labels[i].setText(list.get(i).getKey() + "  " + list.get(i).getValue());
            labels[i].setVisible(true);
        }

        for (int i = list.size(); i < labels.length; i++) {
            labels[i].setVisible(false);
        }

        posPlayer.setText("You: " + mainCtrl.getMultiPlayerSession().getPlayerName() + "\t\t\t\t\tYour score: " + mainCtrl.getMultiPlayerSession().getPlayerScore()+ " !");
        //Update Bar Chart
        leaderbar.getData().clear();
        yAxis.setLabel("score");

        for (int i = 0; i < list.size(); i++) {
            XYChart.Series entries = new XYChart.Series();
            entries.getData().add(new XYChart.Data(list.get(i).getKey(), list.get(i).getValue()));
            leaderbar.getData().add(entries);
        }
    }

    /**
     * sortLeaderboard - this method sorts the leaderboard map in a decreasing order with respect to the Integer value
     * @param leaderboard - a Map object which maps a String representing the name of the player to an Integer value representing their score
     * @return - a sorted List of Map.Entry objects, limited to only the first 10 entries of the leaderboard
     */
    private List<Map.Entry<String, Integer>> sortLeaderboard(Map<String, Integer> leaderboard) {

        Comparator<Map.Entry<String, Integer>> comp = new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if (o1.getValue() < o2.getValue()) return 1;
                else if (o1.getValue() > o2.getValue()) return -1;
                else return 0;
            }
        };

        List<Map.Entry<String, Integer>> list = leaderboard.entrySet().stream().sorted(comp).limit(10).collect(Collectors.toList());

        return list;
    }

    public void toMainMenu() {
        mainCtrl.toSplash();
    }

    public void toWaitingLobby() {
        mainCtrl.getMultiPlayerSession().lobbyEvent("left");
        mainCtrl.getMultiPlayerSession().resetSession();
        mainCtrl.toWaitingLobby();
    }

    public void initialize() {

        labels = new Label[10];

        labels[0] = pos1;
        labels[1] = pos2;
        labels[2] = pos3;
        labels[3] = pos4;
        labels[4] = pos5;
        labels[5] = pos6;
        labels[6] = pos7;
        labels[7] = pos8;
        labels[8] = pos9;
        labels[9] = pos10;

        var mps = mainCtrl.getMultiPlayerSession();
        if(mps != null && mps.getRound() == 10) {
            mainMenu.setDisable(true);
            mainMenu.setVisible(false);

            rejoin.setDisable(true);
            rejoin.setVisible(false);
        }

        if(mps != null && mps.getRound() == 20) {
            mainMenu.setDisable(false);
            mainMenu.setVisible(true);

            rejoin.setDisable(false);
            rejoin.setVisible(true);
        }

        Stop[] stops = new Stop[]{new Stop(0, Color.CADETBLUE), new Stop(1, Color.LIGHTCYAN)};
        LinearGradient lgcolor = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        BackgroundFill bgfill = new BackgroundFill(lgcolor, CornerRadii.EMPTY, Insets.EMPTY);
        hBox.setBackground(new Background(bgfill));

    }

    public ServerUtils getServer() {
        return server;
    }

    public void setIP(String IP) {
        server.setServer(IP);
    }
}
