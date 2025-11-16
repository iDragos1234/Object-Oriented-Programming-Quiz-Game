package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameSessionTest {

    @Test
    public void checkGetSetScore() {
        GameSession gs = new GameSession();
        gs.setScore(500);
        assertEquals(500, gs.getScore());
    }

    @Test
    public void checkGetSetUser() {
        GameSession gs = new GameSession();
        LeaderboardEntrySP user = new LeaderboardEntrySP("Filip", 125);
        gs.setUserSP(new LeaderboardEntrySP("Filip", 125));
        assertEquals(125, gs.getScore());
        assertEquals(user, gs.getUserSP());
    }

    @Test
    public void checkGetSetPowerUps() {
        GameSession gs = new GameSession();
        gs.setDoubleUpUsed(true);
        gs.setJokerUsed(true);
        assertTrue(gs.isDoubleUpUsed());
        assertTrue(gs.isJokerUsed());
    }

    @Test
    public void checkEquals() {
        GameSession gs = new GameSession();
        GameSession gs1 = new GameSession();
        LeaderboardEntrySP user = new LeaderboardEntrySP("Filip", 125);
        LeaderboardEntrySP user1 = new LeaderboardEntrySP("Filip", 125);
        gs.setUserSP(user);
        gs1.setUserSP(user1);
        assertEquals(gs1,gs);
    }
}