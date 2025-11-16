package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ActivityTest {
    @Test
    public void checkConstructor() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        assertEquals("fi", a.id);
        assertEquals("image-path", a.image_path);
        assertEquals("ti", a.title);
        assertEquals(1, a.consumption_in_wh);
        assertEquals("source", a.source);
    }

    @Test
    void testEquals() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var a2 = new Activity("fi", "image-path", "ti", 1, "source");

        assertEquals(a, a2);
    }

    @Test
    void testNotEquals() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var a2 = new Activity("gi", "photo-path", "si", 12, "inspiration");
        assertNotEquals(a, a2);
    }

    @Test
    public void equalsHashCode() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var a2 = new Activity("fi", "image-path", "ti", 1, "source");

        assertEquals(a, a2);
        assertEquals(a.hashCode(), a2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        var a2 = new Activity("gi", "photo-path", "si", 12, "inspiration");
        assertNotEquals(a, a2);
        assertNotEquals(a.hashCode(), a2.hashCode());
    }

    @Test
    void testToString() {
        var actual = new Activity("fi", "image-path", "ti", 1, "source").toString();
        assertTrue(actual.contains("title"));
        assertTrue(actual.contains("consumption"));
    }

    @Test
    void testGetId() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        assertEquals("fi", a.getId());
    }

    @Test
    void testSetId() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        a.setId("gi");
        assertEquals("gi", a.getId());
    }

    @Test
    void testGetTitle() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        assertEquals("ti", a.getTitle());
    }

    @Test
    void testSetTitle() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        a.setTitle("gi");
        assertEquals("gi", a.getTitle());
    }

    @Test
    void testGetConsumption_in_wh() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        assertEquals(1, a.getConsumption_in_wh());
    }

    @Test
    void testSetConsumption_in_wh() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        a.setConsumption_in_wh(5);
        assertEquals(5, a.getConsumption_in_wh());
    }

    @Test
    void testGetImage_Path() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        assertEquals("image-path", a.getImage_path());
    }

    @Test
    void testSetImage_Path() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        a.setImage_path("photo-path");
        assertEquals("photo-path", a.getImage_path());
    }

    @Test
    void testGetSource() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        assertEquals("source", a.getSource());
    }

    @Test
    void testSetSource() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        a.setSource("inspiration");
        assertEquals("inspiration", a.getSource());
    }

    @Test
    void testGetIndex() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        assertEquals(0, a.getIndex());
    }

    @Test
    void testSetIndex() {
        var a = new Activity("fi", "image-path", "ti", 1, "source");
        a.setIndex(1);
        assertEquals(1, a.getIndex());
    }
}
