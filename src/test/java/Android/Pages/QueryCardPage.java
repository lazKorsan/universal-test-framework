package Android.Pages;

import Android.Utilities.AppiumServerController;
import Android.Utilities.ReusableMethods;
import Android.Utilities.RunTimeEmulatorStarter;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.student.ConfigManager;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;


import static org.junit.Assert.assertTrue;

public class QueryCardPage {

    private AndroidDriver driver;

    public QueryCardPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(15)), this);
    }

    public void kullaniciQueryCartUygulamasiniAcar() {
        String appPackage = "com.wise.querycart";
        String appActivity = "com.wise.querycart.MainActivity";
        //io.appium.java_client.android.AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver(appPackage, appActivity);
        io.appium.java_client.android.AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");

        // Test senaryonuzu buraya ekleyebilirsiniz.
        // Örneğin: driver.findElement(AppiumBy.accessibilityId("Login Screen")).click();

        System.out.println("   📱 AndroidDriver başarıyla başlatıldı.");
        System.out.println("   -> Oturum ID: " + driver.getSessionId());
        System.out.println("   -> Cihaz Bilgileri: " + driver.getCapabilities().getCapability("deviceName"));
    }

    public void userHasBeenOpenAppAutomaticaly(){

        System.out.println("🚀 Appium server başlatılıyor...");
        AppiumServerController.startServer();

        System.out.println("📱 Emülatör başlatılıyor...");
        RunTimeEmulatorStarter.startEmulator("pixel_7_pro");

        System.out.println("⏳ Boot bekleniyor (45 saniye)...");
        ReusableMethods.wait(45);

        System.out.println("🔧 AndroidDriver başlatılıyor...");

        // DEĞİŞİKLİK BURADA: Lokal değişken DEĞİL, class field'ına ata!
        this.driver = Android.Utilities.AndroidDriver.getDriver();
        Assert.assertNotNull(this.driver, "AndroidDriver başlatılamadı!");

        System.out.println("✅ Driver başlatıldı:");
        System.out.println("   Session ID: " + this.driver.getSessionId());

        ReusableMethods.wait(4);

        // Test
        this.driver.findElement(AppiumBy.accessibilityId("Profile")).click();

        System.out.println("🎉 Uygulama başarıyla açıldı!");

    }





    @AndroidFindBy(xpath = "(//android.widget.ImageView[1])[1]")
    private WebElement queryCardLogoElement;

    @AndroidFindBy(xpath = "(//android.widget.ImageView[1])[2]")
    private WebElement searchBoxElement;

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"Sign In\").instance(1)")
    private WebElement signInLoginButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(0)")
    private WebElement phoneTextBox;

    @AndroidFindBy(accessibility = "See All")
    private WebElement seeAllIconElement;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(1)")
    private WebElement passwordTextBox;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.ImageView\").instance(2)")
    private WebElement rememberMeCheckBox;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.view.View\").instance(4)")
    public WebElement addWishListToast;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.view.View\").instance(9)")
    private List<WebElement> categories;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\")")
    private WebElement forgotPssPhoneBox;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(0)")
    private WebElement newPasswordBox;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(1)")
    private WebElement confirmPasswordBox;

    @AndroidFindBy(xpath = "//android.view.View[@content-desc=\"Add\n    Added to Wishlist\"]")
    public WebElement addedWish;

    @AndroidFindBy(xpath = "//android.view.View[@content-desc=\"Remove\n    Removed from Wishlist\"]")
    public WebElement removedWish;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.ImageView\").instance(2)")
    public WebElement whiteHeart;

    @AndroidFindBy(xpath = "(//*[@index='2'])[1]")
    public WebElement wishquant;

    @FindBy(xpath = "(//android.view.View)[6]")
    public WebElement PageTitle;

    @FindBy(xpath = "(//android.view.View)[10]")
    public WebElement productName;

    @FindBy(xpath = "(//*[@class='android.widget.ImageView'])[3]")
    public WebElement removepopup;

    @AndroidFindBy(id = "com.android.chrome:id/search_box_text")
    private WebElement googleSearchBox;

    @AndroidFindBy(id = "com.android.chrome:id/line_2")
    private WebElement googleSearchClick;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Log In\"]")
    private WebElement queryCardUrlLogin;

    @AndroidFindBy(uiAutomator = ("new UiSelector().resourceId(\"formEmail\")"))
    private WebElement qcUrlEmailTextBox;

    @AndroidFindBy(uiAutomator = ("new UiSelector().resourceId(\"formPassword\")"))
    private WebElement qcUrlPasswordTextBox;

    @AndroidFindBy(xpath = ("//android.widget.Button[@text=\"Sign In\"]"))
    private WebElement qcUrlSigninButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"Flower Print Foil T-shirt\n0 (0  Reviews)\n$65.00\")")
    public WebElement firstElementOfMostPopuler;

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"L\")")
    public WebElement LSizeButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.ImageView\").instance(12)")
    public WebElement sepetIcon;

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"Profile\")")
    private WebElement profileLink;

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"*Use Email Instead\")")
    private WebElement useEmailInsteadLink;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(0)")
    private WebElement emailTextBox;

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"Sign In\")")
    private WebElement signInButton;

    public void Login(String phoneNumber, String password) {
        Actions actions = new Actions(driver);

        String phone = phoneNumber.startsWith("${") ?
                ConfigManager.getProperty(phoneNumber.replace("${", "").replace("}", "")) :
                phoneNumber;

        String pass = password.startsWith("${") ?
                ConfigManager.getProperty(password.replace("${", "").replace("}", "")) :
                password;

        phoneTextBoxClickAndSendKeys(phone);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(pass).perform();

        ReusableMethods.wait(1);
        actions.sendKeys(Keys.TAB).perform();
        signInLoginClick();
    }

    public void SearchBoxGorunurlukClickTest() {
        assertTrue(searchBoxElement.isDisplayed());
        searchBoxElement.click();
    }

    public void phoneTextBoxClickAndSendKeys(String phoneNumber) {
        assertTrue(phoneTextBox.isDisplayed());
        phoneTextBox.click();
        phoneTextBox.sendKeys(phoneNumber);
    }

    public void signInLoginClick() {
        assertTrue(signInLoginButton.isDisplayed());
        signInLoginButton.click();
    }

    public void ForgetPasswordPhoneBox(String phoneNumber) {
        assertTrue(forgotPssPhoneBox.isDisplayed());
        assertTrue(forgotPssPhoneBox.isEnabled());
        forgotPssPhoneBox.click();
        forgotPssPhoneBox.sendKeys(phoneNumber);
    }

    public void NewPassword(String newPassword) {
        assertTrue(newPasswordBox.isDisplayed());
        assertTrue(newPasswordBox.isEnabled());
        newPasswordBox.click();
        newPasswordBox.sendKeys(newPassword);
        assertTrue(confirmPasswordBox.isDisplayed());
        assertTrue(confirmPasswordBox.isEnabled());
        confirmPasswordBox.click();
        confirmPasswordBox.sendKeys(newPassword);
    }

    public void googleSearchToQueryCart() {
        ReusableMethods.wait(6);
        googleSearchBox.sendKeys("querycart.com/#/home");
        assertTrue(googleSearchClick.isEnabled());
        googleSearchClick.click();
    }

    public void signInFromUrl() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(queryCardUrlLogin));

        assertTrue(queryCardUrlLogin.isEnabled());
        queryCardUrlLogin.click();
        ReusableMethods.wait(2);

        assertTrue(qcUrlEmailTextBox.isEnabled());
        qcUrlEmailTextBox.click();
        qcUrlEmailTextBox.sendKeys("elifkesen.manager@querycart.com");

        assertTrue(qcUrlPasswordTextBox.isEnabled());
        qcUrlPasswordTextBox.click();
        qcUrlPasswordTextBox.sendKeys("Query.05042025");
    }

    public void hataliLogin(String phoneNumber, String password) {
        Actions actions = new Actions(driver);
        phoneTextBoxClickAndSendKeys("5335333333");
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys("alfaromeso").perform();
        ReusableMethods.wait(1);
        actions.sendKeys(Keys.TAB).perform();
        signInLoginClick();
    }



    public AndroidDriver getDriver() {
        return driver;
    }
}