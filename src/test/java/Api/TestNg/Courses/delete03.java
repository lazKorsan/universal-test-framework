package Api.TestNg.Courses;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

public class delete03 {
    @Test
    public void deleteMaxIdCourseSafe() {
        System.out.println("=== EN BÜYÜK ID'Lİ COURSE'U SİL (GÜVENLİ) ===");

        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/courses");
        Response getResponse = API_Methods.sendRequest("GET", null);

        // Response yapısını bul
        List<Integer> idList = null;

        // Deneme 1: Direkt "id"
        try {
            idList = getResponse.jsonPath().getList("id");
            if (idList != null && !idList.isEmpty()) {
                System.out.println("ID'ler 'id' key'inden alındı");
            }
        } catch (Exception e) {}

        // Deneme 2: "data.id"
        if (idList == null || idList.isEmpty()) {
            try {
                idList = getResponse.jsonPath().getList("data.id");
                System.out.println("ID'ler 'data.id' key'inden alındı");
            } catch (Exception e) {}
        }

        // Deneme 3: "webinars.id"
        if (idList == null || idList.isEmpty()) {
            try {
                idList = getResponse.jsonPath().getList("webinars.id");
                System.out.println("ID'ler 'webinars.id' key'inden alındı");
            } catch (Exception e) {}
        }

        if (idList == null || idList.isEmpty()) {
            System.out.println("HATA: Hiç ID bulunamadı!");
            System.out.println("Response: " + getResponse.getBody().asString());
            return;
        }

        System.out.println("Bulunan ID'ler: " + idList);

        // En büyük ID'yi bul
        int maxId = idList.stream().max(Integer::compareTo).get();
        System.out.println("Silinecek ID: " + maxId);

        // Sil
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/" + maxId);
        Response deleteResponse = API_Methods.sendRequest("DELETE", null);

        System.out.println("Silme sonucu: " + deleteResponse.getBody().asString());
        API_Methods.statusCodeAssert(200);

        System.out.println("✓ TEST BAŞARILI!");
    }
}
