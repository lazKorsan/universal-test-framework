package Api.TestNg.MethodDenemeleri;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class AdvancedGetRequestATIN {

    /**
     * Bu metot, verilen bir modül endpoint'ine giderek toplam kayıt sayısını alır ve konsola yazdırır.
     * @param moduleEndpoint API'de sorgulanacak modülün adı (örn: "courses", "blogs").
     * @param jsonPathToList Response içinde kayıt listesini içeren JSON path'i (örn: "data.webinars", "data.blog").
     */
    public static void getAndPrintModuleCount(String moduleEndpoint, String jsonPathToList) {
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
        // Bazı endpoint'ler (örn: courses) doğrudan toplam sayıyı verebilir, bu daha verimlidir.
        if (moduleEndpoint.equals("courses") && jsonPath.get("data.totalWebinars") != null) {
            count = jsonPath.getInt("data.totalWebinars");
        } else {
            // Diğerleri için listeyi çekip boyutunu alıyoruz.
            List<Object> list = jsonPath.getList(jsonPathToList);
            if (list != null) {
                count = list.size();
            }
        }

        // 7. Sonucu konsola yazdır
        System.out.println("✅ " + moduleEndpoint + " modülünde " + count + " kayıt bulunmaktadır.");
    }

    /**
     * Bu DataProvider, test edilecek modülleri ve bu modüllerin response'larındaki
     * kayıt listelerine ait JSON path'lerini içerir.
     */
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

    /**
     * DataProvider'dan aldığı verilerle her modül için kayıt sayısını kontrol eden test.
     */
    @Test(dataProvider = "moduleCountProvider")
    public void testAllModulesRecordCount(String module, String jsonPath) {
        getAndPrintModuleCount(module, jsonPath);
    }
}