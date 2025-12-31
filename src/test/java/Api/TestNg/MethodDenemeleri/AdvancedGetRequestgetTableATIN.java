package Api.TestNg.MethodDenemeleri;

import Api.Utilities.API_Methods;
import Api.Utilities.ExcelUtil;
import Api.Utilities.HooksAPI;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class AdvancedGetRequestgetTableATIN {

    /**
     * Bu metot, verilen bir modül endpoint'ine giderek verileri alır ve Excel'e yazar.
     * @param moduleEndpoint API'de sorgulanacak modülün adı (örn: "courses", "blogs").
     * @param jsonPathToList Response içinde kayıt listesini içeren JSON path'i.
     */
    public void getAndExportModuleData(String moduleEndpoint, String jsonPathToList) {
        // 1. Admin yetkisi ile token al ve base URL'i kur
        HooksAPI.setUpApi("admin");

        // 2. Path parametresini ayarla
        API_Methods.pathParam("api/" + moduleEndpoint);

        // 3. GET isteği gönder ve response'u al
        Response response = API_Methods.sendRequest("GET", null);

        // 4. Temel doğrulamaları yap
        API_Methods.statusCodeAssert(200);
        API_Methods.assertBody("remark", "success");

        // 5. Response'u JsonPath objesine çevir
        JsonPath jsonPath = response.jsonPath();

        // 6. Listeyi al
        List<Map<String, Object>> dataList = jsonPath.getList(jsonPathToList);

        if (dataList != null && !dataList.isEmpty()) {
            // 7. Excel dosyasını oluştur
            String projectPath = System.getProperty("user.dir");
            String folderPath = projectPath + File.separator + "ModulesTable";
            
            // Klasör yoksa oluştur
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String filePath = folderPath + File.separator + moduleEndpoint + ".xlsx";
            
            ExcelUtil.writeDataToExcel(filePath, moduleEndpoint, dataList);
        } else {
            System.out.println("⚠️ " + moduleEndpoint + " için veri bulunamadı veya liste boş.");
        }
    }

    @DataProvider(name = "moduleDataProvider")
    public Object[][] moduleDataProvider() {
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

    @Test(dataProvider = "moduleDataProvider")
    public void testExportModuleDataToExcel(String module, String jsonPath) {
        getAndExportModuleData(module, jsonPath);
    }
}