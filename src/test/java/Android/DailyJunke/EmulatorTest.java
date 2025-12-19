package Android.DailyJunke;


import org.testng.annotations.*;

import static Android.DailyJunke.AppiumServerController.startServer;

public class EmulatorTest {

    private static final String AVD_NAME = "Pixel_7_Pro";
    private static final String DEVICE_ID = "emulator-5554";
    private static final String APP_PACKAGE = "QueryCart";

    @BeforeSuite
    public void setup() {
        // Mevcut emülatörleri listele
        EmulatorHelper.listAvailableEmulators();

        // Tüm emülatörleri kapat (temiz başlangıç)
        EmulatorHelper.killAllEmulators();
    }

    @Test
    public void testScenario1_LastSession() {
        startServer();

        // 1. Önceki oturumdan başlat
        boolean started = EmulatorHelper.startEmulatorFromLastSession(AVD_NAME);
        if (started) {
            // Boot'u bekle
            EmulatorHelper.waitForBootComplete(DEVICE_ID);

            // Testlerini yap...
        }
    }

    @Test
    public void testScenario2_CleanAppData() {

        startServer();

        // 2. Uygulama verilerini silerek başlat (ana ekrandan)
        boolean started = EmulatorHelper.startEmulatorWithCleanAppData(AVD_NAME);
        if (started && EmulatorHelper.waitForBootComplete(DEVICE_ID)) {
            // Ana ekrana git
            EmulatorHelper.goToHomeScreen(DEVICE_ID);

            // Uygulama verilerini temizle
            EmulatorHelper.clearAppData(DEVICE_ID, APP_PACKAGE);

            // Testlerini yap...
        }
    }

    @Test
    public void testScenario3_FactoryReset() {
        // 3. Fabrika ayarlarına döndürerek başlat
        boolean started = EmulatorHelper.startEmulatorWithFactoryReset(AVD_NAME);
        if (started) {
            EmulatorHelper.waitForBootComplete(DEVICE_ID);
            // İlk kurulum ekranında kalır, manual işlem gerekebilir
        }
    }

    @Test
    public void testScenario4_CompletelyClean() {
        // 4. En temiz başlatma (test öncesi ideal)
        boolean started = EmulatorHelper.startEmulatorCompletelyClean(AVD_NAME);
        if (started) {
            EmulatorHelper.waitForBootComplete(DEVICE_ID);
            // Temiz ortamda testlerini yap...
        }
    }

    @AfterMethod
    public void afterTest() {
        // Her testten sonra emülatörü kapat
        EmulatorHelper.killEmulator();
    }

    @AfterSuite
    public void tearDown() {
        // Suite bitince tüm emülatörleri kapat
        EmulatorHelper.killAllEmulators();
    }
}