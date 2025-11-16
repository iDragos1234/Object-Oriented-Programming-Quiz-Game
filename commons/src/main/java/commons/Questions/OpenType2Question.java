package commons.Questions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import commons.Activity;

import java.util.List;
import java.util.Objects;


@JsonTypeName("OpenType2Question")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenType2Question implements Question {

    /**
     * OpenType2Question - class for the first type of question: "How much energy does it take?"
     * pattern of this type of question: question text, an Activity attachment to this question
     * @question - the question text of this question
     * @attachment - the Activity attachment to this question
     * @userAnswer - the id of the Activity from the list of answers that was selected by the user as correct (is null if no input from user was recorded)
     */

    private final String question = "How much energy does it take?";
    private Activity attachment;

    private String userAnswer;

    public OpenType2Question() {}

    /**
     * constructor for the type 2 question
     * @param attachment - the attached Activity to this question
     * @throws IllegalArgumentException - if the number of possible answers is lower than 2
     */
    public OpenType2Question(Activity attachment)  {

        this.attachment = attachment;

        userAnswer = null;
    }


    /**
     * this method checks the answer of the user by comparing the ID of the Activity answer chosen by the player
     * with the ID of the Activity representing the correct answer.
     * @return - a boolean representing the validity of the user answer
     */
    @Override
    public boolean checkAnswer() {
        if(userAnswer == null) return false;

        long answer = Integer.parseInt(userAnswer);
        long correct = attachment.getConsumption_in_wh();

        return answer == correct;
    }



    // getters and setters

    @Override
    public String getQuestionText() {
        return question;
    }

    @Override
    public int type() {
        return 0;
    }

    @Override
    public Activity getAttachment() {
        return attachment;
    }

    @Override
    public Activity getCorrectAnswer() {
        return attachment;
    }

    public void setAttachment(Activity attachment) {
        this.attachment = attachment;
    }

    @Override
    public int getCapacityAnswers() {
        return 0;
    }




    @Override
    public List<Activity> getAnswers() {
        return null;
    }

    @Override
    public String getUserAnswer() {
        return userAnswer;
    }

    /**
     * this method sets the user answer to this question
     * @param userAnswer - a String containing the answer of the user
     */

    @Override
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    @Override
    public Activity getAnswerByIndex(int index) throws IllegalArgumentException {
        return null;
    }

    @Override
    public String toString() {
        String s = "[";
        s = s + question + "\n";
        s = s + "\t\t" + attachment + "\n";
        s = s + "\t\t" + userAnswer + "\n";
        s = s + "]";
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpenType2Question)) return false;
        OpenType2Question that = (OpenType2Question) o;
        return question.equals(that.question) && attachment.equals(that.attachment) && Objects.equals(userAnswer, that.userAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, attachment, userAnswer);
    }
}
