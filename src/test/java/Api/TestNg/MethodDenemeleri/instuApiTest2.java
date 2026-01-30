package Api.TestNg.MethodDenemeleri;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class instuApiTest2 {

    // 1. Ayarlar (Dokümantasyondaki değerler)
    private final String BASE_URL = "https://qa.instulearn.com";
    private final String X_API_KEY = "1234";
    private String authToken; // Login sonrası alınacak token

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;

        // 2. TOKEN ALMA (Cypress'teki before bloğu karşılığı)
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "162.admin@instulearn.com");
        credentials.put("password", "162162162");

        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("x-api-key", X_API_KEY)
                .body(credentials)
                .when()
                .post("/api/token")
                .then()
                .statusCode(200)
                .extract().response();

        // JSON içerisinden token'ı çekiyoruz
        authToken = response.jsonPath().getString("data.access_token");
        System.out.println("✅ Token başarıyla alındı.");
    }

    @Test(priority = 1)
    public void testGetCourses() {
        // 3. GET SORĞUSU (Dokümantasyondaki cURL yapısına göre)
        // curl --location -g '{{base_url}}/api/courses' --header 'x-api-key: 1234' --header 'Accept: application/json'

        given()
                .header("Accept", "application/json")
                .header("x-api-key", X_API_KEY)
                .header("Authorization", "Bearer " + authToken) // Cypress testinde olduğu için eklendi
                .when()
                .get("/api/courses")
                .then()
                .statusCode(200) // 1. Doğrulama: Status 200
                .body("remark", equalTo("success")) // 2. Doğrulama: remark success
                .body("data", notNullValue()) // Body boş olmamalı
                .log().body(); // Sonucu konsola yazdırır

        System.out.println("✅ Kurs listeleme testi (200 & success) doğrulandı.");
    }

    @Test(priority = 2)
    public void testGetCoursesWithoutToken() {
        // 4. Token Olmadan Erişim Testi (401)
        given()
                .header("Accept", "application/json")
                .header("x-api-key", X_API_KEY)
                // Authorization header'ı eksik
                .when()
                .get("/api/courses")
                .then()
                .statusCode(401);

        System.out.println("✅ Yetkisiz erişim testi (401) başarıyla sonuçlandı.");
    }
}