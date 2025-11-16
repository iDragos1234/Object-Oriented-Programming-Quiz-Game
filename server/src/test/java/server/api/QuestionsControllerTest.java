package server.api;

import commons.Activity;
import commons.Questions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ServerListQuestions;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class QuestionsControllerTest {

    private QuestionsController questionsController;

    private Random random;
    private TestActivityRepository repo;


    @BeforeEach
    void setUp() {
        random = new Random();
        repo = new TestActivityRepository();
        questionsController = new QuestionsController(random, repo);
    }

    @Test
    void getListQuestion() {
        initRepo();
        questionsController.getListQuestions("1&1&3");
    }

    /**
     * generate a list of 100 questions with 3 types and test if all 3 types exists.
     * this may be tested as false if we are really unlucky, that all 100 questions only had 2 or 1 types.
     */
    @Test
    void testGetListQuestionsType2() {
        initRepo();
        String testString = "100&3&3";
        String[] arrayParams = testString.split("&");
        int numberOfQuestions = Integer.parseInt(arrayParams[0]);
        int typesBound = Integer.parseInt(arrayParams[1]);
        int numberAnswers = Integer.parseInt(arrayParams[2]);
        ListQuestions testQuestions = new ServerListQuestions(repo, numberOfQuestions);
        testQuestions.generate(typesBound, numberAnswers);
        ListQuestions questions = new ListQuestions(testQuestions.getCapacity());
        questions.setRandom(null);
        questions.setQuestions(testQuestions.getQuestions());
        assertEquals(questions.getCapacity(), 100);
        boolean type2Exist = false;
        boolean type1Exist = false;
        boolean type3Exist = false;
        for (int i = 0; i < 100; i++) {
            if (!(questions.getQuestions()[i] instanceof Type2Question)) {
                type2Exist = true;
            }
            if (!(questions.getQuestions()[i] instanceof Type3Question)) {
                type3Exist = true;
            }
            if (!(questions.getQuestions()[i] instanceof Type1Question)) {
                type1Exist = true;
            }
        }
        assertTrue(type2Exist);
        assertTrue(type1Exist);
        assertTrue(type3Exist);
    }


    /**
     * Use the method to create a list of questions with size 1 and test its properties
     * test if the question is generated correctly
     */
    @Test
    void testGetListQuestionOfSize1() {
        repo.save(getActivity("1", "a", "b", (long) 1, "s", (long) 1));

        String testString = "1&1&3";
        String[] arrayParams = testString.split("&");
        int numberOfQuestions = Integer.parseInt(arrayParams[0]);
        int typesBound = Integer.parseInt(arrayParams[1]);
        int numberAnswers = Integer.parseInt(arrayParams[2]);

        ListQuestions testQuestions = new ServerListQuestions(repo, numberOfQuestions);
        testQuestions.generate(typesBound, numberAnswers);
        ListQuestions questions = new ListQuestions(testQuestions.getCapacity());
        questions.setQuestions(testQuestions.getQuestions());

        Question q = questions.getQuestions()[0];
        if (q.type() == 0) assertEquals(q.getAttachment(), getActivity("1", "a", "b", (long) 1, "s", (long) 1));
        else assertEquals(q.getAnswers().get(0), getActivity("1", "a", "b", (long) 1, "s", (long) 1));

        assertEquals(questions.getCapacity(), 1);
        assertNotNull(questions);
        assertEquals(questions.getQuestions().length, 1);
    }

    /**
     * generate a list of questions and check if they were randomly generated
     * test if the questions are different (they don't have to be all different)
     * also checks if the questions were all contained in the repository (so they were generated from it)
     */
    @Test
    void TestGeneratedRandomly() {
        initRepo();
        String testString = "10&3&3";
        String[] arrayParams = testString.split("&");

        int numberOfQuestions = Integer.parseInt(arrayParams[0]);
        int typesBound = Integer.parseInt(arrayParams[1]);
        int numberAnswers = Integer.parseInt(arrayParams[2]);

        ListQuestions testQuestions = new ServerListQuestions(repo, numberOfQuestions);
        testQuestions.generate(typesBound, numberAnswers);

        ListQuestions questions = new ListQuestions(testQuestions.getCapacity());
        questions.setQuestions(testQuestions.getQuestions());

        Question question = questions.getQuestions()[0];

        boolean fromRepo = true;

        for (int i = 0; i < 10; i++) {

            Question q = questions.getQuestions()[i];

            if (q.type() == 0) {
                if (!repo.findAll().contains(q.getAttachment())) {
                    fromRepo = false;
                }
            } else {
                if (!repo.findAll().contains(q.getAnswers().get(0))) {
                    fromRepo = false;
                }
            }
        }

        assertTrue(fromRepo);
    }

    /**
     * copied from Dragos's class
     *
     * @param id                id
     * @param image_path        image path
     * @param title             title
     * @param consumption_in_wh consumption
     * @param source            source
     * @param index             index
     * @return a new activity with those parameters
     */
    private static Activity getActivity(String id, String image_path, String title, Long consumption_in_wh, String source, Long index) {
        Activity a = new Activity(id, image_path, title, consumption_in_wh, source);
        a.setIndex(index);
        return a;
    }

    /**
     * initialize the repository
     * so there are actually questions in it
     */
    private void initRepo() {

        long consumption = 20;
        String img = "image_path";
        String title = "title";
        String source = "source";
        long index = 0;

        for (int i = 0; i < 20; i++) {
            Activity activity = getActivity(String.valueOf(i + 1), img, title, consumption, source, (long) i);
            repo.save(activity);
        }
    }

}