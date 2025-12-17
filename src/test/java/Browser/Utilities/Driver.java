package Browser.Utilities;

import com.student.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Driver {

    private static final ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> waitPool = new ThreadLocal<>();

    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE,
        SAFARI,
        CHROME_HEADLESS,
        FIREFOX_HEADLESS
    }

    public static WebDriver getDriver() {
        return getDriver(null);
    }

    public static WebDriver getDriver(BrowserType browserType) {
        if (driverPool.get() == null) {
            if (browserType == null) {
                String browserFromConfig = ConfigManager.getInstance().get("browser", "chrome").toUpperCase();
                try {
                    browserType = BrowserType.valueOf(browserFromConfig);
                } catch (IllegalArgumentException e) {
                    browserType = BrowserType.CHROME;
                }
            }
            driverPool.set(createDriver(browserType));
        }
        return driverPool.get();
    }

    public static WebDriverWait getWait(long seconds) {
        if (waitPool.get() == null) {
            waitPool.set(new WebDriverWait(getDriver(), Duration.ofSeconds(seconds)));
        }
        return waitPool.get();
    }

    private static WebDriver createDriver(BrowserType browserType) {
        WebDriver driver;
        switch (browserType) {
            case FIREFOX -> driver = createFirefoxDriver(false);
            case FIREFOX_HEADLESS -> driver = createFirefoxDriver(true);
            case EDGE -> driver = createEdgeDriver();
            case SAFARI -> {
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
            }
            case CHROME_HEADLESS -> driver = createChromeDriver(true);
            default -> driver = createChromeDriver(false);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--start-maximized", "--disable-notifications", "--disable-popup-blocking",
                "--disable-infobars", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");

        String chromeArgs = ConfigManager.getInstance().get("chrome.args", "");
        if (!chromeArgs.isEmpty()) {
            options.addArguments(chromeArgs.split(","));
        }

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory",
                ConfigManager.getInstance().get("download.path", System.getProperty("user.home") + "/Downloads"));
        options.setExperimentalOption("prefs", prefs);

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--width=1920", "--height=1080");
        options.addPreference("dom.webnotifications.enabled", false);
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        return new EdgeDriver();
    }

    public static void closeDriver() {
        WebDriver driver = driverPool.get();
        if (driver != null) {
            driver.quit();
            driverPool.remove();
        }
        if (waitPool.get() != null) {
            waitPool.remove();
        }
    }
}