package Api.TestNg.MethodDenemeleri;

import org.testng.annotations.Test;

public class GetSorgusuAFIN {

    // BUGUNKU ÇALIŞMAMMAIZDA METHODUNUN SONUNDAKİ
    // BİRİNCİ AF adminbilgisi hatalı
    // IN İSE ıd geekmediği manasında
    // şimdi sen  bu methodun aldını doldurabilrimsin.
    // method içinde status kodu doğrulasın
    // remark bilgisini doğrulasın ve consola yazdırsın.

    public static void getModulesAFIN(String modulesName) {

        // /api/courses endpoint'ine
        // gecersiz (invalid token) authorization bilgileri ile
        // bir GET request gönderildiginde
        // dönen status code'in 401 ve
        // response body'deki message bilgisinin "Unauthenticated." oldugu dogrulanmali.

        // 1. Admin yetkisi ile token al ve base URL'i kur
        Api.Utilities.HooksAPI.setUpApi("invalid");

        // 2. Path parametresini ayarla (api/modulesName)
        Api.Utilities.API_Methods.pathParam("api/" + modulesName);

        // 3. GET isteği gönder
        Api.Utilities.API_Methods.sendRequest("GET", null);

        // 4. Status code'un 200 olduğunu doğrula
        Api.Utilities.API_Methods.statusCodeAssert(401);

        // 5. Remark bilgisinin "success" olduğunu doğrula
        Api.Utilities.API_Methods.assertBody("message", "Unauthenticated.");

        // 6. Konsola bilgi yazdır
        System.out.println("Module: " + modulesName + " - Status Code: 200 - message: Unauthenticated.");
    }

    @Test
    public static void getCourses_TC01() {
        getModulesAFIN("courses");
    }
    @Test
    public static void getCoursesCatogories_TC01() {

        getModulesAFIN("categories");
    }

    @Test
    public static void getpricePlans_TC01() {
        getModulesAFIN("pricePlans");
    }
    @Test
    public static void getcoursefaqs_TC01() {
        getModulesAFIN("coursefaqs");
    }
    @Test
    public static void getproducts_TC01() {
        getModulesAFIN("products");
    }

    @Test
    public static void getproductCategories_TC01() {
        getModulesAFIN("productCategories");
    }


    @Test
    public static void getproductfaqs_TC01() {
        getModulesAFIN("productfaqs");
    }

    @Test
    public static void getblogs_TC01() {
        getModulesAFIN("blogs");
    }

    @Test
    public static void getblogCategories_TC01() {
        getModulesAFIN("blogCategories");
    }
    @Test
    public static void getcoupons_TC01() {
        getModulesAFIN("coupons");
    }

    @Test
    public static void getsupports_TC01(){
        getModulesAFIN("supports");
    }

    @Test
    public static void getdepartmants_TC01(){
        getModulesAFIN("departments");
    }
    @Test
    public static void getcontacts_TC01(){
        getModulesAFIN("contacts");
    }

    @Test
    public static void badges_TC01(){
        getModulesAFIN("badges");
    }
}
