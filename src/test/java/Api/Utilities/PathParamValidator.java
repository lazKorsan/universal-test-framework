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
        validateInternal(jsonPath, "id", null);
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
            throw new IllegalArgumentException(
                    String.format("Expected value not found! ParamKey: %s, PathParams: %s",
                            paramKey, pathParams)
            );
        }

        logValidation(jsonPath, actualValue, expected);
        assertEquality(actualValue, expected);
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
            throw new IllegalArgumentException(
                    String.format("JSON Path '%s' not found in response. Error: %s",
                            jsonPath, e.getMessage())
            );
        }
    }

    private String normalizeJsonPath(String jsonPath) {
        if (!jsonPath.startsWith("$") && !jsonPath.startsWith("[") &&
                (jsonPath.contains(" ") || jsonPath.contains("-") || jsonPath.contains("."))) {
            return "['" + jsonPath + "']";
        }
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