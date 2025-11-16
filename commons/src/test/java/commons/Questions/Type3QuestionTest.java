package commons.Questions;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Type3QuestionTest {

    List<Activity> activities;

    @BeforeEach
    void setUp() {
        var a = new Activity("a", "image-path", "a", 111, "source");
        var b = new Activity("b", "image-path", "b", 22, "source");
        var c = new Activity("c", "image-path", "c", 3, "source");
        activities = new ArrayList<>();
        activities.add(a);
        activities.add(b);
        activities.add(c);
    }

    @Test
    void TestConstructor() {
        Type3Question question = new Type3Question(activities);
        assertEquals(question.getCorrectAnswer(), new Activity("c", "image-path", "c", 3, "source"));
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                activities.remove(0);
                new Type3Question(activities);
            }
        });
    }

    @Test
    void checkAnswer() {
        Type3Question question = new Type3Question(activities);
        assertFalse(question.checkAnswer());
        question.setUserAnswer("a");
        assertFalse(question.checkAnswer());
        question.setUserAnswer("c");
        assertTrue(question.checkAnswer());
    }

    @Test
    void getQuestionText() {
        Type3Question question = new Type3Question(activities);
        assertEquals("Instead of ..., you could do instead ...", question.getQuestionText());
    }

    @Test
    void type() {
        Type3Question question = new Type3Question(activities);
        assertEquals(question.type(), 3);
    }

    @Test
    void getAttachment() {
        Type3Question question = new Type3Question(activities);
        assertEquals(question.getAttachment(), new Activity("b", "image-path", "b", 22, "source"));
    }

    @Test
    void getAnswers() {
        Type3Question question1 = new Type3Question(activities);
        assertEquals(question1.getAnswers(), activities);
    }

    @Test
    void setAnswers() {
        Type3Question question1 = new Type3Question(activities);
        List<Activity> a = new ArrayList<>();
        a.add(new Activity("AAA", "image-path", "AA", 111, "source"));
        a.add(new Activity("BBB", "image-path", "BB", 222, "source"));
        a.add(new Activity("CCC", "image-path", "CCC", 333, "source"));
        question1.setAnswers(a);
        assertEquals(question1.getAnswers(), a);
    }

    @Test
    void getCapacityAnswers() {
        Type3Question question1 = new Type3Question(activities);
        assertEquals(question1.getCapacityAnswers(), 2);
    }

    @Test
    void getUserAnswer() {
        Type3Question question1 = new Type3Question(activities);
        assertEquals(null, question1.getUserAnswer());
    }

    @Test
    void setUserAnswer() {
        Type3Question question1 = new Type3Question(activities);
        question1.setUserAnswer("AAA");
        assertEquals("AAA", question1.getUserAnswer());
    }

    @Test
    void getCorrectAnswer() {
        Type3Question question1 = new Type3Question(activities);
        assertEquals(question1.getCorrectAnswer(), new Activity("c", "image-path", "c", 3, "source"));
    }

    @Test
    void getAnswerByIndex() {
        Type3Question question = new Type3Question(activities);
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                question.getAnswerByIndex(3);
            }
        });
        assertEquals(question.getAnswerByIndex(1), activities.get(1));
    }

    @Test
    void testToString() {
        Type3Question question = new Type3Question(activities);
        String a = "[Instead of ..., you could do instead ...\n\t\t" + activities.get(1) + "\n\t\t(";
        for (Activity activity : activities) {
            a += activity + ", ";
        }
        a += ")\n\t\t" + null + "\n]";
        assertEquals(question.toString(), a);
    }

    @Test
    void testEquals() {
        activities.add(new Activity("d", "image-path", "d", 55555, "source"));
        Type3Question question = new Type3Question(activities);
        List<Activity> list = new ArrayList<>();

        list.add(new Activity("a", "image-path", "a", 111, "source"));
        list.add(new Activity("b", "image-path", "b", 22, "source"));
        list.add(new Activity("c", "image-path", "c", 3, "source"));
        list.add(new Activity("d", "image-path", "d", 55555, "source"));
        Type3Question question1 = new Type3Question(list);
        assertTrue(question.equals(question1));
    }

    @Test
    void testHashCode() {
        activities.add(new Activity("d", "image-path", "d", 55555, "source"));
        Type3Question question = new Type3Question(activities);
        List<Activity> list = new ArrayList<>();

        list.add(new Activity("a", "image-path", "a", 111, "source"));
        list.add(new Activity("b", "image-path", "b", 22, "source"));
        list.add(new Activity("c", "image-path", "c", 3, "source"));
        list.add(new Activity("d", "image-path", "d", 55555, "source"));
        Type3Question question1 = new Type3Question(list);
        assertEquals(question1.hashCode(), question.hashCode());
    }
}