package commons.Questions;

import commons.Activity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenType2QuestionTest {

    @Test
    void checkAnswer() {
        OpenType2Question q = new OpenType2Question(new Activity("Id", "path", "title",123, "source"));
        q.setUserAnswer("123");
        assertTrue(q.checkAnswer());
    }

    @Test
    void getQuestionText() {
        OpenType2Question q = new OpenType2Question(new Activity("Id", "path", "title",123, "source"));
        assertEquals(q.getQuestionText(),"How much energy does it take?");
    }

    @Test
    void type() {
        OpenType2Question q = new OpenType2Question(new Activity("Id", "path", "title",123, "source"));
        assertEquals(q.type(),0);
    }

    @Test
    void getAttachment() {
        Activity a = new Activity("Id", "path", "title",123, "source");
        OpenType2Question q = new OpenType2Question(a);
        assertEquals(q.getAttachment(),a);
    }

    @Test
    void getCorrectAnswer() {
        Activity a = new Activity("Id", "path", "title",123, "source");
        OpenType2Question q = new OpenType2Question(a);
        assertEquals(q.getCorrectAnswer(),a);
    }

    @Test
    void setAttachment() {
        Activity a = new Activity("Id", "path", "title",123, "source");
        Activity b = new Activity("Id2", "path2", "title2",123, "source2");
        OpenType2Question q = new OpenType2Question(a);
        q.setAttachment(b);
        assertEquals(q.getAttachment(),b);
    }

    @Test
    void getCapacityAnswers() {
        OpenType2Question q = new OpenType2Question();
        assertEquals(q.getCapacityAnswers(),0);
    }

    @Test
    void getAnswers() {
        OpenType2Question q = new OpenType2Question();
        assertNull(q.getAnswers());
    }

    @Test
    void getUserAnswer() {
        OpenType2Question q = new OpenType2Question();
        assertNull(q.getUserAnswer());
    }

    @Test
    void setUserAnswer() {
        OpenType2Question q = new OpenType2Question();
        q.setUserAnswer("123");
        assertEquals(q.getUserAnswer(),"123");
    }

    @Test
    void getAnswerByIndex() {
        OpenType2Question q = new OpenType2Question();
        assertNull(q.getAnswerByIndex(123));
    }

    @Test
    void testToString() {
        OpenType2Question q = new OpenType2Question();
        assertEquals(q.toString(),"[How much energy does it take?\n" +
                "\t\tnull\n" +
                "\t\tnull\n" +
                "]");
    }

    @Test
    void testEquals() {
        Activity a = new Activity("Id", "path", "title",123, "source");
        OpenType2Question q = new OpenType2Question();
        q.setAttachment(a);
        OpenType2Question w = new OpenType2Question();
        w.setAttachment(a);
        assertTrue(q.equals(w));
        assertEquals(q.hashCode(),w.hashCode());
    }
}