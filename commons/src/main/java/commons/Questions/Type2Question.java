package commons.Questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import commons.Activity;

import java.util.List;
import java.util.Objects;

@JsonTypeName("Type2Question")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Type2Question implements Question {

    /**
     * Type2Question - class for the first type of question: "How much energy does it take?"
     * pattern of this type of question: question text, an Activity attachment to this question and a set of possible answers represented by an Activity list
     * attachment - the Activity attachment to this question
     * userAnswer - the id of the Activity from the list of answers that was selected by the user as correct (is null if no input from user was recorded)
     * capacityAnswers - the number of possible answers
     */

    private final String question = "How much energy does it take?";
    private Activity attachment;

    private int capacityAnswers;
    private List<Activity> answers;

    private String userAnswer;

    /**
     * constructor for the type 2 question
     *
     * @param attachment - the attached Activity to this question
     * @param answers    - list of Activity objects that represent possible answers to this question
     * @throws IllegalArgumentException - if the number of possible answers is lower than 2
     */
    public Type2Question(Activity attachment, List<Activity> answers) throws IllegalArgumentException {
        this.capacityAnswers = answers.size();
        if (capacityAnswers < 1) throw new IllegalArgumentException("Not enough activities in the list of answers");

        this.attachment = attachment;
        this.answers = answers;

        userAnswer = null;
    }

    public Type2Question(){

    }

    /**
     * this method checks the answer of the user by comparing the ID of the Activity answer chosen by the player
     * with the ID of the Activity representing the correct answer.
     *
     * @return - a boolean representing the validity of the user answer
     */
    @Override
    public boolean checkAnswer() {
        if (userAnswer == null) return false;
        return userAnswer.equals(attachment.getId());
    }

    @Override
    public String getQuestionText() {
        return question;
    }

    @Override
    public int type() {
        return 2;
    }

    public Activity getAttachment() {
        return attachment;
    }

    @Override
    public Activity getCorrectAnswer() {
        return attachment;
    }

    public int getCapacityAnswers() {
        return capacityAnswers;
    }

    public List<Activity> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Activity> answers) {
        this.answers = answers;
    }

    @Override
    public String getUserAnswer() {
        return userAnswer;
    }

    /**
     * this method sets the user answer to this question
     *
     * @param userAnswer - a String containing the answer of the user
     */

    @Override
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    @Override
    public Activity getAnswerByIndex(int index) throws IllegalArgumentException {
        if (index >= capacityAnswers) throw new IllegalArgumentException("Index exceeds the capacity of the list");
        return answers.get(index);
    }

    @Override
    public String toString() {
        String s = "[";
        s = s + question + "\n";
        s = s + "\t\t" + attachment + "\n";
        s = s + "\t\t(";
        for (Activity a : answers) {
            s = s + a + ", ";
        }
        s = s + ")\n";
        s = s + "\t\t" + userAnswer + "\n";
        s = s + "]";
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Type2Question)) return false;
        Type2Question that = (Type2Question) o;
        return capacityAnswers == that.capacityAnswers && question.equals(that.question) && attachment.equals(that.attachment) && answers.equals(that.answers) && Objects.equals(userAnswer, that.userAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, attachment, capacityAnswers, answers, userAnswer);
    }
}
