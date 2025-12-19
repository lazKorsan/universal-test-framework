package DataBase.Runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/java/DataBase/features", // Database feature dosyaları
        glue = {
                "DataBase/Stepdefinitions",  // Database step definitions
                "DataBase/Hooks",            // Database hooks (oluşturursan)
                "hooks"                      // Genel hooks
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/database-report.html",
                "json:target/cucumber-reports/database-report.json",
                "rerun:target/database-rerun.txt"
        },
        monochrome = true,
        dryRun = false,
        tags = "@database"               // Sadece database testleri
)
public class DatabaseTestRunner extends AbstractTestNGCucumberTests {

    @BeforeClass
    public void setup() {
        System.out.println("=== DATABASE TESTLERİ BAŞLIYOR ===");
        System.out.println("Database Environment: " + System.getProperty("db.env", "local"));
        System.out.println("Database Type: " + System.getProperty("db.type", "mysql"));

        // Log başlangıcı
        System.out.println("Initializing database connections...");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("=== DATABASE TESTLERİ TAMAMLANDI ===");
        System.out.println("Closing all database connections...");
    }

    @Override
    @DataProvider(parallel = false) // Database testleri genelde paralel çalışmaz
    public Object[][] scenarios() {
        return super.scenarios();
    }
}