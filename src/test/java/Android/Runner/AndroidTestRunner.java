package Android.Runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/java/Android/features", // Feature dosyalarının yolu
        glue = {
                "Android/Stepdefinitions",    // Step definitions
                "Android/Hooks",              // Eğer hooks klasörü oluşturursan
                "hooks"                       // Genel hooks (src/test/java/hooks)
        },
        plugin = {
                "pretty",                     // Konsola renkli çıktı
                "html:target/cucumber-reports/android-report.html", // HTML rapor
                "json:target/cucumber-reports/android-report.json", // JSON rapor (Jenkins için)
                "rerun:target/rerun.txt"      // Failed testleri tekrar çalıştırmak için
        },
        monochrome = true,                // Konsol çıktısını siyah-beyaz yapar
        dryRun = false,                   // true yaparsan sadece step'leri kontrol eder, test çalıştırmaz
        //tags = "@EnviermentMader"                 // Sadece @android tag'ine sahip testleri çalıştır
        tags="@zzzx"
)
public class AndroidTestRunner extends AbstractTestNGCucumberTests {

    @BeforeClass
    public void setup() {
        System.out.println("=== ANDROID TESTLERİ BAŞLIYOR ===");
        // Buraya Android driver setup kodlarını ekleyebilirsin
        // Örnek: Appium server start, capability ayarları vs.
    }

    @AfterClass
    public void tearDown() {
        System.out.println("=== ANDROID TESTLERİ TAMAMLANDI ===");
        // Buraya cleanup kodlarını ekleyebilirsin
        // Örnek: driver.quit(), log kayıtları vs.
    }

    @Override
    @DataProvider(parallel = false) // parallel = true yaparak paralel test çalıştırabilirsin
    public Object[][] scenarios() {
        return super.scenarios();
    }
}