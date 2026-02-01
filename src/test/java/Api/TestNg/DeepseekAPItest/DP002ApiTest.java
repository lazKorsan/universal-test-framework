package Api.TestNg.DeepseekAPItest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.*;
import static org.testng.Assert.*;

public class DP002ApiTest {

    @Test
    public void testDummyJSONAPI() {
        System.out.println("🛒 DummyJSON API Testi...");

        Response response = RestAssured.given()
                .baseUri("https://dummyjson.com")
                .when()
                .get("/products/1");

        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Ürün: " + response.jsonPath().getString("title"));
        System.out.println("Fiyat: $" + response.jsonPath().getString("price"));

        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("title"));
        System.out.println("✅ BAŞARILI!");
    }

    @Test
    public void testCatFactAPI() {
        System.out.println("😺 Kedi Bilgisi API...");

        Response response = RestAssured.given()
                .baseUri("https://catfact.ninja")
                .when()
                .get("/fact");

        String fact = response.jsonPath().getString("fact");
        int length = response.jsonPath().getInt("length");

        System.out.println("Kedi Gerçeği: " + fact);
        System.out.println("Uzunluk: " + length + " karakter");

        assertEquals(response.getStatusCode(), 200);
        assertTrue(length > 0);
        System.out.println("✅ KEDİ TESTİ BAŞARILI!");
    }

    @Test
    public void testAgifyAPI() {
        System.out.println("🎂 Yaş Tahmini API...");

        Response response = RestAssured.given()
                .baseUri("https://api.agify.io")
                .param("name", "mehmet")
                .when()
                .get();

        String name = response.jsonPath().getString("name");
        int age = response.jsonPath().getInt("age");
        int count = response.jsonPath().getInt("count");

        System.out.println("İsim: " + name);
        System.out.println("Tahmini Yaş: " + age);
        System.out.println("Kayıt Sayısı: " + count);

        assertEquals(response.getStatusCode(), 200);
        assertEquals(name, "mehmet");
        System.out.println("✅ YAŞ TAHMİNİ BAŞARILI!");
    }

    @Test
    public void testBoredAPI() {
        System.out.println("😎 Sıkıldım API...");

        Response response = RestAssured.given()
                .baseUri("http://www.boredapi.com")
                .when()
                .get("/api/activity");

        String activity = response.jsonPath().getString("activity");
        String type = response.jsonPath().getString("type");
        double price = response.jsonPath().getDouble("price");

        System.out.println("Aktivite: " + activity);
        System.out.println("Tip: " + type);
        System.out.println("Fiyat: " + price);

        assertEquals(response.getStatusCode(), 200);
        assertNotNull(activity);
        System.out.println("✅ AKTİVİTE BULUNDU!");
    }

    @Test
    public void testChuckNorrisJokeAPI() {
        System.out.println("💪 Chuck Norris API...");

        Response response = RestAssured.given()
                .baseUri("https://api.chucknorris.io")
                .when()
                .get("/jokes/random");

        String joke = response.jsonPath().getString("value");
        String url = response.jsonPath().getString("url");

        System.out.println("Şaka: " + joke);
        System.out.println("URL: " + url);

        assertEquals(response.getStatusCode(), 200);
        assertTrue(joke.toLowerCase().contains("chuck"));
        System.out.println("✅ CHUCK NORRIS ONAYLI!");
    }

    @Test
    public void testComplexAPIScenario() {
        System.out.println("🔁 KARMAŞIK API SENARYOSU...");

        // 1. Kullanıcı oluştur
        String createUserJson = """
            {
                "name": "Test Otomasyon",
                "email": "testapi@framework.com",
                "gender": "male",
                "status": "active"
            }
            """;

        Response createResponse = RestAssured.given()
                .baseUri("https://gorest.co.in/public/v2")
                .header("Authorization", "Bearer dummy_token") // Public API, auth gerekmez
                .header("Content-Type", "application/json")
                .body(createUserJson)
                .when()
                .post("/users");

        System.out.println("Create Status: " + createResponse.getStatusCode());

        // 2. Ürün listesi al
        Response productsResponse = RestAssured.given()
                .baseUri("https://fakestoreapi.com")
                .when()
                .get("/products");

        int productCount = productsResponse.jsonPath().getList("$").size();
        System.out.println("Ürün Sayısı: " + productCount);

        // 3. Rastgele bir ürün seç
        Response randomProduct = RestAssured.given()
                .baseUri("https://fakestoreapi.com")
                .when()
                .get("/products/1");

        String productTitle = randomProduct.jsonPath().getString("title");
        System.out.println("Rastgele Ürün: " + productTitle);

        // Assertions
        assertTrue(productCount > 0);
        assertNotNull(productTitle);
        System.out.println("✅ KARMAŞIK SENARYO BAŞARILI!");
    }
}