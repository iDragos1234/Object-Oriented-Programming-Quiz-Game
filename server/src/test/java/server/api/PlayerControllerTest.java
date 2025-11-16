package server.api;

import commons.Identifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import server.Game.Game;
import server.Game.GameSession;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {

    private PlayerController playerController;

    @BeforeEach
    void setUp() {
        playerController = new PlayerController();
    }


    @Test
    void createWaitingLobby() {
        playerController.createWaitingLobby();
        Game game = new Game();
        game.addNewGameSession(new GameSession());
        assertEquals(playerController.getWaitingLobby().getGameSessionID(), game.getWaitingLobby().getGameSessionID());

    }


    @Test
    void retrieveGameSessionID() {

        Identifier identifier = new Identifier();
        assertEquals(ResponseEntity.ok(identifier), playerController.retrieveGameSessionID());
    }

    @Test
    void getWaitingLobbyUpdates() {

        String name = "OvO";
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<String>>(10000L, noContent);

        playerController.getWaitingLobby().put(name, message -> {
            res.setResult(ResponseEntity.ok(message));
        });
        assertEquals(playerController.getWaitingLobbyUpdates(name).getResult(), res.getResult());
    }

    @Test
    void startGame() {
        playerController.startGame();
        createWaitingLobby();
    }

    @Test
    void getListQuestions() {

        GameSession playerGameSession = playerController.getGame().get(new Identifier());
        assertEquals(playerController.getListQuestions(new Identifier()), playerGameSession.getListQuestions());
    }

    @Test
    void lobbyEventTest() {
        playerController.getWaitingLobby().setNumPlayers(0);
        Identifier id = playerController.getWaitingLobby().getGameSessionID();
        String player = "player";
        assertEquals(playerController.lobbyEvent(id, player, "check"), ResponseEntity.ok(0));
        assertEquals(playerController.lobbyEvent(id, player, "joined"), ResponseEntity.ok(1));
        assertEquals(playerController.lobbyEvent(id, player, "left"), ResponseEntity.ok(0));
        assertEquals(playerController.lobbyEvent(id, player, ""), ResponseEntity.badRequest().build());

    }

    @Test
    void checkForSameName() {

        assertEquals(false, playerController.checkForSameName("Tomato"));
        Identifier id = playerController.getWaitingLobby().getGameSessionID();
        playerController.getWaitingLobby().getConsumerHashMap().put("Potato", null);
        assertEquals(playerController.checkForSameName("Potato"), true);
    }

    @Test
    void emojiGreen() {

        GameSession gameSession = new GameSession();
        assertEquals(playerController.emojiPost(playerController.getWaitingLobby().getGameSessionID(), "check"), ResponseEntity.ok(gameSession.getRGB()));
        gameSession.setRGB(new int[]{-3, 3, -3});
        assertEquals(playerController.emojiPost(playerController.getWaitingLobby().getGameSessionID(), "green"), ResponseEntity.ok(gameSession.getRGB()));
    }

    @Test
    void emojiRed() {

        GameSession gameSession = new GameSession();
        gameSession.setRGB(new int[]{3, -3, -3});
        assertEquals(playerController.emojiPost(playerController.getWaitingLobby().getGameSessionID(), "red"), ResponseEntity.ok(gameSession.getRGB()));
    }

    @Test
    void emojiBlue() {

        GameSession gameSession = new GameSession();
        gameSession.setRGB(new int[]{-3, -3, 3});
        assertEquals(playerController.emojiPost(playerController.getWaitingLobby().getGameSessionID(), "blue"), ResponseEntity.ok(gameSession.getRGB()));
    }
}