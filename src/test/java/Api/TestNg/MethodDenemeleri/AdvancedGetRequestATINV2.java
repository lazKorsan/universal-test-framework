package Api.TestNg.MethodDenemeleri;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class AdvancedGetRequestATINV2 {

    // Test sonuçlarını saklamak için statik bir liste
    private static final List<String> results = new ArrayList<>();

    /**
     * Bu metot, verilen bir modül endpoint'ine giderek toplam kayıt sayısını alır ve listeye ekler.
     * @param moduleEndpoint API'de sorgulanacak modülün adı (örn: "courses", "blogs").
     * @param jsonPathToList Response içinde kayıt listesini içeren JSON path'i (örn: "data.webinars", "data.blog").
     */
    public void getAndCollectModuleCount(String moduleEndpoint, String jsonPathToList) {
        // 1. Admin yetkisi ile token al ve base URL'i kur
        HooksAPI.setUpApi("admin");

        // 2. Path parametresini ayarla (örn: api/courses)
        API_Methods.pathParam("api/" + moduleEndpoint);

        // 3. GET isteği gönder ve response'u al
        Response response = API_Methods.sendRequest("GET", null);

        // 4. Temel doğrulamaları yap (Status 200, remark "success")
        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("remark", "success");

        // 5. Response'u JsonPath objesine çevir
        JsonPath jsonPath = response.jsonPath();
        int count = 0;

        // 6. JSON path'ini kullanarak listeyi al ve boyutunu hesapla
        if (moduleEndpoint.equals("courses") && jsonPath.get("data.totalWebinars") != null) {
            count = jsonPath.getInt("data.totalWebinars");
        } else {
            List<Object> list = jsonPath.getList(jsonPathToList);
            if (list != null) {
                count = list.size();
            }
        }

        // 7. Sonucu listeye ekle
        String result = String.format("%-20s : %d kayıt", moduleEndpoint, count);
        results.add(result);
        System.out.println("✓ " + moduleEndpoint + " kontrol edildi.");
    }

    @DataProvider(name = "moduleCountProvider")
    public Object[][] moduleCountProvider() {
        return new Object[][] {
            {"courses", "data.webinars"},
            {"categories", "data.categories"},
            {"pricePlans", "data"},
            {"coursefaqs", "data.coursefaqs"},
            {"products", "data.products"},
            {"productCategories", "data.categories"},
            {"productfaqs", "data"},
            {"blogs", "data.blog"},
            {"blogCategories", "data.blogCategories"},
            {"coupons", "data.discounts"},
            {"supports", "data.supports"},
            {"departments", "data.departments"},
            {"contacts", "data.contacts"},
            {"badges", "data.badges"}
        };
    }

    @Test(dataProvider = "moduleCountProvider")
    public void testAllModulesRecordCount(String module, String jsonPath) {
        getAndCollectModuleCount(module, jsonPath);
    }

    @AfterClass
    public void printAllResults() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("      TÜM MODÜL KAYIT SAYILARI");
        System.out.println("=".repeat(40));
        
        for (String result : results) {
            System.out.println(result);
        }
        
        System.out.println("=".repeat(40));
    }
}