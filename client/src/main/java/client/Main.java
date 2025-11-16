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

package client;

import static com.google.inject.Guice.createInjector;

import client.scenes.MainCtrl;
import client.scenes.MultiMCScreenCtrl;
import client.scenes.SoloMCScreenCtrl;
import client.scenes.SplashScreenCtrl;
import client.scenes.*;
import com.google.inject.Injector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        var splash = FXML.load(SplashScreenCtrl.class, "client", "scenes", "SplashScreen.fxml");
        var soloMC = FXML.load(SoloMCScreenCtrl.class, "client", "scenes", "SoloMCScreen.fxml");
        var mpMC = FXML.load(MultiMCScreenCtrl.class, "client", "scenes", "MultiMCScreen.fxml");
        var soloO = FXML.load(SoloOpenQScreenCtrl.class, "client", "scenes", "SoloOpenQScreen.fxml");
        var mpO = FXML.load(MultiOpenQScreenCtrl.class, "client", "scenes", "MultiOpenQScreen.fxml");
        var waitingLobbyMC = FXML.load(WaitingLobbyCtrl.class, "client", "scenes", "WaitingLobby.fxml");
        var admin = FXML.load(AdminCtrl.class, "client", "scenes", "AdminScreen.fxml");


        var lboardHW = FXML.load(LeaderboardHWScreenCtrl.class, "client", "scenes", "LeaderboardHWScreen.fxml");
        var lboardSP = FXML.load(LeaderboardSPScreenCtrl.class, "client", "scenes", "LeaderboardSPScreen.fxml");
        var lboardMP = FXML.load(LeaderboardMPScreenCtrl.class, "client", "scenes", "LeaderboardMPScreen.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        mainCtrl.initialize(primaryStage, splash, soloMC, mpMC, soloO, mpO, lboardHW, lboardSP, lboardMP, waitingLobbyMC, admin);

        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("Quit Game");
            alert.setHeaderText("You are about to exit the application!");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                mainCtrl.toSplash();
            }else{
                event.consume();
            }
        });
    }


    /**
     * this method generates a ListQuestions object
     * to be used in the SoloPlayer scenario
     * @param numberOfQuestions - takes an integer representing the required number of questions in the returned object
     * @return - a ListQuestion object
     */

    /**
     * Function to return the score of an answer given that is correct
     * 20 is hard codded here which can be bummer later
     *
     * @param timeLeft the time passed
     * @return the int score
     */
    public static int calculateScore(double timeLeft) {
        return (int) ((timeLeft / 14) * 50 + 50);
    }

    /**
     * Simple function that gets the ratio between the guessed and the correct answer
     * If the ratio between both is less than 0.1 we don't reward the user with any points
     * Time accounts for 0.3 of the result and we just multiply it as normal
     * The quality of the approximation is 0.73 of the final result, that means that the closer you are the more points you get
     * This is 1.03 in total, which means that if the user is quick enough and close enough its possible to get more than 100 points
     * Additionally we are 1 sec buffering the time in order to not penalize user from reading the question
     *
     * @param time   the time passed
     * @param guess  the guess of the user
     * @param answer the correct value
     * @return the int score
     */
    public static int calculateScore(double time, double guess, double answer) {
        if(guess>0) {
            double t = answer / guess;
            double partialPoints = Math.abs(Math.log10(t));
            return (int) Math.round(69 / (partialPoints + 1) + 34 * time / 15);
        } else {
            double t = answer / answer - guess;
            double partialPoints = Math.abs(Math.log10(t));
            return (int) Math.round(69 / (partialPoints + 1) + 34 * time / 15);
        }
    }
}