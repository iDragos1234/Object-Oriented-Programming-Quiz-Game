package server.api;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ActivityControllerTest {

    // random - to be implemented

    public int nextInt;
    private MyRandom random;
    private TestActivityRepository repo;

    private ActivityController sut;

    @BeforeEach
    public void setup() {
        random = new MyRandom();
        repo = new TestActivityRepository();
        sut = new ActivityController(random, repo);
    }

    @Test
    public void cannotAddNullActivity() {
        var actual = sut.postActivity(getActivity(null, null, null, (long) 0, null, (long) 0));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testGetRequest() {
        var activity = getActivity("23-swift", "http://nice-image.country/", "driving a car", (long) 2000, "http://source.code/", (long) 107);

        sut.postActivity(activity);

        assertEquals(activity, repo.getById("23-swift"));
    }

    @Test
    public void testPostRequest() {
        var activity1 = getActivity("23-swift", "http://nice-image.country/", "driving a car", (long) 2000, "http://source.code/", (long) 107);
        sut.postActivity(activity1);

        var activity2 = getActivity("32-slow", "http://not-nice-image.code/", "driving a bus", (long) 4000, "http://source.here/", (long) 108);
        sut.postActivity(activity2);

        assertEquals(2, repo.count());
        assertEquals(activity1, repo.getById("23-swift"));
        assertEquals(activity2, repo.getById("32-slow"));

        activity1.setIndex((long) 109);
        sut.postActivity(activity1);


        System.out.println(repo.findAll());
        assertEquals(3, repo.count());
        assertEquals(activity1, repo.getById("23-swift"));

        // activity2.setId("100-very-fast");
        sut.postActivity(activity2);
        assertEquals(4, repo.count());
    }

    @Test
    public void testPutRequest() {
        var activity1 = getActivity("23-swift", "http://nice-image.country/", "driving a car", (long) 2000, "http://source.code/", (long) 107);
        sut.updateActivity(activity1, "23-swift");

        var activity2 = getActivity("32-slow", "http://not-nice-image.code/", "driving a bus", (long) 4000, "http://source.here/", (long) 108);
        sut.updateActivity(activity2, "32-slow");

        assertEquals(2, repo.count());
        assertEquals(activity1, repo.getById("23-swift"));
        assertEquals(activity2, repo.getById("32-slow"));

        activity1.setIndex((long) 109);
        sut.updateActivity(activity1, "23-swift");


        System.out.println(repo.findAll());
        assertEquals(3, repo.count());
        assertEquals(activity1, repo.getById("23-swift"));

        activity2.setId("80-fast");
        sut.updateActivity(activity2, "32-slow");
        assertEquals(4, repo.count());
        assertEquals(activity2, repo.getById("32-slow"));

        activity1.setId("90-faster");
        sut.updateActivity(activity1, "90-faster");
        assertEquals(5, repo.count());
        assertEquals(activity1, repo.getById("90-faster"));
    }






    // database logs

    @Test
    public void databaseLoggingSave() {
        var activity = getActivity("23-swift", "http://nice-image.country/", "driving a car", (long) 2000, "http://source.code/", (long) 107);

        sut.postActivity(activity);
        assertEquals("save", repo.calledMethods.get(0));
    }

    @Test
    public void databaseLoggingFindById() {
        var activity = getActivity("23-swift", "http://nice-image.country/", "driving a car", (long) 2000, "http://source.code/", (long) 107);

        sut.postActivity(activity);
        activity = getActivity("32-slow", "http://not-nice-image.code/", "driving a bus", (long) 4000, "http://source.here/", (long) 108);
        sut.updateActivity(activity, "23-swift");

        assertEquals("save", repo.calledMethods.get(0));
        assertEquals("findById", repo.calledMethods.get(1));
    }

    @Test
    public void databaseLoggingDeleteById() {
        var activity = getActivity("23-swift", "http://nice-image.country/", "driving a car", (long) 2000, "http://source.code/", (long) 107);
        sut.postActivity(activity);

        activity = getActivity("32-slow", "http://not-nice-image.code/", "driving a bus", (long) 4000, "http://source.here/", (long) 108);
        sut.postActivity(activity);

        sut.deleteActivity("23-swift");

        System.out.println(repo.calledMethods);

        assertEquals("save", repo.calledMethods.get(0));
        assertEquals("save", repo.calledMethods.get(1));

        assertEquals("deleteById", repo.calledMethods.get(3));  // index is not 2, because the deleteActivity does a findById beforehand,
                                                                        // in the condition statement
    }

    @Test
    public void databaseLoggingDeleteAll() {
        var activity = getActivity("23-swift", "http://nice-image.country/", "driving a car", (long) 2000, "http://source.code/", (long) 107);
        sut.postActivity(activity);

        activity = getActivity("32-slow", "http://not-nice-image.code/", "driving a bus", (long) 4000, "http://source.here/", (long) 108);
        sut.postActivity(activity);

        sut.deleteAllActivities();

        assertEquals("save", repo.calledMethods.get(0));
        assertEquals("save", repo.calledMethods.get(1));

        assertEquals("deleteAll", repo.calledMethods.get(2));
    }


    // auxiliary method(s) and class(es)

    private static Activity getActivity(String id, String image_path, String title, Long consumption_in_wh, String source, Long index) {
        Activity a = new Activity(id, image_path, title, consumption_in_wh, source);
        a.setIndex((long) index);
        return a;
    }

    @SuppressWarnings("serial")
    public class MyRandom extends Random {

        public boolean wasCalled = false;

        @Override
        public int nextInt(int bound) {
            wasCalled = true;
            return nextInt;
        }
    }
}
