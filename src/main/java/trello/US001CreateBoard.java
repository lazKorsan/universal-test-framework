package trello;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static trello.trelloPage.*;

public class US001CreateBoard {

    String apiKey=trelloApiKey;
    String token=trelloToken;

    @Test
    public void createBoard(){

        // curl --request POST \
        //  --url 'https://api.trello.com/1/boards/?name={name}&key=APIKey&token=APIToken'

        int randomNumber = (int)(Math.random() * 901) + 100;
        String boardName = "newWorkSpace" + randomNumber;
        String url="https://api.trello.com/1/boards/?name="+boardName+"&key="+apiKey+"&token="+token;

        System.out.println(url);

        Response response= given()
                .header("key", apiKey)
                .header("token", token)
                .when()
                .post(url)
                .then()
                .log().all()
                .extract().response();

        response.prettyPrint();
        response.then().statusCode(200);

        String id = "69dc00ae23bb19b37c7cc7eb";
        String visibleIdURL="https://trello.com/b/zdNDW9US/newworkspace";
        String secretId="zdNDW9US";
        String shortUrl ="https://trello.com/b/zdNDW9US";


    }
}
