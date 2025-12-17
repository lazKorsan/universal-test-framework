package Android.TestNg;

import Android.Utilities.AndroidDriver;
import com.student.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FirstAndroidTest extends BaseTest {

    @Test
    public void testAppiumDriver() {
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");

        // Test senaryonuzu buraya ekleyebilirsiniz.
        // Örneğin: driver.findElement(AppiumBy.accessibilityId("Login Screen")).click();

        System.out.println("   📱 AndroidDriver başarıyla başlatıldı.");
        System.out.println("   -> Oturum ID: " + driver.getSessionId());
        System.out.println("   -> Cihaz Bilgileri: " + driver.getCapabilities().getCapability("deviceName"));
    }
}