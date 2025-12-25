package Api.TestNg.Courses;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Api.Utilities.API_Methods.response;
import static Api.Utilities.HooksAPI.spec;
import static io.restassured.RestAssured.given;

public class US001GetCourses {

    JsonPath jsonPath;
    JSONObject jsonObjectBody = new JSONObject();
    int addedProductId;
    int updatedProductID;
    int deletedProductId;
    public static final String TOKEN = "15707|XCnyE8Ov4ZyvaMbZIbMfzOpRUbiyA1eUVPyUhXOB";

    @Test
    public  void GetCourses_TC01() {

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

    @Test
    public void GetCourses_TC02() {

        // US002
        // admin true id true
        //    And response body'deki "data.teacher_id" degerinin 1016 oldugunu dogrular
        //    And response body'deki "data.creator_id" degerinin 1016 oldugunu dogrular
        //    And response body'deki "data.category_id" degerinin "611" oldugunu dogrular
        //    And response body'deki "data.type" degerinin "course" oldugunu dogrular

        //<! --  ============================
        //Api kullanicisi baseUrl olusturur
        HooksAPI.setUpApi("admin");

        //Api kullanicisi Id'si 1995 olan course icin bir get sorgusu yapar
        spec.pathParams("pp1", "api", "pp2", "course", "pp3", 1995);
        response = given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .when()
                .get("/{pp1}/{pp2}/{pp3}");

        // Response yazdirilir (Debug icin)
        response.prettyPrint();

        // Dogrulamalar (Assertions)
        jsonPath = response.jsonPath();

        Assert.assertEquals(jsonPath.getInt("data.teacher_id"), 1016, "Teacher ID eslesmedi");
        Assert.assertEquals(jsonPath.getInt("data.creator_id"), 1016, "Creator ID eslesmedi");
        Assert.assertEquals(jsonPath.getString("data.category_id"), "611", "Category ID eslesmedi");
        Assert.assertEquals(jsonPath.getString("data.type"), "course", "Type bilgisi eslesmedi");

    }

    @Test
    public void GetCourses_TC03() {

        // US003
        // admin(false) id(null)

        // @US001_TC003
        //  Scenario: US001 TC03 - Geçersiz yetkilendirme ile kursları getir
        //    Given api kullanicisi "invalid" token ile base url olusturur.
        //    And api kullanicisi "api/courses" path parametrelerini olusturur.
        //    Then api kullanicisi GET request gonderir ve donen response'i kaydeder.
        //    Then api kullanicisi status code'un 401 oldugunu dogrular.
        //    And api kullanicisi response body'deki "message" bilgisinin "Unauthenticated." oldugunu dogrular.

        HooksAPI.setUpApi("invalid");

        API_Methods.pathParam("api/courses");

        API_Methods.sendRequest("GET", null);

        API_Methods.statusCodeAssert(401);

        API_Methods.assertBody("message", "Unauthenticated.");


    }
}
