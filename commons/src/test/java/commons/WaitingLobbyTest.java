package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaitingLobbyTest {

    @Test
    void testGetName() {
        String l = "FirstPlayer";
        WaitingLobby w = new WaitingLobby(l);
        assertEquals("FirstPlayer", w.getName());
    }

    @Test
    void testSetName() {
        String l = "FirstPlayer";
        WaitingLobby w = new WaitingLobby(l);
        w.setName("SecondPlayer");
        assertEquals("SecondPlayer", w.getName());
    }

    @Test
    void testEquals() {
        String l = "FirstPlayer";
        WaitingLobby w1 = new WaitingLobby(l);
        WaitingLobby w2 = new WaitingLobby(l);
        assertEquals(w1, w2);
    }

    @Test
    void testNotEquals() {
        String l = "FirstPlayer";
        WaitingLobby w1 = new WaitingLobby(l);
        l = "SecondPlayer";
        WaitingLobby w2 = new WaitingLobby(l);
        assertNotEquals(w1, w2);
    }

    @Test
    void testHashCode() {
        String l = "FirstPlayer";
        WaitingLobby w1 = new WaitingLobby(l);
        WaitingLobby w2 = new WaitingLobby(l);
        assertEquals(w1, w2);
        assertEquals(w1.hashCode(), w2.hashCode());
    }

    @Test
    void testNotHashCode() {
        String l = "FirstPlayer";
        WaitingLobby w1 = new WaitingLobby(l);
        l = "SecondPlayer";
        WaitingLobby w2 = new WaitingLobby(l);
        assertNotEquals(w1, w2);
        assertNotEquals(w1.hashCode(), w2.hashCode());
    }
}