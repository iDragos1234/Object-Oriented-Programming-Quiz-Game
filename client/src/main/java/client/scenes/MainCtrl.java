/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client.scenes;

import client.ClientListQuestions;
import client.Main;
import client.MultiPlayerSession;
import client.MyModule;
import client.utils.PlayerUtils;
import client.utils.QuestionUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Injector;
import commons.GameSession;
import commons.LeaderboardEntrySP;
import commons.Questions.ListQuestions;
import commons.Questions.Question;
import commons.Questions.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.util.*;
import java.util.List;

import javafx.application.Platform;
import static com.google.inject.Guice.createInjector;


public class MainCtrl {

    private String serverIP;
    private Stage primaryStage;

    private static final Injector INJECTOR = createInjector(new MyModule());

    private GameSession solo;

    private int round = 0;
    private ListQuestions questions;

    @Inject
    private ServerUtils server;

    @Inject
    private QuestionUtils questionUtils;

    @Inject
    private PlayerUtils playerUtils;

    private SplashScreenCtrl splashCtrl;
    private Scene splashScene;

    private SoloMCScreenCtrl soloMCCtrl;
    private Scene soloMCScene;

    private MultiMCScreenCtrl multiMCCtrl;
    private Scene multiMCScene;

    private SoloOpenQScreenCtrl soloOpenQCtrl;
    private Scene soloOpenQScene;

    private MultiOpenQScreenCtrl multiOpenQCtrl;
    private Scene multiOpenQScene;

    private LeaderboardHWScreenCtrl lboardHWCtrl;
    private Scene leaderboardHWScene;

    private LeaderboardSPScreenCtrl lboardSPCtrl;
    private Scene leaderboardSPScene;

    private LeaderboardMPScreenCtrl lboardMPCtrl;
    private Scene leaderboardMPScene;

    private WaitingLobbyCtrl waitingLobbyCtrl;
    private Scene waitingLobbyScene;

    private AdminCtrl adminCtrl;
    private Scene adminScene;


    private boolean doubleThis = false;
    public boolean doubleUsed = false;  // Used to see if double is used
    private boolean A;
    private boolean B;
    private boolean C;
    public boolean jokerUsed = false;  // Used to see if joker is used
    private Thread timerThread;
    private double seconds = 15;


// MultiPlayerSession ##################################################################################################
    /**
     * @multiPlayerSession - this multiplayer session object assures the linking between the main control and the player
     * details (e.g. name, score etc.).
     * At the same time, it acts as a bridge between the MainCtrl and the PlayerUtils and, consequently,
     * the PlayerController by providing the necessary functionalities (such as interval/long polling,
     * changing player name or score).
     */
    @Inject
    private MultiPlayerSession multiPlayerSession;

    public MultiPlayerSession getMultiPlayerSession() {
        return multiPlayerSession;
    }

    public void setMultiPlayerSession(MultiPlayerSession multiPlayerSession) {
        this.multiPlayerSession = multiPlayerSession;
    }
//######################################################################################################################


