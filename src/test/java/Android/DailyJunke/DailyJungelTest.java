package Android.DailyJunke;

import Android.Utilities.AndroidDriver;
import Android.Utilities.AppiumServerController;
import Android.Utilities.RunTimeEmulatorStarter;
import io.appium.java_client.AppiumBy;
import org.testng.annotations.Test;

public class DailyJungelTest {

    @Test
    public static void testStartServer() {

        AppiumServerController.startServer();
        RunTimeEmulatorStarter.startEmulator("pixel_7_pro");
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();

        QueryCardFlow flow = new QueryCardFlow(driver);
        flow.openAppAndClickProfile();
        driver.findElement(AppiumBy.accessibilityId("Profile")).click();

    }
}
