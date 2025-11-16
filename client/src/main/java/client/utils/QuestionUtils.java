package client.utils;


import commons.Questions.ListQuestions;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


public class QuestionUtils {

    private String server;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    /**
     * getListQuestions - this method is used to request a ListQuestions object created by the server, with given parameters.
     * @param numberOfQuestions - the number of questions in this ListQuestions.
     * @param typesBound - the number of question types that can be found in this list.
     * @param numberAnswers - the number of potential answers for each question.
     * @return - a ListActivity object.
     */
    public ListQuestions getListQuestions(int numberOfQuestions, int typesBound, int numberAnswers) {
        String params = "" + numberOfQuestions + "&" + typesBound + "&" + numberAnswers;
        System.out.println(params);
        var res = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/questions/" + params) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<ListQuestions>() {
                });

        return res;
    }
}
