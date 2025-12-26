package Android.Utilities;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class AndroidTrigger {

    // Bu sınıf, TestNG testlerini tetiklemek için kullanılabilir.
    // Ancak, TestNG testleri doğrudan TestNG runner'ı tarafından tetiklenir.
    // Bu sınıfın özel bir işlevi yoktur, sadece bir yer tutucudur.


    @BeforeMethod
    public static void startServer() {

        RunTimeEmulatorStarter.startEmulator("pixel_7_pro");
        ReusableMethods.wait(12);
        AppiumServerController.startServer();

    }

    @AfterMethod
    public static void stopServer() {
        AppiumServerController.stopServer();
       // RunTimeEmulatorStarter.stopEmulator();

   }
}
