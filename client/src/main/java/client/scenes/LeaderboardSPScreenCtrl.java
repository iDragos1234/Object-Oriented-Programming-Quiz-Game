package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.LeaderboardEntrySP;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.util.List;

public class LeaderboardSPScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    BarChart top10chart;

    @FXML
    TableView leaderboardTable;

    @FXML
    TableColumn nameColumn;

    @FXML
    TableColumn scoreColumn;

    @FXML
    NumberAxis yAxis;

    @FXML
    CategoryAxis xAxis;

    @FXML
    Label posPlayer;

    @FXML
    Button mainMenu;

    @FXML
    HBox hBox;

    @Inject
    public LeaderboardSPScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * This method is for updating/inserting the Leaderboard Entries
     *
     * @param userSP
     */
    public void updateLeaderboardEntries(LeaderboardEntrySP userSP) {
        resetGraphs();
        List<LeaderboardEntrySP> top10 = server.getTop10SP();
        //Update Bar Chart
        yAxis.setLabel("score");

        for (int i = 0; i < top10.size(); i++) {
            XYChart.Series entries = new XYChart.Series();
            entries.getData().add(new XYChart.Data(top10.get(i).getName(), top10.get(i).getScore()));
            top10chart.getData().add(entries);
        }
        //Update tableview
        leaderboardTable.setFixedCellSize(37.3);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        for (int i = 0; i < top10.size(); i++) {
            leaderboardTable.getItems().add(top10.get(i));
        }


        //Update user score
        posPlayer.setText("You: " + userSP.getName() + "\t\t\t\tYour score: " + userSP.getScore() + " !");
    }

    private void resetGraphs() {
        leaderboardTable.getItems().clear();
        top10chart.getData().clear();
    }

    public void toMainMenu() {
        mainCtrl.toSplash();
    }

    public void initialize() {
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
