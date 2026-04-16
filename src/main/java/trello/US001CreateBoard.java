package trello;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class US001CreateBoard {

   // String apiKey=ConfigLoader.getProp("trelloApiKey");
   // String token=ConfigLoader.getProp("trelloToken");
    String apiKey="06bac02cfc9639df5514c36f580ecb8b";
    String token="ATTAd7762c52809ed4d798bd5c1cbd078e4fa6c1834e2c16b87f34d061bdae488c1eD619329A";

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

        // "id": "69dc26accd366993583e9982",
        //    "name": "newWorkSpace307",
        //    "desc": "",
        //    "descData": null,
        //    "closed": false,
        //    "idOrganization": "69dbd8d23e23dca76dda8cde",
        //    "idEnterprise": null,
        //    "pinned": false,
        //    "url": "https://trello.com/b/lOYOD5eK/newworkspace307",
        //    "shortUrl": "https://trello.com/b/lOYOD5eK",




    }
}
