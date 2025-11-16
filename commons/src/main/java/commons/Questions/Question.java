package commons.Questions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import commons.Activity;

import java.util.List;


/**
 * Question interface - contains several mandatory methods for all implementing classes.
 * Implementing classes: Type1Question, Type2Question, Type3Question, OpenType2Question.
 * TODO: create OpenType2Question implementing class.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value=Type1Question.class, name = "Type1Question"),
        @JsonSubTypes.Type(value=Type2Question.class, name = "Type2Question"),
        @JsonSubTypes.Type(value=OpenType2Question.class, name = "OpenType2Question"),
        @JsonSubTypes.Type(value=Type3Question.class, name = "Type3Question")
})
public interface Question {

    /**
     * getter for the text of this type of Question
     * @return - a String representing the question text
     */
    String getQuestionText();

    /**
     * getter for the list of activities representing the possible answers to this question
     * @return - a list of activities
     */
    List<Activity> getAnswers();

    Activity getAnswerByIndex(int index) throws IllegalArgumentException;

    /**
     * getter for the correct answer to this question
     * @return - an Activity representing the correct answer to this question
     */
    Activity getCorrectAnswer();

    /**
     * checker method - compares the answer of the user with the actual correct answer (by comparing Activity IDs)
     * @return - a boolean depicting whether the user answer is correct
     */
    boolean checkAnswer();

    /**
     * getter for this type of question
     * @return - a value of 1 associated with Type1Question
     *         - a value of 2 associated with Type2Question
     *         - a value of 0 associated with OpenType2Question
     *         - a value of 3 associated with Type3Question
     */
    int type();


    int getCapacityAnswers();

    String getUserAnswer();

    /**
     * setter for the user answer
     * @param answer - the answer of the user, a String representing the id of the Activity selected by the user as their answer
     */
    void setUserAnswer(String answer);

    /**
     * toString method
     * @return - returns a String representation of this Question in a human-readable format
     */
    String toString();

    Activity getAttachment();
}
