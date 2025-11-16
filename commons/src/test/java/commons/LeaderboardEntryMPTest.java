package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LeaderboardEntryMPTest {

    @Test
    public void checkConstructor() {
        var l = new LeaderboardEntryMP("fi", 1);
        assertEquals("fi", l.name);
        assertEquals(1, l.score);
    }

    @Test
    void testGetName() {
        var l = new LeaderboardEntryMP("fi", 1);
        assertEquals("fi", l.getName());
    }

    @Test
    void testSetName() {
        var l = new LeaderboardEntryMP("fi", 1);
        l.setName("ha");
        assertEquals("ha", l.name);
    }

    @Test
    void testGetScore() {
        var l = new LeaderboardEntryMP("fi", 1);
        assertEquals(1, l.getScore());
    }

    @Test
    void testSetScore() {
        var l = new LeaderboardEntryMP("fi", 1); //change this after issue #52 merged
        l.setScore(10);
        assertEquals(10, l.score);
    }

    @Test
    void testEquals() {
        var l = new LeaderboardEntryMP("fi", 1);
        var l2 = new LeaderboardEntryMP("fi", 1);
        assertEquals(l, l2);
    }

    @Test
    void testNotEquals() {
        var l = new LeaderboardEntryMP("fi", 1);
        var l2 = new LeaderboardEntryMP("ha", 10);
        assertNotEquals(l, l2);
    }

    @Test
    public void equalsHashCode() {
        var l = new LeaderboardEntryMP("fi", 1);
        var l2 = new LeaderboardEntryMP("fi", 1);
        assertEquals(l, l2);
        assertEquals(l.hashCode(), l2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var l = new LeaderboardEntryMP("fi", 1);
        var l2 = new LeaderboardEntryMP("ha", 10);
        assertNotEquals(l, l2);
        assertNotEquals(l.hashCode(), l2.hashCode());
    }

    @Test
    public void hasToString() {
        var actual = new LeaderboardEntryMP("fi", 1).toString();
        assertTrue(actual.contains("name"));
        assertTrue(actual.contains("score"));
    }
}