package server;

import commons.Activity;
import commons.Questions.ListQuestions;
import server.database.ActivityRepository;

import java.util.Objects;


public class ServerListQuestions extends ListQuestions {

    /**
     * ListQuestions - class for the list of questions - implemented on the server side, for efficient retrieval of activities by the poll method
     * uses the ActivityRepository instance to retrieve a random Activity from the database
     */
    private ActivityRepository repo;

    /**
     * Constructor for this list of questions
     * @param repo - ActivityRepository instance (injected)
     * @param capacity - the number of questions in this list of questions
     */
    public ServerListQuestions(ActivityRepository repo, int capacity) {
        super(capacity);
        this.repo = repo;
    }

    /**
     * pollActivity - retrieve a random activity from the ActivityRepository
     * the actual implementation in this subclass of this abstract method inherited from the parent class
     * uses ActivityRepository as a means to retrieve activities
     * @return - a randomly-chosen Activity from the ActivityRepository
     *         - if no Activity was retrieved from the ActivityRepository, then return null
     */
    @Override
    public Activity pollActivity() {
        var idx = random.nextInt((int) repo.count());
        var res = repo.getByIndex(idx);
        return res.orElse(null);
    }


    // getters and setters

    public ActivityRepository getRepo() {
        return repo;
    }

    public void setRepo(ActivityRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerListQuestions)) return false;
        if (!super.equals(o)) return false;
        ServerListQuestions that = (ServerListQuestions) o;
        return repo.equals(that.repo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), repo);
    }
}
