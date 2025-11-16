package client.scenes;


import client.Main;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.layout.HBox;
import javafx.util.Duration;


/**
 * Class for the screen controls of Multiplayer Open Question Scene.
 */
public class MultiOpenQScreenCtrl extends ScreenCtrl {

    @FXML
    HBox hBox;

    @FXML
    AnchorPane pane;

    @FXML
    Button leaveButton;

    @FXML
    Label questionLabel;

    @FXML
    TextField answerIn;

    @FXML
    Button subBut;

    @FXML
    Button doubleP;

    @FXML
    Button timeAttack;

    @FXML
    Label score;

    @FXML
    TextArea rules;

    @FXML
    Label correctAnswer;

    @FXML
    Label attachment;

    private Image imageAttachment;

    @FXML
    ImageView imageView;

    @FXML
    ProgressBar timeBar;

    @FXML
    ImageView timeUp;

    @FXML
    Label gainedScore;

    @FXML
    TextField timeAttackAlert;

    public MultiOpenQScreenCtrl() {
        super();
    }


// Initialize and Reset ################################################################################################

    /**
     * initializes extra settings to the given scene.
     */
    @Override
    public void initialize() {
        rules.setText("Joker card can be used to eliminate a wrong answer" +
                "\n\nDouble points can be used to\ndouble the points rewards for this\nquestion\n\n" +
                "Time attacks can be used to\nshorten the time of this question\nfor other players");
        rules.setWrapText(true);
        leaveButton.setStyle("-fx-background-color: #ff0000;");

        correctAnswer.setVisible(false);
        correctAnswer.setDisable(true);

        answerIn.setPromptText("");
        subBut.setDisable(false);

        timeAttackAlert.setVisible(false);

        pane.requestFocus();

        score.setText(String.valueOf(multiPlayerSession.getPlayerScore()));

        startTimer();
    }

    @Override
    public void resetCtrl() {
        doublePoints = false;
        doubleP.setOpacity(1);
        doubleP.setDisable(false);
        doublePointsUsed = false;

        timeAttack.setOpacity(1);
        timeAttack.setDisable(false);
        timeAttackUsed = false;

        subBut.setDisable(false);
        score.setText("0");

        correctAnswer.setVisible(false);
        correctAnswer.setDisable(true);
        correctAnswer.setText("");

        questionLabel.setText("Question Text");
        attachment.setText("Attachment Title");

        answerIn.setText("");
        answerIn.setPromptText("");
        timeBar.setProgress(1);

        answerSecond = 0;
        question = null;
    }

    @Override
    public void disableAll() {
        correctAnswer.setVisible(false);
        correctAnswer.setDisable(true);

        subBut.setDisable(true);
    }

    @Override
    public void disablePowerUps(boolean toggle) {

        if (!doublePointsUsed) {
            doubleP.setDisable(toggle);
            if (toggle) {
                doubleP.setOpacity(0.5);
            }
            else doubleP.setOpacity(1);
        }

        if (!timeAttackUsed) {
            timeAttack.setDisable(toggle);
            if (toggle) {
                timeAttack.setOpacity(0.5);
            }
            else timeAttack.setOpacity(1);
        }
    }

//######################################################################################################################

    public void happyEmoji() {
        multiPlayerSession.emojiPost("green");
    }

    public void angryEmoji() {
        multiPlayerSession.emojiPost("red");
    }

    public void puzzledEmoji() {
        multiPlayerSession.emojiPost("blue");
    }

    public void joker_Action() {
        // Nothing to do here
    }



    /**
     * submit - this method is used to register the user input answer
     */
    public void submit() {

        String answer = answerIn.getText();
        System.out.println("SUBMITED! " + answer);

        boolean numCheck = checkValidAnswer(answer);
        System.out.println("NumCheck: " + numCheck);

        if (!numCheck) {
            answerIn.setText("");
            answerIn.setPromptText("Invalid answer. Insert a number.");
            question.setUserAnswer(null);
            return;
        }

        answerIn.setText("");
        answerIn.setPromptText("Answer registered!");
        setUserAnswer(answer);
    }

