package client.scenes;

import client.MultiPlayerSession;
import com.google.inject.Inject;
import commons.Questions.Question;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;

import java.util.*;

/**
 * ScreenCtrl - this abstract class centralizes common functionality for the OpenQuestionCtrl and th MCQuestionCtrl
 */
public abstract class ScreenCtrl {

    /**
     * @multiPlayerSession - this attribute is used to link the player details (such as score or name) to this ScreenCtrl,
     * but also acts as an intermediary between PlayerUtils and this ScreenCtrl, ensuring
     * communication with the server
     * @mainCtrl - this attribute is used to call methods from the MainCtrl class, which centralizes all controls for
     * the scenes
     * @question - the question associated with this ScreenCtrl
     * @doublePoints - this attribute is used when activating the "Double Points" power-up for a question, by doubling
     * the amount of points earned by the player after answering correctly to this question
     * @answerSecond - the exact second when the user answered for this question (used when calculating the score)
     */

    @Inject
    MultiPlayerSession multiPlayerSession;

    @Inject
    MainCtrl mainCtrl;

    Question question;

    boolean doublePoints = false;

    boolean doublePointsUsed = false;
    boolean timeAttackUsed = false;

    double answerSecond = 0;

    /**
     * empty constructor for this class
     */
    public ScreenCtrl() {

    }

    /**
     * constructor for this class
     *
     * @param question - the question associated with this ScreenCtrl
     */
    public ScreenCtrl(Question question) {
        this.question = question;
    }

    /**
     * setQuestion - setter for the question field of this ScreenCtrl
     *
     * @param question - the Question associated with this ScreenCtrl
     */
    public void setQuestion(Question question) {
        this.question = question;
        renderQuestion();
    }

    /**
     * setMultiPlayerSession - setter for the multiPlayerSession field of this ScreenCtrl
     *
     * @param multiPlayerSession - the MultiPlayerSession associated with this ScreenCtrl
     */
    public void setMultiPlayerSession(MultiPlayerSession multiPlayerSession) {
        this.multiPlayerSession = multiPlayerSession;
    }

    /**
     * setUserAnswer - setter for the question.userAnswer
     * - sets the answer input of the user for the question associated with this ScreenCtrl
     * - it also sets the answerSecond, the moment when the user answered
     *
     * @param answer - the answer input of the user (can be an ID if question is of type 1 or 3, and a number if it
     *               is of type 2 or OpenType2, representing the consumption_in_wh).
     */
    public void setUserAnswer(String answer) {
        question.setUserAnswer(answer);
        answerSecond = (int) seconds - 3;
    }

    /**
     * checkAnswer - this method checks whether the answer input of the user matches the actual correct answer to the
     * question associated with this ScreenCtrl
     *
     * @return - a boolean asserting whether the user answer is correct
     */
    public boolean checkAnswer() {
        return question.checkAnswer();
    }

    /**
     * setUserScore - this abstract method sets the score of the user by modifying the score field of multiPlayerSession
     * - its implementation varies between the implementing classes
     */
    public abstract void setUserScore();

    /**
     * initialize - this abstract method initializes this ScreenCtrl
     * - its implementation varies between the implementing classes
     */
    public abstract void initialize();

    /**
     * renderQuestion - this abstract method renders the contents of the question associated with this ScreenCtrl
     * - its implementation varies between the implementing classes
     */
    public abstract void renderQuestion();

    /**
     * disableAll - this abstract method disables some of the controls for this ScreenCtrl
     * - its implementation varies between the implementing classes
     */
    public abstract void disableAll();

    /**
     * showAnswer - this abstract method shows the actual correct answer to the question associated with this ScreenCtrl
     * - its implementation varies between the implementing classes
     */
    public abstract void showAnswer();

    /**
     * getTimer - this abstract method is used to retrieve the timer Label of the Scene associated with this ScreenCtrl,
     * to modify the time shown
     *
     * @return - a Label object of the associated FXML Scene
     */
    public abstract ProgressBar getTimer();

    /**
     * setImages - this abstract method sets the Image contents of the question associated with this ScreenCtrl
     * - its implementation varies between the implementing classes
     */
    public abstract void setImages();

    /**
     * disablePowerUps - this method is used to disable certain power-ups within this ScreenCtrl
     * - its implementation varies between the implementing classes
     *
     * @param toggle - the boolean value which specifies whether the power-ups shall be enabled or disabled
     */
    public abstract void disablePowerUps(boolean toggle);

    /**
     * doubleP_Action - this method is used by the player to activate the "Double points" power-up
     */
    public void doubleP_Action() {
        doublePoints = true;
        mainCtrl.disablePowerUp("double points");
    }

