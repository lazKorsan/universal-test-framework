package Api.TestNg.MethodDenemeleri;

import Api.Utilities.API_Methods;
import Api.Utilities.HooksAPI;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class VerifyResponseBody {
    
    public static void verifyResponseBody(String moduleName, int id){
        
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

        // 6. Modüle göre response body alanlarını doğrula
        switch (moduleName) {
            case "course":
                // teacher_id, creator_id, category_id, type, private, slug, start_date, duration, timezone
                API_Methods.assertPathParam("data.teacher_id");
                API_Methods.assertPathParam("data.creator_id");
                API_Methods.assertPathParam("data.category_id");
                API_Methods.assertPathParam("data.type");
                API_Methods.assertPathParam("data.private");
                API_Methods.assertPathParam("data.slug");
                // start_date null gelebilir, bu yüzden kontrolü kaldırıyoruz
                // API_Methods.assertPathParam("data.start_date"); 
                API_Methods.assertPathParam("data.duration");
                API_Methods.assertPathParam("data.timezone");
                break;

            case "category":
                // slug, parent_id, icon, order, title, id, category_id, locale, title
                API_Methods.assertPathParam("data.slug");
                // parent_id null gelebilir
                // API_Methods.assertPathParam("data.parent_id");
                API_Methods.assertPathParam("data.icon");
                API_Methods.assertPathParam("data.order");
                API_Methods.assertPathParam("data.id");
                
                // Title translations içinde
                API_Methods.assertPathParam("data.translations[0].title");
                API_Methods.assertPathParam("data.translations[0].category_id");
                API_Methods.assertPathParam("data.translations[0].locale");
                break;

            case "pricePlan":
                // creator_id, webinar_id, bundle_id, start_date, end_date, discount, capacity, order, created_at, updated_at, deleted_at, title, id, ticket_id, locale, title
                API_Methods.assertPathParam("data.creator_id");
                API_Methods.assertPathParam("data.webinar_id");
                // bundle_id null gelebilir
                // API_Methods.assertPathParam("data.bundle_id");
                API_Methods.assertPathParam("data.start_date");
                API_Methods.assertPathParam("data.end_date");
                API_Methods.assertPathParam("data.discount");
                API_Methods.assertPathParam("data.capacity");
                // order null gelebilir (örnekte null)
                // API_Methods.assertPathParam("data.order");
                API_Methods.assertPathParam("data.created_at");
                // updated_at ve deleted_at null gelebilir
                // API_Methods.assertPathParam("data.updated_at");
                // API_Methods.assertPathParam("data.deleted_at");
                API_Methods.assertPathParam("data.id");
                
                // Title translations içinde
                API_Methods.assertPathParam("data.translations[0].title");
                API_Methods.assertPathParam("data.translations[0].ticket_id");
                API_Methods.assertPathParam("data.translations[0].locale");
                break;

            case "coursefaq":
                // creator_id, webinar_id, bundle_id, upcoming_course_id, order, created_at, updated_at, title, answer
                API_Methods.assertPathParam("data.creator_id");
                API_Methods.assertPathParam("data.webinar_id");
                // bundle_id null gelebilir
                // API_Methods.assertPathParam("data.bundle_id");
                // upcoming_course_id null gelebilir
                // API_Methods.assertPathParam("data.upcoming_course_id");
                // order null gelebilir
                // API_Methods.assertPathParam("data.order");
                API_Methods.assertPathParam("data.created_at");
                API_Methods.assertPathParam("data.updated_at");
                
                // Title ve Answer translations içinde
                API_Methods.assertPathParam("data.translations[0].title");
                API_Methods.assertPathParam("data.translations[0].answer");
                break;

            case "product":
                // creator_id, type, slug, category_id, price, point, unlimited_inventory, ordering, inventory, id, product_id, locale, title, seo_description, summary, description
                API_Methods.assertPathParam("data.creator_id");
                API_Methods.assertPathParam("data.type");
                API_Methods.assertPathParam("data.slug");
                API_Methods.assertPathParam("data.category_id");
                API_Methods.assertPathParam("data.price");
                API_Methods.assertPathParam("data.point");
                API_Methods.assertPathParam("data.unlimited_inventory");
                API_Methods.assertPathParam("data.ordering");
                API_Methods.assertPathParam("data.inventory");
                API_Methods.assertPathParam("data.id");
                
                // Translations içindeki alanlar
                API_Methods.assertPathParam("data.translations[0].product_id");
                API_Methods.assertPathParam("data.translations[0].locale");
                API_Methods.assertPathParam("data.translations[0].title");
                API_Methods.assertPathParam("data.translations[0].seo_description");
                API_Methods.assertPathParam("data.translations[0].summary");
                API_Methods.assertPathParam("data.translations[0].description");
                break;

            case "productCategory":
                // parent_id, icon, order, title, id, product_category_id, locale, title
                // parent_id null gelebilir
                // API_Methods.assertPathParam("data.parent_id");
                API_Methods.assertPathParam("data.icon");
                // order null gelebilir
                // API_Methods.assertPathParam("data.order");
                API_Methods.assertPathParam("data.id");
                
                // Translations içindeki alanlar
                API_Methods.assertPathParam("data.translations[0].product_category_id");
                API_Methods.assertPathParam("data.translations[0].locale");
                API_Methods.assertPathParam("data.translations[0].title");
                break;

            case "productfaq":
                // creator_id, product_id, order, created_at, title, answer, id, product_faq_id, locale, title, answer
                API_Methods.assertPathParam("data.creator_id");
                API_Methods.assertPathParam("data.product_id");
                API_Methods.assertPathParam("data.order");
                API_Methods.assertPathParam("data.created_at");
                API_Methods.assertPathParam("data.id");
                break;

            case "blog":
                // category_id, author_id, slug, image, visit_count, enable_comment, status, created_at, updated_at, comments_count
                API_Methods.assertPathParam("data.category_id");
                API_Methods.assertPathParam("data.author_id");
                API_Methods.assertPathParam("data.slug");
                API_Methods.assertPathParam("data.image");
                API_Methods.assertPathParam("data.visit_count");
                API_Methods.assertPathParam("data.enable_comment");
                API_Methods.assertPathParam("data.status");
                API_Methods.assertPathParam("data.created_at");
                API_Methods.assertPathParam("data.updated_at");
                break;

            case "blogCategory":
                // title, slug, blog_count
                API_Methods.assertPathParam("data.slug");
                API_Methods.assertPathParam("data.title"); 
                break;

            case "coupon":
                // creator_id, title, discount_type, source, code, percent, amount, max_amount, minimum_order, count, user_type, product_type, for_first_purchase, status, expired_at, created_at
                API_Methods.assertPathParam("data.creator_id");
                API_Methods.assertPathParam("data.title");
                API_Methods.assertPathParam("data.discount_type");
                API_Methods.assertPathParam("data.source");
                API_Methods.assertPathParam("data.code");
                API_Methods.assertPathParam("data.percent");
                API_Methods.assertPathParam("data.amount");
                API_Methods.assertPathParam("data.max_amount");
                API_Methods.assertPathParam("data.minimum_order");
                API_Methods.assertPathParam("data.count");
                API_Methods.assertPathParam("data.user_type");
                API_Methods.assertPathParam("data.product_type");
                API_Methods.assertPathParam("data.for_first_purchase");
                API_Methods.assertPathParam("data.status");
                API_Methods.assertPathParam("data.expired_at");
                API_Methods.assertPathParam("data.created_at");
                break;

            case "support":
                // user_id, webinar_id, department_id, title, status, created_at, updated_at, status_order, id, full_name, role_name
                API_Methods.assertPathParam("data.user_id");
                // webinar_id null gelebilir
                // API_Methods.assertPathParam("data.webinar_id");
                API_Methods.assertPathParam("data.department_id");
                API_Methods.assertPathParam("data.title");
                API_Methods.assertPathParam("data.status");
                API_Methods.assertPathParam("data.created_at");
                API_Methods.assertPathParam("data.updated_at");
                API_Methods.assertPathParam("data.status_order");
                API_Methods.assertPathParam("data.id");
                
                // User objesi içindeki alanlar
                API_Methods.assertPathParam("data.user.full_name");
                API_Methods.assertPathParam("data.user.role_name");
                break;

            case "department":
                // created_at, supports_count, title, id, support_department_id, locale, title
                API_Methods.assertPathParam("data.created_at");
                API_Methods.assertPathParam("data.id");
                
                // Translations içindeki alanlar
                API_Methods.assertPathParam("data.translations[0].support_department_id");
                API_Methods.assertPathParam("data.translations[0].locale");
                API_Methods.assertPathParam("data.translations[0].title");
                break;

            case "contact":
                // name, email, phone, subject, message, reply, status, created_at
                API_Methods.assertPathParam("data.name");
                API_Methods.assertPathParam("data.email");
                API_Methods.assertPathParam("data.phone");
                API_Methods.assertPathParam("data.subject");
                API_Methods.assertPathParam("data.message");
                // reply null gelebilir
                // API_Methods.assertPathParam("data.reply");
                API_Methods.assertPathParam("data.status");
                API_Methods.assertPathParam("data.created_at");
                break;

            case "badge":
                // image, type, from, to, score, created_at, title, description, id, badge_id, locale, title, description
                API_Methods.assertPathParam("data.image");
                API_Methods.assertPathParam("data.type");
                API_Methods.assertPathParam("data.score");
                API_Methods.assertPathParam("data.created_at");
                API_Methods.assertPathParam("data.id");
                
                // Condition objesi içindeki alanlar
                API_Methods.assertPathParam("data.condition.from");
                API_Methods.assertPathParam("data.condition.to");
                
                // Translations içindeki alanlar
                API_Methods.assertPathParam("data.translations[0].badge_id");
                API_Methods.assertPathParam("data.translations[0].locale");
                API_Methods.assertPathParam("data.translations[0].title");
                API_Methods.assertPathParam("data.translations[0].description");
                break;

            default:
                System.out.println("Module not found: " + moduleName);
                break;
        }

        System.out.println("Module: " + moduleName + " ID: " + id + " - Response Body Verified.");
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
    public void testVerifyResponseBody(String module, int id) {
        verifyResponseBody(module, id);
    }
}