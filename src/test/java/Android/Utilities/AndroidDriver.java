package Android.Utilities;

import com.student.ConfigManager;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

public class AndroidDriver {

    private static final ThreadLocal<io.appium.java_client.android.AndroidDriver> driverPool = new ThreadLocal<>();

    public static io.appium.java_client.android.AndroidDriver getDriver() {
        if (driverPool.get() == null) {
            driverPool.set(createDriver());
        }
        return driverPool.get();
    }

    private static io.appium.java_client.android.AndroidDriver createDriver() {
        ConfigManager config = ConfigManager.getInstance();

        // APK yolunu dinamik olarak oluştur
        String appPath = config.get("android.app.path");
        if (appPath.contains("${user.dir}")) {
            appPath = appPath.replace("${user.dir}", System.getProperty("user.dir"));
        }

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(config.get("platformName", "Android"))
                .setDeviceName(config.get("android.device.name"))
                .setPlatformVersion(config.get("android.platform.version"))
                .setAutomationName(config.get("automationName", "UiAutomator2"))
                .setApp(appPath)
                .setNoReset(false) // Uygulama verilerini her testten önce temizle
                .setFullReset(false); // Uygulamayı her testten önce yeniden yükle

        URL url;
        try {
            url = new URL(config.get("appium.server.url"));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Appium sunucu URL'si geçersiz!", e);
        }

        io.appium.java_client.android.AndroidDriver driver = new io.appium.java_client.android.AndroidDriver(url, options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitTimeout()));
        return driver;
    }

    public static void closeDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }
}