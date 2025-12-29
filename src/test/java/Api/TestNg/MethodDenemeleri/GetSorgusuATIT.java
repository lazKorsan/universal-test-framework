package Api.TestNg.MethodDenemeleri;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import org.testng.annotations.Test;

public class GetSorgusuATIT {


    public static void getSorgusuATIT(String module, int id ){
        
        // AT dogru admin
        // IT dogru ID
        
        // /api/course/{id} endpoint'ine 
        // gecerli authorization bilgileri 
        // ve dogru data (id) iceren 
        // bir GET request gönderildiginde 
        // dönen status code'in 200 
        // ve response body'deki remark bilgisinin 
        // "success" oldugu dogrulanmali.

        // 1. Admin yetkisi ile token al ve base URL'i kur
        HooksAPI.setUpApi("admin");

        // 2. Path parametresini ayarla (api/module/id)
        API_Methods.pathParam("api/" + module + "/" + id);

        // 3. GET isteği gönder
        API_Methods.sendRequest("GET", null);

        // 4. Status code'un 200 olduğunu doğrula
        API_Methods.statusCodeAssert(200);

        // 5. Remark bilgisinin "success" olduğunu doğrula
        API_Methods.assertBody("remark", "success");

        // 6. Konsola bilgi yazdır
        System.out.println("Module: " + module + " ID: " + id + " - Status Code: 200 - Remark: success verified.");
        
    }

    // 1
    @Test
    public static void getCourse_TC01() {
        getSorgusuATIT("course",1995);
    }

    // 2
    @Test
    public static void getCoursesCatogory_TC01() {

        getSorgusuATIT("category",1180);
    }

    // 3
    @Test
    public static void getpricePlan_TC01() {
        getSorgusuATIT("pricePlan",413);
    }

    // 4
    @Test
    public static void getcoursefaq_TC01() {
        getSorgusuATIT("coursefaq",14);
    }

    // 5
    @Test
    public static void getproduct_TC01() {
        getSorgusuATIT("product",5);
    }

    // 6
    @Test
    public static void getproductCategory_TC01() {
        getSorgusuATIT("productCategory",184);
    }

    // 7
    @Test
    public static void getproductfaq_TC01() {
        getSorgusuATIT("productfaq",8); // kayıtlı productFag yoktir
    }

    // 8
    @Test
    public static void getblog_TC01() {
        getSorgusuATIT("blog",93);
    }

    // 9
    @Test
    public static void getblogCategory_TC01() {
        getSorgusuATIT("blogCategory",34);
    }

    // 10
    @Test
    public static void getcoupon_TC01() {
        getSorgusuATIT("coupon",398);
    }

    // 11
    @Test
    public static void getsupport_TC01(){
        getSorgusuATIT("support",28);
    }

    // 12
    @Test
    public static void getdepartmant_TC01(){
        getSorgusuATIT("department",57);
    }

    // 13
    @Test
    public static void getcontact_TC01(){
        getSorgusuATIT("contact",41);
    }

    // 14
    @Test
    public static void badge_TC01(){
        getSorgusuATIT("badge",22);
    }
}