    /**
     * joker_Action - this method is used by the player to activate the "Joker" power-up
     */
    public void joker_Action() {
        System.out.println("joker!!!");

        //We compile a list of the indexes of the wrong answers, and choose one of them at random
        int i = question.getAnswers().indexOf(question.getCorrectAnswer());
        List<Integer> wrongList = new ArrayList();
        for (int j = 0; j < 3; j++) {
            if (j != i) {
                wrongList.add(j);
            }

        }
        Random random = new Random();
        int r = random.nextInt(2);
        int indexOfElimination = wrongList.get(r);
        mainCtrl.multiJoker(indexOfElimination);

        //disable the power up for future use
        mainCtrl.disablePowerUp("joker");
    }

    /**
     * timeA_Action - this method is used by the player to activate the "time attack" power-up
     */
    public void timeA_Action() {
        System.out.println("time attack!");

        //communicate the time attack to the other players
        mainCtrl.timeAttack();

        //disable the power up for future use
        mainCtrl.disablePowerUp("time attack");
    }

    /**
     * resetCtrl - this abstract method resets the controls and attributes to a default state for this ScreenCtrl
     * - its implementation varies between the implementing classes
     */
    public abstract void resetCtrl();

    /**
     * toSplash - this method reroutes the player to the Splash screen, and it also cancels the Timer
     */
    public void toSplash() {
        cancelTimer();
        mainCtrl.toSplash();
    }


// Timer ###############################################################################################################

    /**
     * @seconds - the number of seconds available to answer the question
     * @breakTime - the duration of the break between questions in seconds
     */
    double seconds = 0;
    int breakTime = 0;

    /**
     * @timer - the Timer object for this ScreenCtrl
     * - renders the remaining time for the player to answer the question
     */
    Timer timer;

    /**
     * startTimer - this method sets a new Timer object and the available time to answer the question
     * - executes a TimerTask at a regular interval of time
     */
    public void startTimer() {
        timer = new Timer();

        seconds = 19;
        breakTime = 3;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds = seconds - 0.05;
                Platform.runLater(() -> timerTask());
            }
        }, 0, 50);
    }

    /**
     * cancelTimer - this method cancels the Timer
     */
    public void cancelTimer() {
        set = false;
        timer.cancel();
    }

    boolean set = false; //checks if the information for the breaktime is set
    /**
     * timerTask - this method is called by the TimerTask associated with the Timer attribute of this ScreenCtrl
     * - executes various functionalities, based on the number of seconds (next question, show score etc.)
     */
    private void timerTask() {

        if (seconds <= 0) {
            cancelTimer();
            disablePowerUps(false);
            disableAll();
            resetGainedScore();

            if(multiPlayerSession.getRound() == 10) {
                showLeaderboard();
                return;
            }
            mainCtrl.showQuestion();
            return;
        }
        if (seconds < breakTime-1) {
            this.getTimeUp().setVisible(false);
            return;
        }
        if (seconds <= breakTime) {
            if(!set){
                disablePowerUps(true);
                this.getTimer().setProgress(((float) (seconds - 1) - breakTime) / 15);
                this.getTimeUp().setVisible(true);
                System.out.println("CHANGE SCORE");
                setUserScore();

                if(multiPlayerSession.getRound() == 10 || multiPlayerSession.getRound() == 20) {
                    multiPlayerSession.scorePost();
                }

                //this.getTimer().setText("TIME IS UP!");
                this.disableAll();
                this.showAnswer();
                set = true;
            }
            return;
        }
        if (seconds > breakTime + 10) {
            this.getTimer().setStyle("-fx-accent: green");
        }
        if (seconds >= breakTime + 5 && seconds < breakTime + 10) {
            this.getTimer().setStyle("-fx-accent: yellow");
        }
        if (seconds < breakTime + 5) {
            this.getTimer().setStyle("-fx-accent: red");
        }
        this.getTimer().setProgress(((float) seconds - breakTime) / 15);
    }

    /**
     * Reduces the remaining time. The remaining time can never go below some constant minTime.
     * It is also guaranteed that the current time is never increased because of this action.
     * @param timeRemainingPercentage a number between 0 and 1, that shows the part that should remain after the time reduction
     */
    public void setTimer(double timeRemainingPercentage) {

        int minTime = 3;
        seconds = 3 + Math.min(seconds-3,
                Math.max(minTime, (int)(Double.valueOf(seconds-3) * timeRemainingPercentage)));

    }

    protected abstract ImageView getTimeUp();

    protected abstract void resetGainedScore();

//######################################################################################################################


// Leaderboard Timer ###################################################################################################

    private void showLeaderboard() {
        Platform.runLater(() -> mainCtrl.toLeaderboardMP());
        startLeaderboardTimer();
    }

    private void startLeaderboardTimer() {
        timer = new Timer();

        seconds = 6;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds--;
                leaderboardTimerTask();
            }
        }, 0, 1000);
    }

    private void leaderboardTimerTask() {
        if(seconds == 0) {
            cancelTimer();
            Platform.runLater(() -> mainCtrl.showQuestion());
        }
    }

//######################################################################################################################
}
