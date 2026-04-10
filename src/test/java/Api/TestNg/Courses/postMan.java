package Api.TestNg.Courses;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class postMan {

    String baseUrl = "https://qa.instulearn.com";
    String token = "21280|IXANZZxwc3nozA5wFfEyA7MIC1cfYwP8ZFBeYZyy";

    @Test
    public void getCoursesAndDeleteMaxId() {

        // STEP 1: GET /api/courses - tüm kursları al
        Response getResponse = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .header("x-api-key", "1234")
                .header("Accept", "application/json")
                .when()
                .get("/api/courses")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("📋 GET /api/courses status: " + getResponse.getStatusCode());

        // STEP 2: Max ID'yi bul
        List<Map<String, Object>> courses = getResponse
                .jsonPath()
                .getList("AddedCourseID.webinars");

        if (courses == null || courses.isEmpty()) {
            System.out.println("⚠️ No courses found in response.");
            return;
        }

        System.out.println("📦 Total courses returned: " + courses.size());

        int maxId = courses.stream()
                .mapToInt(c -> (int) c.get("id"))
                .max()
                .getAsInt();

        System.out.println("🏆 Max course ID found: " + maxId);

        // STEP 3: DELETE /api/deleteCourse/{maxId}
        Response deleteResponse = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .header("x-api-key", "1234")
                .header("Accept", "application/json")
                .when()
                .delete("/api/deleteCourse/" + maxId)
                .then()
                .extract().response();

        System.out.println("🗑️ DELETE status: " + deleteResponse.getStatusCode());
        System.out.println("Response: " + deleteResponse.getBody().asString());

        // STEP 4: Test assertion
        assertEquals(deleteResponse.getStatusCode(), 200,
                "❌ DELETE failed for course ID: " + maxId);

        System.out.println("✅ Course with ID " + maxId + " successfully deleted.");
    }
}