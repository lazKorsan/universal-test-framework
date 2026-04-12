package trello;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static trello.trelloPage.trelloApiKey;
import static trello.trelloPage.trelloToken;

public class US002DeleteBoard {
    String apiKey=trelloApiKey;
    String token=trelloToken;

    @Test
    public void deleteBoard(){
        // Silmek istediğin board'un ID'si
        String boardIdToDelete = "zMEcOEi2";

        // URL Yapısı: /1/boards/{id} şeklinde olmalı
        String deleteUrl = "https://api.trello.com/1/boards/" + boardIdToDelete;

        Response response = given()
                .queryParam("key", apiKey)   // Key ve Token'ı queryParam olarak göndermek daha sağlıklıdır
                .queryParam("token", token)
                .when()
                .delete(deleteUrl)           // .post(url) yerine .delete(deleteUrl)
                .then()
                .log().all()
                .statusCode(200)             // Başarılı silme durumunda 200 döner
                .extract().response();

        System.out.println("Board başarıyla silindi.");
    }
}
