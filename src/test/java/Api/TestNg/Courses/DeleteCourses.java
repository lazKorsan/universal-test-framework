package Api.TestNg.Courses;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class DeleteCourses {

    public String userType;
    public Response response;
    public String accessToken;
    public String coursesEndPoint;
    public String pathParam;
    public static List<Integer> CoursesAllIDs;
    public static int CoursesMaxID;

    @Test
    public void deleteCourse() {

        userType = "admin";

        // 1. SPEC oluşturma
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri("https://qa.instulearn.com")
                .build();

        // 2. TOKEN al
        JSONObject json = new JSONObject();
        json.put("email", "elif@instulearn.com");
        json.put("password", "Instu2025!");  // Şifre aynı kaldı
        System.out.println("🔄 Admin girişi yapılıyor...");

        response = given()
                .log().all()
                .spec(spec)
                .header("User-Agent", "Mozilla/5.0")
                .contentType("application/json")
                .header("Accept", "application/json")
                .header("x-api-key", 1234)
                .body(json.toString())
                .post("/api/token");

        System.out.println("Token Status Code: " + response.getStatusCode());
        System.out.println("Token Response Body: " + response.asString());

        if (response.getStatusCode() == 200) {
            accessToken = response.jsonPath().getString("data.access_token");
            System.out.println("✅ Access Token alındı: " + accessToken);
        } else {
            System.out.println("❌ Token alınamadı! Status: " + response.getStatusCode());
            return;
        }

        // 3. GET isteği ile kursları al ve MaxID'yi bul
        String getEndpoint = "https://qa.instulearn.com/api/courses";

        response = given()
                .header("x-api-key", "1234")
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get(getEndpoint);

        System.out.println("\n📥 GET isteği Status: " + response.getStatusCode());
        System.out.println("GET Response: " + response.asString());

        if (response.getStatusCode() != 200) {
            System.out.println("❌ Kurslar alınamadı!");
            return;
        }

        // 4. MaxID'yi bul
        JsonPath jsonPath = response.jsonPath();
        CoursesAllIDs = jsonPath.getList("AddedCourseID.webinars.id", Integer.class);

        if (CoursesAllIDs != null && !CoursesAllIDs.isEmpty()) {
            CoursesMaxID = Collections.max(CoursesAllIDs);
            System.out.println("\n📊 Kurs ID Listesi: " + CoursesAllIDs);
            System.out.println("🔝 Silinecek Max ID: " + CoursesMaxID);
        } else {
            System.out.println("❌ Silinecek kurs bulunamadı!");
            return;
        }

        // 5. DELETE isteği gönder (URL düzeltildi)
        coursesEndPoint = "https://qa.instulearn.com/api/deleteCourse/" + CoursesMaxID;
        System.out.println("\n🗑️ DELETE URL: " + coursesEndPoint);

        response = given()
                .header("x-api-key", "1234")
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete(coursesEndPoint);

        System.out.println("\n📤 DELETE isteği Status: " + response.getStatusCode());
        System.out.println("DELETE Response Body: " + response.asString());

        // 6. Response doğrulama
        if (response.getStatusCode() == 200) {
            String remark = response.jsonPath().getString("remark");
            String message = response.jsonPath().getString("Message");

            System.out.println("\n✅ Silme işlemi başarılı!");
            System.out.println("   Remark: " + remark);
            System.out.println("   Message: " + message);
            System.out.println("   Silinen Course ID: " + CoursesMaxID);
        } else {
            System.out.println("\n❌ Silme işlemi başarısız! Status: " + response.getStatusCode());
        }
    }

    // ============= BELİRTİLEN SAYIDA KURS SİLME =============
    @Test
    public void deleteMultipleCourses() {
        int deleteCount = 5;  // Kaç kurs silmek istediğinizi buraya yazın

        System.out.println("\n" + "=".repeat(70));
        System.out.println("🔁 " + deleteCount + " kez kurs silme işlemi başlatılıyor...");
        System.out.println("=".repeat(70));

        int successCount = 0;
        int failCount = 0;

        for (int i = 0; i < deleteCount; i++) {
            System.out.println("\n--- " + (i+1) + "/" + deleteCount + " ---");

            // Token al
            RequestSpecification spec = new RequestSpecBuilder()
                    .setBaseUri("https://qa.instulearn.com")
                    .build();

            JSONObject json = new JSONObject();
            json.put("email", "ahmet.instructor4@InstuLearn.com");
            json.put("password", "InsruLearn.2026!");

            response = given()
                    .spec(spec)
                    .header("User-Agent", "Mozilla/5.0")
                    .contentType("application/json")
                    .header("Accept", "application/json")
                    .header("x-api-key", 1234)
                    .body(json.toString())
                    .post("/api/token");

            if (response.getStatusCode() != 200) {
                System.out.println("❌ Token alınamadı!");
                failCount++;
                continue;
            }

            accessToken = response.jsonPath().getString("data.access_token");

            // Kursları getir
            response = given()
                    .header("x-api-key", "1234")
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .when()
                    .get("https://qa.instulearn.com/api/courses");

            if (response.getStatusCode() != 200) {
                System.out.println("❌ Kurslar alınamadı!");
                failCount++;
                continue;
            }

            // MaxID'yi bul
            JsonPath jsonPath = response.jsonPath();
            CoursesAllIDs = jsonPath.getList("AddedCourseID.webinars.id", Integer.class);

            if (CoursesAllIDs == null || CoursesAllIDs.isEmpty()) {
                System.out.println("⚠️ Silinecek kurs kalmadı! Döngü durduruluyor.");
                break;
            }

            CoursesMaxID = Collections.max(CoursesAllIDs);
            System.out.println("📌 Silinecek ID: " + CoursesMaxID);

            // DELETE isteği
            coursesEndPoint = "https://qa.instulearn.com/api/deleteCourse/" + CoursesMaxID;
            response = given()
                    .header("x-api-key", "1234")
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .when()
                    .delete(coursesEndPoint);

            if (response.getStatusCode() == 200 &&
                    response.jsonPath().getString("remark").equals("success")) {
                System.out.println("✅ Silme başarılı: " + CoursesMaxID);
                successCount++;
            } else {
                System.out.println("❌ Silme başarısız! Status: " + response.getStatusCode());
                failCount++;
            }
        }

        // RAPOR
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 TOPLU SİLME RAPORU");
        System.out.println("=".repeat(70));
        System.out.println("🎯 Hedeflenen silme sayısı: " + deleteCount);
        System.out.println("✅ Başarılı silme sayısı: " + successCount);
        System.out.println("❌ Başarısız silme sayısı: " + failCount);
        System.out.println("=".repeat(70));
    }

    // ============= SABIT ID ILE SILME =============
    @Test
    public void deleteCourseWithFixedId() {
        int courseId = 4137;  // Silmek istediğiniz ID'yi yazın

        userType = "admin";

        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri("https://qa.instulearn.com")
                .build();

        JSONObject json = new JSONObject();
        json.put("email", "elif@instulearn.com");
        json.put("password", "Instu2025!");
        System.out.println("🔄 Admin girişi yapılıyor...");

        response = given()
                .log().all()
                .spec(spec)
                .header("User-Agent", "Mozilla/5.0")
                .contentType("application/json")
                .header("Accept", "application/json")
                .header("x-api-key", 1234)
                .body(json.toString())
                .post("/api/token");

        if (response.getStatusCode() == 200) {
            accessToken = response.jsonPath().getString("data.access_token");
            System.out.println("✅ Access Token alındı: " + accessToken);
        } else {
            System.out.println("❌ Token alınamadı!");
            return;
        }

        coursesEndPoint = "https://qa.instulearn.com/api/deleteCourse/" + courseId;
        System.out.println("🗑️ Silinecek URL: " + coursesEndPoint);

        response = given()
                .header("x-api-key", "1234")
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete(coursesEndPoint);

        System.out.println("\n📤 DELETE isteği Status: " + response.getStatusCode());
        System.out.println("DELETE Response: " + response.asString());

        if (response.getStatusCode() == 200) {
            System.out.println("\n✅ Course ID " + courseId + " başarıyla silindi!");
        } else {
            System.out.println("\n❌ Silme başarısız! Course ID " + courseId + " bulunamadı veya silinemez.");
        }
    }
}