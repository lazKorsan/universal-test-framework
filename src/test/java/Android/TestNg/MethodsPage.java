package Android.TestNg;

import Android.Pages.QueryCardPage;
import Android.Utilities.AppiumServerController;
import Android.Utilities.ReusableMethods;
import Android.Utilities.RunTimeEmulatorStarter;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MethodsPage {


    private io.appium.java_client.android.AndroidDriver driver;




    @Test
    public void methodKolCalismasiWithTestNG(){

        System.out.println("🚀 Appium server başlatılıyor...");
        AppiumServerController.startServer();

        System.out.println("📱 Emülatör başlatılıyor...");
        RunTimeEmulatorStarter.startEmulator("pixel_7_pro");

        System.out.println("⏳ Boot bekleniyor (45 saniye)...");
        ReusableMethods.wait(12);

        System.out.println("🔧 AndroidDriver başlatılıyor...");

        // DEĞİŞİKLİK BURADA: Lokal değişken DEĞİL, class field'ına ata!
        this.driver = Android.Utilities.AndroidDriver.getDriver();
        Assert.assertNotNull(this.driver, "AndroidDriver başlatılamadı!");

        System.out.println("✅ Driver başlatıldı:");
        System.out.println("   Session ID: " + this.driver.getSessionId());

        ReusableMethods.wait(4);

        // Test
        this.driver.findElement(AppiumBy.accessibilityId("Profile")).click();

        System.out.println("🎉 Uygulama başarıyla açıldı!");










    }



}
