package commons;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class WaitingLobby {

    /**
     * An instance of this class holds a member of the waiting lobby
     * When the MP game is started, we should gather all WaitingLobby entities, and put them in the same game
     */
    @Id
    public String name;

    /**
     * Gets the ID of the Waiting Lobby
     * @return a String that is the Waiting Lobby's ID
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the ID of the Waiting Lobby
     * We must ensure that no two players can have the same name!
     * @param id
     */
    public void setName(String id) {
        this.name = id;
    }

    /**
     * Constructor
     * @param name
     */
    public WaitingLobby(String name) {
        this.name = name;
    }

    public WaitingLobby() {

    }

    /**
     * Returns all of the fields in a string format
     *
     * @return
     */
    @Override
    public String toString() {
        return "WaitingLobby{" +
                "name=" + name +
                '}';
    }

    /**
     * Decides whether the Object o is equals to the current object
     * @param o an Object
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaitingLobby that = (WaitingLobby) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


}
