package server.api;

import commons.Questions.ListQuestions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.ServerListQuestions;
import server.database.ActivityRepository;

import java.util.Random;

@RestController
@RequestMapping("/api/questions")
public class QuestionsController {

    private Random random;
    private ActivityRepository repo;

    public QuestionsController(Random random, ActivityRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    /**
     * This method receives a String params containing the number of questions, the number of answers for
     * every question and the number of types of questions; which is parsed by the
     * method to extract these parameters
     * @param params - a String containing parameters for the ListQuestions instance, separated by '&'
     * @return - a ListQuestions object
     */

    @GetMapping("/{params}")
    public ResponseEntity<ListQuestions> getListQuestions(@PathVariable String params) {

        System.out.println("CHEKPOINT");

        String[] arrayParams = params.split("&");
        int numberOfQuestions = Integer.parseInt(arrayParams[0]);
        int typesBound = Integer.parseInt(arrayParams[1]);
        int numberAnswers = Integer.parseInt(arrayParams[2]);

        ListQuestions aux = (ListQuestions) new ServerListQuestions(repo, numberOfQuestions);
        aux.generate(typesBound, numberAnswers);

        ListQuestions questions = new ListQuestions(aux.getCapacity());
        questions.setQuestions(aux.getQuestions());
        questions.setRandom(null);


        return ResponseEntity.ok(questions);
    }
}