    /**
     * @param primaryStage param overview obsolete I'm keeping them just in case you need to check something from them. I'll delete them soon.
     *                     param add obsolete  Pair<QuoteOverviewCtrl, Parent> overview,
     *                     Pair<AddQuoteCtrl, Parent> add,
     * @param splash       each pair should be added to the initialize
     * @param mpMC         We initialize all the needed scenes from the beginning and just switch between them
     * @param soloO
     * @param mpO
     * @param mpMC         We initialize all the needed scenes from the beginning and just switch between them
     */
    public void initialize(Stage primaryStage, Pair<SplashScreenCtrl, Parent> splash,
                           Pair<SoloMCScreenCtrl, Parent> soloMC, Pair<MultiMCScreenCtrl, Parent> mpMC, Pair<SoloOpenQScreenCtrl, Parent> soloO,
                           Pair<MultiOpenQScreenCtrl, Parent> mpO, Pair<LeaderboardHWScreenCtrl, Parent> lboardHW, Pair<LeaderboardSPScreenCtrl, Parent> lboardSP,
                           Pair<LeaderboardMPScreenCtrl, Parent> lboardMP, Pair<WaitingLobbyCtrl, Parent> waitingLobbyMC, Pair<AdminCtrl, Parent> admin) {

        File f = new File("stylesheet.css");
        String css = "file:///" + f.getAbsolutePath().replace("\\", "/");
        this.primaryStage = primaryStage;

        this.splashCtrl = splash.getKey();
        this.splashScene = new Scene(splash.getValue());
        this.soloMCCtrl = soloMC.getKey();
        this.soloMCScene = new Scene(soloMC.getValue());
        soloMCScene.getStylesheets().add(css);
        this.multiMCCtrl = mpMC.getKey();
        this.multiMCScene = new Scene(mpMC.getValue());
        this.soloOpenQCtrl = soloO.getKey();
        this.soloOpenQScene = new Scene(soloO.getValue());
        soloOpenQScene.getStylesheets().add(css);
        this.multiOpenQCtrl = mpO.getKey();
        this.multiOpenQScene = new Scene(mpO.getValue());

        this.lboardHWCtrl = lboardHW.getKey();
        this.leaderboardHWScene = new Scene(lboardHW.getValue());
        this.lboardSPCtrl = lboardSP.getKey();
        this.leaderboardSPScene = new Scene(lboardSP.getValue());
        this.lboardMPCtrl = lboardMP.getKey();
        this.leaderboardMPScene = new Scene(lboardMP.getValue());
        this.waitingLobbyCtrl = waitingLobbyMC.getKey();
        this.waitingLobbyScene = new Scene(waitingLobbyMC.getValue());

        this.adminCtrl = admin.getKey();
        this.adminScene = new Scene(admin.getValue());

        toSplash();
        primaryStage.show();
    }

//######################################################################################################################

    public void toSplash() {
        //reset gained score

        multiMCCtrl.gainedScore.setText("");
        multiOpenQCtrl.gainedScore.setText("");
        soloMCCtrl.gainedScore.setText("");
        soloOpenQCtrl.gainedScore.setText("");
        // tell the server that this player left the waiting lobby or the game session
        // -- also reset the session
        if (multiPlayerSession.inGame()) {
            multiPlayerSession.lobbyEvent("left");
            multiPlayerSession.resetSession();
        }

        multiMCCtrl.cancelTimer();
        multiOpenQCtrl.cancelTimer();

        primaryStage.setScene(splashScene);
        primaryStage.setTitle("Splash Screen");
        resetToNormal();
    }

    public void resetToNormal() {
        stopTimer();
        round = 0;
        jokerUsed = false;
        doubleThis = false;
        doubleUsed = false;
        soloMCCtrl.setScore(0);
        soloOpenQCtrl.setScore(0);
        soloMCCtrl.hideAnswer();
        soloOpenQCtrl.hideAnswer();

    }

// Timer ###############################################################################################################

    public void setSeconds(double a) {
        seconds = a;
    }

