package Android.TestNg.Junke;

import Android.Pages.MethodsPage;
import org.testng.annotations.Test;

public class testbozuldu {

    @Test
    public void testBozuldu(){
        MethodsPage methodsPage = new MethodsPage();
        io.appium.java_client.android.AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();

        methodsPage.signupWithEMail();
        methodsPage.addToAddress();
        methodsPage.createOrder();


    }
}
