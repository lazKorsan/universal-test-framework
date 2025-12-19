package Api.Utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TestData{
    HashMap<String, Object> requestBody;

    public HashMap updateCategoryRequestBody() {

        requestBody = new HashMap<>();

        requestBody.put("title", "Education and Training");

        return requestBody;
    }


    public static Map<String,Object> expectedTranslationforProductCategory(){
        Map<String, Object> expectedTranslation = new HashMap<>();
        expectedTranslation.put("id", 72);
        expectedTranslation.put("product_category_id", 66);
        expectedTranslation.put("locale", "en");
        expectedTranslation.put("title", "Education and Training");

        return expectedTranslation;

    }
    public static Map<String,Object> expectedDataforProductCategory(){
        // Beklenen Ana Veri
        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("id", 66);
        expectedData.put("parent_id", null);
        expectedData.put("icon", "/store/1/default_images/categories_icons/code.png");
        expectedData.put("order", null);
        expectedData.put("title", null);
        expectedData.put("translations", Arrays.asList(expectedTranslationforProductCategory())); // List of Maps
        return expectedData;
    }

    public static Map<String,Object> expectedResponseMapforProductCategory(){
        // Beklenen Tüm Yanıt
        Map<String, Object> expectedResponseMap = new HashMap<>();
        expectedResponseMap.put("remark", "success");
        expectedResponseMap.put("status", 200);
        expectedResponseMap.put("data", expectedDataforProductCategory());

        return expectedResponseMap;
    }







}
