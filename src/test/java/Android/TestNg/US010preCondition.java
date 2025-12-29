package Android.TestNg;

import Android.Pages.MethodsPage;
import org.testng.annotations.Test;

public class US010preCondition {
    MethodsPage methodsPage = new MethodsPage();
    // US010 için ön koşul olarak bir sipariş oluşturulur.
    // Bu sipariş daha sonra US010 testinde kullanılacaktır.
    @Test
    public void createOrderForUS010() {
        io.appium.java_client.android.AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();

        methodsPage.signupWithEMail();
        methodsPage.addToAddress();
        methodsPage.createOrder();
    }

    @Test
    public void testUS010() {

    }

}