    /**
     * This method creates a Countdown
     * - Initializes a separate Thread which is put to sleep for 1000ms/
     * it then subtracts one from the counter and it updates it on the according screen
     * - Based on the time left it changes colour
     * - Else if checks are present in order to only update the needed scene
     */
    public void createTimer() {
        //System.out.println("Timer created for question " + (round+1));
        setSeconds(15);
        soloMCCtrl.getTimeUp().setVisible(false);
        soloOpenQCtrl.getTimeUp().setVisible(false);
        soloMCCtrl.getTimeBar().setProgress((float) seconds / (float) 15);
        soloMCCtrl.getTimeBar().setStyle("-fx-accent: GREEN");
        soloOpenQCtrl.getTimeBar().setProgress((float) seconds / (float) 15);
        soloOpenQCtrl.getTimeBar().setStyle("-fx-accent: GREEN");
        System.out.println("Timer created for question " + (round + 1));
        timerThread = new Thread(() -> {
            try {
                while (seconds >= 0) {
                    timerThread.sleep(50);
                    int type = questions.getQuestions()[round].type();
                    if (type == 1 || type == 2 || type == 3) {
                        Platform.runLater(() -> {
                            seconds = seconds - 0.05;
                            if (seconds <= 10) {
                                soloMCCtrl.getTimeBar().setStyle("-fx-accent: yellow");
                            }
                            if (seconds <= 5) {
                                soloMCCtrl.getTimeBar().setStyle("-fx-accent: RED");

                            }
                            if (seconds <= 0) {
                                soloMCCtrl.getTimeUp().setVisible(true);
                                soloMCCtrl.disableAll();

                            }
                            if (seconds <= -1) {
                                soloMCCtrl.getTimeUp().setVisible(false);
                                soloMCCtrl.showAnswer();
                                round++;
                                stopTimer();
                            }
                            soloMCCtrl.getTimeBar().setProgress((float) seconds / (float) 15);
                        });
                    } else if (questions.getQuestions()[round] instanceof OpenType2Question) {
                        Platform.runLater(() -> {
                            seconds = seconds - 0.05;
                            if (seconds <= 10) {
                                soloOpenQCtrl.getTimeBar().setStyle("-fx-accent: yellow");
                            }
                            if (seconds <= 5) {
                                soloOpenQCtrl.getTimeBar().setStyle("-fx-accent: red");

                            }
                            if (seconds <= 0) {
                                soloOpenQCtrl.getTimeUp().setVisible(true);
                                soloOpenQCtrl.getTimeUp().toFront();
                                soloOpenQCtrl.disableAll();

                            }
                            if (seconds <= -1) {
                                soloOpenQCtrl.getTimeUp().setVisible(false);
                                soloOpenQCtrl.showAnswer();
                                round++;
                                stopTimer();
                            }
                            soloOpenQCtrl.getTimeBar().setProgress((float) seconds / (float) 15);
                        });
                    }

                }


            } catch (InterruptedException e) {
            }
        });
    }

    /**
     * Stops the timer from running in order to track at what second the user answered
     * This will raise some exceptions but are not present to the user and do not affect the program
     */
    public void stopTimer() {
        if (timerThread != null)
            timerThread.interrupt();
    }

//######################################################################################################################

    /**
     * This is the first round of the game, it initiates the whole game with all of the necessary attributes
     * - Quesitons are generated
     * - User is created with the given  name
     * - Game session object is created
     * - A question is taken
     * - Question method is called
     *
     * @throws InterruptedException
     */
    public void toSoloGame() throws InterruptedException {

//        via Remote (server) builder - polling the server
//      --------------------------------------------------
        questions = generateQuestions(20);
        System.out.println(questions);
//      --------------------------------------------------
        resetToNormal();
        soloMCCtrl.setScore(0);
        solo = new GameSession();
        resetAnswers();
        LeaderboardEntrySP user = new LeaderboardEntrySP(splashCtrl.getUsernameField().getText(), 0);
        solo.setUserSP(user);
        int type = questions.getQuestions()[round].type();
        if (type == 1 || type == 2 || type == 3) {
            setType1();
        } else if (questions.getQuestions()[round] instanceof OpenType2Question) {
            setType2();
        }
        System.out.println("Question number " + round);
    }

    /**
     * This method sets a Type2 question(Open)
     * - Creates a timer and starts it
     * - Checks whether a power up is used and sends the information over
     * - Explicitly hides the answer Fields in case of an error
     */
    private void setType2() {
        soloOpenQCtrl.importQuestionData(questions.getQuestions()[round], round);
        createTimer();
        startTimer();
        if (doubleUsed) {
            soloOpenQCtrl.getDoublePoints().setDisable(true);
        }
        soloOpenQCtrl.hideAnswer();
        soloOpenQCtrl.reset();
        primaryStage.setScene(soloOpenQScene);
        primaryStage.setTitle("Solo");
        primaryStage.show();
    }

    /**
     * Used to start the timer
     * - Starts executing the run method inside the thread
     */
    private void startTimer() {
        timerThread.start();
    }

