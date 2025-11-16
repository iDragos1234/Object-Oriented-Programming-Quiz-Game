package commons;

import commons.Questions.Question;

import java.util.Objects;
import java.util.Random;

public class GameSession {

    /**
     * The idea of this class is to hold a single instance where we have the information of each player
     * This is for single player
     * Here we will be able to save whenever a user uses a joker, so we can disable it, we can update their score accordingly,
     * A good way to keep the information about the game instance for single player(similar for multiplayer)
     */
    LeaderboardEntrySP userSP;
    Question currentQuestion; //getters and setters have to be created
    int questionNumber = 0;
    long score;
    boolean jokerUsed = false;
    boolean doubleUpUsed = false;

    /**
     * Gets the current question
     *
     * @return
     */
    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    /**
     * Sets the current question, will be used when we have to set the same question to all of the multiplayer users
     *
     * @param currentQuestion
     */
    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    /**
     * sets the number of the question
     *
     * @param questionNumber
     */
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public GameSession() {
    }

    public long updateScore(long i) {
        score = score + i;
        userSP.setScore((int) score);
        return score;
    }

    /**
     * Randomly generates a question type to be created
     *
     * @return an int which decides on the type of question
     */
    public int questionType() {
        Random r = new Random(2);
        if (r.nextInt() % 2 == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * Gets the next question and creates it from the given class
     *
     * @return will return of parent type Question
     */
    public void nextQuestion() {
        questionNumber++;
    }

    /**
     * Returns all of the fields of the fields in a string format
     *
     * @return
     */
    @Override
    public String toString() {
        return "GameSP{" +
                "userSP=" + userSP +
                ", score=" + score +
                ", jokerUsed=" + jokerUsed +
                ", doubleUpUsed=" + doubleUpUsed +
                '}';
    }

    /**
     * Checks if the current object is equal to the given "o"
     *
     * @param o the object to be compared
     * @return true if both of the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSession gameSP = (GameSession) o;
        return score == gameSP.score && jokerUsed == gameSP.jokerUsed && doubleUpUsed == gameSP.doubleUpUsed && userSP.equals(gameSP.userSP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userSP, score, jokerUsed, doubleUpUsed);
    }

    /**
     * Gets the user information
     *
     * @return
     */
    public LeaderboardEntrySP getUserSP() {
        return userSP;
    }

    /**
     * Sets the user that is going to play the game
     *
     * @param userSP
     */
    public void setUserSP(LeaderboardEntrySP userSP) {
        setScore(userSP.getScore());
        this.userSP = userSP;
    }

    /**
     * Gets the score of the user
     *
     * @return
     */
    public long getScore() {
        return score;
    }

    /**
     * Sets/updates the score of the user
     *
     * @param score
     */
    public void setScore(long score) {
        this.score = score;
    }

    public boolean isJokerUsed() {
        return jokerUsed;
    }

    public void setJokerUsed(boolean jokerUsed) {
        this.jokerUsed = jokerUsed;
    }

    public boolean isDoubleUpUsed() {
        return doubleUpUsed;
    }

    public void setDoubleUpUsed(boolean doubleUpUsed) {
        this.doubleUpUsed = doubleUpUsed;
    }
}
