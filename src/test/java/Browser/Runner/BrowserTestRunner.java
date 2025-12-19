package Browser.Runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/java/Browser/features", // Browser feature dosyaları
        glue = {
                "Browser/Stepdefinitions",  // Browser step definitions
                "Browser/Hooks",            // Eğer Browser/Hooks klasörü oluşturursan
                "hooks"                     // Genel hooks
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/browser-report.html",
                "json:target/cucumber-reports/browser-report.json",
                "rerun:target/browser-rerun.txt",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" // Allure rapor için (isteğe bağlı)
        },
        monochrome = true,
        dryRun = false,
        tags = "@browser"               // Sadece browser testleri
)
public class BrowserTestRunner extends AbstractTestNGCucumberTests {

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("=== BROWSER TEST SUITE BAŞLIYOR ===");
        // Driver path'leri set edilebilir
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chrome.exe");
    }

    @BeforeClass
    public void setup() {
        System.out.println("=== BROWSER TESTİ BAŞLIYOR ===");
        System.out.println("Browser: " + System.getProperty("browser", "chrome"));
        System.out.println("URL: " + System.getProperty("url", "https://www.google.com"));

        // Ekran çözünürlüğü ayarı
        if (System.getProperty("headless", "false").equals("true")) {
            System.out.println("Headless mode: ACTIVE");
        }
    }

    @AfterClass
    public void tearDown() {
        System.out.println("=== BROWSER TESTİ TAMAMLANDI ===");
        // Ekran görüntüsü alma, log toplama vs.
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}