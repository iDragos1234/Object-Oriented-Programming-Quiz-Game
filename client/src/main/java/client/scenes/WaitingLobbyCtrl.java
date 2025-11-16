package client.scenes;

import client.utils.PlayerUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class WaitingLobbyCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private int numPlayers = 0;

    @Inject
    private PlayerUtils playerUtils;

    @FXML
    Button startButton;
    @FXML
    Button leaveButton;
    @FXML
    Button helpButton;
    @FXML
    TextArea rules;
    @FXML
    TextField numOfP;
    @FXML
    TextField title;

    public TextArea getRules() {
        return rules;
    }

    public void setRules(TextArea rules) {
        this.rules = rules;
    }

    @FXML
    Text playersInLobby;
    @FXML
    AnchorPane pane;

    @Inject
    public WaitingLobbyCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void toSplash() {
        mainCtrl.toSplash();
    }

    public void clickHelp() {
        rules.setVisible(true);
    }

    public void releaseHelp() {
        rules.setVisible(false);
    }

    public void initialize() {
        leaveButton.setStyle("-fx-background-color: #ff0000;");
        numOfP.setEditable(false);
        title.setEditable(false);
        rules.setText("Joker card can be used to eliminate a wrong answer" +
                "\n\nDouble points can be used to\ndouble the points rewards for this\nquestion\n\n" +
                "Time attacks can be used to\nShorten the time of this question\nfor other players");
        rules.setWrapText(true);
        numOfP.setFocusTraversable(false);
        title.setFocusTraversable(false);

    }

    public void playerJoined() {
        numPlayers++;
        playersInLobby.setText(Integer.toString(numPlayers));
    }

    public void playerLeft() {
        numPlayers--;
        playersInLobby.setText(Integer.toString(numPlayers));
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
        playersInLobby.setText(Integer.toString(numPlayers));
    }

    public void startMulti() {
        mainCtrl.startMulti();
    }

}
