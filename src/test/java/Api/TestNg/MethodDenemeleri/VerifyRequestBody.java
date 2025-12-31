package Api.TestNg.MethodDenemeleri;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class VerifyRequestBody {

    public static void verifyRequestBody(String moduleName, int id) {
        
        // 1. Admin yetkisi ile token al ve base URL'i kur
        HooksAPI.setUpApi("admin");

        // 2. Path parametresini ayarla (api/moduleName/id)
        API_Methods.pathParam("api/" + moduleName + "/" + id);

        // 3. GET isteği gönder
        API_Methods.sendRequest("GET", null);

        // 4. Status code'un 200 olduğunu doğrula
        API_Methods.statusCodeAssert(200);

        // 5. Remark bilgisinin "success" olduğunu doğrula
        API_Methods.assertBody("remark", "success");

        // 6. Modüle göre response body değerlerini doğrula
        switch (moduleName) {
            case "course":
                // ID: 1995
                API_Methods.assertBody("data.id", 1995);
                API_Methods.assertBody("data.teacher_id", 1016);
                API_Methods.assertBody("data.creator_id", 1016);
                API_Methods.assertBody("data.category_id", 611);
                API_Methods.assertBody("data.type", "course");
                API_Methods.assertBody("data.slug", "Become-a-Project-Manager");
                API_Methods.assertBody("data.duration", 150);
                API_Methods.assertBody("data.timezone", "America/New_York");
                API_Methods.assertBody("data.status", "active");
                API_Methods.assertBody("data.translations[0].title", "Become a Project Manager");
                break;

            case "category":
                // ID: 1180
                API_Methods.assertBody("data.id", 1180);
                API_Methods.assertBody("data.slug", "Online-Education-41");
                API_Methods.assertBody("data.order", 232);
                API_Methods.assertBody("data.translations[0].title", "Online Education");
                API_Methods.assertBody("data.translations[0].locale", "en");
                break;

            case "pricePlan":
                // ID: 413
                API_Methods.assertBody("data.id", 413);
                API_Methods.assertBody("data.creator_id", 1914);
                API_Methods.assertBody("data.webinar_id", 2002);
                API_Methods.assertBody("data.discount", 20);
                API_Methods.assertBody("data.capacity", 50);
                API_Methods.assertBody("data.created_at", 1764523251);
                API_Methods.assertBody("data.translations[0].title", "Test Price Plans");
                break;

            case "coursefaq":
                // ID: 14
                API_Methods.assertBody("data.id", 14);
                API_Methods.assertBody("data.creator_id", 1016);
                API_Methods.assertBody("data.webinar_id", 1995);
                API_Methods.assertBody("data.created_at", 1624908812);
                API_Methods.assertBody("data.updated_at", 1718059480);
                API_Methods.assertBody("data.translations[0].title", "What are the key features that differentiate your online learning platform from others? Updated");
                break;

            case "product":
                // ID: 5
                API_Methods.assertBody("data.id", 5);
                API_Methods.assertBody("data.creator_id", 1015);
                API_Methods.assertBody("data.type", "physical");
                API_Methods.assertBody("data.slug", "Business-Software");
                API_Methods.assertBody("data.category_id", 2);
                API_Methods.assertBody("data.price", 1990);
                API_Methods.assertBody("data.point", 3);
                API_Methods.assertBody("data.status", "active");
                API_Methods.assertBody("data.translations[0].title", "Updated Product Title");
                break;

            case "productCategory":
                // ID: 184
                API_Methods.assertBody("data.id", 184);
                API_Methods.assertBody("data.icon", "/store/1/default_images/categories_icons/code.png");
                API_Methods.assertBody("data.translations[0].title", "Educational Equipment");
                API_Methods.assertBody("data.translations[0].locale", "en");
                break;

            case "blog":
                // ID: 93
                API_Methods.assertBody("data.id", 93);
                API_Methods.assertBody("data.category_id", 34);
                API_Methods.assertBody("data.author_id", 1914);
                API_Methods.assertBody("data.slug", "The-Growing-Impact-of-Online-Education-46");
                API_Methods.assertBody("data.visit_count", 6);
                API_Methods.assertBody("data.status", "publish");
                API_Methods.assertBody("data.translations[0].title", "The Growing Impact of Online Education");
                break;

            case "blogCategory":
                // ID: 34
                API_Methods.assertBody("data.id", 34);
                API_Methods.assertBody("data.title", "Blog");
                API_Methods.assertBody("data.slug", "blog");
                break;

            case "coupon":
                // ID: 398
                API_Methods.assertBody("data.id", 398);
                API_Methods.assertBody("data.creator_id", 1914);
                API_Methods.assertBody("data.title", "Duplicate Code Coupon");
                API_Methods.assertBody("data.discount_type", "percentage");
                API_Methods.assertBody("data.source", "course");
                API_Methods.assertBody("data.code", "322");
                API_Methods.assertBody("data.percent", 10);
                API_Methods.assertBody("data.amount", 5);
                API_Methods.assertBody("data.status", "active");
                break;

            case "support":
                // ID: 28
                API_Methods.assertBody("data.id", 28);
                API_Methods.assertBody("data.user_id", 923);
                API_Methods.assertBody("data.department_id", 3);
                API_Methods.assertBody("data.title", "Commission Rate");
                API_Methods.assertBody("data.status", "open");
                API_Methods.assertBody("data.user.full_name", "Sawyer Emerson");
                API_Methods.assertBody("data.user.role_name", "teacher");
                break;

            case "department":
                // ID: 57
                API_Methods.assertBody("data.id", 57);
                API_Methods.assertBody("data.created_at", 1760805065);
                API_Methods.assertBody("data.translations[0].title", "Technical Office");
                API_Methods.assertBody("data.translations[0].locale", "en");
                break;

            case "contact":
                // ID: 41
                API_Methods.assertBody("data.id", 41);
                API_Methods.assertBody("data.name", "NIHAD");
                API_Methods.assertBody("data.email", "farajovnihad@gmail.com");
                API_Methods.assertBody("data.phone", "57193829");
                API_Methods.assertBody("data.subject", "dd");
                API_Methods.assertBody("data.message", "ff");
                API_Methods.assertBody("data.status", "pending");
                break;

            case "badge":
                // ID: 22
                API_Methods.assertBody("data.id", 22);
                API_Methods.assertBody("data.type", "register_date");
                API_Methods.assertBody("data.score", 10);
                API_Methods.assertBody("data.condition.from", "31");
                API_Methods.assertBody("data.condition.to", "365");
                API_Methods.assertBody("data.translations[0].title", "Loyal User");
                API_Methods.assertBody("data.translations[0].description", "1 year of Membership");
                break;

            default:
                System.out.println("Module not found: " + moduleName);
                break;
        }

        System.out.println("Module: " + moduleName + " ID: " + id + " - Response Body Values Verified.");
    }

    @DataProvider(name = "moduleProvider")
    public Object[][] moduleProvider() {
        return new Object[][] {
            {"course", 1995},
            {"category", 1180},
            {"pricePlan", 413},
            {"coursefaq", 14},
            {"product", 5},
            {"productCategory", 184},
            // {"productfaq", 8}, // Örnek response olmadığı için geçici olarak kapattım
            {"blog", 93},
            {"blogCategory", 34},
            {"coupon", 398},
            {"support", 28},
            {"department", 57},
            {"contact", 41},
            {"badge", 22}
        };
    }

    @Test(dataProvider = "moduleProvider")
    public void testVerifyRequestBody(String module, int id) {
        verifyRequestBody(module, id);
    }
}