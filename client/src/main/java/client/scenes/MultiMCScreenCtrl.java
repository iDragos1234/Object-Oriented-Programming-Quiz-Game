package client.scenes;


import client.Main;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.layout.*;
import javafx.util.Duration;


public class MultiMCScreenCtrl extends ScreenCtrl {


    @FXML
    HBox hBox;

    @FXML
    AnchorPane pane;

    @FXML
    Button leaveButton;

    @FXML
    Label questionLabel;

    @FXML
    Button buttonA;

    @FXML
    Button buttonB;

    @FXML
    Button buttonC;

    private Image imageA;
    private Image imageB;
    private Image imageC;
    private Image imageAttachment;

    @FXML
    ImageView imageView;

    @FXML
    Button helpButton;

    @FXML
    TextArea rules;

    @FXML
    Label correctAnswer;

    @FXML
    Label attachment;

    @FXML
    Label score;

    @FXML
    Button doubleP;

    @FXML
    Button joker;

    @FXML
    Button timeA;


    @FXML
    ProgressBar timeBar;

    @FXML
    ImageView timeUp;

    @FXML
    Label gainedScore;

    @FXML
    TextField timeAttackAlert;

    private int indexAnswer = -1;
    boolean jokerUsed = false;

    public MultiMCScreenCtrl() {

    }

// Initialize and Reset ################################################################################################

    /**
     * initializes extra settings to the given scene
     */
    @Override
    public void initialize() {
        leaveButton.setStyle("-fx-background-color: #ff0000;");
        rules.setText("Joker card can be used to eliminate a wrong answer" +
                "\n\nDouble points can be used to\ndouble the points rewards for this\nquestion\n\n" +
                "Time attacks can be used to\nshorten the time of this question\nfor other players");
        rules.setWrapText(true);

        correctAnswer.setVisible(false);
        correctAnswer.setDisable(true);
        correctAnswer.setStyle("-fx-background-color: FF00FF; -fx-background-radius: 5em");
        buttonA.setDisable(false);
        buttonB.setDisable(false);
        buttonC.setDisable(false);
        setStyleButtons();

        indexAnswer = -1;

        timeAttackAlert.setVisible(false);

        pane.requestFocus();

        score.setText(String.valueOf(super.multiPlayerSession.getPlayerScore()));

        startTimer();
    }

    public void setStyleButtons() {

        buttonA.setStyle("-fx-background-radius: 5em; -fx-background-color: linear-gradient(to right, #e7b8f2, #dd68f7)");
        buttonB.setStyle("-fx-background-radius: 5em; -fx-background-color: linear-gradient(to right, #dd68f7, #d131f5)");
        buttonC.setStyle("-fx-background-radius: 5em; -fx-background-color: linear-gradient(to right, #d131f5, #ad00d4)");
    }

    @Override
    public void resetCtrl() {
        doublePoints = false;
        doubleP.setOpacity(1);
        doubleP.setDisable(false);
        doublePointsUsed = false;

        joker.setOpacity(1);
        joker.setDisable(false);
        jokerUsed = false;

        timeA.setOpacity(1);
        timeA.setDisable(false);
        timeAttackUsed = false;

        buttonA.setDisable(false);
        buttonA.setText("A.");
        buttonB.setDisable(false);
        buttonB.setText("B.");
        buttonC.setDisable(false);
        buttonC.setText("C.");

        correctAnswer.setVisible(false);
        correctAnswer.setDisable(true);
        correctAnswer.setText("");
        correctAnswer.setStyle("-fx-background-color: FF00FF; -fx-background-radius: 5em");
        attachment.setText("Attachment Title");

        score.setText("0");
        questionLabel.setText("Question Text");
        timeBar.setProgress(1);

        answerSecond = 0;
        question = null;
    }

    @Override
    public void disableAll() {
        buttonA.setDisable(true);
        buttonB.setDisable(true);
        buttonC.setDisable(true);

        correctAnswer.setDisable(true);
        correctAnswer.setVisible(false);
    }

