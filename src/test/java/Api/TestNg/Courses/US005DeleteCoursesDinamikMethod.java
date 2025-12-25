package Api.TestNg.Courses;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.regex.Pattern;

public class US005DeleteCoursesDinamikMethod {

    // ==================== YARDIMCI METHODLAR (SADECE BU SINIFTA) ====================

    // 1. CREATE Course Methodu
    private int createCourse() {
        System.out.println("\n=== YENİ COURSE OLUŞTURMA ===");

        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/addCourse");

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Dinamik Test Course " + System.currentTimeMillis());
        requestBody.put("type", "course");
        requestBody.put("slug", "dinamik-test-course-" + System.currentTimeMillis());
        requestBody.put("start_date", "2025-11-28");
        requestBody.put("duration", 40);
        requestBody.put("capacity", 40);
        requestBody.put("price", 0);
        requestBody.put("description", "Dinamik olarak oluşturulan kurs.");
        requestBody.put("teacher_id", 1016);
        requestBody.put("category_id", 611);

        Response createResponse = API_Methods.sendRequest("POST", requestBody);

        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("remark", "success");
        API_Methods.assertBody("Message", "Successfully Added.");

        // Oluşturulan Course ID'yi response'tan al
        String responseBody = createResponse.getBody().asString();
        int createdId = extractAddedCourseId(responseBody);

        System.out.println("✓ Oluşturulan Course ID: " + createdId);
        return createdId;
    }

    // 2. "Added Course ID" Çıkarma Methodu
    private int extractAddedCourseId(String responseBody) {
        Pattern pattern = Pattern.compile("\"Added Course ID\"\\s*:\\s*(\\d+)");
        java.util.regex.Matcher matcher = pattern.matcher(responseBody);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        // Alternatif: Manual parse
        if (responseBody.contains("\"Added Course ID\":")) {
            int start = responseBody.indexOf("\"Added Course ID\":") + "\"Added Course ID\":".length();
            int end = responseBody.indexOf("}", start);
            String idStr = responseBody.substring(start, end).trim().replace(",", "");
            return Integer.parseInt(idStr);
        }

        throw new RuntimeException("Added Course ID bulunamadı!");
    }

    // 3. DELETE Course Methodu
    private void deleteCourse(int courseId) {
        System.out.println("\n=== COURSE SİLME ===");
        System.out.println("Silinecek Course ID: " + courseId);

        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/" + courseId);
        Response deleteResponse = API_Methods.sendRequest("DELETE", null);

        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("remark", "success");
        API_Methods.assertBody("Message", "Successfully Deleted.");

        // Silinen ID'yi doğrula
        String deletedId = extractDeletedCourseId(deleteResponse.getBody().asString());
        System.out.println("Response'taki Deleted Course ID: " + deletedId);

        if (String.valueOf(courseId).equals(deletedId)) {
            System.out.println("✓ Silinen ID doğrulandı!");
        } else {
            System.out.println("✗ ID'ler eşleşmiyor! Beklenen: " + courseId + ", Bulunan: " + deletedId);
        }
    }

    // 4. "Deleted Course ID" Çıkarma Methodu (DÜZELTİLMİŞ VERSİYON)
    private String extractDeletedCourseId(String responseBody) {
        System.out.println("Response Body: " + responseBody);

        // METHOD 1: Regex ile (en güvenli)
        Pattern pattern1 = Pattern.compile("\"Deleted Course ID\"\\s*:\\s*\"(\\d+)\"");
        java.util.regex.Matcher matcher1 = pattern1.matcher(responseBody);

        if (matcher1.find()) {
            return matcher1.group(1);
        }

        // METHOD 2: String manipulation
        String[] parts = responseBody.split(",");
        for (String part : parts) {
            part = part.trim();
            if (part.contains("Deleted Course ID")) {
                // Format: "Deleted Course ID": "3390"
                String[] keyValue = part.split(":");
                if (keyValue.length > 1) {
                    String value = keyValue[1].trim();
                    // Tırnak işaretlerini temizle
                    value = value.replace("\"", "").replace("}", "").replace("]", "");
                    return value;
                }
            }
        }

        // METHOD 3: Manual parse
        int start = responseBody.indexOf("\"Deleted Course ID\": \"");
        if (start != -1) {
            start += "\"Deleted Course ID\": \"".length();
            int end = responseBody.indexOf("\"", start);
            if (end != -1) {
                return responseBody.substring(start, end);
            }
        }

        return "BULUNAMADI";
    }

