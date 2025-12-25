package Api.TestNg.Courses;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import org.testng.annotations.Test;

public class GetMethod {

    public static void getModules(String userType, String moduleName, String expectedRemark, int expectedStatusCode) {
        HooksAPI.setUpApi(userType);
        API_Methods.pathParam("api/" + moduleName);
        API_Methods.sendRequest("GET", null);
        API_Methods.statusCodeAssert(expectedStatusCode);

        if (expectedRemark != null) {
            API_Methods.assertBody("remark", expectedRemark);
        }
    }


    @Test
    public static void testGetCourses() {
        getModules("admin", "courses", "success", 200);
    }

}
