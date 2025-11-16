package client;

import client.scenes.MainCtrl;
import client.scenes.MultiMCScreenCtrl;
import client.scenes.MultiOpenQScreenCtrl;
import client.scenes.WaitingLobbyCtrl;
import client.utils.PlayerUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Identifier;
import commons.Questions.ListQuestions;
import commons.Questions.Question;
import javafx.application.Platform;

import java.util.*;

public class MultiPlayerSession {

    @Inject
    private PlayerUtils playerUtils;

    @Inject
    private ServerUtils server;

    @Inject
    private MainCtrl mainCtrl;

    @Inject
    private WaitingLobbyCtrl waitingLobbyCtrl;

    @Inject
    private MultiMCScreenCtrl multiMCScreenCtrl;

    @Inject
    private MultiOpenQScreenCtrl multiOpenQScreenCtrl;

    /**
     * @playerName - name of the player
     * @playerScore - the score of the player
     * @gameSessionID - the Identifier object for the game session this player currently participates in.
     *                  This Identifier is used to identify the corresponding Server.GameSession object.
     *                  This Identifier needs to be sent in a POST request every time the client communicates with the server in a multiplayer game scenario.
     * @intervalPoll - this Timer object is used to schedule Interval Polls made by the player to the server.
     * @inGame - this boolean value asserts whether this player is currently involved in a multiplayer game session
     */

    private String playerName;
    private int playerScore;
    private Identifier gameSessionID;
    private Timer intervalPoll;
    private boolean inGame;

    private ListQuestions listQuestions;

    /**
     * constructor for this class
     */
    public MultiPlayerSession() {
        playerScore = 0;
        inGame = false;
    }

    /**
     * resetSession - this method resets the non-injected fields of this class to their corresponding default values
     * after this player exited the lobby or their ongoing game session.
     */
    public void resetSession() {

        unregisterFromUpdates();
        cancelIntervalPoll();

        playerName = null;
        playerScore = 0;
        gameSessionID = null;
        intervalPoll = null;
        inGame = false;
    }

    /**
     * inGame - this method asserts whether this player is currently playing a multiplayer game.
     * @return - a boolean asserting whether this player is currently in a multiplayer game.
     */
    public boolean inGame() {
        return inGame;
    }


// getters and setters #################################################################################################

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        System.out.println("Player Score: " + this.playerScore);
        this.playerScore = this.playerScore + playerScore;
    }

    public Identifier getGameSessionID() {
        return gameSessionID;
    }

    public void setGameSessionID(Identifier gameSessionID) {
        this.gameSessionID = gameSessionID;
    }

    /**
     * setInGame - setter for the inGame field
     * @param inGame - a boolean value asserting whether the player is currently in a multiplayer game
     */
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

//######################################################################################################################

