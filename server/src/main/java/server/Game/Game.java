package server.Game;

import commons.Identifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Game {

    private HashMap<Identifier, GameSession> gameSessionHashMap;
    private GameSession waitingLobby;

    private Identifier id = new Identifier();

    public Game() {
        gameSessionHashMap = new HashMap<>();

        addNewGameSession(new GameSession());
    }


    public void addNewGameSession(GameSession gameSession) {

        if(waitingLobby != null) System.out.println("Game session is over: numPlayers: " + waitingLobby.getNumPlayers() + " | HashTable: " + gameSessionHashMap);
        System.out.println("NEW WAITING LOBBY!");

        // assign the next game session ID in the Identifier class sequence
        Identifier gameSessionID = id.nextID();
        gameSession.setGameSessionID(gameSessionID);

        put(gameSessionID, gameSession);
        waitingLobby = gameSession;
    }

    public GameSession getWaitingLobby() {
        return waitingLobby;
    }

    // HashMap methods
    public GameSession get(Identifier id) {
        return gameSessionHashMap.get(id);
    }

    private void put(Identifier id, GameSession gameSession) {
        gameSessionHashMap.put(id, gameSession);
    }

    public boolean containsKey(Identifier id) {
        return gameSessionHashMap.containsKey(id);
    }

    public boolean containsValue(GameSession gameSession) {
        return gameSessionHashMap.containsValue(gameSession);
    }

    public GameSession remove(Identifier id) {
        return gameSessionHashMap.remove(id);
    }




    public int size() {
        return gameSessionHashMap.size();
    }

    public Set<Identifier> keySet() {
        return gameSessionHashMap.keySet();
    }

    public Collection<GameSession> values() {
        return gameSessionHashMap.values();
    }

    public Set<Map.Entry<Identifier, GameSession>> entrySet() {
        return gameSessionHashMap.entrySet();
    }

    public void clear() {
        gameSessionHashMap.clear();
    }
}