    /**
     * checkValidAnswer - this method verifies whether the user input answer is a valid number
     *
     * @param answer - the answer input of the user, represented as a String
     * @return - a boolean value asserting whether the user input is a valid number
     */
    private boolean checkValidAnswer(String answer) {
        if (answer == null || answer.isBlank()) return false;

        for (int i = 0; i < answer.length(); i++) {
            char c = answer.charAt(i);
            if (c < 48 || c > 57) return false;
        }

        return true;
    }

    @Override
    public void setUserScore() {

        System.out.println("MultiOpenQCtrl -> SetUserScore");
        System.out.println("Answer Second: " + answerSecond);


        if (question.getUserAnswer() == null || question.getUserAnswer().isBlank()) {
            return;
        }

        if (checkAnswer()) {
            if (doublePoints) multiPlayerSession.setPlayerScore(200);
            else multiPlayerSession.setPlayerScore(100);
        } else {

            int answer = Integer.parseInt(question.getUserAnswer());
            int correct = (int) question.getCorrectAnswer().getConsumption_in_wh();

            if (doublePoints) {
                multiPlayerSession.setPlayerScore(2 * Main.calculateScore(answerSecond, answer, correct));
                this.setGainedScore(2 * Main.calculateScore(answerSecond, answer, correct));
            } else {
                multiPlayerSession.setPlayerScore(Main.calculateScore(answerSecond, answer, correct));
                this.setGainedScore(Main.calculateScore(answerSecond, answer, correct));
            }
        }
        if (doublePoints) doublePoints = false;

        score.setText(String.valueOf(multiPlayerSession.getPlayerScore()));
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

    public AnchorPane getPane() {
        return pane;
    }

    public void setPane(AnchorPane pane) {
        this.pane = pane;
    }

// render ##############################################################################################################

    @Override
    public void renderQuestion() {
        int round = multiPlayerSession.getRound();

        questionLabel.setText(round + ") " + question.getQuestionText());
        attachment.setText(question.getAttachment().getTitle());

        setImages();
    }
    public void clickHelp() {
        rules.setVisible(true);
    }

    public void releaseHelp() {
        rules.setVisible(false);
    }
//######################################################################################################################

// image setter ########################################################################################################

    @Override
    public void setImages() {
        String path = "client/src/main/resources/images/Activity-Images/";
        String imagePathCorrect = path + question.getAttachment().getImage_path();

        try {
            imageAttachment = new Image(new FileInputStream(imagePathCorrect));
        } catch (FileNotFoundException ignored) {
            //TODO: add placeholder image not found
        }

        imageView.setImage(imageAttachment);
    }

//######################################################################################################################

    @Override
    public ProgressBar getTimer() {
        return timeBar;
    }


    @Override
    public void showAnswer() {
        imageView.setImage(null);
        correctAnswer.setVisible(true);
        correctAnswer.setDisable(false);
        correctAnswer.setStyle("-fx-background-color: FF00FF; -fx-background-radius: 5em");
        correctAnswer.setText(String.valueOf(question.getCorrectAnswer().getConsumption_in_wh()) + " Wh");
        return;
    }

    public void updateEmojis(int[] arr) {
        String red = Integer.toHexString(arr[0]);
        if (red.length() < 2) red = "0" + red;
        String green = Integer.toHexString(arr[1]);
        if (green.length() < 2) green = "0" + green;
        String blue = Integer.toHexString(arr[2]);
        if (blue.length() < 2) blue = "0" + blue;
        String color = red + green + blue;
        hBox.setStyle("-fx-background-color: #" + color + ";");
    }

    @Override
    public ImageView getTimeUp() {
        return timeUp;
    }

    @Override
    public void resetGainedScore() {
        gainedScore.setText("");
    }
}
