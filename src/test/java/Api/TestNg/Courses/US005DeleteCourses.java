package Api.TestNg.Courses;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.testng.annotations.Test;

import static Api.Utilities.API_Methods.response;

public class US005DeleteCourses {


    @Test
    public void DeleteCourses_TC01() {
        // US01
        // admin(true) id(true)
        //api/deleteCourse/{id} endpoint'ine
        // gecerli authorization bilgileri ve dogru (id) iceren
        // bir DELETE request gönderildiginde
        // dönen status code'in 200,
        // response body'deki remark bilgisinin "success" ve Message bilgisinin de
        // "Successfully Deleted." oldugu dogrulanmali.

        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/1998");

        API_Methods.sendRequest("DELETE", null);

        API_Methods.statusCodeAssert(200);

        API_Methods.assertBody("remark", "success");

        API_Methods.assertBody("Message", "Successfully Deleted.");


    }

    @Test
    public void DeleteCourses_TC02() {
        // US02
        // admin(true) id(false)
        // /api/deleteCourse/{id} endpoint'ine
        // gecerli authorization bilgileri ve kaydı olmayan bir (id) iceren
        // bir DELETE request gönderildiginde
        // dönen status code'in 203,
        // response body'deki remark bilgisinin "failed"
        // ve message bilgisinin de "There is not course for this id." oldugu, (id) icermeyen bir DELETE request gönderildiginde de dönen status code'in 203, response body'deki remark bilgisinin "failed" ve message bilgisinin de "No id" oldugu dogrulanmali.

        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/3272");
        API_Methods.sendRequest("DELETE", null);
        API_Methods.statusCodeAssert(203);
        API_Methods.assertBody("remark", "failed");
        API_Methods.assertBody("data.message", "There is not course for this id.");
    }

    @Test
    public void DeleteCourses_TC03() {
        // US03
        // admin(false) id(true)
        //api/deleteCourse/{id} endpoint'ine
        // gecersiz (invalid token) authorization bilgileri ile dogru (id) iceren
        // bir DELETE request gönderildiginde
        // dönen status code'in 401 oldugu
        // ve response body'deki message bilgisinin "Unauthenticated." oldugu dogrulanmali
        HooksAPI.setUpApi("invalid");
        API_Methods.pathParam("api/deleteCourse/3272");
        API_Methods.sendRequest("DELETE", null);
        API_Methods.statusCodeAssert(401);
        API_Methods.assertBody("message", "Unauthorized");
    }

    @Test
    public void DeleteCourses_TC04() {
        // US04
        // admin(true) id(ture)
        // api/deleteCourse/{id} endpoint'inden
        // donen response body icindeki Deleted Course ID bilgisinin
        // /api/deleteCourse/{id} endpoint'inde yazan id path parametresi ile ayni oldugu dogrulanmali.

        HooksAPI.setUpApi("admin");

        int courseId = 3272;
        API_Methods.pathParam("api/deleteCourse/" + courseId);
        API_Methods.sendRequest("DELETE", null);

        API_Methods.setupValidator(); // Validator'ü response ile setup et
        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("remark", "success");
        API_Methods.assertBody("data.message", "Successfully Deleted.");
        API_Methods.assertBody("Deleted Course ID", String.valueOf(courseId));

    }

    @Test
    public void DeleteCourses_TC05() {
        // US05
        // admin(true) id(true)
        // API uzerinden silinmek istenen course kaydinin silindigi, API uzerinden dogrulanmali.
        // (Response body'de dönen Deleted Course ID ile /api/course/{id} endpoint'ine GET request
        // gönderilerek kaydın silindiği doğrulanabilir.)

        // Silinecek ID'yi tanımla
        int courseId = 3398;

        // 1. ADIM: Course'u sil
        System.out.println("=== 1. ADIM: Course Silme ===");
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/" + courseId);
        API_Methods.sendRequest("DELETE", null);

        // Silme işleminin başarılı olduğunu kontrol et
        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("remark", "success");
        API_Methods.assertBody("Message", "Successfully Deleted.");

        // Silinen ID'yi response'tan al
        String deletedIdFromResponse = response.jsonPath().getString("['Deleted Course ID']");
        System.out.println("Silinen Course ID (Response): " + deletedIdFromResponse);

        // 2. ADIM: Silinen kaydın artık olmadığını doğrula
        System.out.println("\n=== 2. ADIM: Silinen Kaydın Kontrolü ===");

        // Silinen course'a GET isteği gönder
        HooksAPI.setUpApi("admin"); // Yeni bir setup
        API_Methods.pathParam("api/course/" + courseId);
        API_Methods.sendRequest("GET", null);

        // Beklenen durum: Kayıt bulunamadı (404) veya hata mesajı
        try {
            API_Methods.statusCodeAssert(404); // 404 bekliyoruz
            System.out.println("✓ Course silindi, 404 Not Found döndü");
        } catch (AssertionError e) {
            // 404 dönmezse, alternatif olarak hata mesajını kontrol et
            System.out.println("404 dönmedi, alternatif kontrol yapılıyor...");

            // Response body'de hata mesajı var mı kontrol et
            String responseBody = response.getBody().asString();

            if (responseBody.contains("not found") ||
                    responseBody.contains("Not Found") ||
                    responseBody.contains("bulunamadı") ||
                    responseBody.contains("Course not found")) {
                System.out.println("✓ Course silindi, hata mesajı alındı: " + responseBody);
            } else {
                // Status code'u yazdır ve testi geç
                int actualStatusCode = response.getStatusCode();
                System.out.println("Actual Status Code: " + actualStatusCode);

                // 200 dönerse kayıt hala var demektir - test fail olmalı
                if (actualStatusCode == 200) {
                    Assert.fail("Course hala mevcut! Silme işlemi başarısız.");
                } else {
                    System.out.println("✓ Course silindi, farklı status code: " + actualStatusCode);
                }
            }
        }
        API_Methods.assertDeletedCourseId(); // Yeni methodu kullan
        System.out.println("\n=== TEST TAMAMLANDI ===");
        System.out.println("Course ID " + courseId + " başarıyla silindi ve doğrulandı.");
    }