// Lobby Communicators #################################################################################################

    /**
     * retrieveGameSessionID - this method is used to retrieve the ID of the game session in which this player is
     * currently participating.
     */
    public void retrieveGameSessionID() {
        gameSessionID = playerUtils.retrieveGameSessionID();
    }

    /**
     * registerForLobbyUpdates - this method is used to listen for when the game is started.
     * This method activates the Long Polling process.
     * Once receiving a positive message (represented by the "start" String), the scene is changed to the game itself.
     */
    public void registerForLobbyUpdates() {
        playerUtils.registerForUpdates(playerName, message -> {
            System.out.println("MPSession -> registerForLobbyUpdates: " + message);
            if(message.equals("start")) {
                playerUtils.stopExec(); // this line is provisionally here: used to test waiting lobby
                Platform.runLater(() -> mainCtrl.toMulti()); // this command is important: without it the multiplayer screen is not rendered, since it affects the FXML thread
            }
        });
    }

    /**
     * pollForLobbyNumPlayers - this method listens for any variation in the number of player
     * by means of Interval Polling.
     */
    public void pollForLobbyNumPlayers() {
        intervalPoll = new Timer();
        intervalPoll.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int numPlayers = playerUtils.lobbyEvent(gameSessionID, playerName, "check");
                if(numPlayers != waitingLobbyCtrl.getNumPlayers()) {
                    waitingLobbyCtrl.setNumPlayers(numPlayers);
                }
            }
        }, 1000, 3000);
    }

    /**
     * polling for updating the background color according to input emojis. In order to do that we use emojiPost in playerUtils method with "check"
     */
    public void pollForEmojis() {
        intervalPoll = new Timer();
        intervalPoll.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int[] rgb= playerUtils.emojiPost(gameSessionID,"check");
                if(rgb != null) {
                    multiMCScreenCtrl.updateEmojis(rgb);
                    multiOpenQScreenCtrl.updateEmojis(rgb);
                }
            }
        }, 1000, 1000);
    }

    /**
     * unregisterFromUpdates - this method stops listening for the "start" game message
     * It also interrupts the associated Long Polling process.
     */
    public void unregisterFromUpdates() {
        playerUtils.stopExec();
    }

    /**
     * cancelIntervalPoll - this method cancels the timer field intervalPoll
     * It can be used to stop listening for variations in the number of players in the lobby.
     */
    public void cancelIntervalPoll() {
        System.out.println("INTERVAL POLL CANCELLED!");
        intervalPoll.cancel();
    }

    /**
     * lobbyEvent - this method sends a certain message to the server.
     * @param message - a String containing the message sent to the server PlayerController.
     * @throws IllegalArgumentException - if the message is not a standard one.
     * For communicating diverse events when in waiting lobby, please use: (player) "joined", (player) "left", "check" (this last choice is not feasible here)
     */
    public void lobbyEvent(String message) throws IllegalArgumentException {
        if(!(message.equals("check") || message.equals("joined") || message.equals("left")))
            throw new IllegalArgumentException("Message sent to server is not valid.");

        waitingLobbyCtrl.setNumPlayers(playerUtils.lobbyEvent(gameSessionID, playerName, message));
    }

//######################################################################################################################

// Time Attack long polling #########################################################################

    /**
     * This does the time attack.
     *
     */
    public void timeAttackLongPolling() {
        playerUtils.timeAttackLongPolling(gameSessionID, playerName, message -> {
            System.out.println("MPSession -> timeAttackLongPolling: " + message);
            if(message.equals("time attack")) {
                System.out.println("MPSession time attack is happening!");
                playerUtils.stopExec();
                Platform.runLater(() -> mainCtrl.setTimerForTimeAttack(0.5)); // the Platform.runlater might be useless? This is where we can change the ratio of the attack

            }
        });
    }


// List Questions ######################################################################################################

    public void retrieveListQuestions() {
        listQuestions = playerUtils.retrieveListQuestions(gameSessionID);
        System.out.println("MultiPlayerSession -> retrieveListQuestions: " + listQuestions);
    }

    public  boolean hasNextQuestion() {
        return listQuestions.hasNextQuestion();
    }

    public Question nextQuestion() {
        return listQuestions.nextQuestion();
    }

    public int getRound() {
        if(listQuestions == null) return -1;
        return listQuestions.getCounter();
    }

//######################################################################################################################

    /**
     * Utility method to send emojis, the methods inputs the message and gameSessionID to posting it to the server
     * @param message emoji color
     * @throws IllegalArgumentException
     */
    public void emojiPost(String message) throws IllegalArgumentException {
        if(!(message.equals("green") || message.equals("red") || message.equals("blue")))
            throw new IllegalArgumentException("Message sent to server is not valid.");

        multiMCScreenCtrl.updateEmojis(playerUtils.emojiPost(gameSessionID, message));
        multiOpenQScreenCtrl.updateEmojis(playerUtils.emojiPost(gameSessionID, message));
    }

    public void scorePost() {
        playerUtils.scorePost(gameSessionID, playerName, playerScore);
    }

    public Map<String, Integer> getMultiplayerScores() {
        return playerUtils.getMultiplayerScores(gameSessionID);
    }
    public PlayerUtils getPlayerUtils() {
        return playerUtils;
    }

    public void setPlayerUtils(PlayerUtils playerUtils) {
        this.playerUtils = playerUtils;
    }

    public ServerUtils getServer() {
        return server;
    }

    public void setServer(ServerUtils server) {
        this.server = server;
    }
}
