package Api.TestNg;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;

public class CoursesMethod {

    public static void GetCourses_TC01() {

        // USOO1
        // admin(true) id(null)
        // Given api kullanicisi "admin" token ile base url olusturur.
        //    And api kullanicisi "api/courses" path parametrelerini olusturur.
        //    When api kullanicisi GET request gonderir ve donen response'i kaydeder.
        //    Then api kullanicisi status code'un 200 oldugunu dogrular.
        //    And api kullanicisi response body'deki "remark" bilgisinin "success" oldugunu dogrular.

        // Kullanici yonetici olarak adminToken alır
        HooksAPI.setUpApi("admin");

        API_Methods.pathParam("api/courses");

        // admin get sorgu sonucunu kayt eder
        API_Methods.sendRequest("GET", null);

        // admin status code 200 oldugunu dogrular
        API_Methods.statusCodeAssert(200);

        // api_kullanicisi_response_body_deki_bilgisinin_oldugunu_dogrular : path = remark  value = success
        API_Methods.assertBody("remark", "success");

    }
}
