package Api.Utilities;

import config_Requirements.ConfigLoader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class Authentication{

    static ConfigLoader configLoader = new ConfigLoader();
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    public static String generateToken() {

        RequestSpecification spec = new RequestSpecBuilder().setBaseUri(configLoader.getApiConfig("base_url")).build();

        spec.pathParams("pp1", "api", "pp2", "token");

        JSONObject reqBody  = new JSONObject();
        reqBody.put("email", configLoader.getApiConfig("adminEmail"));
        reqBody.put("password", configLoader.getApiConfig("adminPassword"));

        Response response = given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("x-api-key", "1234")
                .header("User-Agent", USER_AGENT) // User-Agent eklendi
                .when()
                .body(reqBody.toString())
                .post("/{pp1}/{pp2}");

        // Hata ayıklama için response'u yazdır
        System.out.println("Authentication Response Status Code: " + response.getStatusCode());
        response.prettyPrint();

        JsonPath repJP = response.jsonPath();

        String token = repJP.getString("data.access_token");
        System.out.println("Token : " + token);

        return token;
    }
}