    @Override
    public void disablePowerUps(boolean toggle) {

        if (!jokerUsed) {
            joker.setDisable(toggle);
            if (toggle) {
                joker.setOpacity(0.5);
            }
            else joker.setOpacity(1);
        }

        if (!doublePointsUsed) {
            doubleP.setDisable(toggle);
            if (toggle) {
                doubleP.setOpacity(0.5);
            }
            else doubleP.setOpacity(1);
        }

        if (!timeAttackUsed) {
            timeA.setDisable(toggle);
            if (toggle) {
                timeA.setOpacity(0.5);
            }
            else timeA.setOpacity(1);
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

    public void clickHelp() {
        rules.setVisible(true);
    }

    public void releaseHelp() {
        rules.setVisible(false);
    }

    public AnchorPane getPane() {
        return pane;
    }

    public void setPane(AnchorPane pane) {
        this.pane = pane;
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

// render ##############################################################################################################

    @Override
    public void renderQuestion() {
        int round = multiPlayerSession.getRound();

        questionLabel.setText(round + ") " + question.getQuestionText());

        if (question.type() == 1) {
            attachment.setText("");
            buttonA.setText("A. " + question.getAnswerByIndex(0).getTitle());
            buttonB.setText("B. " + question.getAnswerByIndex(1).getTitle());
            buttonC.setText("C. " + question.getAnswerByIndex(2).getTitle());
        }

        if (question.type() == 2) {
            attachment.setText(question.getAttachment().getTitle());
            buttonA.setText("A. " + question.getAnswerByIndex(0).getConsumption_in_wh());
            buttonB.setText("B. " + question.getAnswerByIndex(1).getConsumption_in_wh());
            buttonC.setText("C. " + question.getAnswerByIndex(2).getConsumption_in_wh());
        }

        if (question.type() == 3) {
            attachment.setText(question.getAttachment().getTitle());
            buttonA.setText("A. " + question.getAnswerByIndex(0).getTitle());
            buttonB.setText("B. " + question.getAnswerByIndex(1).getTitle());
            buttonC.setText("C. " + question.getAnswerByIndex(2).getTitle());
        }

        setImages();
    }

//######################################################################################################################

// image setter ########################################################################################################

    @Override
    public void setImages() {

        int type = question.type();

        String path = "client/src/main/resources/images/Activity-Images/";

        String imagePathA = path + question.getAnswers().get(0).getImage_path();
        String imagePathB = path + question.getAnswers().get(1).getImage_path();
        String imagePathC = path + question.getAnswers().get(2).getImage_path();

        String imagePathCorrect;

        if (type == 1) {
            imageAttachment = null;

            try {
                imageA = new Image(new FileInputStream(imagePathA));
                imageB = new Image(new FileInputStream(imagePathB));
                imageC = new Image(new FileInputStream(imagePathC));
            } catch (FileNotFoundException ignored) {
                // TODO: add placeholder image not found
            }
        }

        if (type == 2) {
            imagePathCorrect = path + question.getAttachment().getImage_path();

            try {
                imageAttachment = new Image(new FileInputStream(imagePathCorrect));
                imageA = imageB = imageC = null;

                buttonA.setOnMouseEntered(null);
                buttonA.setOnMouseExited(null);

                buttonB.setOnMouseEntered(null);
                buttonB.setOnMouseExited(null);

                buttonC.setOnMouseEntered(null);
                buttonC.setOnMouseExited(null);

            } catch (FileNotFoundException ignored) {
                //TODO: add placeholder image not found
            }
        }

        if (type == 3) {
            imagePathCorrect = path + question.getAttachment().getImage_path();

            try {
                imageAttachment = new Image(new FileInputStream(imagePathCorrect));
                imageA = new Image(new FileInputStream(imagePathA));
                imageB = new Image(new FileInputStream(imagePathB));
                imageC = new Image(new FileInputStream(imagePathC));
            } catch (FileNotFoundException ignored) {
                //TODO: add placeholder image not found
            }
        }

        if (type == 3) {
            buttonA.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    showImageA();
                }
            });
            buttonB.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    showImageB();
                }
            });
            buttonC.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    showImageC();
                }
            });

            buttonA.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    showImageAttachment();
                }
            });
            buttonB.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    showImageAttachment();
                }
            });
            buttonC.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    showImageAttachment();
                }
            });
        }
        if (type == 1) {
            buttonA.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    showImageA();
                }
            });
            buttonB.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    showImageB();
                }
            });
            buttonC.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    showImageC();
                }
            });

        }

        System.out.println("SET IMAGES");

        showImageAttachment();
    }

