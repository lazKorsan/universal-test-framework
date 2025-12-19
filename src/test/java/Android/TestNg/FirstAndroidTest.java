package Android.TestNg;

import Android.Utilities.AndroidDriver;
import Android.Utilities.ReusableMethods;
import com.student.BaseTest;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FirstAndroidTest extends BaseTest {

    @Test
    public void testAppiumDriver() {
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");

        ReusableMethods.wait(4);

        // Test senaryonuzu buraya ekleyebilirsiniz.
       driver.findElement(AppiumBy.accessibilityId("Profile")).click();
       //     OptionsMet.clickButtonByDescription("Sign In");
        //

        System.out.println("   📱 AndroidDriver başarıyla başlatıldı.");
        System.out.println("   -> Oturum ID: " + driver.getSessionId());
        System.out.println("   -> Cihaz Bilgileri: " + driver.getCapabilities().getCapability("deviceName"));
    }
}