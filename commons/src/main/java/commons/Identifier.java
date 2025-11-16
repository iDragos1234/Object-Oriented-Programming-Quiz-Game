package commons;


import java.util.Objects;


/**
 * Identifier - this class is used to identify each GameSession object within the Game.gameSessionHasMap.
 * Thus, each player can offer their gameSessionID when communicating with the server to map the player
 * to their corresponding GameSession.
 */
public class Identifier {

    /**
     * This class has 4 long fields for better identification tags.
     */
    public long id1;
    public long id2;
    public long id3;
    public long id4;

    /**
     * constructor for this class - sets all fields to default value 0.
     */
    public Identifier() {
        id1 = id2 = id3 = id4 = 0;
    }

    /**
     * nextID - this method retrieves the next ID from the Identifier sequence.
     * @return - an Identifier object.
     */
    public Identifier nextID() {
        Identifier res = deepCopy(this);
        incrementID();
        return res;
    }

    /**
     * deepCopy - this method performs a deep copy operation on the given Identifier object
     * @param original - the given original Identifier
     * @return - a deep copy of the original
     */
    private static Identifier deepCopy(Identifier original) {
        Identifier copy = new Identifier();

        copy.id4 = original.id4;
        copy.id3 = original.id3;
        copy.id2 = original.id2;
        copy.id1 = original.id1;

        return copy;
    }

    /**
     * set this Identifier to the next ID in the sequence
     */
    private void incrementID() {
        long factor = Long.MAX_VALUE-1;

        if(this.id4 == factor) {
            id1 = id2 = id3 = id4 = 0;
            return;
        }

         id4 = (id4 + id3/factor)*(1 - id4/factor);
         id3 = (id3 + id2/factor)*(1 - id3/factor);
         id2 = (id2 + id1/factor)*(1 - id2/factor);
         id1 = (id1+1)*(1 - id1/factor);
    }

    /**
     * equals method
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return id1 == that.id1 && id2 == that.id2 && id3 == that.id3 && id4 == that.id4;
    }

    /**
     * hash code method
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, id3, id4);
    }

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return "Identifier{" +
                "id1=" + id1 +
                ", id2=" + id2 +
                ", id3=" + id3 +
                ", id4=" + id4 +
                '}';
    }
}
