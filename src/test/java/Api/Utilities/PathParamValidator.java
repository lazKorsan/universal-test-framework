package Api.Utilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathParamValidator {

    private final Map<String, Object> pathParams = new HashMap<>();
    private Response response;

    // Constructor'lar
    public PathParamValidator() {}

    public PathParamValidator(Response response) {
        this.response = response;
    }

    // Factory method
    public static PathParamValidator withResponse(Response response) {
        return new PathParamValidator(response);
    }

    // Main validation methods
    public void validate(String jsonPath, String paramKey) {
        validateInternal(jsonPath, paramKey, null);
    }

    public void validate(String jsonPath) {
        // Eğer jsonPath bir key ise ve değer kontrolü yapılmıyorsa, sadece varlığını kontrol et
        // Ancak mevcut yapı değer karşılaştırması üzerine kurulu.
        // Eğer sadece varlık kontrolü isteniyorsa (null check), bu metot güncellenmeli.
        // Şimdilik "id" parametresi ile karşılaştırma yapıyor gibi görünüyor.
        
        // Eğer jsonPath "data.teacher_id" gibi bir şeyse ve paramKey verilmemişse,
        // muhtemelen sadece null olmadığını kontrol etmek istiyoruzdur.
        // Ancak validateInternal metodu expectedValue veya paramKey bekliyor.
        
        // Bu metodu "varlık kontrolü" (assertion for existence) olarak değiştirelim mi?
        // Kullanıcı kodunda API_Methods.assertPathParam("data.teacher_id") şeklinde çağrılıyor.
        // Bu durumda değerin null olmadığını kontrol etmesi daha mantıklı.
        
        validateExistence(jsonPath);
    }

    public void validate(String jsonPath, Object expectedValue) {
        validateInternal(jsonPath, null, expectedValue);
    }

    public void validateDeletedId() {
        validateInternal("['Deleted Course ID']", "id", null);
    }

    public void validateCreatedId() {
        validateInternal("id", "id", null);
    }

    public void validateUpdatedId() {
        validateInternal("['Updated ID']", "id", null);
    }

    // Core validation logic
    private void validateInternal(String jsonPath, String paramKey, Object expectedValue) {
        checkPreconditions();

        Object actualValue = extractFromResponse(jsonPath);
        Object expected = (expectedValue != null) ? expectedValue : pathParams.get(paramKey);

        if (expected == null) {
            // Eğer expected value yoksa ve paramKey de bulunamadıysa hata fırlat
            throw new IllegalArgumentException(
                    String.format("Expected value not found! ParamKey: %s, PathParams: %s",
                            paramKey, pathParams)
            );
        }

        logValidation(jsonPath, actualValue, expected);
        assertEquality(actualValue, expected);
    }
    
    // Yeni metot: Sadece varlık kontrolü (null olmama)
    private void validateExistence(String jsonPath) {
        checkPreconditions();
        Object actualValue = extractFromResponse(jsonPath);
        
        System.out.println("=".repeat(50));
        System.out.println("EXISTENCE CHECK:");
        System.out.println("JSON Path: " + jsonPath);
        System.out.println("Actual Value: " + actualValue);
        System.out.println("=".repeat(50));
        
        Assert.assertNotNull("JSON Path '" + jsonPath + "' returned null!", actualValue);
    }

    // Utility methods
    public void extractParamsFromPath(String path) {
        Pattern pattern = Pattern.compile("/(\\d+)");
        Matcher matcher = pattern.matcher(path);

        int index = 0;
        while (matcher.find()) {
            String value = matcher.group(1);
            String key = "param" + index;

            try {
                int intValue = Integer.parseInt(value);
                pathParams.put(key, intValue);
                pathParams.put("id", intValue);

                System.out.printf("✓ Extracted path param [%s]: %s%n", key, value);
                index++;
            } catch (NumberFormatException e) {
                pathParams.put(key, value);
                pathParams.put("id", value);
            }
        }

        // URL'deki son segment'i de sakla
        String[] segments = path.split("/");
        if (segments.length > 0) {
            String lastSegment = segments[segments.length - 1];
            if (!lastSegment.isEmpty()) {
                pathParams.put("lastSegment", lastSegment);
            }
        }
    }

    public void addParam(String key, Object value) {
        pathParams.put(key, value);
    }

    public Object getParam(String key) {
        return pathParams.get(key);
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Map<String, Object> getAllParams() {
        return new HashMap<>(pathParams);
    }

    // Private helper methods
    private Object extractFromResponse(String jsonPath) {
        JsonPath jsonPathObj = response.jsonPath();
        String normalizedPath = normalizeJsonPath(jsonPath);

        try {
            return jsonPathObj.get(normalizedPath);
        } catch (Exception e) {
            // Hata durumunda orijinal path ile de deneyelim
            try {
                return jsonPathObj.get(jsonPath);
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        String.format("JSON Path '%s' (normalized: '%s') not found in response. Error: %s",
                                jsonPath, normalizedPath, e.getMessage())
                );
            }
        }
    }

    private String normalizeJsonPath(String jsonPath) {
        // Eğer path zaten $ veya [ ile başlıyorsa dokunma
        if (jsonPath.startsWith("$") || jsonPath.startsWith("[")) {
            return jsonPath;
        }
        
        // Eğer path içinde nokta (.) varsa, muhtemelen standart bir JSON path'tir (örn: data.id)
        // Bu durumda dokunmamak en iyisi.
        if (jsonPath.contains(".")) {
            return jsonPath;
        }

        // Eğer path içinde boşluk veya tire varsa ve nokta yoksa, ['...'] içine al
        // Örn: "Deleted Course ID" -> "['Deleted Course ID']"
        if (jsonPath.contains(" ") || jsonPath.contains("-")) {
            return "['" + jsonPath + "']";
        }
        
        // Diğer durumlarda olduğu gibi döndür
        return jsonPath;
    }

    private void logValidation(String jsonPath, Object actual, Object expected) {
        System.out.println("=".repeat(50));
        System.out.println("PATH PARAM VALIDATION:");
        System.out.println("JSON Path: " + jsonPath);
        System.out.println("Actual Value: " + actual);
        System.out.println("Expected Value: " + expected);
        System.out.println("=".repeat(50));
    }

    private void assertEquality(Object actual, Object expected) {
        String actualStr = String.valueOf(actual);
        String expectedStr = String.valueOf(expected);

        Assert.assertEquals(
                String.format("Path param validation failed! Actual: %s, Expected: %s", actualStr, expectedStr),
                expectedStr,
                actualStr
        );
    }

    private void checkPreconditions() {
        if (response == null) {
            throw new IllegalStateException("Response is null! Call setResponse() first.");
        }
    }
}