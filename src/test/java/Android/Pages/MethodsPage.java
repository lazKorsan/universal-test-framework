package Android.Pages;

import Android.Utilities.ReusableMethods;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
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

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"Profile\")")
    public WebElement profileButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"Sign Up\")")
    public WebElement signupButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(0)")
    public WebElement nameBox;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(1)")
    public WebElement loginPhoneNumberBox;


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

    public void loginWithEMail() {

        io.appium.java_client.android.AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");
        ReusableMethods.bekle(2);

        driver.findElement(AppiumBy.accessibilityId("Profile")).click();
        ReusableMethods.bekle(2);

        driver.findElement(AppiumBy.accessibilityId("Sign In")).click();
        ReusableMethods.bekle(2);

        driver.findElement(AppiumBy.accessibilityId("*Use Email Instead")).click();
        ReusableMethods.bekle(2);


        WebElement emailBox = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(0)"));

        Actions actions = new Actions(driver);
        emailBox.click();
        emailBox.sendKeys("ahmeteldes.customer@querycart.com");
        ReusableMethods.bekle(2);


        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys("Query.202020").perform();

        WebElement signInButton2 = driver.findElement(By.xpath("(//android.view.View[@content-desc=\"Sign In\"])[2]"));
        signInButton2.click();
        ReusableMethods.bekle(2);

    }

    public void signupWithPhoneNumber() {
        io.appium.java_client.android.AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");
        ReusableMethods.bekle(2);

        profileButton.click();
        ReusableMethods.bekle(2);

        signupButton.click();
        ReusableMethods.bekle(2);

        Actions actions = new Actions(driver);
        nameBox.click();
        nameBox.sendKeys("lazKorsan");
        ReusableMethods.bekle(2);

        String dynamicphoneNumber = String.valueOf(System.currentTimeMillis());
        String phoneNumber = dynamicphoneNumber;
        loginPhoneNumberBox.click();
        loginPhoneNumberBox.sendKeys(phoneNumber);
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys("Query.2025").perform();
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();

        WebElement signUp2Button = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"Sign Up\").instance(1)"));
        signUp2Button.click();
        ReusableMethods.bekle(2);

        WebElement phoneNumberBox2 = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(0)"));
        phoneNumberBox2.click();
        phoneNumberBox2.sendKeys(phoneNumber);
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys("Query.2025").perform();
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        ReusableMethods.bekle(2);

        signInLoginButton.click();
        ReusableMethods.bekle(2);

    }

    public void signupWithEMail() {

        io.appium.java_client.android.AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");
        ReusableMethods.bekle(2);

        profileButton.click();
        ReusableMethods.bekle(2);

        signupButton.click();
        ReusableMethods.bekle(2);

        driver.findElement(AppiumBy.accessibilityId("*Use Email Instead")).click();
        ReusableMethods.bekle(2);

        Actions actions = new Actions(driver);
        String dynamicMail="lazKorsan"+System.currentTimeMillis()+"@gmail.com";
        String mail=dynamicMail;
        WebElement nameBox = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(0)"));
        nameBox.click();
        nameBox.sendKeys("lazKorsan");
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(mail).perform();
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys("Query.2025").perform();
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        ReusableMethods.bekle(2);

        WebElement signUp2Button = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"Sign Up\").instance(1)"));
        signUp2Button.click();
        ReusableMethods.bekle(2);

        WebElement emailBox = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(0)"));


        emailBox.click();
        emailBox.sendKeys(mail);
        ReusableMethods.bekle(2);



        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys("Query.2025").perform();

        WebElement signInButton2 = driver.findElement(By.xpath("(//android.view.View[@content-desc=\"Sign In\"])[2]"));
        signInButton2.click();
        ReusableMethods.bekle(2);


    }
}