    // 5. Course'un Silindiğini Doğrulama Methodu
    private void verifyCourseDeleted(int courseId) {
        System.out.println("\n=== SİLİNEN KAYIT DOĞRULAMA ===");

        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/course/" + courseId);
        Response getResponse = API_Methods.sendRequest("GET", null);

        int statusCode = getResponse.getStatusCode();
        System.out.println("GET Status Code: " + statusCode);

        if (statusCode == 404) {
            System.out.println("✓ Course başarıyla silinmiş (404 Not Found)");
        } else if (statusCode == 200) {
            System.out.println("✗ Course hala mevcut!");
            System.out.println("Response: " + getResponse.getBody().asString());
        } else if (statusCode == 203) {
            String responseBody = getResponse.getBody().asString();
            if (responseBody.contains("There is not course for this id") ||
                    responseBody.contains("not found") ||
                    responseBody.contains("bulunamadı")) {
                System.out.println("✓ Course silinmiş (203 with error message)");
            } else {
                System.out.println("? Beklenmeyen 203 response: " + responseBody);
            }
        } else {
            System.out.println("? Beklenmeyen status code: " + statusCode);
        }
    }

    // ==================== TEST METHODLARI ====================

    @Test
    public void dinamikCreateAndDeleteTest() {
        System.out.println("=== DİNAMİK CREATE VE DELETE TESTİ ===");

        // 1. Yeni course oluştur
        int createdCourseId = createCourse();

        // 2. Oluşturulan course'u sil
        deleteCourse(createdCourseId);

        // 3. Silindiğini doğrula
        verifyCourseDeleted(createdCourseId);

        System.out.println("\n=== TEST BAŞARIYLA TAMAMLANDI ===");
    }

    @Test
    public void multipleCreateDeleteTest() {
        System.out.println("=== ÇOKLU CREATE-DELETE TESTİ ===");

        // 3 tane course oluştur ve sil
        for (int i = 1; i <= 3; i++) {
            System.out.println("\n--- İterasyon " + i + " ---");

            int courseId = createCourse();
            deleteCourse(courseId);
            verifyCourseDeleted(courseId);

            System.out.println("--- İterasyon " + i + " tamamlandı ---\n");
        }

        System.out.println("=== ÇOKLU TEST BAŞARIYLA TAMAMLANDI ===");
    }

    @Test
    public void testOnlyDeleteWithDynamicId() {
        System.out.println("=== DİNAMİK ID İLE DELETE TESTİ ===");

        // Önce bir course oluştur
        int courseId = createCourse();

        System.out.println("\n=== SADECE DELETE TESTİ ===");

        // DELETE işlemini test et
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/" + courseId);
        Response deleteResponse = API_Methods.sendRequest("DELETE", null);

        // Response'u analiz et
        String responseBody = deleteResponse.getBody().asString();
        System.out.println("Response Body: " + responseBody);

        // Tüm verileri çıkar
        extractAllDataFromResponse(responseBody);

        // Silindiğini doğrula
        verifyCourseDeleted(courseId);
    }

    // 6. Response'tan Tüm Verileri Çıkarma (Debug için)
    private void extractAllDataFromResponse(String responseBody) {
        System.out.println("\n=== RESPONSE ANALİZİ ===");

        // Tüm key-value çiftlerini bul
        responseBody = responseBody.replace("{", "").replace("}", "");
        String[] pairs = responseBody.split(",");

        for (String pair : pairs) {
            pair = pair.trim();
            if (pair.contains(":")) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replace("\"", "");
                    String value = keyValue[1].trim().replace("\"", "");
                    System.out.println(key + " = " + value);
                }
            }
        }
    }

    // 7. Hard-coded ID ile Test (Backup için)
    @Test
    public void testWithHardcodedId() {
        System.out.println("=== HARD-CODED ID İLE TEST ===");

        int courseId = 3390; // Hard-coded ID

        // DELETE
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/" + courseId);
        Response deleteResponse = API_Methods.sendRequest("DELETE", null);

        // Response analizi
        String responseBody = deleteResponse.getBody().asString();
        System.out.println("Full Response: " + responseBody);

        // Deleted Course ID'yi bul
        String deletedId = extractDeletedCourseId(responseBody);
        System.out.println("Extracted Deleted Course ID: " + deletedId);

        // Doğrulama
        if (String.valueOf(courseId).equals(deletedId)) {
            System.out.println("✓ BAŞARILI: ID'ler eşleşiyor!");
        } else {
            System.out.println("✗ HATA: Beklenen: " + courseId + ", Bulunan: " + deletedId);

            // Debug için tüm response'u göster
            extractAllDataFromResponse(responseBody);
        }
    }
}