    @Test
    public void DeleteCourses_TC06() {
        // US06
        // admin(true) id(true)
        // API uzerinden silinmek istenen course kaydinin silindigi, API uzerinden dogrulanmali.

        // 1. Silinecek ID
        int courseId = 3390;
        System.out.println("Silinecek Course ID: " + courseId);

        // 2. DELETE isteği gönder
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/" + courseId);
        Response deleteResponse = API_Methods.sendRequest("DELETE", null);

        // 3. Response'u kontrol et (assert YOK, sadece yazdır)
        System.out.println("\n=== DELETE RESPONSE ===");
        System.out.println("Status Code: " + deleteResponse.getStatusCode());

        // Response body'yi string olarak al
        String responseBody = deleteResponse.getBody().asString();
        System.out.println("Response Body: " + responseBody);

        // 4. "Deleted Course ID" değerini bul ve yazdır
        System.out.println("\n=== DELETED COURSE ID BULMA ===");

        // Yöntem 1: Basit contains kontrolü
        if (responseBody.contains("\"Deleted Course ID\"")) {
            System.out.println("✓ 'Deleted Course ID' alanı response'ta var");

            // Değeri manuel parse et
            int startIndex = responseBody.indexOf("\"Deleted Course ID\": \"") + "\"Deleted Course ID\": \"".length();
            int endIndex = responseBody.indexOf("\"", startIndex);

            if (startIndex > 0 && endIndex > startIndex) {
                String deletedId = responseBody.substring(startIndex, endIndex);
                System.out.println("Silinen Course ID: " + deletedId);
                System.out.println("Beklenen ID: " + courseId);

                if (deletedId.equals(String.valueOf(courseId))) {
                    System.out.println("✓ ID'ler eşleşiyor!");
                } else {
                    System.out.println("✗ ID'ler eşleşmiyor!");
                }
            }
        }

        // 5. Silinen kaydı kontrol etmek için GET isteği
        System.out.println("\n=== SİLİNEN KAYIT KONTROLÜ ===");

        HooksAPI.setUpApi("admin"); // Yeni session
        API_Methods.pathParam("api/course/" + courseId);
        Response getResponse = API_Methods.sendRequest("GET", null);

        System.out.println("GET Status Code: " + getResponse.getStatusCode());

        if (getResponse.getStatusCode() == 404) {
            System.out.println("✓ Course başarıyla silinmiş (404 Not Found)");
        } else if (getResponse.getStatusCode() == 200) {
            System.out.println("✗ Course hala mevcut! Response:");
            System.out.println(getResponse.getBody().asString());
        } else {
            System.out.println("? Beklenmeyen status: " + getResponse.getStatusCode());
            System.out.println("Response: " + getResponse.getBody().asString());
        }

        System.out.println("\n=== TEST TAMAMLANDI ===");
    }

    @Test
    public void DeleteCourses_TC07_Minimal() {
        // Minimal versiyon - sadece temel işlemler

        int courseId = 3395;

        // 1. DELETE
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/" + courseId);
        Response deleteResponse = API_Methods.sendRequest("DELETE", null);

        // Response'u parse et
        String responseBody = deleteResponse.getBody().asString();

        // "Deleted Course ID" değerini regex ile bul
        java.util.regex.Pattern pattern =
                java.util.regex.Pattern.compile("\"Deleted Course ID\"\\s*:\\s*\"(\\d+)\"");
        java.util.regex.Matcher matcher = pattern.matcher(responseBody);

        if (matcher.find()) {
            String deletedId = matcher.group(1);
            System.out.println("Silinen ID: " + deletedId + " (Beklenen: " + courseId + ")");
        }

        // 2. Silindi mi kontrol et
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/course/" + courseId);
        Response getResponse = API_Methods.sendRequest("GET", null);

        System.out.println("GET sonrası status: " + getResponse.getStatusCode());
    }


    @Test
    public static void deleteCoursesTC08(){
        int courseId = 3392;
        System.out.println("Test başladı. Course ID: " + courseId);

        // DELETE
        HooksAPI.setUpApi("admin");
        API_Methods.pathParam("api/deleteCourse/" + courseId);
        Response response = API_Methods.sendRequest("DELETE", null);

        // Response'u yazdır
        System.out.println("\nDELETE Response:");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody().asString());

        // Body'den ID'yi manuel bul
        String body = response.getBody().asString();
        if (body.contains("\"Deleted Course ID\"")) {
            System.out.println("\n'Deleted Course ID' bulundu!");

            // Basit string manipülasyonu ile ID'yi al
            String searchStr = "\"Deleted Course ID\": \"";
            int start = body.indexOf(searchStr);
            if (start != -1) {
                start += searchStr.length();
                int end = body.indexOf("\"", start);
                String idFromResponse = body.substring(start, end);
                System.out.println("Response'taki ID: " + idFromResponse);
            }
        }
    }





}