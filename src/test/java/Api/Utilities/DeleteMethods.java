package Api.Utilities;

import org.junit.Test;

public class DeleteMethods {

   public static void deleteCoursesMethod(int CourseID){

       HooksAPI.setUpApi("admin");
       String endPoint="api/deleteCourse/"+CourseID;
       API_Methods.pathParam(endPoint);
       API_Methods.sendRequest("DELETE", null);
       API_Methods.statusCodeAssert(200);


   }

   @Test
   public void test(){
       DeleteMethods deleteMethods = new DeleteMethods();
       deleteCoursesMethod(3973);
   }
}
