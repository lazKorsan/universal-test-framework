package collectAPI;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class havaDurumu {

    String URL ="https://api.collectapi.com/weather/getWeather?lang=tr&city=denizli";
    String CONTENT_TYPE="content-type";
    String contentType="application/json";
    String AUTHORIZATION="authorization";
    String apikey="apikey 43mgAenlx77SRyuHHuCijD:5lBqbHdHNrl0uWfYlIMSaJ";

    @Test
    public void tc01(){

        Response response=given()
                .header(CONTENT_TYPE,contentType)
                .header(AUTHORIZATION,apikey)
                .when()
                .get(URL)
                .then()
                .log().all()
                .extract().response();

        response.prettyPrint();
        response.then().statusCode(200);

    }

}
