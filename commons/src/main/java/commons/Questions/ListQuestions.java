package commons.Questions;


import commons.Activity;
import java.util.*;


public class ListQuestions {

    /**
     * ListQuestions - abstract class for the list of questions
     * equipped with useful tools to generate the list of questions and retrieve the next question in this list
     */

    private Question[] questions;
    private int counter;
    private int capacity;

    protected Random random;

    /**
     * empty constructor for this ListQuestions class
     */
    public ListQuestions() {

    }

    /**
     * Constructor for this ListQuestions class
     * @param capacity - the number of questions in this list of questions
     * @throws IllegalArgumentException - if the specified capacity is less than 1
     * this constructor also initializes the question counter to 0 and creates a new array of questions
     */
    public ListQuestions(int capacity) throws IllegalArgumentException {
        if(capacity < 1) throw new IllegalArgumentException();
        this.random = new Random();
        this.counter = 0;
        this.questions = new Question[capacity];
        this.capacity = capacity;
    }

    /**
     * constructor for this class
     * @param capacity - the number of questions in this list of questions
     * @param seed - seed for the Random field of this class (Random is used to pick a random question type in the generate method)
     * @throws IllegalAccessException - if the specified capacity is less than 1
     * this constructor also initializes the question counter to 0 and creates a new array of questions
     */
    public ListQuestions(int capacity, int seed) throws IllegalAccessException {
        if(capacity < 1){
            throw new IllegalAccessException();
        }
        this.random = new Random(seed);
        this.counter = 0;
        this.questions = new Question[capacity];
        this.capacity = capacity;
    }

    /**
     * generate a list of questions
     * @param typesBound
     * @param numberAnswers
     */
    public void generate(int typesBound, int numberAnswers) {

        for(int i = 0; i < capacity; i++) {
            int type = random.nextInt(typesBound);

            if(type == 0) {
                Activity attachment = pollActivity();
                questions[i] = new OpenType2Question(attachment);
                continue;
            }

            List<Activity> list = compileListOfActivities(numberAnswers);

            if(type == 3) {
                list.add(pollActivity());
                questions[i] = new Type3Question(list);
                continue;
            }
            if(type == 2) {
                Activity attachment = list.get(random.nextInt(list.size()));
                questions[i] = new Type2Question(attachment, list);
                continue;
            }
            if (type == 1){
                questions[i] = new Type1Question(list);
            }
        }
    }

    /**
     * pollActivity - retrieve a random activity from the ActivityRepository
     * this method is abstract; its actual implementation is found in the subclasses of this class
     * @return - a randomly-chosen Activity from the ActivityRepository
     */
    public Activity pollActivity() {
        return null;
    };


    /**
     * compileListOfActivities - this method is used to compile a list of randomly chosen activities
     * @param size - the needed number of questions in this list of activities
     * @return - a list of randomly chosen activities
     * @throws NullPointerException - if a retrieved Activity is null (to prevent adding null to the list of activities)
     */
    public List<Activity> compileListOfActivities(int size) throws NullPointerException {
        List<Activity> list = new ArrayList<>();

        for(int i=0; i<size; i++) {
            Activity a = pollActivity();
            if (a == null) throw new NullPointerException();
            list.add(a);
        }

        return list;
    }



    // getters and setters

    public Question[] getQuestions() {
        return questions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }


    public boolean hasNextQuestion() {
        return counter >= 0 && counter < capacity;
    }

    /**
     * getter for the next question in this list of questions
     * @return - the next Question in this list of questions, and increments the counter
     * @throws NoSuchElementException - if the counter exceeds the size of this list of questions
     */
    public Question nextQuestion() throws NoSuchElementException {
        if(!hasNextQuestion()) throw new NoSuchElementException();
        return questions[counter++];
    }


    /**
     * toString method
     * @return - a String representation of this ListQuestion in a human-readable format
     */
    @Override
    public String toString() {
        return "ListQuestions{" +
                ", counter=" + counter +
                ", capacity=" + capacity +
                ", random=" + random + "\n\n" +
                "questions (table):\n" + questionsToString() +
                '}';
    }

    /**
     * questionsToString method (helper for the overridden toString method)
     * @return - a String representation of the list of questions in this ListQuestions in a human-readable format
     */
    public String questionsToString() {
        String s = "";
        for(int i = 0; i < capacity; i++) {
            s = s + "\n" + i + ") " + questions[i].toString();
        }
        s += "\n";

        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListQuestions)) return false;
        ListQuestions questions1 = (ListQuestions) o;
        return counter == questions1.counter && capacity == questions1.capacity && Arrays.equals(questions, questions1.questions) && random.equals(questions1.random);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(counter, capacity, random);
        result = 31 * result + Arrays.hashCode(questions);
        return result;
    }
}
