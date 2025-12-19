package Api.Utilities;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Arrays;
import java.util.HashMap;

import static hooks.HooksAPI.spec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class API_Methods {
    public static String fullPath;
    public static Response response;
    public static int id;

    public static HashMap<String, Object> responseMap;

    // endpoint olusturma
    public static void pathParam(String rawPaths) {//
        // Eğer içinde sayı yoksa → sabit endpoint → pathParam yapma
        if (!rawPaths.matches(".*\\d+.*")) {
            fullPath = "/" + rawPaths;
            System.out.println("[STATIC ENDPOINT] fullPath = " + fullPath);
            return;
        }//   api/categories

        String[] paths = rawPaths.split("/");  //  [api, categories]

        System.out.println(Arrays.toString(paths));  //  [api, categories]

        StringBuilder tempPath = new StringBuilder("/{");  // "/{pp0}/{pp1}" daha sonra oluşturulacak URL path şablonunu hazırla


        for (int i = 0; i < paths.length; i++) {

            String key = "pp" + i;  // pp1
            String value = paths[i].trim();  // categories

            spec.pathParam(key, value);   //   spec.pathParam("pp0", "api");   spec.pathParam("pp1", "categories");

            tempPath.append(key + "}/{");

            if (value.matches("\\d+")) {  // value.matches("\\d+") burada value rakam iceriyorsa dedik
                id = Integer.parseInt(value);
            }
        }
        tempPath.deleteCharAt(tempPath.lastIndexOf("/"));// fazlaliklari siliyoruz
        tempPath.deleteCharAt(tempPath.lastIndexOf("{"));

        fullPath = tempPath.toString();
        System.out.println("fullPath = " + fullPath); // Örnek fullPath = /{pp0}/{pp1}
        System.out.println("id : " + id);
    }


    // request gönderme
    public static Response sendRequest(String httpMethod, Object requestBody) {

        switch (httpMethod.toUpperCase()) {
            case "GET":
                if (requestBody != null) {
                    response = given()
                            .spec(spec)
                            .contentType(ContentType.JSON)
                            .when()
                            .body(requestBody)
                            .get(fullPath);
                } else {
                    response = given()
                            .spec(spec)
                            .when()
                            .get(fullPath);
                }
                break;
            case "POST":
                response = given()
                        .spec(spec)
                        .contentType(ContentType.JSON)
                        .when()
                        .body(requestBody)
                        .post(fullPath);
                break;
            case "PATCH":
                response = given()
                        .spec(spec)
                        .contentType(ContentType.JSON)
                        .when()
                        .body(requestBody)
                        .patch(fullPath);
                break;
            case "DELETE":
                response = given()
                        .spec(spec)
                        .when()
                        .delete(fullPath);
                break;
        }

        if (response != null) {
            response.prettyPrint();
        }

        return response;
    }


    // exception yakalama
    public static String tryCatchRequest(String httpMethod, Object requestBody) {
        String exceptionMesaj = null;
        try {
            switch (httpMethod.toUpperCase()) {
                case "GET":
                    if (requestBody != null) {
                        response = given()
                                .spec(spec)
                                .contentType(ContentType.JSON)
                                .when()
                                .body(requestBody)
                                .get(fullPath);
                    } else {
                        response = given()
                                .spec(spec)
                                .when()
                                .get(fullPath);
                    }
                    break;
                case "DELETE":
                    response = given()
                            .spec(spec)
                            .when()
                            .delete(fullPath);
                    break;
                case "PATCH":
                    response = given()
                            .spec(spec)
                            .contentType(ContentType.JSON)
                            .when()
                            .body(requestBody)
                            .patch(fullPath);
                    break;
            }
        } catch (Exception e) {
            exceptionMesaj = e.getMessage();
        }
        System.out.println("Exception Mesaj : " + exceptionMesaj);
        return exceptionMesaj;
    }

    // status code dogrulama
    public static void statusCodeAssert(int statusCode) {
        response.then()
                .assertThat()
                .statusCode(statusCode);
    }

    // body dogrulama
    public static void assertBody(String path, String value) {
        response.then()
                .assertThat()
                .body(path, equalTo(value));
    }

    // path param dogrulama
    public static void assertPathParam(String responseId) {
        JsonPath jsonPath = response.jsonPath();

        // Boşluk veya özel karakter varsa ['key'] şeklinde al
        int updatedId = jsonPath.getInt("['" + responseId + "']");

        System.out.println("Updated ID in response: " + updatedId);
        System.out.println("Expected ID: " + id);

        Assert.assertEquals("Response ID ile path param ID eşleşmiyor!", id, updatedId);
    }
}
