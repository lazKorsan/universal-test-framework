package Api.TestNg.Courses;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import org.testng.annotations.Test;

import java.util.HashMap;

public class PostMethod {
    @Test
    public static void postMethod(){


        HooksAPI.setUpApi("admin");

        API_Methods.pathParam("api/addCourse");

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "course görmek güzeldir");
        requestBody.put("type", "course");
        requestBody.put("slug", "course-gormek-guzeldir");
        requestBody.put("start_date", "2025-11-28");
        requestBody.put("duration", 40);
        requestBody.put("capacity", 40);
        requestBody.put("price", 0);
        requestBody.put("description", "Yeni oluşturulan kursun açıklamasıdır.");
        requestBody.put("teacher_id", 1016);
        requestBody.put("category_id", 611);

        API_Methods.sendRequest("POST", requestBody);

        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("remark", "success");
        API_Methods.assertBody("Message", "Successfully Added.");

    }

    @Test
    public static void postMethod2() throws InterruptedException {
        for (int i = 1; i <= 100; i++) {
            System.out.println("Çalışma #" + i);
            postMethod();

            if (i < 10) {
                Thread.sleep(2000); // 5 saniye bekle
            }
        };
    }

    @Test
    public static void postMethod3() throws InterruptedException {
        postMethod2();
    }
}
