package trello;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static trello.trelloPage.*;

public class US001CreateBoard_V2 {

    String apiKey = trelloApiKey;
    String token = trelloToken;

    @Test
    public void createBoardWithDynamicName() {
        // 1. Dinamik Board İsmi Oluşturma (Rastgele Sayı ile)
        int randomNumber = (int)(Math.random() * 901) + 100;
        String expectedBoardName = "newWorkSpace" + randomNumber;

        System.out.println("Oluşturulacak Board Adı: " + expectedBoardName);

        // 2. API İsteğinin Hazırlanması ve Gönderilmesi
        // Not: Parametreleri URL içine gömmek yerine queryParam kullanmak daha temiz bir yaklaşımdır.
        Response response = given()
                .queryParam("name", expectedBoardName)
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .contentType("application/json")
                .when()
                .post("https://api.trello.com/1/boards/")
                .then()
                .log().ifValidationFails() // Sadece hata varsa log basar, konsolu temiz tutar
                .extract().response();

        // 3. Yanıtın (Response) Doğrulanması
        Assert.assertEquals(response.statusCode(), 200, "Board oluşturma işlemi başarısız!");

        // 4. JSON Path Kullanarak İsim Karşılaştırması
        JsonPath jsonPath = response.jsonPath();
        String actualBoardName = jsonPath.getString("name");
        String boardId = jsonPath.getString("id");

        System.out.println("Trello'dan Dönen Board İsmi: " + actualBoardName);
        System.out.println("Yeni Board ID: " + boardId);

        // Dinamik olarak oluşturduğumuz isim ile API'den gelen ismi karşılaştırıyoruz
        Assert.assertEquals(actualBoardName, expectedBoardName, "Oluşturulan isim ile dönen isim eşleşmiyor!");

        // Opsiyonel: Diğer JSON alanlarını da doğrulayabilirsin (Örn: kapalı mı?)
        Assert.assertFalse(jsonPath.getBoolean("closed"), "Yeni oluşturulan board kapalı olmamalı!");
    }
}