package Api.TestNg;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import org.testng.annotations.Test;

import java.util.HashMap;

public class US004PatchCourses {

    @Test
    public void patchCoursesTC01() {
        // US01
        // admin(true) id(true)
        //api/updateCourse/{id} endpoint'ine gecerli authorization bilgileri ile dogru (id) ve
        // dogru datalar (duration, capacity, price, title) iceren bir PATCH
        // body gönderildiginde dönen status code'in 200,
        // response body'deki remark bilgisinin "success"
        // ve Message bilgisinin de "Successfully Updated." oldugu dogrulanmali.

        HooksAPI.setUpApi("admin");

        API_Methods.pathParam("api/updateCourse/3279");

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("duration", 40);
        requestBody.put("capacity", 40);
        requestBody.put("price", 0);
        requestBody.put("title", "course bilgileri güncellendi ");

        API_Methods.sendRequest("PATCH", requestBody);

        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("remark", "success");
        API_Methods.assertBody("Message", "Successfully Updated.");
    }

    @Test
    public void patchCoursesTC02() {
        // US02
        // /api/updateCourse/{id} endpoint'ine gecerli authorization bilgileri
        // ile dogru (id) ve data icermeyen bir PATCH request gönderildiginde
        // dönen status code'in 203,
        // response body'deki remark bilgisinin "failed"
        // ve message bilgisinin de "There is no information to update." oldugu dogrulanmali.

        HooksAPI.setUpApi("admin");

        API_Methods.pathParam("api/updateCourse/3280");

        HashMap<String, Object> requestBody = new HashMap<>();

        API_Methods.sendRequest("PATCH", requestBody);

        API_Methods.statusCodeAssert(203);
        API_Methods.assertBody("remark", "failed");
        API_Methods.assertBody("data.message", "There is no information to update.");

    }

    @Test
    public void patchCoursesTC03A01() {
        // US03A01
        // admin(true) id(false)
        // /api/updateCourse/{id} endpoint'ine gecerli authorization bilgileri ile
        // kaydı olmayan bir (id) ve dogru datalar (duration, capacity, price, title) iceren bir PATCH body gönderildiginde
        // dönen status code'in 203, response body'deki remark bilgisinin "failed"
        // ve message bilgisinin de "There is not course for this id." oldugu,
        //<!-- ==============================0
        // (id) icermeyen ve dogru datalar (duration, capacity, price, title) iceren bir PATCH body gönderildiginde de
        // dönen status code'in 203,
        // response body'deki remark bilgisinin "failed"
        // ve message bilgisinin de "No id" oldugu dogrulanmali.

        HooksAPI.setUpApi("admin");

        API_Methods.pathParam("api/updateCourse/13280");

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("duration", 40);
        requestBody.put("capacity", 40);
        requestBody.put("price", 0);
        requestBody.put("title", "course bilgileri güncellendi ");

        API_Methods.sendRequest("PATCH", requestBody);

        API_Methods.statusCodeAssert(203);
        API_Methods.assertBody("remark", "failed");
        API_Methods.assertBody("data.message", "There is not course for this id.");

    }

    @Test
    public void patchCoursesTC03A02() {

        // US03A02
        // admin(true) id(false)
        // /api/updateCourse/{id} endpoint'ine gecerli authorization bilgileri ile
        // (id) icermeyen ve dogru datalar (duration, capacity, price, title) iceren bir PATCH body gönderildiginde de
        // dönen status code'in 203,
        // response body'deki remark bilgisinin "failed"
        // ve message bilgisinin de "No id" oldugu dogrulanmali.
        // Scenario: ID Numarası olmadan (No id) güncelleme denemesi

        HooksAPI.setUpApi("admin");


        API_Methods.pathParam("api/updateCourse");

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("duration", 40);
        requestBody.put("capacity", 40);
        requestBody.put("price", 0);
        requestBody.put("title", "course bilgileri güncellendi");

        API_Methods.sendRequest("PATCH", requestBody);

        API_Methods.statusCodeAssert(203);
        API_Methods.assertBody("remark", "failed");
        API_Methods.assertBody("data.message", "No id");
    }

    @Test
    public void patchCoursesTC04() {

        //<!-- fixme ==================================
        // US04
        // admin(FALSE) id(true)
        // /api/updateCourse/{id} endpoint'ine gecersiz (invalid token) authorization bilgileri ile dogru (id)
        // ve dogru datalar (duration, capacity, price, title) iceren bir PATCH body gönderildiginde
        // dönen status code'in 401 oldugu ve
        // response body'deki message bilgisinin "Unauthenticated." oldugu dogrulanmali

        HooksAPI.setUpApi("invalid");


        API_Methods.pathParam("api/updateCourse/1995");

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("duration", 40);
        requestBody.put("capacity", 40);
        requestBody.put("price", 0);
        requestBody.put("title", "course bilgileri güncellendi");

        API_Methods.sendRequest("PATCH", requestBody);

        API_Methods.statusCodeAssert(401);

        API_Methods.assertBody("message", "Unauthenticated.");

    }

    @Test
    public void patchCoursesTC05(){

        // US05
        // /api/updateCourse/{id} endpoint'inden donen
        // response body icindeki Updated Course ID bilgisinin /api/updateCourse/{id}
        // endpoint'inde yazan id path parametresi ile ayni oldugu dogrulanmali.

        HooksAPI.setUpApi("admin");

        API_Methods.pathParam("api/updateCourse/3279");

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("duration", 40);
        requestBody.put("capacity", 40);
        requestBody.put("price", 0);
        requestBody.put("title", "course bilgileri güncellendi");

        API_Methods.sendRequest("PATCH", requestBody);

        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("remark", "success");
        API_Methods.assertBody("Message", "Successfully Updated.");

        // URL'deki ID (3279) ile Response'daki "Updated Course ID" degerini karsilastirir
        API_Methods.assertPathParam("Updated Course ID");
    }
    
    @Test
    public void patchCoursesTC06(){
        // US06
        // API uzerinden güncellenmek istenen course kaydinin güncellendigi,
        // API uzerinden dogrulanmali.
        //(Response body'de dönen Updated Course ID
        // ile /api/course/{id} endpoint'ine GET request gönderilerek kaydın güncellendiği doğrulanabilir.)

        HooksAPI.setUpApi("admin");

        // Onceki testlerde kullandigimiz ID (3279) uzerinden gidelim
        API_Methods.pathParam("api/updateCourse/3279");

        String updatedTitle = "course bilgileri güncellendi - Verified via GET";

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("duration", 55);
        requestBody.put("capacity", 55);
        requestBody.put("price", 0);
        requestBody.put("title", updatedTitle);

        // 1. Adim: Guncelleme Istegi (PATCH)
        API_Methods.sendRequest("PATCH", requestBody);

        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("Message", "Successfully Updated.");

        // 2. Adim: Updated ID'yi Response'dan al
        int updatedCourseId = API_Methods.response.jsonPath().getInt("'Updated Course ID'");

        // 3. Adim: GET ile dogrulama yap
        API_Methods.pathParam("api/course/" + updatedCourseId);

        API_Methods.sendRequest("GET", null);

        API_Methods.statusCodeAssert(200);

        // Title bilgisinin guncellendigini dogrula
        // Not: API yapisinda title bazen data.title yerine data.translations[0].title icinde donuyor
        API_Methods.assertBody("data.translations[0].title", updatedTitle);
        API_Methods.assertBody("data.duration", 55);
        API_Methods.assertBody("data.capacity", 55);
    }
}