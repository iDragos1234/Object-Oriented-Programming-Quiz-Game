package client;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.Questions.ListQuestions;

import java.util.Objects;

public class ClientListQuestions extends ListQuestions {

    /**
     * ListQuestions - class for the list of questions - implemented on the server side, for efficient retrieval of activities by the poll method
     * uses the ServerUtils injected instance to retrieve a random Activity from the database
     */

    /**
     * Field injected ServerUtils instance
     */
    @Inject
    private ServerUtils server;

    /**
     * empty constructor
     */
    public ClientListQuestions() {

    }

    /**
     * Constructor for this list of questions
     * @param capacity - the number of questions in this list of questions
     */
    public ClientListQuestions(int capacity) {
        super(capacity);
    }


    /**
     * pollActivity - retrieve a random activity from the ActivityRepository
     * the actual implementation in this subclass of this abstract method inherited from the parent class
     * uses the ServerUtils class as a means to retrieve activities
     * @return - a randomly-chosen Activity from the ActivityRepository
     */
    @Override
    public Activity pollActivity() {
        return server.getRandomActivity();
    }


    // getters and setters

    public ServerUtils getServer() {
        return server;
    }

    public void setServer(ServerUtils server) {
        this.server = server;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientListQuestions)) return false;
        if (!super.equals(o)) return false;
        ClientListQuestions that = (ClientListQuestions) o;
        return server.equals(that.server);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), server);
    }

}
