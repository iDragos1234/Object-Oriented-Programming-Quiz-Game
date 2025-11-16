package server.api;


import commons.Identifier;
import commons.Questions.ListQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.Game.Game;
import server.Game.GameSession;
import java.util.Map;


@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private QuestionsController questionsController;

    /**
     * @game - this Game object is created uniquely created when starting the server. It is used to manage game sessions.
     * @waitingLobby - this GameSession object is used to keep data about a newly created game session that has not
     * started yet (some player from the lobby needs to press the "start game" button and inform the server about this
     * for this new game session to properly start).
     */
    private final Game game = new Game();
    private GameSession waitingLobby = game.getWaitingLobby(); // when server starts, there already needs to be one open waitingLobby

// Lobby ###############################################################################################################
    /**
     * createWaitingLobby - this method is used to create a new game session (at first it plays the role of
     * a waiting lobby) and add it to the Game.gameSessionHashMap
     */
    @GetMapping("/lobby/create")
    public void createWaitingLobby() {
        game.addNewGameSession(new GameSession());
        waitingLobby = game.getWaitingLobby();
    }

    /**
     * retrieveGameSessionID - this GET endpoint is used to retrieve the Object identifier for this newly created game session (here: waiting lobby)
     * The gameSessionID is used by the server to identify to which game session each player belongs (within a game session, each player is identified by their name).
     *
     * @return - an Object representing the game session ID for this newly created game session.
     */
    @GetMapping("lobby/game_session_ID")
    public ResponseEntity<Identifier> retrieveGameSessionID() {
        Identifier res = waitingLobby.getGameSessionID();
        System.out.println("\nPlayerController -> retrieveGameSessionID: " + res + "\n");
        return ResponseEntity.ok(res);
    }

    /**
     * getWaitingLobbyUpdates - this GET endpoint keeps track of when the game starts (manages long polling made by the players)
     *
     * @param name - the name identifier of the player (acts as an id for the LeaderboardEntryMP entity)
     * @return - a String depicting whether the game has started
     */
    @GetMapping("/lobby/updates/{name}")
    public DeferredResult<ResponseEntity<String>> getWaitingLobbyUpdates(@PathVariable String name) {

        System.out.println("PlayerController -> getWaitingLobbyUpdates: " + name);

        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // NO_CONTENT -> 204

        var res = new DeferredResult<ResponseEntity<String>>(10000L, noContent);

        waitingLobby.put(name, message -> res.setResult(ResponseEntity.ok(message)));

        return res;
    }

    /**
     * startGame - this method is called when a player from the waiting lobby starts the game
     * Once this method is called, all other players in the waiting lobby are informed about this event
     * The game cannot start unless there are two or more players in the waiting lobby.
     */
    @GetMapping("/lobby/start_game")
    public void startGame() {
        if (waitingLobby.getNumPlayers() > 0) {

            var res = questionsController.getListQuestions("20&4&3");

            System.out.println("PlayerController -> startGame: " + res.getStatusCode());

            ListQuestions listQuestions = res.getBody();

            System.out.println(listQuestions);

            waitingLobby.setListQuestions(listQuestions);

            waitingLobby.values().forEach(consumer -> consumer.accept("start"));
            createWaitingLobby(); // after a player starts the game,a new game session is created as waiting lobby
        }
    }


    /**
     * lobbyEvent - method that can be used to keep track of the number of players in the waiting lobby
     *
     * @param message - the message specifies what sort of event is required: to check the number of players, a new player has joined or a player just left
     * @return - the number of players in the waiting lobby
     */
    @PostMapping("/lobby/event/{player}/{message}")
    public ResponseEntity<Integer> lobbyEvent(@RequestBody Identifier gameSessionID, @PathVariable String player, @PathVariable String message) {

        GameSession playerGameSession = game.get(gameSessionID);

        System.out.println("PlayerController -> lobbyEvent: " + player + " | " + message + " [=] gameSessionID: " + waitingLobby.getGameSessionID() + " | " + playerGameSession.getGameSessionID() + " | " + gameSessionID);

        if (waitingLobby.getNumPlayers() < 0)
            throw new IndexOutOfBoundsException("Number of players is out of bounds.");

        if (message.equals("check")) {
            return ResponseEntity.ok(playerGameSession.getNumPlayers());
        }

        if (message.equals("joined")) {

            if (!waitingLobby.equals(game.get(gameSessionID)))
                throw new UnsupportedOperationException("Player not allowed to join this game session.");

            waitingLobby.addPlayer();
            return ResponseEntity.ok(waitingLobby.getNumPlayers());
        }

        if(message.equals("left")) {

            playerGameSession.remove(player);
            playerGameSession.removePlayer();

            if(!playerGameSession.equals(waitingLobby) && playerGameSession.getNumPlayers() == 0) {
                game.remove(playerGameSession.getGameSessionID());
                playerGameSession = null;
                System.out.println("No players left in this game session. Game session deleted.");
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(playerGameSession.getNumPlayers());
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * checkForSameName - this method allows the player to check whether there is another player with the same name in the waiting lobby (used in a MultiPlayer scenario)
     *
     * @param name - a String representing the name of the player
     * @return - a boolean that asserts whether there is another player with the same name (a true value means that there is one!)
     */
    @GetMapping("/lobby/check_name/{name}")
    public boolean checkForSameName(@PathVariable String name) {
        return waitingLobby.keySet().contains(name);
    }

//######################################################################################################################


// ListQuestions #######################################################################################################

    /**
     * getListQuestions - this endpoint is used to retrieve the ListQuestions corresponding to a certain game session
     * @param gameSessionID
     * @return
     */
    @PostMapping("/game/list_questions")
    public ListQuestions getListQuestions(@RequestBody Identifier gameSessionID) {
        GameSession playerGameSession = game.get(gameSessionID);

        if (playerGameSession == null) throw new NullPointerException("Game session not found.");

        ListQuestions listQuestions = playerGameSession.getListQuestions();

        return listQuestions;
    }

//######################################################################################################################


// Emojis ##############################################################################################################

    /**
     * getTimeAttackUpdates - this GET endpoint keeps track of when a time attack happens (manages long polling made by the players)
     * @param name - the name identifier of the player (acts as an id for the LeaderboardEntryMP entity)
     * @return - a String depicting whether the time attack has happened
     */
    @PostMapping("/timeAttack/updates/{name}")
    public DeferredResult<ResponseEntity<String>> getTimeAttackUpdates(@RequestBody Identifier identifier, @PathVariable String name) {

        System.out.println("PlayerController -> getTimeAttackUpdates: " + name);

        GameSession gameSession = game.get(identifier);

        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // NO_CONTENT -> 204

        var res  = new DeferredResult<ResponseEntity<String>>(9000L, noContent); //TODO Change when we change the time amount!!

        gameSession.put(name, message -> {
            res.setResult(ResponseEntity.ok(message));
        });

        return res;
    }

    /**
     * timeAttackHappened - this method is called when a player clicks on a time attack
     * Once this method is called, all other players in the game lobby are informed about this event
     * @param identifier - the identifier of the game lobby the attack should happen in
     */
    @PostMapping ("/timeAttack/timeAttackHappened/")
    public void timeAttackHappened(@RequestBody Identifier identifier) {

        System.out.println("PlayerController -> timeAttackHappened");

        GameSession gameSession = game.get(identifier);

        gameSession.values().forEach(consumer -> consumer.accept("time attack"));
    }

    /**
     * Endpoint for changing the color of the background according to the user input emojis
     *
     * @param gameSessionID id to indicate which game session we will be changing the rgb color
     * @param message       the emoji color is send via message parameter
     * @return a response entity which contains the adjusted rgb colors
     */
    @PostMapping("/emoji/{message}")
    public ResponseEntity<int[]> emojiPost(@RequestBody Identifier gameSessionID, @PathVariable String message) {
        GameSession playerGameSession = game.get(gameSessionID);

        if (playerGameSession == null) return ResponseEntity.badRequest().build();

        int[] rgb = playerGameSession.getRGB();

        if(message.equals("green")) {
            int[] arr = {-3, 3, -3};
            playerGameSession.setRGB(arr);
            return ResponseEntity.ok(rgb);
        }
        if (message.equals("red")) {
            int[] arr = {3, -3, -3};
            playerGameSession.setRGB(arr);
            return ResponseEntity.ok(rgb);
        }
        if (message.equals("blue")) {
            int[] arr = {-3, -3, 3};
            playerGameSession.setRGB(arr);
            return ResponseEntity.ok(rgb);
        }
        if (message.equals("check")) {
            return ResponseEntity.ok(rgb);
        }

        return ResponseEntity.badRequest().build();
    }

//######################################################################################################################


// Scores ##############################################################################################################

    /**
     * getMultiplayerScores - this method fetches the leaderboard scores for a certain game session
     * @param gameSessionID - the game session ID
     * @return - a List of LeaderboardEntryMP
     */
    @PostMapping("/getScores")
    public ResponseEntity<Map<String, Integer>> getMultiplayerScores(@RequestBody Identifier gameSessionID) {
        GameSession playerGameSession = game.get(gameSessionID);

        Map<String, Integer> map = playerGameSession.getLeaderboard();

        System.out.println("PlayerController -> getMultiplayerScores: " + waitingLobby.getGameSessionID() + " | " + playerGameSession.getGameSessionID() + " | " + gameSessionID);
        System.out.println("LEADERBOARD: " + map);

        return ResponseEntity.ok(map);
    }

    /**
     * postScore - method for entering a users score in the scoreHashmap
     * @param gameSessionID - game session identifier
     * @param player - player name
     * @param score - score of the player
     */
    @PostMapping("/scores/{player}/{score}")
    public void postScore(@RequestBody Identifier gameSessionID, @PathVariable String player, @PathVariable String score) {
        GameSession playerGameSession = game.get(gameSessionID);

        Integer s = Integer.parseInt(score);

        System.out.println("PlayerController -> postScore: " + player + " | score: " + score + " [=] " + waitingLobby.getGameSessionID() + " | " + playerGameSession.getGameSessionID() + " | " + gameSessionID);

        playerGameSession.putScore(player, s);
    }

//######################################################################################################################

    /**
     * Getter for game used for testing
     *
     * @return game parameter
     */
    public Game getGame() {
        return game;
    }

    /**
     * Getter for waiting lobby used for testing
     *
     * @return the waiting lobby
     */
    public GameSession getWaitingLobby() {
        return waitingLobby;
    }

}


//    @PostMapping("/game/get_leaderboard")
//    public DeferredResult<ResponseEntity<Map<String, Integer>>> getLeaderboard(@RequestBody Identifier identifier, @PathVariable String name) {
//
//        System.out.println("PlayerController -> getWaitingLobbyUpdates: " + name);
//
//        GameSession gameSession = game.get(identifier);
//
//        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // NO_CONTENT -> 204
//
//        var res  = new DeferredResult<ResponseEntity<Map<String, Integer>>>(10000L, noContent);
//
//        gameSession.put(name, map -> res.setResult(ResponseEntity.ok(map)));
//
//        return res;
//    }
