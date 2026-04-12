package collectAPI;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class atlasianToken {

    // curl --request GET \
    //  --url 'https://api.trello.com/1/actions/{id}?key=APIKey&token=APIToken'

    @Test
    public void  tc03()
    {
        Response response = given()
                .when()
                .get("https://api.trello.com/1/actions/")
                .then()
                .log().all()
                .extract().response();
        response.prettyPrint();
    }
}
