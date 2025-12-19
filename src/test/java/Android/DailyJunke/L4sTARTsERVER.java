package Android.DailyJunke;

import Android.Utilities.AndroidDriver;
import Android.Utilities.AppiumServerController;
import Android.Utilities.ReusableMethods;
import Android.Utilities.RunTimeEmulatorStarter;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class L4sTARTsERVER {

    @Test
    public void testStartServer() {
        AppiumServerController.startServer();
        //ReusableMethods.wait(3);

        RunTimeEmulatorStarter.startEmulator("pixel_7_pro");

        ReusableMethods.wait(6);

        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");

        ReusableMethods.wait(4);

        // Test senaryonuzu buraya ekleyebilirsiniz.
        driver.findElement(AppiumBy.accessibilityId("Profile")).click();




    }
}
