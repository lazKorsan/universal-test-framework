package collectAPI;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class collectAPI {

    String contentType = "application/json";
    String apikey = "apikey 43mgAenlx77SRyuHHuCijD:5lBqbHdHNrl0uWfYlIMSaJ";
    String url = "https://api.collectapi.com/health/kktcCityList";

    @Test
    public void tc01() {
        // Rest Assured kullanarak API isteği
        Response response = given()
                .header("content-type", "application/json")
                .header("authorization", "apikey 43mgAenlx77SRyuHHuCijD:5lBqbHdHNrl0uWfYlIMSaJ")
                .when()
                .get("https://api.collectapi.com/health/kktcCityList");

        // Yanıtı konsola yazdırma ve doğrulama
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test
    public void tc02(){

        Response response = given()
                .header("content-type",contentType)
                .header("authorization",apikey)
                .when()
                .get(url);

        response.prettyPrint();
        response.then().statusCode(200);

    }


}