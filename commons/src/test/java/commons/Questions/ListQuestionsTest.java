package commons.Questions;

import commons.Activity;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ListQuestionsTest {

    @Test
    void getQuestions() {
        Question[] qs =new Question[1];
        qs[0] = new OpenType2Question(new Activity("Id", "path", "title",123, "source"));
        ListQuestions listQuestions = new ListQuestions(4);
        listQuestions.setQuestions(qs);
        assertArrayEquals(listQuestions.getQuestions(),qs);
    }

    @Test
    void setQuestions() {
        Question[] qs =new Question[1];
        qs[0] = new OpenType2Question(new Activity("Id", "path", "title",123, "source"));
        ListQuestions l1 = new ListQuestions(1);
        ListQuestions l2 = new ListQuestions(1);
        l1.setQuestions(qs);
        l2.setQuestions(qs);
        assertEquals(l1.getQuestions(),l2.getQuestions());
    }

    @Test
    void getCounter() {
        ListQuestions l1 = new ListQuestions(1);
        assertEquals(l1.getCounter(),0);
    }

    @Test
    void setCounter() {
        ListQuestions l1 = new ListQuestions(1);
        l1.setCounter(5);
        assertEquals(l1.getCounter(),5);
    }

    @Test
    void getCapacity() {
        ListQuestions l1 = new ListQuestions(1);
        assertEquals(l1.getCapacity(),1);
    }

    @Test
    void setCapacity() {
        ListQuestions l1 = new ListQuestions(1);
        l1.setCapacity(10);
        assertEquals(l1.getCapacity(),10);
    }

    @Test
    void getRandom() throws IllegalAccessException {
        ListQuestions l1 = new ListQuestions(1);
        assertInstanceOf(Random.class,l1.getRandom());
    }

    @Test
    void setRandom() {
        ListQuestions l1 = new ListQuestions(1);
        Random random = new Random(1);
        l1.setRandom(random);
        assertEquals(l1.getRandom(),random);
    }

    @Test
    void hasNextQuestion() {
        ListQuestions l1 = new ListQuestions(5);
        assertTrue(l1.hasNextQuestion());
        l1.nextQuestion();
        assertTrue(l1.hasNextQuestion());
    }

    @Test
    void nextQuestion() {
        Question[] qs =new Question[]{new OpenType2Question(new Activity("Id", "path", "title",123, "source"))};
        ListQuestions l1 = new ListQuestions(1);
        l1.setQuestions(qs);
        assertEquals(l1.nextQuestion(),qs[0]);
    }

    @Test
    void testToString() {
        Question[] qs =new Question[]{new OpenType2Question(new Activity("Id", "path", "title",123, "source"))};
        ListQuestions l1 = new ListQuestions(1);
        l1.setQuestions(qs);
        assertEquals(l1.toString(),"ListQuestions{, counter=0, capacity=1, random="+l1.getRandom()+"\n" +
                "\n" +
                "questions (table):\n" +
                "\n" +
                "0) [How much energy does it take?\n" +
                "\t\tActivity{id='Id', image_path='path', title='title', consumption_in_wh=123, source='source', index=0}\n" +
                "\t\tnull\n" +
                "]\n" +
                "}");
    }

    @Test
    void questionsToString() {
        Question[] qs =new Question[]{new OpenType2Question(new Activity("Id", "path", "title",123, "source"))};
        ListQuestions l1 = new ListQuestions(1);
        l1.setQuestions(qs);
        assertEquals(l1.questionsToString(),"" + "\n"+
                "0) [How much energy does it take?\n" +
                "\t\tActivity{id='Id', image_path='path', title='title', consumption_in_wh=123, source='source', index=0}\n" +
                "\t\tnull\n" +
                "]\n");
    }

    @Test
    void testEquals() {
        Question[] qs =new Question[]{new OpenType2Question(new Activity("Id", "path", "title",123, "source"))};
        ListQuestions l1 = new ListQuestions(1);
        l1.setQuestions(qs);
        ListQuestions l2 = new ListQuestions(1);
        l2.setQuestions(qs);
        assertFalse(l1.equals(l2));
        l2.setRandom(l1.getRandom());
        assertTrue(l1.equals(l2));
    }

    @Test
    void testHashCode() {
        Question[] qs =new Question[]{new OpenType2Question(new Activity("Id", "path", "title",123, "source"))};
        ListQuestions l1 = new ListQuestions(1);
        l1.setQuestions(qs);
        ListQuestions l2 = new ListQuestions(1);
        l2.setQuestions(qs);
        l2.setRandom(l1.getRandom());
        assertEquals(l1.hashCode(),l2.hashCode());
    }
}