    /**
     * This sets a Type1Question as the next scene
     * - It imports the data
     * - Resets the answer fields in the mainCtrl that are used for the correct answer detection
     * - Creates and starts a Countdown from 15
     */
    private void setType1() {
        soloMCCtrl.importQuestionData(questions.getQuestions()[round], round);
        resetAnswers();
        soloMCCtrl.reset();
        soloMCCtrl.hideAnswer();
        soloMCCtrl.initialize();
        createTimer();
        startTimer();
        if (jokerUsed) {
            soloMCCtrl.joker.setDisable(true);
        } else {
            soloMCCtrl.joker.setDisable(false);
        }

        if (doubleUsed) {
            soloMCCtrl.doublePoints.setDisable(true);
        } else {
            soloMCCtrl.doublePoints.setDisable(false);
        }
        primaryStage.setScene(soloMCScene);
        primaryStage.setTitle("Solo");
        primaryStage.show();
    }

// Waiting Lobby #######################################################################################################

    /**
     * toWaitingLobby - this method sends the user to the waiting lobby and executes the corresponding functionality
     * for introducing the user to a new multiplayer game session
     */
    public void toWaitingLobby() {
        // retrieve the input player name of the user
        String playerName = splashCtrl.getUsernameField().getText();

        //check whether there is another player in the lobby with the same name
        if(playerUtils.checkForSameName(playerName)) {
            toSplash();
            splashCtrl.usernameField.setText("");
            splashCtrl.usernameField.setPromptText("Name already taken! Try a different one.");
            return;
        }

        // if name is valid, start a new MultiPlayerSession for this player (with this new player name)
        multiPlayerSession.setPlayerName(playerName);

        // set inGame status of player to "true"
        multiPlayerSession.setInGame(true);

        // retrieve ID of the game session in which this player is currently playing from the server
        multiPlayerSession.retrieveGameSessionID();

        // start/restart the EXEC thread from PlayerUtils
        playerUtils.restartEXEC();

        // register for updates: receive a message when the game starts
        multiPlayerSession.registerForLobbyUpdates();

        // tell the server (and all the other players in the lobby) that this new player has just joined the lobby for
        multiPlayerSession.lobbyEvent("joined");

        // regularly poll the server to get updates on the number of players in the lobby
        multiPlayerSession.pollForLobbyNumPlayers();

        // set the lobby scene
        primaryStage.setScene(waitingLobbyScene);
        primaryStage.setTitle("WaitingLobby");

    }

// MultiPlayerCTRL #####################################################################################################

    /**
     * startMulti - this method is called in the waitingLobbyCtrl by the player that presses the "Start Game" button
     * - it is used to send a signal (via server) to all other players in the waiting lobby to start the game
     */
    public void startMulti() {
        playerUtils.startGame();
    }

    /**
     * toMulti - this method sends the user to the actual game and executes the corresponding functionality
     * for setting up the multiplayer game
     */
    // method called by all players to set the scene for the multiplayer game
    public void toMulti() {

        // unregister from "start game" updates
        multiPlayerSession.unregisterFromUpdates();

        // stop listening to changes in the number of players
        multiPlayerSession.cancelIntervalPoll();

        // retrieve ListQuestions object for this game session from the server
        multiPlayerSession.retrieveListQuestions();

        // set the screen controls' multiplayer session
        multiOpenQCtrl.resetCtrl();
        multiMCCtrl.resetCtrl();

        // set the MultiPlayerSession object for the multiMCCtrl and multiOpenQCtrl objects
        multiMCCtrl.setMultiPlayerSession(multiPlayerSession);
        multiOpenQCtrl.setMultiPlayerSession(multiPlayerSession);

        // poll for emojis
        multiPlayerSession.pollForEmojis();

        // render the question content
        showQuestion();
    }

    /**
     * showQuestion - this method show the question to the user, and sets the scene according to the type of question
     * - it is called for each question in the listQuestions of the multiPlayerSession attribute
     * - here, the methods for setting the half-time and final leaderboards can be called
     *
     * @implNote - TODO: Create the Leaderboard methods for the multiplayer game and call these methods from here
     */
    public void showQuestion() {
        // as long as there are remaining questions in the multiPlayerSession.listQuestions, move to the next question
        if (multiPlayerSession.hasNextQuestion()) {
            // fetch next question
            Question question = multiPlayerSession.nextQuestion();

            //start long polling for the time attack
            playerUtils.stopExec();
            playerUtils.restartEXEC();
            System.out.println("Mainctrl -> timattacklongpolling");
            multiPlayerSession.timeAttackLongPolling();

            // decide what scene to show, based on the type of question (Open or MC)
            if (question.type() == 0) {
                primaryStage.setScene(multiOpenQScene);
                primaryStage.setTitle("Multi");
                primaryStage.show();

                multiOpenQCtrl.setQuestion(question);
                multiOpenQCtrl.initialize();
            } else {
                primaryStage.setScene(multiMCScene);
                primaryStage.setTitle("Multi");
                primaryStage.show();

                multiMCCtrl.setQuestion(question);
                multiMCCtrl.initialize();
            }
            return;
        }

        multiMCCtrl.cancelTimer();
        multiOpenQCtrl.cancelTimer();

        multiOpenQCtrl.resetCtrl();
        multiMCCtrl.resetCtrl();

        toLeaderboardMP();
    }

