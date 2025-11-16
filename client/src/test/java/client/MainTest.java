package client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    /**
     * Example score calculation test cases for both types of questions
     */
    @Test
    void testCalculateScore() {
        assertEquals(57,Main.calculateScore(2));
        assertEquals(67,Main.calculateScore(5));
        assertEquals(70,Main.calculateScore(2,850,750));
        assertEquals(74,Main.calculateScore(2,750,750));
        assertEquals(21,Main.calculateScore(1,-500,750));
    }
}