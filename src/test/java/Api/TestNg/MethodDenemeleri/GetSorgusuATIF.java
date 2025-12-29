package Api.TestNg.MethodDenemeleri;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import org.testng.annotations.Test;

public class GetSorgusuATIF {
    
    
    public static void getModulesATIF(String modulesName, int id ){
        
        // AT dogru admin
        // IF yanlıs  ID
        //  /api/course/{id} endpoint'ine 
        //  gecerli authorization bilgileri 
        //  ve kaydı olmayan bir (id) iceren 
        //  bir GET request gönderildiginde 
        //  dönen status code'in 203, 
        //  response body'deki remark bilgisinin "failed" 
        //  ve message bilgisinin de "There is not course for this id." oldugu, 

        // 1. Admin yetkisi ile token al ve base URL'i kur
        HooksAPI.setUpApi("admin");

        // 2. Path parametresini ayarla (api/modulesName/id)
        API_Methods.pathParam("api/" + modulesName + "/" + id);

        // 3. GET isteği gönder
        API_Methods.sendRequest("GET", null);

        // 4. Status code'un 203 olduğunu doğrula
        API_Methods.statusCodeAssert(203);

        // 5. Remark bilgisinin "failed" olduğunu doğrula
        API_Methods.assertBody("remark", "failed");

        // 6. Message bilgisini modüle göre dinamik olarak belirle ve doğrula
        String expectedMessage = "";
        
        switch (modulesName) {
            case "course":
                expectedMessage = "There is not course for this id.";
                break;
            case "category":
            case "blogCategory":
                expectedMessage = "There is not category for this id.";
                break;
            case "pricePlan":
                expectedMessage = "There is not ticket for this id.";
                break;
            case "coursefaq":
                expectedMessage = "There is not course faq for this id.";
                break;
            case "product":
                expectedMessage = "There is not product for this id.";
                break;
            case "productCategory":
                expectedMessage = "There is not product category for this id.";
                break;
            case "productfaq":
                expectedMessage = "There is not product faq for this id.";
                break;
            case "blog":
                expectedMessage = "There is not blog for this id.";
                break;
            case "coupon":
                expectedMessage = "There is not coupon for this id.";
                break;
            case "support":
                expectedMessage = "There is not support message for this id.";
                break;
            case "department":
                expectedMessage = "There is not support department for this id.";
                break;
            case "contact":
                expectedMessage = "There is not contact message for this id.";
                break;
            case "badge":
                expectedMessage = "There is not badge for this id.";
                break;
            default:
                // Varsayılan olarak modül ismini kullan (eğer listede yoksa)
                expectedMessage = "There is not " + modulesName + " for this id.";
                break;
        }

        API_Methods.assertBody("data.message", expectedMessage);

        // 7. Konsola bilgi yazdır
        System.out.println("Module: " + modulesName + " ID: " + id + " - Status Code: 203 - Remark: failed verified.");
        System.out.println("Verified Message: " + expectedMessage);
    }

    // 1
    @Test
    public static void getCourse_TC01() {
        getModulesATIF("course",11995);
    }

    // 2
    @Test
    public static void getCoursesCatogory_TC01() {
        getModulesATIF("category",11180);
    }

    // 3
    @Test
    public static void getpricePlan_TC01() {
        getModulesATIF("pricePlan",1413);
    }

    // 4
    @Test
    public static void getcoursefaq_TC01() {
        getModulesATIF("coursefaq",1114);
    }

    // 5
    @Test
    public static void getproduct_TC01() {
        getModulesATIF("product",1115);
    }

    // 6
    @Test
    public static void getproductCategory_TC01() {
        getModulesATIF("productCategory",111184);
    }

    // 7
    @Test
    public static void getproductfaq_TC01() {
        getModulesATIF("productfaq",11118);
    }

    // 8
    @Test
    public static void getblog_TC01() {
        getModulesATIF("blog",11193);
    }

    // 9
    @Test
    public static void getblogCategory_TC01() {
        getModulesATIF("blogCategory",11134);
    }

    // 10
    @Test
    public static void getcoupon_TC01() {
        getModulesATIF("coupon",111398);
    }

    // 11
    @Test
    public static void getsupport_TC01(){
        getModulesATIF("support",111128);
    }

    // 12
    @Test
    public static void getdepartmant_TC01(){
        getModulesATIF("department",11157);
    }

    // 13
    @Test
    public static void getcontact_TC01(){
        getModulesATIF("contact",111141);
    }

    // 14
    @Test
    public static void badge_TC01(){
        getModulesATIF("badge",111122);
    }
}