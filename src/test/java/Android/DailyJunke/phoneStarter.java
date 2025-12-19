package Android.DailyJunke;

import Android.Utilities.AndroidDriver;
import Android.Utilities.ReusableMethods;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Android.DailyJunke.AppiumServerController.*;
import static Android.DailyJunke.EmulatorController.*;
import static Android.DailyJunke.EmulatorHelper.*;

public class phoneStarter {

    public static final String avdName = "Pixel_7_Pro";

    @Test
    public void tc01(){

        startServer();
        ReusableMethods.wait(2);
        //startEmulatorWithCleanState();
        //startEmulator();
        //isEmulatorRunning();
        //startEmulatorFromLastSession(avdName);
        //startEmulatorWithCleanAppData( avdName);
        startEmulatorFromSnapshot(avdName, "snapshot");
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");

        ReusableMethods.wait(4);





    }
}
