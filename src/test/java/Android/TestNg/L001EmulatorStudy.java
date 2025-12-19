package Android.TestNg;

import Android.Utilities.AndroidDriver;
import Android.Utilities.AppiumServerController;
import Android.Utilities.ReusableMethods;
import Android.Utilities.RunTimeEmulatorStarter;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class L001EmulatorStudy {

   public static final String AVD_NAME = "pixel_7_pro";


    @Test
    public void testEmulatorStartStop() {

        AppiumServerController.startServer();
        RunTimeEmulatorStarter.startEmulator(AVD_NAME);
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");

        ReusableMethods.wait(4);

        // Test senaryonuzu buraya ekleyebilirsiniz.
        driver.findElement(AppiumBy.accessibilityId("Profile")).click();


    }

}
