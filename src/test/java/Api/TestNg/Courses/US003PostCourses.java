package Api.TestNg.Courses;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import org.testng.annotations.Test;

import java.util.HashMap;

public class US003PostCourses {

    @Test
    public void postCoursesTC01() {

        //   @US03_TC01
        // admin(true) id(null)
        //  Scenario: US03_TC01 Geçerli Bilgilerle yeni course olusturmak
        //    Given api kullanicisi "admin" token ile base url olusturur.
        //    And api kullanicisi "api/addCourse" path parametrelerini olusturur.
        //    Then api kullanicisi addCourse icin request body hazırlar
        //    And api kullanicisi "POST" request gonderir ve donen response'i kaydeder.
        //    Then api kullanicisi status code'un 200 oldugunu dogrular.
        //    And api kullanicisi response body'deki "remark" bilgisinin "success" oldugunu dogrular.
        //    Then api kullanicisi response body'deki "Message" bilgisinin "Successfully Added." oldugunu dogrular.

        // /api/addCourse endpoint'ine gecerli authorization bilgileri ve
        // dogru datalar (title, type, slug, start_date, duration, capacity, price,
        // description, teacher_id) iceren bir POST body gönderildiginde
        // dönen status code'in 200, response body'deki remark bilgisinin "success"
        // ve Message bilgisinin de "Successfully Added." oldugu dogrulanmali.

        //title, type, slug, start_date, duration, capacity, price, description, teacher_id

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
    public void postCoursesTC02() {

        // US003 TC02
        // admin(true) id(null)


        // /api/addCourse endpoint'ine gecerli authorization bilgileri
        // ve eksik data (teacher_id) iceren bir
        // POST body (title, type, slug, start_date, duration, capacity, price, description)
        // gönderildiginde dönen status code'in 422 oldugu ve
        // response body'deki message bilgisinin "The teacher id field is required."
        // oldugu, data icermeyen bir POST request gönderildiginde de dönen status
        // code'in 422 oldugu ve response body'deki message bilgisinin
        // "The teacher id field is required. (and 2 more errors)" oldugu dogrulanmali

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
        //requestBody.put("teacher_id", 1016);
        requestBody.put("category_id", 611);

        API_Methods.sendRequest("POST", requestBody);

        API_Methods.statusCodeAssert(422);
        API_Methods.assertBody("message", "The teacher id field is required.");


    }

    @Test
    public void postCoursesTC03() {

        // /api/addCourse endpoint'ine gecersiz (invalid token) authorization bilgileri ile
        // dogru datalar (title, type, slug, start_date, duration, capacity, price, description, teacher_id)
        // iceren bir POST body gönderildiginde
        // dönen status code'in 401 oldugu
        // ve response body'deki message bilgisinin "Unauthenticated." oldugu dogrulanmali

        HooksAPI.setUpApi("invalid");

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

        API_Methods.statusCodeAssert(401);
        API_Methods.assertBody("message", "Unauthenticated.");
    }

    @Test
    public void postCoursesTC04() {

        // US004
        // admin(true) id(null)
        // API uzerinden olusturulmak istenen yeni course kaydinin olustugu API uzerinden dogrulanmali.
        //(Response bodyde dönen Added Course ID ile /api/course/{id}
        // endpoint'ine GET request gönderilerek kayıt oluşturulduğu doğrulanabilir.)

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

        // 1. Adim: Kursu Olustur (POST)
        API_Methods.sendRequest("POST", requestBody);

        // Olusturma isleminin basarili oldugunu dogrula
        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("remark", "success");
        API_Methods.assertBody("Message", "Successfully Added.");

        // 2. Adim: Olusan Kursun ID'sini Response'dan al (Extract)
        // API donusunde "Added Course ID" anahtari ile ID veriyor
        int newCourseId = API_Methods.response.jsonPath().getInt("'Added Course ID'");
        System.out.println("Yeni Olusturulan Kurs ID: " + newCourseId);

        // 3. Adim: Bu ID ile kaydin olustugunu dogrula (GET)
        // Endpoint: api/course/{id}
        API_Methods.pathParam("api/course/" + newCourseId);

        API_Methods.sendRequest("GET", null);

        // 4. Adim: Get sorgusunun saglamasini yap
        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("data.id", newCourseId);
        API_Methods.assertBody("data.translations[0].title", "course görmek güzeldir");
    }
}