//######################################################################################################################

    /**
     * sets the imageview to display the image corresponding to the attachment
     */
    public void showImageAttachment() {
        imageView.setImage(imageAttachment);
    }

    /**
     * sets the imageview to display the image corresponding to answer 1.
     */
    public void showImageA() {
        imageView.setImage(imageA);
    }

    /**
     * sets the imageview to display the image corresponding to answer 2.
     */
    public void showImageB() {
        imageView.setImage(imageB);
    }

    /**
     * sets the imageview to display the image corresponding to answer 3.
     */
    public void showImageC() {
        imageView.setImage(imageC);
    }

//######################################################################################################################

    // set user answer

    public void answerA() {
        setUserAnswer(question.getAnswerByIndex(0).getId());
        indexAnswer = 0;
        setStyleButtons();
        buttonA.setStyle("-fx-background-radius: 5em;-fx-background-color: rgb(255,221,0);");
    }

    public void answerB() {
        setUserAnswer(question.getAnswerByIndex(1).getId());
        indexAnswer = 1;
        setStyleButtons();
        buttonB.setStyle("-fx-background-radius: 5em; -fx-background-color: rgb(255,221,0);");
    }

    public void answerC() {
        setUserAnswer(question.getAnswerByIndex(2).getId());
        indexAnswer = 2;
        setStyleButtons();
        buttonC.setStyle("-fx-background-radius: 5em; -fx-background-color: rgb(255,221,0);");
    }

//######################################################################################################################

    public void setUserScore() {
        System.out.println("Answer Second: " + answerSecond);

        String color;

        if (checkAnswer()) {
            if (doublePoints) {
                multiPlayerSession.setPlayerScore(2 * Main.calculateScore(answerSecond));
                this.setGainedScore(2 * Main.calculateScore(answerSecond));
            } else {
                multiPlayerSession.setPlayerScore(Main.calculateScore(answerSecond));
                this.setGainedScore(Main.calculateScore(answerSecond));
            }

            color = "-fx-background-color: rgba(0,255,89,0.47);";
        } else color = "-fx-background-color: rgba(255,0,0,0.78);";

        if (indexAnswer == -1) {
            buttonA.setStyle(color);
            buttonB.setStyle(color);
            buttonC.setStyle(color);
        } else {
            if (indexAnswer == 0) buttonA.setStyle(color);
            if (indexAnswer == 1) buttonB.setStyle(color);
            if (indexAnswer == 2) buttonC.setStyle(color);
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

//######################################################################################################################

    @Override
    public void showAnswer() {
        imageView.setImage(null);

        correctAnswer.setVisible(true);
        correctAnswer.setDisable(false);
        correctAnswer.setStyle("-fx-background-color: FF00FF");
        if (question.type() == 1 || question.type() == 3) {
            correctAnswer.setText(question.getCorrectAnswer().getTitle());
            return;
        }

        if (question.type() == 2) {
            correctAnswer.setText(String.valueOf(question.getCorrectAnswer().getConsumption_in_wh()) + " Wh");
        }
    }

    @Override
    public ProgressBar getTimer() {
        return timeBar;
    }

//######################################################################################################################

    /**
     * Update method for changing the background color, transforms integers into hex and inputs them into style property
     *
     * @param arr rgb array which contains three integer color values
     */
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

