package client.utils;


import commons.Identifier;
import commons.Questions.ListQuestions;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


public class PlayerUtils {

    private String server;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    /**
     * retrieveGameSessionID - this method is used by MultiPlayerSession objects to retrieve the Object identifier of the game session in which this player participates
     * @return - an Identifier object for game session in which this player is currently participating.
     */
    public Identifier retrieveGameSessionID() {
        var res = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/player/lobby/game_session_ID") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Response.class);

        if(res.getStatus() != 200) System.out.println("PlayerUtils -> retrieveGameSessionID: error status " + res.getStatus());

        Identifier gameSessionID = res.readEntity(Identifier.class);

        System.out.println("PlayerUtils -> retrieveGameSessionID: status " + res.getStatus() + " | ID " + gameSessionID);

        return gameSessionID;
    }


    /**
     * lobbyEvent - this method is used to request events from the server PlayerController class
     * @param gameSessionID - the Identifier object acting as a game session identifier (identifies the game session in which this player is currently participating)
     * @param playerName - The name of the player (representing the id of the LeaderboardEntryMP object associated with the player)
     * @param message - the type of event that is requested:
     *                                                      "check" -> check the number of players in the lobby,
     *                                                     "joined"-> this player has joined the lobby,
     *                                                      "left" -> this player has left the lobby.
     * @return - an Integer representing the current number of players in the lobby
     */
    public int lobbyEvent(Identifier gameSessionID, String playerName, String message) {
        if(gameSessionID == null) throw new NullPointerException("GS-ID is null.");

        System.out.println("1) Player Utils -> lobbyEvent: " + message + " | GS-ID: " + gameSessionID);

        var res = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/player/lobby/event/" + playerName + "/" + message) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(gameSessionID, APPLICATION_JSON), Response.class);

        if(res.getStatus() != 200) {
            return Integer.MIN_VALUE;
        }

        int val = res.readEntity(int.class);
        System.out.println("2) Player Utils -> lobbyEvent: status " + res.getStatus() + " | value " + val + " | GS-ID: " + gameSessionID);
        return val;
    }

    /**
     * startGame - this method is called when a player presses the start button.
     * Sends a signal to the server to start the game session, close the current lobby, and open a new one.
     */
    public void startGame() {
        var res = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/player/lobby/start_game") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Response.class);
        System.out.println("PlayerUtils -> startGame: status " + res.getStatus());
    }

    // Long Polling

    /**
     * @EXEC - the Executor thread that manages the Long Polling process
     */
    private static ExecutorService EXEC = Executors.newSingleThreadExecutor();

    /**
     * restartEXEC - method that starts/restarts the EXEC thread.
     */
    public void restartEXEC() {
        EXEC = Executors.newSingleThreadExecutor();
    }

    /**
     * registerForUpdates - this method is used for Long Polling the server, when player is in the waiting lobby
     * @param playerName - The name of the player (representing the id of the LeaderboardEntryMP object associated with the player)
     * @param consumer - the lambda function that asserts what shall be done with the received message (here: it starts the game, once another player has started the game)
     * NOTE: this method may be used further, during the game itself, or another method can be constructed, but PlayerController methods may also need modifications.
     */
    public void registerForUpdates(String playerName, Consumer<String> consumer) {

        System.out.println("PlayerUtils -> registerForUpdates");
        EXEC.submit(() -> {
           while(!Thread.interrupted()) {
               var res = ClientBuilder.newClient(new ClientConfig()) //
                       .target(server).path("api/player/lobby/updates/" + playerName) //
                       .request(APPLICATION_JSON) //
                       .accept(APPLICATION_JSON) //
                       .get(Response.class);

               if(res.getStatus() == 204) {
                   System.out.println("Player Utils -> registerForUpdates: status 204");
                   continue;
               }

               String message = res.readEntity(String.class);
               consumer.accept(message);
           }
        });
    }

