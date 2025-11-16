package client.utils;

import com.github.tomakehurst.wiremock.*;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import commons.Activity;
import commons.LeaderboardEntrySP;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class ServerUtilsTest {

    private static final int PORT = 8000;
    private static final String HOST = "localhost";
    private static final String ip = "http://localhost:8000";
    public ServerUtils server = new ServerUtils();
    WireMockServer mockServer;
    ResponseDefinitionBuilder mockResponse;
    Activity a;

    @AfterEach
    void stop() {
        mockServer.stop();
    }

    @BeforeEach
    void setup() {
        mockServer = new WireMockServer(8000);
        server.setServer(ip);
        a = new Activity("38-hairdryer", "38/hairdryer.png", "Using a hairdryer for an hour", 1200, "https://blog.arcadia.com/electricity-costs-10-key-household-products/#:~:text=Hair%20Dryer%20Electricity%20Costs&text=Since%20it%20takes%201200%20watts,to%20run%20for%2030%20minutes.");
        a.setIndex(0);
        mockResponse = new ResponseDefinitionBuilder();
        mockResponse.withStatus(200);
        mockResponse.withHeader("Content-type", "application/json");
        WireMock.configureFor(HOST, PORT); // http://localhost:8000/
        mockServer.start();
    }

    @Test
    void testBasicMethods() {
        ServerUtils testServer = new ServerUtils();
        testServer.setServer("http://localhost:8000");
        assertEquals("http://localhost:8000",testServer.getServer());
        testServer.setServer("http://localhost:8800");
        assertEquals("http://localhost:8800", testServer.getServer());
    }

    @Test
    void getTop10SP() {
        mockResponse.withBody("[\n" +
                "    {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"asd\",\n" +
                "        \"score\": 931\n" +
                "    }\n" +
                "]");
        mockServer.stubFor(
                get("/api/leaderboardsp/top10")
                        .willReturn(mockResponse));
        assertEquals(1, server.getTop10SP().get(0).getId());
    }

    @Test
    void getRandomActivity() {
        mockResponse.withBody("{\n" +
                "    \"id\": \"54-laptop\",\n" +
                "    \"image_path\": \"54/laptop.png\",\n" +
                "    \"title\": \"Using laptop for 6 hours\",\n" +
                "    \"consumption_in_wh\": 360,\n" +
                "    \"source\": \"https://energyusecalculator.com/electricity_laptop.htm\",\n" +
                "    \"index\": 1166\n" +
                "}");
        mockServer.stubFor
                (get("/api/activities/rnd")
                        .willReturn(mockResponse)
                );
        assertEquals("54-laptop", server.getRandomActivity().getId());
    }

    @Test
    void getAllActivities() {
        mockResponse.withBody("[\n" +
                "    {\n" +
                "        \"id\": \"38-hairdryer\",\n" +
                "        \"image_path\": \"38/hairdryer.png\",\n" +
                "        \"title\": \"Using a hairdryer for an hour\",\n" +
                "        \"consumption_in_wh\": 1200,\n" +
                "        \"source\": \"https://blog.arcadia.com/electricity-costs-10-key-household-products/#:~:text=Hair%20Dryer%20Electricity%20Costs&text=Since%20it%20takes%201200%20watts,to%20run%20for%2030%20minutes.\",\n" +
                "        \"index\": 0\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": \"38-leafblower\",\n" +
                "        \"image_path\": \"38/leafblower.png\",\n" +
                "        \"title\": \"Using a leafblower for 15 minutes\",\n" +
                "        \"consumption_in_wh\": 183,\n" +
                "        \"source\": \"https://www.kompulsa.com/how-much-power-are-your-appliances-consuming/#power_consumption_of_leaf_blowers\",\n" +
                "        \"index\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": \"38-coffee\",\n" +
                "        \"image_path\": \"38/coffee.png\",\n" +
                "        \"title\": \"Making a hot cup of coffee\",\n" +
                "        \"consumption_in_wh\": 300,\n" +
                "        \"source\": \"https://business.directenergy.com/blog/2017/september/international-coffee-day#:~:text=Depending%20on%20the%20model%2C%20it,an%20automated%20drip%20coffee%20maker.\",\n" +
                "        \"index\": 2\n" +
                "    }]");
        mockServer.stubFor(
                get("/api/activities")
                        .willReturn(mockResponse));
        assertEquals(3, server.getAllActivities().size());
        assertEquals(a, server.getAllActivities().get(0));
    }

    @Test
    void addEntrySP() {
        LeaderboardEntrySP user = new LeaderboardEntrySP("asd", 931);
        LeaderboardEntrySP user1 = new LeaderboardEntrySP("asd", 932);
        user.setId(1);
        mockResponse.withBody("    {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"asd\",\n" +
                "        \"score\": 931\n" +
                "    }");
        //mockResponse.withHeader("Content-type", "text/plain");
        mockServer.stubFor(
                post("/api/leaderboardsp/post").
                        withRequestBody(
                                equalToJson("    {\n" +
                                "        \"id\": 1,\n" +
                                "        \"name\": \"asd\",\n" +
                                "        \"score\": 931\n" +
                                "    }"))
                        .willReturn(mockResponse));
        assertNotEquals(user1, server.addEntrySP(user));
        assertThrows(NotFoundException.class, () -> {
            server.addEntrySP(user1);
        });
        assertEquals(1, server.addEntrySP(user).getId());
    }

    @Test
    void addActivity() {
        mockResponse.withBody("{\n" +
                "    \"id\": \"54-laptop\",\n" +
                "    \"image_path\": \"54/laptop.png\",\n" +
                "    \"title\": \"Using laptop for 6 hours\",\n" +
                "    \"consumption_in_wh\": 360,\n" +
                "    \"source\": \"https://energyusecalculator.com/electricity_laptop.htm\",\n" +
                "    \"index\": 1166\n" +
                "}");
        mockServer.stubFor(
                post("/api/activities/post").
                        withRequestBody(
                                equalToJson("    {\n" +
                "        \"id\": \"54-laptop\",\n" +
                "        \"image_path\": \"54/laptop.png\",\n" +
                "        \"title\": \"Using laptop for 6 hours\",\n" +
                "        \"consumption_in_wh\": 360,\n" +
                "        \"source\": \"https://energyusecalculator.com/electricity_laptop.htm\",\n" +
                "        \"index\": 1166\n" +
                "    }"))
                        .willReturn(mockResponse));
        Activity activity = new Activity("54-laptop", "54/laptop.png", "Using laptop for 6 hours", 360, "https://energyusecalculator.com/electricity_laptop.htm");
        activity.setIndex(1166);
        assertThrows(NotFoundException.class, () -> {
            server.addActivity(a);
        });
        assertEquals(activity, server.addActivity(activity));
    }
}