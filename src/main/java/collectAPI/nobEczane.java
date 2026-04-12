package collectAPI;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class nobEczane {

    String base_url="https://api.collectapi.com";
    String pp1="/health";
    String pp2="/dutyPharmacy";
    String CONTENT_TYPE="content-type";
    String contentType= "application/json";
    String AUTHORIZATION="authorization";
    String apikey="apikey 43mgAenlx77SRyuHHuCijD:5lBqbHdHNrl0uWfYlIMSaJ";
    String il="Denizli";
    String ilce="Sarayköy";
    String endPoint= "?ilce=Sarayköy&il=Denizli";

    @Test
    public void tc01(){
   Response response =given()
                .header(CONTENT_TYPE,contentType)
                .header(AUTHORIZATION,apikey)
                .when()
                .get(base_url+pp1+pp2+endPoint)
                .then()
                .log().all()
                .extract().response();

        response.prettyPrint();
        response.then().statusCode(200);
    }

}
