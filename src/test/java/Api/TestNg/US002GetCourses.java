package Api.TestNg;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import org.json.JSONObject;
import org.testng.annotations.Test;

public class US002GetCourses {


    @Test
    public void getCourseTC01() {

        // US002
        // admin(true) id(195)
        //  @US002_TC001
        //  Scenario: US002_TC001 - Geçerli ID ile belirli bir kuponu getir
        //    Given api kullanicisi "admin" token ile base url olusturur.
        //    And api kullanicisi "api/course/1995" path parametrelerini olusturur.
        //    Then api kullanicisi GET request gonderir ve donen response'i kaydeder.
        //    Then api kullanicisi status code'un 200 oldugunu dogrular.
        //    And api kullanicisi response body'deki "remark" bilgisinin "success" oldugunu dogrular.

        HooksAPI.setUpApi("admin");

        API_Methods.pathParam("api/course/1995");

        API_Methods.sendRequest("GET", null);

        API_Methods.statusCodeAssert(200);

        API_Methods.assertBody("remark", "success");

    }

    @Test
    public void getCourseTC02() {

        //  @US02_TC02
        // admin(true) id(1995)
        //  Scenario: US02 TC02 Bilinen ID ile Course Bilgilerini dogrula
        //    Given api kullanicisi "admin" token ile base url olusturur.
        //    And api kullanicisi "api/course/1995" path parametrelerini olusturur.
        //    Then api kullanicisi GET request gonderir ve donen response'i kaydeder.
        //    Then api kullanicisi status code'un 200 oldugunu dogrular.
        //    And response body'deki degerlerin asagidaki gibi oldugunu dogrular:
        //      | path                    | value                            |
        //      | data.creator_id         | 1016                             |
        //      | data.slug               | Become-a-Project-Manager         |
        //      | data.duration           | 150                              |
        //      | data.support            | 1                                |
        //      | data.timezone           | America/New_York                 |
        //      | data.thumbnail          | /store/1/product-management-.jpg |
        //      | data.image_cover        | /store/1/product-management.jpg  |
        //      | data.downloadable       | 1                                |
        //      | data.partner_instructor | 0                                |
        //      | data.subscribe          | 0                                |
        //      | data.forum              | 0                                |
        //      | data.created_at         | 1624867858                       |
        //      | data.updated_at         | 1711967895                       |


        HooksAPI.setUpApi("admin");

        API_Methods.pathParam("api/course/1995");

        API_Methods.sendRequest("GET", null);

        API_Methods.statusCodeAssert(200);

        API_Methods.assertBody("data.creator_id", 1016);
        API_Methods.assertBody("data.slug", "Become-a-Project-Manager");
        API_Methods.assertBody("data.duration", 150);
        API_Methods.assertBody("data.support", 1);
        API_Methods.assertBody("data.timezone", "America/New_York");
        API_Methods.assertBody("data.thumbnail", "/store/1/product-management-.jpg");
        API_Methods.assertBody("data.image_cover", "/store/1/product-management.jpg");
        API_Methods.assertBody("data.downloadable", 1);
        API_Methods.assertBody("data.partner_instructor", 0);
        API_Methods.assertBody("data.subscribe", 0);
        API_Methods.assertBody("data.forum", 0);
        API_Methods.assertBody("data.created_at", 1624867858);
        API_Methods.assertBody("data.updated_at", 1711967895);
    }

    @Test
    public void getCourseTC03A01() {

        //  @US02_TC03
        // admin(true) id(false)
        //  @US02_TC03_A01
        //  Scenario: US02_TC03  Geçersiz ID ile Courses Bilgilerinin Status degerlerinin dogrulanması
        //
        //    Given api kullanicisi "admin" token ile base url olusturur.
        //    And api kullanicisi "api/course/9999" path parametrelerini olusturur.
        //    Then api kullanicisi GET request gonderir ve donen response'i kaydeder.
        //    Then api kullanicisi status code'un 203 oldugunu dogrular.
        //    And api kullanicisi response body'deki "remark" bilgisinin "failed" oldugunu dogrular.
        //    Then api kullanicisi response body'deki "message" bilgisinin "There is not course for this id." oldugunu dogrular.

        HooksAPI.setUpApi("admin");

        API_Methods.pathParam("api/course/11995");

        API_Methods.sendRequest("GET", null);

        API_Methods.statusCodeAssert(203);

        API_Methods.assertBody("remark", "failed");

        API_Methods.assertBody("data.message", "There is not course for this id.");
    }

    @Test
    public void getCourseTC03A02() {

        //  @US02_TC03
        // admin(true) id(false)
        //   @US02_TC03_A02
        //  Scenario: US047 TC03 A02 ID Numarası olmadan Course Status degerlerinin dogrulamsı
        //    Given api kullanicisi "admin" token ile base url olusturur.
        //    And api kullanicisi "api/course/" path parametrelerini olusturur.
        //    Then api kullanicisi GET request gonderir ve donen response'i kaydeder.
        //    Then api kullanicisi status code'un 203 oldugunu dogrular.
        //    And api kullanicisi response body'deki "remark" bilgisinin "failed" oldugunu dogrular.
        //    Then api kullanicisi response body'deki "message" bilgisinin "No id" oldugunu dogrular.

        HooksAPI.setUpApi("admin");

        API_Methods.pathParam("api/course/");

        API_Methods.sendRequest("GET", null);

        API_Methods.statusCodeAssert(203);

        API_Methods.assertBody("remark", "failed");

        API_Methods.assertBody("data.message", "No id");
    }

    @Test
    public void getCourseTC004(){

        //   @US02_TC04
        // admin(false) id(null)
        //  Scenario: US02_TC04 Geçersiz Invalid Token
        //    Given api kullanicisi "invalid" token ile base url olusturur.
        //    And api kullanicisi "api/course/1995" path parametrelerini olusturur.
        //    Then api kullanicisi GET request gonderir ve donen response'i kaydeder.
        //    Then api kullanicisi status code'un 401 oldugunu dogrular.
        //    Then api kullanicisi response body'deki "message" bilgisinin "Unauthenticated." oldugunu dogrular.

        HooksAPI.setUpApi("invalid");

        API_Methods.pathParam("api/course/1995");

        API_Methods.sendRequest("GET", null);

        API_Methods.statusCodeAssert(401);

        // 401 hatalarinda genellikle data objesi donmez, direkt message doner
        API_Methods.assertBody("message", "Unauthenticated.");
    }

    @Test
    public void postCoursesTC01() {
        //  Scenario: Yeni Kurs Ekleme (POST)
        HooksAPI.setUpApi("admin");

        // Endpoint: api/courses (Genellikle create islemi icin id gonderilmez)
        API_Methods.pathParam("api/courses");

        // Body Hazirligi (Ornek veriler)
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "Java API Test Course");
        requestBody.put("category_id", 611); // API dokumanina gore int veya String olabilir
        // Diger zorunlu alanlar buraya eklenebilir...

        // Request Gönder (POST)
        API_Methods.sendRequest("POST", requestBody.toString());

        // Doğrulamalar
        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("remark", "success");
    }
}