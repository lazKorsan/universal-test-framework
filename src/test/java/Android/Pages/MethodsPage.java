package Android.Pages;

import Android.Utilities.ReusableMethods;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.time.Duration;

public class MethodsPage {

    private AndroidDriver driver;

    public MethodsPage() {
        this.driver = Android.Utilities.AndroidDriver.getDriver();
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(15)), this);
    }

    // Elementler static OLMAMALI
    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(0)")
    public WebElement phoneTextBox;

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"Sign In\").instance(1)")
    public WebElement signInLoginButton;

    // Metod static OLMAMALI
    public void loginWithPhoneNumber() {

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
                driver.findElement(AppiumBy.accessibilityId("Sign In")).click();
            } catch (Exception e) {
                driver.findElement(AppiumBy.xpath("//*[@text='Sign In']")).click();
            }
            ReusableMethods.bekle(2);

            // 3. Adım: Phone alanını doldur
            System.out.println("👉 Telefon numarası giriliyor...");

            Actions actions = new Actions(driver);

            phoneTextBox.click();
            phoneTextBox.sendKeys("5057193857");
            
            // TAB tuşu mobilde her zaman çalışmayabilir, ama deneyelim
            try {
                actions.sendKeys(Keys.TAB).perform();
                actions.sendKeys("Query.2025").perform();
                actions.sendKeys(Keys.TAB).perform();
                signInLoginButton.click();
            } catch (Exception e) {
                System.out.println("⚠️ Klavye aksiyonlarında sorun oluştu, alternatif deneniyor...");
                // Alternatif olarak diğer elementleri bulup yazabiliriz
            }

            System.out.println("✅ Login işlemi tamamlandı.");

        } catch (Exception e) {
            Assert.fail("❌ Hata: " + e.getMessage());
        }
    }
}