    /**
     * stopExec - this method interrupts the EXEC thread used for Long Polling updates
     */
    public void stopExec() {
        EXEC.shutdownNow();
    }

    //##################################################################
    // Long polling for time attack
    //##################################################################

    /**
     * registerForUpdates - this method is used for Long Polling the server, when player is in the waiting lobby
     * @param gameSessionID - the ID of the game the player is in
     * @param playerName - The name of the player (representing the id of the LeaderboardEntryMP object associated with the player)
     * @param consumer - the lambda function that asserts what shall be done with the received message (here: it starts the game, once another player has started the game)
     */
    public void timeAttackLongPolling(Identifier gameSessionID, String playerName, Consumer<String> consumer) {

        System.out.println("PlayerUtils -> timeAttackLongPolling");
        EXEC.submit(() -> {
            while(!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig()) //
                        .target(server).path("api/player/timeAttack/updates/" + playerName) //
                        .request(APPLICATION_JSON) //
                        .accept(APPLICATION_JSON) //
                        .post(Entity.entity(gameSessionID, APPLICATION_JSON), Response.class);

                if(res.getStatus() == 204) {
                    System.out.println("Player Utils -> timeAttackLongPolling: status 204");
                    continue;
                }

                String message = res.readEntity(String.class);
                consumer.accept(message);
            }
        });
    }

    /**
     * timeAttackHappens - this method is called when a player presses the time attack.
     * Sends a signal to the server to time attack.
     */
    public void timeAttackHappens(Identifier gameSessionID) {
        var res = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/player/timeAttack/timeAttackHappened/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(gameSessionID, APPLICATION_JSON), Response.class);
        System.out.println("PlayerUtils -> happened the time attack: status " + res.getStatus());
    }


    //##################################################################

    /**
     * checkForSameName - this method is used by the client to check with the server whether there is another player
     * with the same name as this player in the waiting lobby.
     * @param playerName - the String name of this player.
     * @return - a boolean value (there is another player with the same name -> "true", otherwise -> "false").
     */
    public boolean checkForSameName(String playerName) {
        var res = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/player/lobby/check_name/" + playerName) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Response.class);

        boolean val = res.readEntity(boolean.class);
        System.out.println("PlayerUtils -> checkForSameName: status " + res.getStatus() + " | player: " + playerName + " | " + val);
        return val;
    }

    /**
     *
     * @param gameSessionID game session identifier
     * @return List of questions
     */
    public ListQuestions retrieveListQuestions(Identifier gameSessionID) {
        var res = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/player/game/list_questions") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(gameSessionID, APPLICATION_JSON), Response.class);

        if (res.getStatus() != 200)
            System.out.println("PlayerUtils -> retrieveListQuestions: status " + res.getStatus());

        return res.readEntity(ListQuestions.class);
    }

    /**
     * Post method for emojis
     * @param gameSessionID id of the gameSession to later on get and set the rgb values
     * @param message dedicates the emoji color or the check command
     * @return an integer array which contains rgb values
     */
    public int[] emojiPost(Identifier gameSessionID, String message) {
        var res = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/player/emoji/" + message) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(gameSessionID,APPLICATION_JSON),Response.class);
        return res.readEntity(int[].class);
    }

    /**
     * @param gameSessionID id of the gameSession to identify and validate which game is being played
     * @param score the score of the current player
     * @param playerName the name of the current player
     */
    public void scorePost(Identifier gameSessionID, String playerName, Integer score) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/player/scores/" + playerName + "/" + score) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(gameSessionID,APPLICATION_JSON));
    }

    /**
     * @param gameSessionID game session identifier
     * @return collection of scores of players
     */
    public Map<String, Integer> getMultiplayerScores(Identifier gameSessionID) {
        var res = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/player/getScores") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(gameSessionID,APPLICATION_JSON), Response.class);

        Map<String, Integer> map = res.readEntity(Map.class);
        System.out.println("PlayerUtils -> getMultiplayerScores status " + res.getStatus() + " | result: " + map);


        return map;
    }
}
