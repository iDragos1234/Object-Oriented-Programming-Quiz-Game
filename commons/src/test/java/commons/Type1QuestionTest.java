package commons;

import commons.Questions.Type1Question;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Type1QuestionTest {

    @Test
    void testGetQuestionText() {
        List<Activity> aList = new LinkedList<Activity>();
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var b = new Activity("fii", "image-path", "tii", 2, "source");
        var c = new Activity("ffi", "image-path", "tti", 3, "source");
        aList.add(a);
        aList.add(b);
        aList.add(c);
        Type1Question q = new Type1Question(aList);
        assertEquals("What requires more energy?", q.getQuestionText());
    }

    @Test
    void testGetCorrectAnswer() {
        List<Activity> aList = new LinkedList<Activity>();
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var b = new Activity("fii", "image-path", "tii", 2, "source");
        var c = new Activity("ffi", "image-path", "tti", 3, "source");
        aList.add(a);
        aList.add(b);
        aList.add(c);
        Type1Question q = new Type1Question(aList);
        assertEquals(c, q.getCorrectAnswer());
    }

    @Test
    void testType() {
        List<Activity> aList = new LinkedList<Activity>();
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var b = new Activity("fii", "image-path", "tii", 2, "source");
        var c = new Activity("ffi", "image-path", "tti", 3, "source");
        aList.add(a);
        aList.add(b);
        aList.add(c);
        Type1Question q = new Type1Question(aList);
        assertEquals(1, q.type());
    }

    @Test
    void testGetCapacityAnswers() {
        List<Activity> aList = new LinkedList<Activity>();
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var b = new Activity("fii", "image-path", "tii", 2, "source");
        var c = new Activity("ffi", "image-path", "tti", 3, "source");
        aList.add(a);
        aList.add(b);
        aList.add(c);
        Type1Question q = new Type1Question(aList);
        assertEquals(3, q.getCapacityAnswers());
    }

    @Test
    void testGetAnswers() {
        List<Activity> aList = new LinkedList<Activity>();
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var b = new Activity("fii", "image-path", "tii", 2, "source");
        var c = new Activity("ffi", "image-path", "tti", 3, "source");
        aList.add(a);
        aList.add(b);
        aList.add(c);
        Type1Question q = new Type1Question(aList);
        assertEquals(aList, q.getAnswers());
    }

    @Test
    void testGetSetUserAnswer() {
        List<Activity> aList = new LinkedList<Activity>();
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var b = new Activity("fii", "image-path", "tii", 2, "source");
        var c = new Activity("ffi", "image-path", "tti", 3, "source");
        aList.add(a);
        aList.add(b);
        aList.add(c);
        Type1Question q = new Type1Question(aList);
        assertEquals(null, q.getUserAnswer());
        q.setUserAnswer("testString");
        assertEquals("testString", q.getUserAnswer());
    }

    @Test
    void testEquals() {
        List<Activity> aList = new LinkedList<Activity>();
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var b = new Activity("fii", "image-path", "tii", 2, "source");
        var c = new Activity("ffi", "image-path", "tti", 3, "source");
        aList.add(a);
        aList.add(b);
        aList.add(c);
        Type1Question q1 = new Type1Question(aList);
        Type1Question q2 = new Type1Question(aList);
        assertEquals(q1, q2);
    }

    @Test
    void testNotEquals() {
        List<Activity> aList = new LinkedList<Activity>();
        List<Activity> bList = new LinkedList<Activity>();
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var b = new Activity("fii", "image-path", "tii", 2, "source");
        var c = new Activity("ffi", "image-path", "tti", 3, "source");
        aList.add(a);
        aList.add(b);
        aList.add(c);
        bList.add(a);
        bList.add(b);
        Type1Question q1 = new Type1Question(aList);
        Type1Question q2 = new Type1Question(bList);
        assertNotEquals(q1, q2);
    }

    @Test
    void testHashCode() {
        List<Activity> aList = new LinkedList<Activity>();
        List<Activity> bList = new LinkedList<Activity>();
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var b = new Activity("fii", "image-path", "tii", 2, "source");
        var c = new Activity("ffi", "image-path", "tti", 3, "source");
        aList.add(a);
        aList.add(b);
        aList.add(c);
        bList.add(a);
        bList.add(b);
        Type1Question q1 = new Type1Question(aList);
        Type1Question q2 = new Type1Question(aList);
        Type1Question q3 = new Type1Question(bList);
        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());
        assertNotEquals(q1, q3);
        assertNotEquals(q1.hashCode(), q3.hashCode());
    }
}