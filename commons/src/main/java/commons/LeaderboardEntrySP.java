package commons;

import java.util.Objects;
import javax.persistence.*;


@Entity
public class LeaderboardEntrySP {


    public @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    public String name;
    public int score;

    public LeaderboardEntrySP(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public LeaderboardEntrySP() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeaderboardEntrySP)) return false;
        LeaderboardEntrySP that = (LeaderboardEntrySP) o;
        return getId() == that.getId() && getScore() == that.getScore() && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getScore());
    }

    @Override
    public String toString() {
        return "LeaderboardEntrySP{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }

    /**
     * This method is for getting the text to be displayed in the application.
     * @return text displayed in scene
     */
    public String toDisplay() {
        return name + "\t\t\t\t\t" + getScore(); //todo: add score labels in scene
    }
}
