package collectAPI;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class halFiyatlari {

    String url="https://api.collectapi.com/bazaar/single?city=istanbul";
    String contentType="application/json";
    String AUTHORIZATION="authorization";
    String CONTENT_TYPE="content-type";
    String apikey="apikey 43mgAenlx77SRyuHHuCijD:5lBqbHdHNrl0uWfYlIMSaJ";


    @Test
    public void tc01(){

        Response response=given()
                .header(CONTENT_TYPE,contentType)
                .header(AUTHORIZATION,apikey)
                .when()
                .get(url)
                .then()
                .log().all()
                .extract().response();

        response.prettyPrint();
        response.then().statusCode(200);

    }



}
