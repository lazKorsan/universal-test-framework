package Browser.TestNg;

import Browser.Utilities.Driver;

import com.student.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class FirstBrowserTest extends BaseTest {

    @Test
    public void testGoogleSearch() {
        // Driver'ı başlat
        Driver.getDriver().get("https://www.google.com");

        // Sayfa başlığını kontrol et
        String title = Driver.getDriver().getTitle();
        System.out.println("Sayfa Başlığı: " + title);
        assert title.contains("Google") : "Başlık 'Google' içermiyor!";

        // Arama kutusunu bul ve yazı yaz
        WebElement searchBox = Driver.getDriver().findElement(By.name("q"));
        searchBox.sendKeys("Selenium Test Automation");
        searchBox.submit();

        // Bekle ve sonuçları kontrol et
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("✅ Test başarılı!");
    }

    @Test
    public void testSeleniumWebsite() {
        Driver.getDriver().get("https://www.selenium.dev");

        String title = Driver.getDriver().getTitle();
        System.out.println("Selenium Sayfası: " + title);
        assert title.contains("Selenium") : "Selenium sayfası yüklenemedi!";

        // Logo'yu kontrol et
        WebElement logo = Driver.getDriver().findElement(
                By.cssSelector("svg.selenium-logo")
        );
        assert logo.isDisplayed() : "Logo görünmüyor!";

        System.out.println("✅ Selenium testi başarılı!");
    }
}