    /**
     * disablePowerUp - this method is used by the screen controls to disable a certain power-up
     * in all the other screen controls.
     *
     * @param powerUp - the String name of the power-up to be disabled.
     */
    public void disablePowerUp(String powerUp) { // http://localhost:8080

        if (powerUp.equals("double points")) {
            multiMCCtrl.doubleP.setDisable(true);
            multiMCCtrl.doubleP.setOpacity(0.5);
            multiMCCtrl.doublePointsUsed = true;

            multiOpenQCtrl.doubleP.setDisable(true);
            multiOpenQCtrl.doubleP.setOpacity(0.5);
            multiOpenQCtrl.doublePointsUsed = true;
        }

        if (powerUp.equals("joker")) {
            multiMCCtrl.joker.setDisable(true);
            multiMCCtrl.joker.setOpacity(0.5);
            multiMCCtrl.jokerUsed = true;
        }

        if(powerUp.equals("time attack")) {
            multiMCCtrl.timeA.setDisable(true);
            multiMCCtrl.timeA.setOpacity(0.5);
            multiMCCtrl.timeAttackUsed = true;

            multiOpenQCtrl.timeAttack.setDisable(true);
            multiOpenQCtrl.timeAttack.setOpacity(0.5);
            multiOpenQCtrl.timeAttackUsed = true;
        }
    }

    /**
     * This method is called in the ScreenCtrl (when the joker is clicked).
     * It calls the method in MultiMCCtrl that eliminates a wrong answer.
     *
     * @param wrongIndex the index of the answer that will be eliminated
     */
    public void multiJoker(int wrongIndex) {
        multiMCCtrl.joker(wrongIndex);
    }

    /**
     * This is called when a time attack happens
     */
    public void timeAttack() {
        playerUtils.timeAttackHappens(multiPlayerSession.getGameSessionID());
    }

    /**
     * toLeaderboardMP - this method shows the leaderboard for the current game session
     */
    public void toLeaderboardMP() {

        lboardMPCtrl.initialize();
        lboardMPCtrl.updateLeaderboardEntries();
        // TODO - Timer for leaderboard

        primaryStage.setScene(leaderboardMPScene);
        primaryStage.setTitle("Leaderboard");
        primaryStage.show();
    }

    /**
     * if a time attack happens, this method is called
     * it changes the time remaining no matter the type of screen
     */
    public void setTimerForTimeAttack(double timeRemainingPercentage) {
        multiMCCtrl.setTimer(timeRemainingPercentage);
        multiOpenQCtrl.setTimer(timeRemainingPercentage);
        multiMCCtrl.timeAttackAlert.setVisible(true);
        multiOpenQCtrl.timeAttackAlert.setVisible(true);
        playerUtils.restartEXEC();
        multiPlayerSession.timeAttackLongPolling();

    }

//######################################################################################################################

    private void updateScoreDouble(double seconds){
        long result = solo.updateScore(2 * Main.calculateScore(seconds));
        soloMCCtrl.setScore(result);
        soloMCCtrl.setGainedScore(2 * Main.calculateScore(seconds));
        soloOpenQCtrl.setScore(result);
    }
    private void updateScoreNormal(double seconds){
        long result = solo.updateScore(Main.calculateScore(seconds));
        soloMCCtrl.setScore(result);
        soloMCCtrl.setGainedScore(Main.calculateScore(seconds));
        soloOpenQCtrl.setScore(result);
    }

