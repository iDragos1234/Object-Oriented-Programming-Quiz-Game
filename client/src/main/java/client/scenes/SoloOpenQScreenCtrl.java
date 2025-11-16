package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Questions.Question;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class for the screen controls of Solo Open Question Scene.
 */
public class SoloOpenQScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Integer startTime;
    private Timer timer;
    private TimerTask task;
    private int secondsLeft;

    private Image image;

    @FXML
    Label questionText;

    @FXML
    Button leaveButton;

    @FXML
    TextField answerIn;

    @FXML
    Button subBut;

    @FXML
    Label score;

    @FXML
    Button joker;

    @FXML
    Label time;

    @FXML
    TextArea rules;

    @FXML
    Button doublePoints;

    public Button getDoublePoints() {
        return doublePoints;
    }

    @FXML
    Button nextQuestion;

    @FXML
    Label answer;

    @FXML
    ImageView activityImage;

    @FXML
    ProgressBar timeBar;

    @FXML
    ImageView timeUp;

    @FXML
    Label gainedScore;

    @Inject
    public SoloOpenQScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        startTime = 15;
    }

    public void clickHelp() {
        rules.setVisible(true);
    }

    /**
     * help close
     */
    public void releaseHelp() {
        rules.setVisible(false);
    }

    /**
     * double the point for this question
     */
    public void doublePoints() {
        doublePoints.setDisable(true);
        mainCtrl.clickDouble();
    }

    public void nextQClicked() {
        gainedScore.setText("");
        mainCtrl.nextQuestion();
    }

    /**
     * disable all buttons
     */
    public void enableAll() {
        subBut.setDisable(false);
        if (!mainCtrl.doubleUsed) {
            doublePoints.setDisable(false);
        }
    }

    public void disableAll() {
        subBut.setDisable(true);
        joker.setDisable(true);
        doublePoints.setDisable(true);
    }

    public void showAnswer() {
        disableAll();
        nextQuestion.setVisible(true);
        answer.setVisible(true);
    }

    /**
     * reset everything
     */
    public void reset() {
        enableAll();
    }

    /**
     * Initializes extra settings to the given scene,
     * changes background colour.
     */
    public void initialize() {

        leaveButton.setStyle("-fx-background-color: #ff0000;");
        rules.setText("Joker card can be used to eliminate a wrong answer" +
                "\n\nDouble points can be used to\ndouble the points rewards for this\nquestion");
        time.setTextFill(Color.BLACK);
        answer.setVisible(false);
        nextQuestion.setVisible(false);
        rules.setWrapText(true);
        joker.setDisable(true);
    }

    /**
     * This method hides the answer and disables the Joker (some errors appeared and this was a quick fix)
     */
    public void hideAnswer() {
        answer.setVisible(false);
        nextQuestion.setVisible(false);
        joker.setDisable(true);
    }

    /**
     * This method is linked to the submit button, it stops the timer and shows the answer
     */
    public void submit() {
        mainCtrl.stopTimer();
        mainCtrl.checkCorrect();
        showAnswer();
    }

    public void toSplash() {
        mainCtrl.toSplash();
    }

    /**
     * Imports the question data
     *
     * @param question This is the question we use to fill in the scene with information
     * @param round    This is the current round of the game so the user knows how many questions /
     *                 are left and how many have passed
     */
    public void importQuestionData(Question question, int round) {
        questionText.setText((round + 1) + ". " + question.getCorrectAnswer().getTitle());
        answer.setText(Long.toString(question.getCorrectAnswer().getConsumption_in_wh()));

        String imagePath = "client/src/main/resources/images/Activity-Images/" + question.getCorrectAnswer().getImage_path();
        try {
            image = new Image(new FileInputStream(imagePath));
        } catch (FileNotFoundException ignore) {
        }
        activityImage.setImage(image);
    }

    public void setScore(long updateScore) {
        score.setText(String.valueOf(updateScore));
    }

    public void setGainedScore(long calculatedScore) {
        System.out.println(calculatedScore);
        gainedScore.setText("+" + calculatedScore);
        FadeTransition addedScore = new FadeTransition(Duration.seconds(0.5), gainedScore);
        addedScore.setAutoReverse(true);
        addedScore.setFromValue(1);
        addedScore.setToValue(0);
        addedScore.setCycleCount(Transition.INDEFINITE);
        addedScore.play();
    }

    public TextField getAnswerIn() {
        return answerIn;
    }

    public Label getAnswer() {
        return answer;
    }

    public Label getTime() {
        return time;
    }

    public ProgressIndicator getTimeBar() {
        return timeBar;
    }

    public ImageView getTimeUp() {
        return timeUp;
    }

}
