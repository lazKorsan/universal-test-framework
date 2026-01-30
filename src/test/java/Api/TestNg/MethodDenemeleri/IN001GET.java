package Api.TestNg.MethodDenemeleri; // Kendi paket adınıza göre düzenleyebilirsiniz

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class IN001GET {

    // 1. Ayarlar ve Sabitler
    private final String BASE_URL = "https://qa.instulearn.com";
    private final String X_API_KEY = "1234";
    private String authToken;

    // Tarayıcı gibi davranması için User-Agent (403 hatasını önlemek için kritik)
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;

        // 2. Token Alma (Login) İşlemi
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("email", "162.admin@instulearn.com");
        credentials.put("password", "162162162");

        System.out.println("--- Login İsteği Başlatılıyor ---");

        Response response = given()
                .header("User-Agent", USER_AGENT) // Sunucu engellemesini aşar
                .header("x-api-key", X_API_KEY)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(credentials)
                .log().all() // Request detaylarını konsola basar
                .when()
                .post("/api/token")
                .then()
                .log().all() // Response detaylarını konsola basar
                .statusCode(200) // Burada 403 alıyorsanız User-Agent veya API Key kontrol edilmeli
                .extract().response();

        // Token'ı response içinden al
        authToken = response.jsonPath().getString("data.access_token");
        Assert.assertNotNull(authToken, "HATA: Token alınamadı!");
        System.out.println("✅ Token başarıyla alındı.");
    }

    @Test
    public void testGetCoursesSuccessfully() {

        Response response = given()
                .header("User-Agent", USER_AGENT)
                .header("x-api-key", X_API_KEY)
                .header("Authorization", "Bearer " + authToken)
                .header("Accept", "application/json")
                .when()
                .get("/api/courses")
                .then()
                .log().ifError()
                .extract().response();

        System.out.println("Yanıt: " + response.prettyPrint());
        // 3. GET Sorgusu
        given()
                .header("User-Agent", USER_AGENT)
                .header("x-api-key", X_API_KEY)
                .header("Authorization", "Bearer " + authToken)
                .header("Accept", "application/json")
                .when()
                .get("/api/courses")
                .then()
                .log().ifError() // Sadece hata varsa log bas
                .statusCode(200)
                .body("remark", equalTo("success")) // Remark success kontrolü
                .body("data", notNullValue()); // Data alanı boş olmamalı

        System.out.println("✅ GET /api/courses: Status 200 ve Remark 'success' doğrulandı.");
    }


}