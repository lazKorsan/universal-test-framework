package Android.TestNg;

import Android.Pages.QueryCardPage;
import Android.Utilities.AndroidDriver;
import Android.Utilities.ReusableMethods;
import com.student.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FirstAndroidTest extends BaseTest {

    QueryCardPage query = new QueryCardPage(AndroidDriver.getDriver());

    @Test
    public void testAppiumDriver() {
        
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");

        ReusableMethods.bekle(2);

        try {
            // 1. Adım: Profile butonuna tıkla
           System.out.println("👉 Profile butonuna tıklanıyor...");
            driver.findElement(AppiumBy.accessibilityId("Profile")).click();
            ReusableMethods.bekle(2);

            // 2. Adım: Sign In butonuna tıkla
            System.out.println("👉 Sign In butonuna tıklanıyor...");
            try {
                // Önce Accessibility ID ile dene
                driver.findElement(AppiumBy.accessibilityId("Sign In")).click();
            } catch (Exception e) {
                // Bulamazsa XPath (text) ile dene
                driver.findElement(AppiumBy.xpath("//*[@text='Sign In']")).click();
            }
            ReusableMethods.bekle(2);

            // 3. Adım: Phone alanını XPath ile bul ve yaz
            System.out.println("👉 Telefon numarası giriliyor...");

            Actions actions = new Actions(driver);

            query.phoneTextBox.click();
            query.phoneTextBox.sendKeys("5057193857");
            actions.sendKeys(Keys.TAB).perform();
            actions.sendKeys("Query.2025").perform();
            actions.sendKeys(Keys.TAB).perform();
            query.signInLoginClick();
            
            System.out.println("✅ Telefon numarası başarıyla girildi.");

        } catch (Exception e) {
            Assert.fail("❌ Hata: " + e.getMessage());
        }
    }


}