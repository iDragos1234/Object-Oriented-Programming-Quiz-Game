package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdentifierTest {
    @Test
    void nextID() {
        Identifier id = new Identifier();
        Identifier checker = new Identifier();
        assertEquals(id.nextID(),checker);
    }

    @Test
    void testEquals() {
        Identifier id = new Identifier();
        Identifier checker = new Identifier();
        int i = 0;
        while(i >0) {
            id.nextID();
            checker.nextID();
            i--;
        }
        assertEquals(id,checker);
    }

    @Test
    void testHashCode() {
        Identifier id = new Identifier();
        Identifier checker = new Identifier();
        assertEquals(id.hashCode(),checker.hashCode());
    }

    @Test
    void testToString() {
        Identifier id = new Identifier();
        assertEquals(id.toString(),"Identifier{"+ "id1=0, "+"id2=0, "+"id3=0, "+"id4=0"+'}');
    }
}