package Api.Runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/java/Api/features", // API feature dosyalarının yolu
        glue = {
                "Api/Stepdefinitions",    // API step definitions
                "Api/Hooks",              // API hooks (büyük H ile olan)
                "hooks"                   // Genel hooks
        },
        plugin = {
                "pretty",                     // Konsola renkli çıktı
                "html:target/cucumber-reports/api-report.html", // HTML rapor
                "json:target/cucumber-reports/api-report.json", // JSON rapor
                "rerun:target/api-rerun.txt"  // Failed testler için
        },
        monochrome = true,
        dryRun = false,
        tags = "@ssd"                     // Sadece @api tag'li testler
)
public class APITestRunner extends AbstractTestNGCucumberTests {

    @BeforeClass
    public void setup() {
        System.out.println("=== API TESTLERİ BAŞLIYOR ===");
        System.out.println("Environment: " + System.getProperty("env", "local"));

        // API base URL veya auth token setup yapabilirsin
        // Örnek: TestData.setBaseUrl("https://api.example.com");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("=== API TESTLERİ TAMAMLANDI ===");
        // Cleanup: Cache temizleme, log toplama vs.
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}