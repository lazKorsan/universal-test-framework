package Api.TestNg.Courses;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class delete04 {

    @Test
    public void deleteMaxIdCourseSafe() {
        System.out.println("=== EN BÜYÜK ID'Lİ COURSE'U SİL ===");

        try {
            // Step 1: Get all courses
            int maxId = getMaxCourseId();
            System.out.println("Silinecek en büyük ID: " + maxId);

            // Step 2: Delete the course
            deleteCourseById(maxId);

            // Step 3: Verify deletion
            verifyCourseDeleted(maxId);

            System.out.println("✓ TEST BAŞARILI! ID: " + maxId + " başarıyla silindi.");

        } catch (AssertionError e) {
            System.err.println("✗ TEST BAŞARISIZ: " + e.getMessage());
            throw e;
        }
    }

    private int getMaxCourseId() {
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/courses");
        Response response = API_Methods.sendRequest("GET", null);

        // Assert: Status code 200
        Assertions.assertEquals(200, response.getStatusCode(),
                "GET courses başarısız! Status: " + response.getStatusCode());

        // Extract IDs
        List<Integer> idList = extractIdList(response);

        // Assert: ID list not empty
        Assertions.assertNotNull(idList, "ID listesi null!");
        Assertions.assertFalse(idList.isEmpty(), "Hiç kurs bulunamadı!");

        System.out.println("Toplam kurs sayısı: " + idList.size());
        System.out.println("ID'ler: " + idList);

        // Get max ID
        int maxId = idList.stream().max(Integer::compareTo)
                .orElseThrow(() -> new AssertionError("Max ID bulunamadı!"));

        Assertions.assertTrue(maxId > 0, "Geçersiz ID: " + maxId);

        return maxId;
    }

    private List<Integer> extractIdList(Response response) {
        List<Integer> idList = null;

        // Try different JSON paths
        String[] paths = {"id", "data.id", "webinars.id", "courses.id", "items.id"};

        for (String path : paths) {
            try {
                idList = response.jsonPath().getList(path);
                if (idList != null && !idList.isEmpty()) {
                    System.out.println("ID'ler '" + path + "' key'inden alındı");
                    return idList;
                }
            } catch (Exception e) {
                // Continue to next path
            }
        }

        // If all fail, try to parse from array directly
        try {
            idList = response.jsonPath().getList("$");
            if (idList != null && !idList.isEmpty()) {
                System.out.println("ID'ler direkt array'den alındı");
                return idList;
            }
        } catch (Exception e) {}

        return idList;
    }

    private void deleteCourseById(int courseId) {
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/" + courseId);
        Response response = API_Methods.sendRequest("DELETE", null);

        // Assert: Status code 200
        Assertions.assertEquals(200, response.getStatusCode(),
                "DELETE başarısız! Status: " + response.getStatusCode());

        // Assert: Success message
        String body = response.getBody().asString();
        Assertions.assertTrue(
                body.contains("success") || body.contains("Successfully Deleted") || body.contains("deleted"),
                "Silme mesajı doğrulanamadı! Response: " + body
        );

        System.out.println("✓ Silme işlemi başarılı. Response: " + body);
    }

    private void verifyCourseDeleted(int courseId) {
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/course/" + courseId);
        Response response = API_Methods.sendRequest("GET", null);

        // Assert: Silinen kursa erişim 404 olmalı
        Assertions.assertEquals(404, response.getStatusCode(),
                "Kurs silinmemiş! ID: " + courseId + " hala erişilebilir. Status: " + response.getStatusCode());

        System.out.println("✓ Silme doğrulandı: Kurs " + courseId + " bulunamıyor (404)");
    }

    @org.testng.annotations.Test
    public void deleteMaxIdCourse() {
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/4138");
        API_Methods.sendRequest("DELETE", null);



        // Assert: Success message











    }
}