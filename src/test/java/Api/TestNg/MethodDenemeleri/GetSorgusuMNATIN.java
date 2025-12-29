package Api.TestNg.MethodDenemeleri;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import org.testng.annotations.Test;

public class GetSorgusuMNATIN {

    public static void getModuleMNATIN(String moduleName){

        // MN Modul Name
        // AT Admin True
        // IN Id Null (No ID)

        // (id) icermeyen bir GET request gönderildiginde de 
        // dönen status code'un 203, 
        // response body'deki remark bilgisinin "failed" 
        // ve message bilgisinin de "No id" oldugu dogrulanmali.

        // 1. Admin yetkisi ile token al ve base URL'i kur
        HooksAPI.setUpApi("admin");

        // 2. Path parametresini ayarla (api/moduleName/)
        // Not: ID olmadığı için path'in sonuna / ekliyoruz veya eklemiyoruz, API'nin davranışına göre.
        // Genellikle "api/course/" gibi çağrılırsa No id döner.
        API_Methods.pathParam("api/" + moduleName + "/");

        // 3. GET isteği gönder
        API_Methods.sendRequest("GET", null);

        // 4. Status code'un 203 olduğunu doğrula
        API_Methods.statusCodeAssert(203);

        // 5. Remark bilgisinin "failed" olduğunu doğrula
        API_Methods.assertBody("remark", "failed");

        // 6. Message bilgisinin "No id" olduğunu doğrula
        API_Methods.assertBody("data.message", "No id");

        // 7. Konsola bilgi yazdır
        System.out.println("Module: " + moduleName + " (No ID) - Status Code: 203 - Remark: failed verified.");
        
    }

    // 1
    @Test
    public void testGetModulecourse_TC01() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("course");
    }

    // 2
    @Test
    public void testGetModulecourse_TC02() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("category");
    }

    // 3
    @Test
    public void testGetModulecourse_TC03() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("pricePlan");
    }

    // 4
    @Test
    public void testGetModulecourse_TC04() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("coursefaq");
    }

    // 5
    @Test
    public void testGetModulecourse_TC05() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("product");
    }

    // 6
    @Test
    public void testGetModulecourse_TC06() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("productCategory");
    }

    // 7
    @Test
    public void testGetModulecourse_TC07() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("productfaq");
    }

    // 8
    @Test
    public void testGetModulecourse_TC08() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("blog");
    }

    // 9
    @Test
    public void testGetModulecourse_TC09() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("coupon");
    }

    // 10
    @Test
    public void testGetModulecourse_TC10() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("support");
    }

    // 11
    @Test
    public void testGetModulecourse_TC11() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("department");
    }

    // 12
    @Test
    public void testGetModulecourse_TC12() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("contact");
    }

    // 13
    @Test
    public void testGetModulecourse_TC13() {
        // Örnek kullanım: Course modülü için ID olmadan sorgu
        getModuleMNATIN("badge");
    }
}