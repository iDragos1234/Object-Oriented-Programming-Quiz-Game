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
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import commons.*;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private String server;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    /**
     * This method retrieves the top 10 single player leaderboard entries in order to populate the leaderboard
     *
     * @return returns a list of entries that can be modified later on to fit our needs
     */
    public List<LeaderboardEntrySP> getTop10SP() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/leaderboardsp/top10") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<LeaderboardEntrySP>>() {
                });
    }

    /**
     * this method retrieves a random Activity object from the server ActivityRepository
     *
     * @return - a randomly chosen Activity object
     */

    public Activity getRandomActivity() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/activities/rnd") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Activity>() {
                });
    }

    public List<Activity> getAllActivities() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/activities") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Activity>>() {
                });
    }

    /**
     * A user is added to the database along with all their information
     *
     * @param entrySP the user that will be added to the database
     * @return returns what is added
     */
    public LeaderboardEntrySP addEntrySP(LeaderboardEntrySP entrySP) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/leaderboardsp/post") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(entrySP, APPLICATION_JSON), LeaderboardEntrySP.class);
    }

    /**
     * (For the admin)
     * add a question to the repository
     *
     * @param activity the question going to be added
     * @return the question
     */
    public Activity addActivity(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/activities/post")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }

    /**
     * used in admin scene
     * (Edit database)
     * Get the activity with certain id
     *
     * @param id the id for the question
     * @return the question
     */
    public Activity getActivity(String id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/activities/get_id/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Activity>() {
                });
    }

    /**
     * used in admin scene to delete the question
     * delete a question with certain id from the database
     *
     * @param id id of the question
     * @return the question
     */
    public Activity deleteById(String id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/activities/delete/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(new GenericType<Activity>() {
                });
    }


}
