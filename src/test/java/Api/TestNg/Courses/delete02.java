package Api.TestNg.Courses;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

public class delete02 {

    @Test
    public void deleteMaxIdCourse() {
        System.out.println("=== EN BÜYÜK ID'Lİ COURSE'U SİL ===");

        // 1. Tüm kursları getir
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/courses"); // veya "courses" endpoint'iniz neyse
        Response getResponse = API_Methods.sendRequest("GET", null);

        // Response'u yazdır (debug için)
        System.out.println("GET Response: " + getResponse.getBody().asString());

        // 2. ID'leri çek

        //List<Integer> idList = getResponse.jsonPath().getList("$");
        //Integer[] ids = getResponse.as(Integer[].class);
        //List<Integer> idList = getResponse.jsonPath().getList("data.id");
        //List<Integer> idList = getResponse.jsonPath().getList("id");
        // veya data.id, courses.id gibi yapıya göre
         //List<Integer> idList = getResponse.jsonPath().getList("data.id");
         List<Integer> idList = getResponse.jsonPath().getList("courses.id");

        System.out.println("Tüm ID'ler: " + idList);

        // 3. En büyük ID'yi bul
        int maxId = idList.stream().max(Integer::compareTo).get();
        System.out.println("En büyük ID: " + maxId);

        // 4. Bu ID'yi sil
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/" + maxId);
        Response deleteResponse = API_Methods.sendRequest("DELETE", null);

        // 5. Doğrula
        API_Methods.statusCodeAssert(200);
        System.out.println("✓ Course silindi! Silinen ID: " + maxId);

        // 6. Silindiğini kontrol et (opsiyonel)
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/course/" + maxId);
        Response verifyResponse = API_Methods.sendRequest("GET", null);

        if (verifyResponse.getStatusCode() == 404) {
            System.out.println("✓ Silindiği doğrulandı!");
        } else {
            System.out.println("✗ Hala var veya beklenmeyen durum!");
        }
    }
}