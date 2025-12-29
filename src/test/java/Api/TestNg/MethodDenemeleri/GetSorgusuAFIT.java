package Api.TestNg.MethodDenemeleri;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GetSorgusuAFIT {
    
    public static void getModulesAFIT(String modulesName, int id){
        
        // AF: Admin False (Invalid Token)
        // IT: ID True (Valid ID)

        // /api/course/{id} endpoint'ine 
        // gecersiz (invalid token) authorization bilgileri 
        // ve dogru data (id) iceren bir GET request gönderildiginde 
        // dönen status code'in 401 
        // ve response body'deki message bilgisinin "Unauthenticated." oldugu dogrulanmali.

        // 1. Geçersiz token ile base URL'i kur
        HooksAPI.setUpApi("invalid");

        // 2. Path parametresini ayarla (api/modulesName/id)
        API_Methods.pathParam("api/" + modulesName + "/" + id);

        // 3. GET isteği gönder
        API_Methods.sendRequest("GET", null);

        // 4. Status code'un 401 olduğunu doğrula
        API_Methods.statusCodeAssert(401);

        // 5. Message bilgisinin "Unauthenticated." olduğunu doğrula
        // Not: 401 hatalarında genellikle data objesi dönmez, direkt message döner.
        API_Methods.assertBody("message", "Unauthenticated.");

        // 6. Konsola bilgi yazdır
        System.out.println("Module: " + modulesName + " ID: " + id + " (Invalid Token) - Status Code: 401 - Message: Unauthenticated. verified.");
    }

    @DataProvider(name = "moduleProvider")
    public Object[][] moduleProvider() {
        return new Object[][] {
            {"course", 1995},
            {"category", 1180},
            {"pricePlan", 413},
            {"coursefaq", 14},
            {"product", 5},
            {"productCategory", 184},
            {"productfaq", 8},
            {"blog", 93},
            {"blogCategory", 34},
            {"coupon", 398},
            {"support", 28},
            {"department", 57},
            {"contact", 41},
            {"badge", 22}
        };
    }

    @Test(dataProvider = "moduleProvider")
    public void testGetModulesAFIT(String module, int id) {
        getModulesAFIT(module, id);
    }
}