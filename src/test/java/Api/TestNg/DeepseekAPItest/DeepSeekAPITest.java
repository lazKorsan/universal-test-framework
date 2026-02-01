package Api.TestNg.DeepseekAPItest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class DeepSeekAPITest {

    // BU KISMI KENDİ API KEY'İNLE DEĞİŞTİR!
    private static final String API_KEY = "sk-d9a1d39bfdea405ba931f5e090299c8a";
    private static final String BASE_URL = "https://api.deepseek.com";

    @Test
    public void testDeepSeekChatCompletion() {
        System.out.println("🤖 DeepSeek API Testi Başlıyor...");

        // 1. Request body hazırla
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "Merhaba, sana TestNG ile API testi yapıyorum. Bana Java'da 'Hello World' nasıl yazılır kısaca açıkla.");

        requestBody.put("messages", new Map[]{message});
        requestBody.put("max_tokens", 100);
        requestBody.put("temperature", 0.7);

        // 2. API Request gönder
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/v1/chat/completions")
                .then()
                .extract()
                .response();

        // 3. Response'u yazdır (debug için)
        System.out.println("📥 Response Status Code: " + response.getStatusCode());
        System.out.println("📥 Response Body: \n" + response.getBody().asPrettyString());

        // 4. Assertions (Doğrulamalar)
        Assert.assertEquals(response.getStatusCode(), 200, "API çağrısı başarısız!");

        String responseContent = response.jsonPath().getString("choices[0].message.content");
        Assert.assertNotNull(responseContent, "Response content boş geldi!");
        Assert.assertFalse(responseContent.isEmpty(), "Response content boş!");

        // 5. Model kontrolü
        String model = response.jsonPath().getString("model");
        Assert.assertEquals(model, "deepseek-chat", "Model yanlış!");

        System.out.println("✅ API Testi BAŞARILI!");
        System.out.println("📝 DeepSeek'in Cevabı: " +
                responseContent.substring(0, Math.min(100, responseContent.length())) + "...");
    }

    @Test
    public void testDeepSeekWithTurkishQuestion() {
        System.out.println("🇹🇷 Türkçe Soru Testi...");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "Selam! Test otomasyonu nedir ve neden önemlidir? Kısaca açıkla.");

        requestBody.put("messages", new Map[]{message});
        requestBody.put("max_tokens", 150);
        requestBody.put("temperature", 0.5);

        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/v1/chat/completions");

        Assert.assertEquals(response.getStatusCode(), 200);

        String answer = response.jsonPath().getString("choices[0].message.content");
        System.out.println("🤖 Cevap: " + answer);

        // Cevapta anahtar kelimeleri kontrol et
        Assert.assertTrue(answer.toLowerCase().contains("test") ||
                        answer.toLowerCase().contains("otomasyon"),
                "Cevap konuyla ilgili değil!");
    }

    @Test
    public void testDeepSeekMultipleMessages() {
        System.out.println("💬 Çoklu Mesaj Testi...");

        Map<String, Object>[] messages = new Map[]{
                Map.of("role", "system", "content", "Sen yardımsever bir asistansın."),
                Map.of("role", "user", "content", "API testi nasıl yapılır?"),
                Map.of("role", "assistant", "content", "API testi için REST Assured veya Postman kullanabilirsin."),
                Map.of("role", "user", "content", "Peki Java'da hangi kütüphaneleri önerirsin?")
        };

        Map<String, Object> requestBody = Map.of(
                "model", "deepseek-chat",
                "messages", messages,
                "temperature", 0.3
        );

        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/v1/chat/completions");

        System.out.println("📊 Response Time: " + response.getTime() + "ms");
        Assert.assertEquals(response.getStatusCode(), 200);

        // Token sayısını kontrol et
        int totalTokens = response.jsonPath().getInt("usage.total_tokens");
        System.out.println("🔢 Kullanılan Token: " + totalTokens);
        Assert.assertTrue(totalTokens > 0, "Token kullanımı sıfır!");
    }

    @Test
    public void testInvalidApiKey() {
        System.out.println("🚫 Hatalı API Key Testi...");

        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer yanlisApiKey123")
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "model", "deepseek-chat",
                        "messages", new Map[]{Map.of("role", "user", "content", "Merhaba")}
                ))
                .post("/v1/chat/completions");

        // Hatalı key ile 401 veya 403 bekliyoruz
        int statusCode = response.getStatusCode();
        System.out.println("Hata Kodu: " + statusCode);

        Assert.assertTrue(statusCode == 401 || statusCode == 403,
                "Hatalı key için beklenen hata kodu gelmedi!");

        System.out.println("✅ Hatalı API key testi başarılı!");
    }
}