    /**
     * Correct answer detection
     * - Takes in the index of the answer and takes in the answer that the user has entered
     * - e.g. answer id is 0 if user answered "A" => correct
     * - Points are rewarded based on a formula, which gives the user 50 points /
     * and based on time left they get a % of the other 50 points (Scoring methods can be seen in Main)
     * - The result is set on the scene after the question has passed the question, and is added to the amount of points
     * - Read SoloMC methods for the User's answer; We set A,B,C to true if its clicked and instantly go to this menu to check correctness
     * - A users answer is compared to the actual one and based on percentages we award the points
     */
    public void checkCorrect() {
        int type = questions.getQuestions()[round].type();
        if (type == 1 || type == 2 || type == 3) {
            long result;
            int i = (questions.getQuestions()[round]).getAnswers().indexOf(questions.getQuestions()[round].getCorrectAnswer());
            System.out.println(i);
            if (A && i == 0) {
                if (doubleThis) {
                    updateScoreDouble(seconds);
                } else {
                    updateScoreNormal(seconds);
                }
                soloMCCtrl.answer.setStyle("-fx-background-color: #00ff00; -fx-background-radius: 1em;");

            } else if (B && i == 1) {
                if (doubleThis) {
                    updateScoreDouble(seconds);
                } else {
                    updateScoreNormal(seconds);
                }
                soloMCCtrl.answer.setStyle("-fx-background-color: #00ff00; -fx-background-radius: 1em;");

            } else if (C && i == 2) {
                if (doubleThis) {
                    updateScoreDouble(seconds);
                } else {
                    updateScoreNormal(seconds);
                }
                soloMCCtrl.answer.setStyle("-fx-background-color: #00ff00; -fx-background-radius: 1em;");
            } else {
                soloMCCtrl.answer.setStyle("-fx-background-color: #ff0000; -fx-background-radius: 1em;");
            }
        } else {
            if (soloOpenQCtrl.getAnswer().getText().equals(soloOpenQCtrl.getAnswerIn().getText())) {
                soloOpenQCtrl.setScore(solo.updateScore(100));
            } else {
                try {
                    long a = Long.parseLong(soloOpenQCtrl.getAnswerIn().getText());
                    int res = Main.calculateScore(seconds, a, Double.parseDouble(soloOpenQCtrl.getAnswer().getText()));
                    if (doubleThis) {
                        res = res * 2;
                    }
                    var result = solo.updateScore(res);
                    soloOpenQCtrl.setScore(result);
                    soloOpenQCtrl.setGainedScore(res);
                    soloMCCtrl.setScore(result);
                    soloOpenQCtrl.answer.setStyle("-fx-background-radius: 1em; -fx-background-color: orange");
                } catch (Exception e) {

                }

            }
        }
        doubleThis = false;
        resetAnswers(); // We Reset the answers a couple of times, better safe than sorry!
        stopTimer();  // We stop the timer (We do it a couple of times, just to make sure)
        round++;
    }

    /**
     * This method is the game loop of the Quizzz
     * - First we check if the rounds are over 20, when we add the user to the leaderboard/
     * and go to the leaderboard screen
     * - We reset the answers to all be false in order to allow for correctly working answer checking
     * - We get the question based on the round of the game from our generated list(in the beggining)
     * - reset() method resets the timer and allows for correct flow
     * - importQuestionData(Question) has a parameter question it retrieves its info and puts it in the fields
     * - We check if power-ups are used and then disable them if so
     * - We then set the newly updated scene
     */
    public void nextQuestion() {
        if (round >= 20) {
            server.addEntrySP(solo.getUserSP());
            stopTimer();
            resetToNormal();
            soloMCCtrl.hideAnswer();
            soloOpenQCtrl.hideAnswer();
            toLeaderboardSP();
        } else {
            resetAnswers();
            doubleThis = false;
            int type = questions.getQuestions()[round].type();
            if (type == 1 || type == 2 || type == 3) {
                soloMCCtrl.hideAnswer();
                setType1();
            } else if (questions.getQuestions()[round] instanceof OpenType2Question) {
                soloOpenQCtrl.hideAnswer();
                setType2();
            }

            if (jokerUsed) {
                soloMCCtrl.joker.setDisable(true);
            }
            if (doubleUsed) {
                soloMCCtrl.doublePoints.setDisable(true);
            }

        }
    }

