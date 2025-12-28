package Android.TestNg.Junke;

import Android.Pages.MethodsPage;
import org.testng.annotations.Test;

public class mixedTest {

    MethodsPage methodsPage = new MethodsPage();

    @Test
    public void testtttt(){
        io.appium.java_client.android.AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();
        methodsPage.loginWithPhoneNumber();
        browserTest.browserTes2t();
        methodsPage.profileButton.click();
    }
}
