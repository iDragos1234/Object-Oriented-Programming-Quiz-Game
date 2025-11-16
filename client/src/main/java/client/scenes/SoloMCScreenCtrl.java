package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Questions.Question;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SoloMCScreenCtrl {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;
    private final Integer startTime;
    private Timeline timeline;
    private int secCount;

    private Image imageA;
    private Image imageB;
    private Image imageC;
    private Image imageCorrect;

    @FXML
    Label questionText;

    @FXML
    Button leaveButton;

    @FXML
    Button buttonA;

    @FXML
    Button buttonB;

    @FXML
    Button buttonC;

    public ServerUtils getServer() {
        return server;
    }

    @FXML
    Button helpButton;

    @FXML
    TextArea rules;

    @FXML
    Label time;

    @FXML
    Button joker;

    @FXML
    Button doublePoints;

    @FXML
    Label score;

    @FXML
    Label answer;

    @FXML
    Button nextQuestion;

    @FXML
    ImageView answerImage;

    @FXML
    ProgressBar timeBar;

    @FXML
    ImageView timeUp;

    @FXML
    Label gainedScore;

    public Question currentQuestion;

    @Inject
    public SoloMCScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        startTime = 15;
    }

    /**
     * help open
     */
    public void clickHelp() {
        rules.setVisible(true);
    }

    /**
     * Once your mouse stops hovering the window disappears
     */
    public void releaseHelp() {
        rules.setVisible(false);
    }

    /**
     * This function disables the button based on the given index
     * - if index = 0 -> disable A
     */
    public void joker(int wrongIndex) {
        if (wrongIndex == 0) {
            buttonA.setDisable(true);
        } else if (wrongIndex == 1) {
            buttonB.setDisable(true);
        } else if (wrongIndex == 2) {
            buttonC.setDisable(true);
        }
        joker.setDisable(true);
    }

    /**
     * Calls the joker function and saves the information to mainCtrl /
     * that the joker has been used in the game
     */
    public void callJoker() {
        mainCtrl.clickJoker();
    }

    /**
     * Double the point for this question
     */
    public void doublePoints() {
        doublePoints.setDisable(true);
        mainCtrl.clickDouble();
    }

    /**
     * Hides the answer fields, so the user can't see them
     */
    public void hideAnswer() {
        answer.setVisible(false);
        nextQuestion.setVisible(false);
    }

    /**
     * Initializes extra settings to the given scene,
     * changes background colour.
     */
    public void initialize() {

        leaveButton.setStyle("-fx-background-color: #ff0000;");
        rules.setText("Joker card can be used to eliminate a wrong answer" +
                "\n\nDouble points can be used to\ndouble the points rewards for this\nquestion");
        enableAll();
        hideAnswer();
        rules.setWrapText(true);
        if (currentQuestion != null && currentQuestion.type() == 2) {
            answerImage.setImage(imageCorrect);
        }
    }

    /**
     * When the "Next" button is clicked we check for a correct answer and fetch the next question
     */
    public void nextQClicked() {
        answer.setStyle("-fx-background-color: #ffffff; ");
        gainedScore.setText("");
        mainCtrl.nextQuestion();
    }

    /**
     * disable all buttons
     */

    public void disableAll() {
        buttonA.setDisable(true);
        buttonB.setDisable(true);
        buttonC.setDisable(true);
        joker.setDisable(true);
        doublePoints.setDisable(true);
    }

    /**
     * enable all buttons
     */
    public void enableAll() {
        buttonA.setDisable(false);
        buttonB.setDisable(false);
        buttonC.setDisable(false);
    }

    /**
     * click buttonA
     * Tells the MainCtrl that this answer is selected by the user
     */
    public void answerA() {
        mainCtrl.stopTimer();
        mainCtrl.selectA();
        showAnswer();
        mainCtrl.checkCorrect();
    }

    /**
     * click buttonB
     * Tells the MainCtrl that this answer is selected by the user
     */
    public void answerB() {
        mainCtrl.stopTimer();
        mainCtrl.selectB();
        showAnswer();
        mainCtrl.checkCorrect();
    }

    /**
     * click buttonC
     * Tells the MainCtrl that this answer is selected by the user
     */
    public void answerC() {
        mainCtrl.stopTimer();
        mainCtrl.selectC();
        showAnswer();
        mainCtrl.checkCorrect();
    }

    /**
     * show answer at the end of each question
     * stop time
     * enable next button
     */
    public void showAnswer() {
        disableAll();
        nextQuestion.setVisible(true);
        nextQuestion.toFront();
        answer.setVisible(true);
        answer.toFront();
        answerImage.setImage(imageCorrect);
    }

    /**
     * reset everything
     */
    public void reset() {
        joker.setDisable(false);
        doublePoints.setDisable(false);
        enableAll();
        answerImage.setImage(null);
    }


    public void toSplash() {
        mainCtrl.toSplash();
    }


    /**
     * Imports the data from a question to the acording fields of the scene
     *
     * @param mcQuestion The question whose data we will import
     * @param round      the round (its array index which is why we add 1)
     */
    public void importQuestionData(Question mcQuestion, int round) {
        currentQuestion = mcQuestion;
        if (mcQuestion.type() == 1) {
            questionText.setText((round + 1) + ". " + mcQuestion.getQuestionText());
            answer.setText(mcQuestion.getCorrectAnswer().getTitle());
            buttonA.setText(mcQuestion.getAnswers().get(0).getTitle());
            buttonB.setText(mcQuestion.getAnswers().get(1).getTitle());
            buttonC.setText(mcQuestion.getAnswers().get(2).getTitle());

        } else if (mcQuestion.type() == 2) {
            questionText.setText((round + 1) + ". " + mcQuestion.getQuestionText() + "\n" + mcQuestion.getAttachment().getTitle());
            answer.setText(Long.toString(mcQuestion.getCorrectAnswer().getConsumption_in_wh()));
            buttonA.setText(Long.toString(mcQuestion.getAnswerByIndex(0).getConsumption_in_wh()));
            buttonB.setText(Long.toString(mcQuestion.getAnswerByIndex(1).getConsumption_in_wh()));
            buttonC.setText(Long.toString(mcQuestion.getAnswerByIndex(2).getConsumption_in_wh()));

        } else if (mcQuestion.type() == 3) {
            questionText.setText((round + 1) + ". " + "Instead of " + mcQuestion.getAttachment().getTitle() + "\n you could do");
            answer.setText(mcQuestion.getCorrectAnswer().getTitle());
            buttonA.setText(mcQuestion.getAnswerByIndex(0).getTitle());
            buttonB.setText(mcQuestion.getAnswerByIndex(1).getTitle());
            buttonC.setText(mcQuestion.getAnswerByIndex(2).getTitle());
        }
        setImages(mcQuestion);
    }

    /**
     * Sets the new score
     *
     * @param updateScore the score to be added to the current label
     */
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

    public int getSecCount() {
        return secCount;
    }

    /**
     * This method sets the image of the all the answers.
     *
     * @param mcQuestion The question whose data we will import
     */
    public void setImages(Question mcQuestion) {
        if (mcQuestion.type() == 1) {
            String imagePathA = "client/src/main/resources/images/Activity-Images/" + mcQuestion.getAnswers().get(0).getImage_path();
            String imagePathB = "client/src/main/resources/images/Activity-Images/" + mcQuestion.getAnswers().get(1).getImage_path();
            String imagePathC = "client/src/main/resources/images/Activity-Images/" + mcQuestion.getAnswers().get(2).getImage_path();
            String imagePathCorrect = "client/src/main/resources/images/Activity-Images/" + mcQuestion.getCorrectAnswer().getImage_path();
            try {
                imageA = new Image(new FileInputStream(imagePathA));
                imageB = new Image(new FileInputStream(imagePathB));
                imageC = new Image(new FileInputStream(imagePathC));
                imageCorrect = new Image(new FileInputStream(imagePathCorrect));
            } catch (FileNotFoundException ignored) {
                //TODO: add placeholder image not found
            }
        } else if (mcQuestion.type() == 2) {
            String imagePathCorrect = "client/src/main/resources/images/Activity-Images/" + mcQuestion.getCorrectAnswer().getImage_path();
            try {
                imageCorrect = new Image(new FileInputStream(imagePathCorrect));
            } catch (FileNotFoundException ignored) {
                //TODO: add placeholder image not found
            }
            answerImage.setImage(imageCorrect);
        } else if (mcQuestion.type() == 3) {
            String imagePathA = "client/src/main/resources/images/Activity-Images/" + mcQuestion.getAnswers().get(0).getImage_path();
            String imagePathB = "client/src/main/resources/images/Activity-Images/" + mcQuestion.getAnswers().get(1).getImage_path();
            String imagePathC = "client/src/main/resources/images/Activity-Images/" + mcQuestion.getAnswers().get(2).getImage_path();
            String imagePathDefault = "client/src/main/resources/images/Activity-Images/" + mcQuestion.getCorrectAnswer().getImage_path();
            try {
                imageA = new Image(new FileInputStream(imagePathA));
                imageB = new Image(new FileInputStream(imagePathB));
                imageC = new Image(new FileInputStream(imagePathC));
                imageCorrect = new Image(new FileInputStream(imagePathDefault));
            } catch (FileNotFoundException ignored) {
                //TODO: add placeholder image not found
            }
        }
    }

    /**
     * sets the imageview to display the image corresponding to answer 1.
     */
    public void showImageA() {
        if (!(currentQuestion.type() == 2)) {
            answerImage.setImage(imageA);
        } else {
            answerImage.setImage(imageCorrect);
        }

    }

    /**
     * sets the imageview to display the image corresponding to answer 2.
     */
    public void showImageB() {
        if (!(currentQuestion.type() == 2)) {
            answerImage.setImage(imageB);
        } else {
            answerImage.setImage(imageCorrect);
        }
    }

    /**
     * sets the imageview to display the image corresponding to answer 3.
     */
    public void showImageC() {
        if (!(currentQuestion.type() == 2)) {
            answerImage.setImage(imageC);
        } else {
            answerImage.setImage(imageCorrect);
        }
    }

    public Label getTime() {
        return time;
    }

    public ProgressBar getTimeBar() {
        return timeBar;
    }

    public ImageView getTimeUp() {
        return timeUp;
    }
}
