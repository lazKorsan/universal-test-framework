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

        API_Methods.pathParam("api/updateCourse/3280");

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
        API_Methods.assertBody("message", "There is no information to update.");

    }
}