    /**
     * Used to "Activate" the doubleUp powerup
     *
     * @doubleUsed holds information if the power up is already used!
     */
    public void clickDouble() {
        doubleThis = true;
        doubleUsed = true;
    }

    /**
     * This is the method we use to generate 20 questions
     *
     * @param numberOfQuestions this parameter is the amount of questions we would like to be generated
     * @return A list of generated questions
     */
    public ListQuestions generateQuestions(int numberOfQuestions) {
        ClientListQuestions questions = INJECTOR.getInstance(ClientListQuestions.class);
        questions.getServer().setServer(serverIP);
        questions.setRandom(new Random());
        questions.setCapacity(numberOfQuestions);
        questions.setQuestions(new Question[numberOfQuestions]);

        questions.generate(4, 3);

        return questions;
    }

    public void selectA() {
        A = true;
    }

    public void selectB() {
        B = true;
    }

    public void selectC() {
        C = true;
    }

    /**
     * Resets answers, so no bugs and false-positives appear in the answer checking
     */
    public void resetAnswers() {
        C = false;
        A = false;
        B = false;
    }

    /**
     * This is the Joker logic
     * - It eliminates one wrong answer
     * - Compiles a list of the indexes of the wrong answers
     * - Removes one of the wrong indexes using the .joker() method in the SoloMCCtrl
     */
    public void clickJoker() {
        int i = (questions.getQuestions()[round]).getAnswers().indexOf(questions.getQuestions()[round].getCorrectAnswer());
        List<Integer> wrongList = new ArrayList();
        for (int j = 0; j < 3; j++) {
            if (j != i) {
                wrongList.add(j);
            }

        }
        jokerUsed = true;
        Random random = new Random();
        int r = random.nextInt(2);
        int indexOfElimination = wrongList.get(r);
        soloMCCtrl.joker(indexOfElimination);
    }

    public void toLeaderboardHW() {
        lboardHWCtrl.initialize();
        lboardHWCtrl.updateLeaderboardEntries();
        primaryStage.setScene(leaderboardHWScene);
        primaryStage.setTitle("Leaderboard");
        primaryStage.show();
    }

    /**
     * Leads the user to the end game leaderboard
     * updateLeaderboardEntries(UserSP) updates the leaderboard with the users score
     */
    public void toLeaderboardSP() {
        lboardSPCtrl.updateLeaderboardEntries(solo.getUserSP());
        lboardSPCtrl.initialize();
        primaryStage.setScene(leaderboardSPScene);
        primaryStage.setTitle("Leaderboard");
        primaryStage.show();
    }


    /**
     * Forward to the admin scene
     */
    public void toAdmin() {
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin");
        primaryStage.show();
    }

    /**
     * for the admin scene
     * Notify the user that the question was added successfully
     * or that the question was changed/deleted successfully
     * +some basic settings for the dialog
     */
    public void successDialog() {
        final Stage dialog = new Stage();
        dialog.setX(800);
        dialog.setY(400);
        dialog.setTitle("Succeed");
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        Text text = new Text("Process done successfully!");
        text.setFont(Font.font(33));
        text.setTextAlignment(TextAlignment.CENTER);
        dialogVbox.getChildren().add(text);
        dialogVbox.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(dialogVbox, 400, 200);
        dialog.setScene(dialogScene);
        dialog.show();
        dialogVbox.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

    }

    /**
     * Sets the IP for all the services the game is going to use
     *
     * @param text
     */
    public void setIP(String text) {
        serverIP = text;
        server.setServer(serverIP);
        playerUtils.setServer(serverIP);
        questionUtils.setServer(serverIP);
        multiPlayerSession.getServer().setServer(serverIP);
        multiPlayerSession.getPlayerUtils().setServer(serverIP);
        adminCtrl.getServer().setServer(serverIP);
        lboardHWCtrl.setIP(serverIP);
        lboardMPCtrl.setIP(serverIP);
        lboardSPCtrl.setIP(serverIP);
    }
}