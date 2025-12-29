package Api.TestNg.MethodDenemeleri;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import org.testng.annotations.Test;

public class GetSorgusuATIN {

    // BUGUNKU ÇALIŞMAMMAIZDA METHODUNUN SONUNDAKİ 
    // BİRİNCİ AT admin dogru
    // IN İSE ıd geekmediği manasında 
    // şimdi sen  bu methodun aldını doldurabilrimsin. 
    // method içinde status kodu doğrulasın 
    // remark bilgisini doğrulasın ve consola yazdırsın. 

    public static void getModulesATIN(String modulesName) {

        // /api/courses endpoint'ine gecerli authorization bilgileri 
        // ile bir GET request gönderildiginde 
        // dönen status code'in 200 ve response body'deki 
        // remark bilgisinin "success" oldugu dogrulanmali.

        // 1. Admin yetkisi ile token al ve base URL'i kur
        HooksAPI.setUpApi("admin");

        // 2. Path parametresini ayarla (api/modulesName)
        API_Methods.pathParam("api/" + modulesName);

        // 3. GET isteği gönder
        API_Methods.sendRequest("GET", null);

        // 4. Status code'un 200 olduğunu doğrula
        API_Methods.statusCodeAssert(200);

        // 5. Remark bilgisinin "success" olduğunu doğrula
        API_Methods.assertBody("remark", "success");

        // 6. Konsola bilgi yazdır
        System.out.println("Module: " + modulesName + " - Status Code: 200 - Remark: success verified.");
    }

    // 1
    @Test
    public static void getCourses_TC01() {
        getModulesATIN("courses");
    }

    // 2
    @Test
    public static void getCoursesCatogories_TC01() {

        getModulesATIN("categories");
    }

    // 3
    @Test
    public static void getpricePlans_TC01() {
        getModulesATIN("pricePlans");
    }

    // 4
    @Test
    public static void getcoursefaqs_TC01() {
        getModulesATIN("coursefaqs");
    }

    // 5
    @Test
    public static void getproducts_TC01() {
        getModulesATIN("products");
    }

    // 6
    @Test
    public static void getproductCategories_TC01() {
        getModulesATIN("productCategories");
    }

    // 7
    @Test
    public static void getproductfaqs_TC01() {
        getModulesATIN("productfaqs");
    }

    // 8
    @Test
    public static void getblogs_TC01() {
        getModulesATIN("blogs");
    }

    // 9
    @Test
    public static void getblogCategories_TC01() {
        getModulesATIN("blogCategories");
    }

    // 10
    @Test
    public static void getcoupons_TC01() {
        getModulesATIN("coupons");
    }

    // 11
    @Test
    public static void getsupports_TC01(){
        getModulesATIN("supports");
    }

    // 12
    @Test
    public static void getdepartmants_TC01(){
        getModulesATIN("departments");
    }

    // 13
    @Test
    public static void getcontacts_TC01(){
        getModulesATIN("contacts");
    }

    // 14
    @Test
    public static void badges_TC01(){
        getModulesATIN("badges");
    }
}