package commons.Questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import commons.Activity;

import java.util.List;
import java.util.Objects;

@JsonTypeName("Type3Question")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Type3Question implements Question {

    /**
     * Type3Question - class for the first type of question: "Instead of ..., you could do instead ..."
     * pattern of this type of question: question text, an Activity attachment to this question and a set of possible answers represented by an Activity list (or a list of titles)
     * attachment - the Activity attachment to this question
     * userAnswer - the id of the Activity from the list of answers that was selected by the user as correct (is null if no input from user was recorded)
     * capacityAnswers - the number of possible answers
     * correctAnswer - the real answer to this question
     * TODO: asses whether the answers should be gotten as a list of activities or numbers (representing the associated title)
     */

    private final String question = "Instead of ..., you could do instead ...";
    private Activity attachment;

    private List<Activity> answers;
    private int capacityAnswers;

    private String userAnswer;
    private Activity correctAnswer;

    /**
     * constructor for the type 3 question
     *
     * @param answers - list of Activity objects that represent possible answers to this question
     * @throws IllegalArgumentException - if the number of possible answers is lower than 2
     *                                  the activityHandler (read its documentation for more information) method is called here to assess which is the correct answer from the list of answers and the attachment of this question
     */
    public Type3Question(List<Activity> answers) throws IllegalArgumentException {
        this.capacityAnswers = answers.size() - 1;
        if (capacityAnswers < 2) throw new IllegalArgumentException();

        this.answers = answers;
        userAnswer = null;
        activityHandler();

    }

    public Type3Question(){}

    /**
     * this method handles the list of answers, the question attachment and the correctAnswer to this question
     * the attachment is selected as the second least consuming Activity from the list of Activities retrieved from the server
     * the correctAnswer is selected as the least consuming one
     */
    private void activityHandler() {

        correctAnswer = answers.get(0);

        long minimum = correctAnswer.getConsumption_in_wh();

        for (Activity a : answers) {
            if (a.getConsumption_in_wh() < minimum) {
                correctAnswer = a;
                minimum = a.getConsumption_in_wh();
            }
        }

        long secondMinimum = Long.MAX_VALUE;

        for (Activity a : answers) {
            if (!correctAnswer.getId().equals(a.getId())) {
                if (a.getConsumption_in_wh() < secondMinimum) {
                    secondMinimum = a.getConsumption_in_wh();
                    attachment = a;
                }
            }
        }

        answers.remove(attachment);
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
        return userAnswer.equals(correctAnswer.getId());
    }

    @Override
    public String getQuestionText() {
        return question;
    }

    @Override
    public int type() {
        return 3;
    }

    public Activity getAttachment() {
        return attachment;
    }

    public List<Activity> getAnswers() {
        return answers;

    }

    public void setAnswers(List<Activity> answers) {
        this.answers = answers;
    }

    public int getCapacityAnswers() {
        return capacityAnswers;
    }

    @Override
    public String getUserAnswer() {
        return userAnswer;
    }

    @Override
    public void setUserAnswer(String answer) {
        this.userAnswer = answer;
    }

    @Override
    public Activity getCorrectAnswer() {
        return correctAnswer;
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
        s = s + "\t\t" + correctAnswer + "\n";
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
        if (!(o instanceof Type3Question)) return false;
        Type3Question that = (Type3Question) o;
        return capacityAnswers == that.capacityAnswers && question.equals(that.question) &&
                attachment.equals(that.attachment) && answers.equals(that.answers) &&
                Objects.equals(userAnswer, that.userAnswer) && correctAnswer.equals(that.correctAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, attachment, answers, capacityAnswers, userAnswer, correctAnswer);
    }
}
