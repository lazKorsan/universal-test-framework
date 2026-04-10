package Api.TestNg.Courses;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class postMan2 {

    @Test
    public void getCoursesAndDeleteMaxId() {

        // STEP 1: GET /api/courses - tüm kursları al
        HooksAPI.setUpApi("admin");

        API_Methods.pathParam("api/courses");

        API_Methods.sendRequest("GET", null);

        API_Methods.statusCodeAssert(200);

        // STEP 2: Max ID'yi bul
        List<Map<String, Object>> courses = API_Methods.response
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
        HooksAPI.setUpApi("admin");

        API_Methods.pathParam("api/deleteCourse/" + maxId);

        API_Methods.sendRequest("DELETE", null);

        API_Methods.statusCodeAssert(200);

        System.out.println("✅ Course with ID " + maxId + " successfully deleted.");
    }
}