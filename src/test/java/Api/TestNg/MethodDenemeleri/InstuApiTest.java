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

public class InstuApiTest {

    // 1. Tüm ayarlar (Constants)
    private final String BASE_URL = "https://qa.instulearn.com";
    private final String X_API_KEY = "1234";
    private String authToken; // Token burada saklanacak

    @BeforeClass
    public void setup() {
        // RestAssured için temel URL tanımı
        RestAssured.baseURI = BASE_URL;

        // 2. Admin Bilgileri ve Login (Token Alma)
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("email", "162.admin@instulearn.com");
        loginBody.put("password", "162162162");

        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("x-api-key", X_API_KEY)
                .body(loginBody)
                .when()
                .post("/api/token")
                .then()
                .statusCode(200) // Status 200 kontrolü
                .extract().response();

        // Token'ı extract et (Cypress'teki authToken = response.body.data.access_token karşılığı)
        authToken = response.jsonPath().getString("data.access_token");

        System.out.println("✅ Token alındı: " + (authToken != null ? authToken.substring(0, 20) + "..." : "HATA!"));
    }

    @Test
    public void testGetCoursesSuccess() {
        // Token kontrolü
        Assert.assertNotNull(authToken, "Token alınamadığı için test başlatılamaz!");

        // 4. ASIL GET TESTİ
        given()
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .header("x-api-key", X_API_KEY)
                .log().all() // Request detaylarını konsola yazdırır (Cypress log karşılığı)
                .when()
                .get("/api/courses")
                .then()
                .log().ifValidationFails() // Hata varsa response'u yazdır
                .statusCode(200) // Status kontrolü
                .body("success", is(true)) // success: true kontrolü
                .body("data", notNullValue()) // data boş olmamalı
                .body("data.size()", greaterThanOrEqualTo(0)); // Data bir array olmalı

        System.out.println("✅ Kurs listeleme testi başarılı.");
    }


}