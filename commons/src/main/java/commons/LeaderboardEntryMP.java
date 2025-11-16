package commons;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class LeaderboardEntryMP {


    public @Id
    String name;
    public int score;

    public LeaderboardEntryMP(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public LeaderboardEntryMP() {

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
        if (!(o instanceof LeaderboardEntryMP)) return false;
        LeaderboardEntryMP that = (LeaderboardEntryMP) o;
        return getScore() == that.getScore() && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getScore());
    }

    @Override
    public String toString() {
        return "LeaderboardEntryMP{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }

    /**
     * This method is for getting the text to be displayed in the application.
     * @return text displayed in scene
     */
    public String toDisplay() {
        return name + " - " + score; //todo: add score labels in scene
    }
}
