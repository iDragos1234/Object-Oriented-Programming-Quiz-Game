package commons.Questions;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Type2QuestionTest {

    List<Activity> activities;

    @BeforeEach
    void setUp() {
        var a = new Activity("a", "image-path", "a", 1, "source");
        var b = new Activity("b", "image-path", "b", 2, "source");
        var c = new Activity("c", "image-path", "c", 3, "source");
        activities = new ArrayList<>();
        activities.add(a);
        activities.add(b);
        activities.add(c);
    }

    @Test
    void TestConstructor() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                new Type2Question(activities.get(0), new ArrayList<>());
            }
        });
    }

    @Test
    void checkAnswer() {
        Type2Question question = new Type2Question(activities.get(0), activities);
        assertFalse(question.checkAnswer());
        question.setUserAnswer("a");
        assertTrue(question.checkAnswer());
        question.setUserAnswer("b");
        assertFalse(question.checkAnswer());
    }

    @Test
    void getQuestionText() {
        Type2Question question = new Type2Question(activities.get(1), activities);
        assertEquals(question.getQuestionText(), "How much energy does it take?");
        assertNotEquals(question.getQuestionText(), "this is question text");
    }

    @Test
    void type() {
        Type2Question question = new Type2Question(activities.get(1), activities);
        assertEquals(question.type(), 2);
    }

    @Test
    void getAttachment() {
        Type2Question question = new Type2Question(activities.get(2), activities);
        Activity activity = new Activity("c", "image-path", "c", 3, "source");
        assertEquals(question.getAttachment(), activity);
    }

    @Test
    void getCorrectAnswer() {
        Type2Question question = new Type2Question(activities.get(2), activities);
        Activity activity = new Activity("c", "image-path", "c", 3, "source");
        assertEquals(question.getCorrectAnswer(), activity);
    }

    @Test
    void getCapacityAnswers() {
        Type2Question question = new Type2Question(activities.get(1), activities);
        Type2Question question2 = new Type2Question(activities.get(2), activities);
        List<Activity> a = new LinkedList<>();
        a.add(activities.get(2));
        Type2Question question3 = new Type2Question(activities.get(2), a);
        assertEquals(question3.getCapacityAnswers(), 1);
        assertEquals(question.getCapacityAnswers(), 3);
        assertEquals(question2.getCapacityAnswers(), 3);
    }

    @Test
    void getAnswers() {
        Type2Question question2 = new Type2Question(activities.get(2), activities);
        assertEquals(activities, question2.getAnswers());
    }

    @Test
    void setAnswers() {
        Type2Question question2 = new Type2Question(activities.get(2), activities);
        List<Activity> a = new ArrayList<>();
        var one = new Activity("1", "image-path", "1", 1, "source");
        var two = new Activity("2", "image-path", "2", 2, "source");
        var three = new Activity("3", "image-path", "3", 3, "source");
        a.add(one);
        a.add(two);
        a.add(three);
        question2.setAnswers(a);
        assertNotEquals(question2.getAnswers(), activities);
        assertEquals(question2.getAnswers(), a);
    }

    @Test
    void getUserAnswer() {
        Type2Question question = new Type2Question(activities.get(1), activities);
        assertNull(question.getUserAnswer());
        question.setUserAnswer("a");
        assertEquals(question.getUserAnswer(), "a");

    }

    @Test
    void setUserAnswer() {
        Type2Question question = new Type2Question(activities.get(2), activities);
        question.setUserAnswer("b");
        assertNotNull(question.getUserAnswer());
        assertEquals(question.getUserAnswer(), "b");
    }

    @Test
    void getAnswerByIndex() {
        Type2Question question = new Type2Question(activities.get(2), activities);
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
        Type2Question question = new Type2Question(activities.get(2), activities);
        String a = "[How much energy does it take?\n\t\t" + activities.get(2) + "\n\t\t(";
        for (Activity activity : activities) {
            a += activity + ", ";
        }
        a += ")\n\t\t" + null + "\n]";
        assertEquals(question.toString(), a);
    }

    @Test
    void testEquals() {
        var cc = new Activity("c", "image-path", "c", 3, "source");
        Type2Question question = new Type2Question(activities.get(2), activities);
        Type2Question question1 = new Type2Question(cc, activities);
        assertTrue(question.equals(question1));
    }

    @Test
    void testHashCode() {
        var cc = new Activity("c", "image-path", "c", 3, "source");
        Type2Question question = new Type2Question(activities.get(2), activities);
        Type2Question question1 = new Type2Question(cc, activities);
        assertEquals(question.hashCode(), question1.hashCode());
    }
}