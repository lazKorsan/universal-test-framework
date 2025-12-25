package Api.Utilities;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Arrays;
import java.util.HashMap;

import static Api.Utilities.HooksAPI.spec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class API_Methods {
    public static String fullPath;
    public static Response response;
    public static int id;
    public static HashMap<String, Object> responseMap;
    private static PathParamValidator validator;

    // ==================== PATH PARAM METHODS ====================

    public static void pathParam(String rawPaths) {
        if (!rawPaths.matches(".*\\d+.*")) {
            fullPath = "/" + rawPaths;
            System.out.println("[STATIC ENDPOINT] fullPath = " + fullPath);
            return;
        }

        String[] paths = rawPaths.split("/");
        System.out.println("Path segments: " + Arrays.toString(paths));

        StringBuilder tempPath = new StringBuilder("/{");

        for (int i = 0; i < paths.length; i++) {
            String key = "pp" + i;
            String value = paths[i].trim();

            spec.pathParam(key, value);
            tempPath.append(key + "}/{");

            if (value.matches("\\d+")) {
                id = Integer.parseInt(value);
            }
        }

        tempPath.deleteCharAt(tempPath.lastIndexOf("/"));
        tempPath.deleteCharAt(tempPath.lastIndexOf("{"));

        fullPath = tempPath.toString();
        System.out.println("fullPath = " + fullPath);
        System.out.println("Extracted ID: " + id);

        // Validator'e path parametrelerini ekle
        initValidator();
        validator.extractParamsFromPath(rawPaths);
    }

    // ==================== REQUEST METHODS ====================

    public static Response sendRequest(String httpMethod, Object requestBody) {
        response = null;

        switch (httpMethod.toUpperCase()) {
            case "GET":
                response = buildRequest(requestBody).get(fullPath);
                break;
            case "POST":
                response = buildRequest(requestBody).post(fullPath);
                break;
            case "PATCH":
                response = buildRequest(requestBody).patch(fullPath);
                break;
            case "DELETE":
                response = buildRequest(requestBody).delete(fullPath);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        }

        if (response != null) {
            response.prettyPrint();
        }

        return response;
    }

    private static io.restassured.specification.RequestSpecification buildRequest(Object requestBody) {
        io.restassured.specification.RequestSpecification request = given().spec(spec);

        if (requestBody != null) {
            request.contentType(ContentType.JSON).body(requestBody);
        }

        return request;
    }

    // ==================== EXCEPTION HANDLING ====================

    public static String tryCatchRequest(String httpMethod, Object requestBody) {
        String exceptionMessage = null;
        try {
            switch (httpMethod.toUpperCase()) {
                case "GET":
                case "DELETE":
                case "PATCH":
                    sendRequest(httpMethod, requestBody);
                    break;
                default:
                    throw new IllegalArgumentException("Method not supported in tryCatch: " + httpMethod);
            }
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        System.out.println("Exception Message: " + exceptionMessage);
        return exceptionMessage;
    }

    // ==================== VALIDATION METHODS ====================

    public static void statusCodeAssert(int statusCode) {
        response.then().assertThat().statusCode(statusCode);
    }

    public static void assertBody(String path, Object value) {
        response.then().assertThat().body(path, equalTo(value));
    }

    // ==================== PATH PARAM VALIDATION METHODS ====================

    // Validator initialization
    private static void initValidator() {
        if (validator == null) {
            validator = new PathParamValidator();
        }
    }

    public static void setupValidator(Response response) {
        initValidator();
        validator.setResponse(response);
    }

    public static void setupValidator() {
        initValidator();
        if (response != null) {
            validator.setResponse(response);
        }
    }

    // Manual param management
    public static void addPathParam(String key, Object value) {
        initValidator();
        validator.addParam(key, value);
    }

    public static Object getPathParam(String key) {
        checkValidator();
        return validator.getParam(key);
    }

    // Validation methods using validator
    public static void assertPathParam(String jsonPath, String paramKey) {
        checkValidatorAndResponse();
        validator.validate(jsonPath, paramKey);
    }

    public static void assertPathParam(String jsonPath) {
        checkValidatorAndResponse();
        validator.validate(jsonPath);
    }

    public static void assertPathParam(String jsonPath, Object expectedValue) {
        checkValidatorAndResponse();
        validator.validate(jsonPath, expectedValue);
    }

    // Specialized validation methods
    public static void assertDeletedId() {
        checkValidatorAndResponse();
        validator.validateDeletedId();
    }

    public static void assertCreatedId() {
        checkValidatorAndResponse();
        validator.validateCreatedId();
    }

    public static void assertUpdatedId() {
        checkValidatorAndResponse();
        validator.validateUpdatedId();
    }

    // Legacy method (for backward compatibility)
    public static void assertDeletedIdLegacy() {
        JsonPath jsonPath = response.jsonPath();
        String deletedId = jsonPath.getString("['Deleted Course ID']");

        System.out.println("Deleted Course ID in response: " + deletedId);
        System.out.println("Expected ID from path param: " + id);

        Assert.assertEquals("Deleted ID doesn't match!",
                String.valueOf(id), deletedId);
    }

    public static void assertPathParamLegacy(String jsonKey, Object expectedValue) {
        JsonPath jsonPath = response.jsonPath();

        String jsonPathExpr = jsonKey.contains(" ") || jsonKey.contains("-") || jsonKey.contains(".")
                ? "['" + jsonKey + "']"
                : jsonKey;

        Object actualValue = jsonPath.get(jsonPathExpr);

        System.out.println("Response value (" + jsonKey + "): " + actualValue);
        System.out.println("Expected value: " + expectedValue);

        Assert.assertEquals(jsonKey + " value doesn't match!",
                String.valueOf(expectedValue), String.valueOf(actualValue));
    }

    // Private helper methods
    private static void checkValidator() {
        if (validator == null) {
            throw new IllegalStateException("Validator not initialized! Call setupValidator() first.");
        }
    }

    private static void checkValidatorAndResponse() {
        checkValidator();
        if (response == null) {
            throw new IllegalStateException("Response is null! Send a request first.");
        }
        if (validator.getParam("id") == null && id == 0) {
            System.out.println("⚠️ Warning: No ID found in path params or extracted ID.");
        }
    }

    // API_Methods.java'ya ekle:
    public static void assertDeletedCourseId() {
        JsonPath jsonPath = response.jsonPath();

        // Doğrudan JsonPath ile al (boşluklu key için ['key'] syntax)
        String deletedId = jsonPath.getString("['Deleted Course ID']");

        System.out.println("Deleted Course ID in response: " + deletedId);
        System.out.println("Expected ID: " + id);

        Assert.assertEquals("Deleted Course ID doesn't match!",
                String.valueOf(id), deletedId);
    }
}