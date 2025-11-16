package server.Game;

import commons.Identifier;
import commons.Questions.ListQuestions;
import java.util.*;
import java.util.function.Consumer;


public class GameSession {

    /**
     * Game session class represents one of the game sessions available in the ongoing Game object
     * Before players start a game session, this game session acts as a waiting lobby
     *
     * @gameSessionID - 
     * @numPlayers - the number of players in this game session
     * @consumerHashMap - a hash map which hashes the player name String object to Consumer<String>, which represents the action that sends messages back to the player via long polling
     *                    This pair links each player in this game session to their corresponding communicator (represented by the Consumer object)
     * @listQuestions - the ListQuestions object associated with this game
     *                  This ListQuestions is initialized once a player starts the game, and then it is sent to all the other players in this game session
     * @leaderboardMultiplayer - a priority queue, which represents the multiplayer leaderboard.
     */
    private Identifier gameSessionID;
    private int numPlayers;
    private HashMap<String, Consumer<String>> consumerHashMap;

    private LinkedHashMap<String, Integer> leaderboard;

    private ListQuestions listQuestions;
    private int[] rgb = {95,158,160};

    public GameSession() {
        numPlayers = 0;
        consumerHashMap = new HashMap<>();

//        leaderboardLongPolling = new HashMap<>();

        leaderboard = new LinkedHashMap<>();
    }

    // Leaderboard Multiplayer Methods

    public Map<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    public void putScore(String name, Integer score) {
        leaderboard.put(name, score);
    }



    // HashMap methods

    public Consumer<String> get(String playerName) {
        return consumerHashMap.get(playerName);
    }

    public void put(String playerName, Consumer<String> consumer) {
        consumerHashMap.put(playerName, consumer);
    }

    public boolean containsKey(String playerName) {
        return consumerHashMap.containsKey(playerName);
    }

    public boolean containsValue(Consumer<String> consumer) {
        return consumerHashMap.containsValue(consumer);
    }

    public Consumer<String> remove(String playerName) {
        return consumerHashMap.remove(playerName);
    }

    public HashMap<String, Consumer<String>> getConsumerHashMap() {
        return consumerHashMap;
    }


    // additional HashMap methods

    public int size() {
        return consumerHashMap.size();
    }

    public Set<String> keySet() {
        return consumerHashMap.keySet();
    }

    public Collection<Consumer<String>> values() {
        return consumerHashMap.values();
    }

    public Set<Map.Entry<String, Consumer<String>>> entrySet() {
        return consumerHashMap.entrySet();
    }

    public void clear() {
        consumerHashMap.clear();
    }


    // ListQuestion methods

    public ListQuestions getListQuestions() {
        return listQuestions;
    }

    public void setListQuestions(ListQuestions listQuestions) {
        this.listQuestions = listQuestions;
    }

    // getters and setters

    public void setConsumerHashMap(HashMap<String, Consumer<String>> consumerHashMap) {
        this.consumerHashMap = consumerHashMap;
    }

    // numPlayers methods

    public int getNumPlayers() {
        return numPlayers;
    }
    public void addPlayer() {
        numPlayers++;
    }
    public void removePlayer() {
        numPlayers--;
    }
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }


    // Game session identifier methods

    public Identifier getGameSessionID() {
        return gameSessionID;
    }

    public void setGameSessionID(Identifier gameSessionID) {
        this.gameSessionID = gameSessionID;
    }


    // equals and hashCode methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSession that = (GameSession) o;
        return gameSessionID.equals(that.gameSessionID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameSessionID);
    }

    public int[] getRGB() {
        return rgb;
    }

    public void setRGB(int[] arr) {
        for(int i =0; i<3; i++){
            System.out.println(rgb[i]);
            if(rgb[i]+arr[i]>255) rgb[i]=255;
            else if(rgb[i]+arr[i]<0) rgb[i]=0;
            else rgb[i]=arr[i]+rgb[i];
        }
    }
}
