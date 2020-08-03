/**
 * Created by sc16kh on 19/11/19.
 */
import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class TimeTest
{

    private static final String MY_REST_URI = "http://localhost:9999/MyServer";
    private static final String YEARS = "date/years";
    private static final String NUMBERS_URI = "http://numbersapi.com/";
    private static final String PIRATE_URI = "https://api.funtranslations.com/translate/pirate.json";

    public static void main(String[] args)
    {
        long startTime = System.nanoTime();
        webService1Test();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Webservice1 response time = "+ duration + " milliseconds ");

        startTime = System.nanoTime();
        webService2Test();
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
        System.out.println("Webservice2 response time = "+ duration + " milliseconds ");

        startTime = System.nanoTime();
        webService3Test();
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
        System.out.println("Webservice3 response time = "+ duration + " milliseconds ");


    }

    private static void webService1Test()
    {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(MY_REST_URI);
        WebResource Years = service.path(YEARS).path(2019 + "/" + 11 + "/" + 18 + "/" + 1997 + "/" + 9 + "/" + 16);
        String yearsResponse = Years.accept(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println("Response: " + yearsResponse + " years");
    }

    private static void webService2Test()
    {
        //WEB SERVICE 2 - returns information about output numbers from web service 1
        ClientConfig config = new DefaultClientConfig();
        Client client2 = Client.create(config);
        WebResource NumberTrivia = client2.resource(NUMBERS_URI);
        WebResource YearTrivia = NumberTrivia.path("22");
        String yearTriviaResponse = YearTrivia.accept(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println("Response :" + yearTriviaResponse);
    }

    private static void webService3Test()
    {
        ClientConfig config = new DefaultClientConfig();
        Client client3 = Client.create(config);
        WebResource pirate = client3.resource(PIRATE_URI);
        WebResource text1 = pirate.queryParam("text","22");
        ClientResponse response = text1.accept("application/json").get(ClientResponse.class);
        String output = response.getEntity(String.class);
        System.out.println(output);
    }

}
