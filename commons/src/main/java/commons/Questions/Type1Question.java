package commons.Questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import commons.Activity;
import java.util.List;
import java.util.Objects;

@JsonTypeName("Type1Question")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Type1Question implements Question {

    /**
     * Type1Question - class for the first type of question: "What requires more energy?"
     * pattern of this type of question: question text and a set of possible answers represented by an Activity list
     * userAnswer - the id of the Activity from the list of answers that was selected by the user as correct (is null if no input from user was recorded)
     * correctAnswer - the real answer to this question
     * capacityAnswers - the number of possible answers
     */

    private final String question = "What requires more energy?";

    private int capacityAnswers;
    private List<Activity> answers;

    private Activity correctAnswer;
    private String userAnswer;

    public Type1Question() {}
    /**
     * constructor for the type 1 question
     * @param answers - list of Activity objects that represent possible answers to this question
     * @throws IllegalArgumentException - if the number of possible answers is lower than 2
     * also sets the correct answer as the most consuming Activity from answers
     */
    public Type1Question(List<Activity> answers) throws IllegalArgumentException{
        this.capacityAnswers = answers.size();
        if(capacityAnswers < 2) throw new IllegalArgumentException();
        this.userAnswer = null;
        this.answers = answers;
        setCorrectAnswer();
    }


    /**
     * setCorrectAnswer - sets the correct answer as the most consuming Activity from the list of answers of this question
     * Was bugged: with the old version with functional java the new max value wasn't set to the new
     * Example: answers were 1 10 9; max = 1, 10 > 1  => correct answer = 10, but max was still 1, so when we got to 9/
     * we also had 9 > 1 => correct answer = 9; even though 10 is the max; Now its fixed
     */
    private void setCorrectAnswer() {
        correctAnswer = answers.get(0);
        long maximum = correctAnswer.getConsumption_in_wh();

        for(int i = 1; i < answers.size(); i++){
            if(answers.get(i).getConsumption_in_wh() > maximum){
                maximum = answers.get(i).getConsumption_in_wh();
                correctAnswer = answers.get(i);
            }
        }
    }


    // getters and setters

    public String getQuestionText() {
        return question;
    }

    /**
     * this method checks the answer of the user by comparing the ID of the Activity answer chosen by the player
     * with the ID of the Activity representing the correct answer.
     * @return - a boolean representing the validity of the user answer
     */
    @Override
    public boolean checkAnswer() {
        if (userAnswer == null) return false;
        return userAnswer.equals(correctAnswer.getId());
    }

    @Override
    public int type() {
        return 1;
    }

    public int getCapacityAnswers() {
        return capacityAnswers;
    }

    public void setCapacityAnswers(int capacityAnswers) {
        this.capacityAnswers = capacityAnswers;
    }

    @Override
    public List<Activity> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Activity> answers) {
        this.answers = answers;
        setCorrectAnswer();
    }

    @Override
    public Activity getCorrectAnswer() throws NullPointerException {
        if(correctAnswer == null) throw new NullPointerException();
        return correctAnswer;
    }

    public void setCorrectAnswer(Activity correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String getUserAnswer() {
        return userAnswer;
    }

    @Override
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    @Override
    public Activity getAnswerByIndex(int index) throws IllegalArgumentException {
        if(index >= capacityAnswers) throw new IllegalArgumentException("Index exceeds the capacity of the list");
        return answers.get(index);
    }

    @Override
    public Activity getAttachment() {
        return null;
    }

    @Override
    public String toString() {
        String s = "[";
        s = s + question + "\n";
        s = s + "\t\t" + correctAnswer + "\n";
        s = s + "\t\t(";
        for(Activity a : answers) {
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
        if (!(o instanceof Type1Question)) return false;
        Type1Question that = (Type1Question) o;
        return capacityAnswers == that.capacityAnswers && question.equals(that.question) && answers.equals(that.answers) && correctAnswer.equals(that.correctAnswer) && Objects.equals(userAnswer, that.userAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, capacityAnswers, answers, correctAnswer, userAnswer);
    }
}
