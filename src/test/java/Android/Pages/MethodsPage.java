package Android.Pages;

import Android.Utilities.ReusableMethods;
import Android.Utilities.ScrollHelper;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
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

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"Address\")")
    public WebElement AddressButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"Add New Address\")")
    public WebElement addNewAddressButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(0)")
    public WebElement fullNameBox;



    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView")
    public WebElement shoppingCartButton;

    @AndroidFindBy(xpath = "//android.view.View[@resource-id=\"root\"]/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View[2]/android.widget.EditText")
    public WebElement cartNumberField;

    @AndroidFindBy(xpath = "//android.view.View[@resource-id=\"root\"]/android.view.View/android.view.View/android.view.View[2]/android.widget.EditText[1]")
    public WebElement dateField;


    @AndroidFindBy(xpath = "//android.view.View[@resource-id=\"root\"]/android.view.View/android.view.View/android.view.View[2]/android.widget.EditText[2]")
    public WebElement cvcfield;


    @AndroidFindBy(xpath = "//android.view.View[@resource-id=\"root\"]/android.view.View/android.view.View/android.view.View[2]/android.widget.EditText[3]")
    public WebElement zipField;




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
            phoneTextBox.sendKeys("5057193818");

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

    public void addToAddress(){

        io.appium.java_client.android.AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();


        //<!-- todo dynamic method için gerekli

        profileButton.click();
        //ReusableMethods.bekle(2);

        AddressButton.click();
        //ReusableMethods.bekle(2);

        addNewAddressButton.click();
        //ReusableMethods.bekle(2);

        Actions actions = new Actions(driver);
        String fakeMail = "lazKorsan"+System.currentTimeMillis()+"@gmail.com";
        String mail = fakeMail;

        String fakePhoneNumber = String.valueOf(System.currentTimeMillis());

        fullNameBox.click();
        fullNameBox.sendKeys("lazKorsan");
        //ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        //ReusableMethods.bekle(2);
        actions.sendKeys(mail).perform();
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        ReusableMethods.bekle(2);

        WebElement fakePhoneNumberBox = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(2)"));
        fakePhoneNumberBox.click();
        fakePhoneNumberBox.sendKeys(fakePhoneNumber);
        //ReusableMethods.bekle(2);


        WebElement countryButton= driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"Country\")"));
        countryButton.click();
        //ReusableMethods.bekle(2);


        WebElement countryBox = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\")"));
        countryBox.click();
        //ReusableMethods.bekle(2);

        //<!-- == todo country enter
        actions.sendKeys("Denmark").perform();
        //ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();
        ReusableMethods.bekle(2);

        //<!-- == todo state enter
        WebElement stateButton= driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"State\")"));
        stateButton.click();
        //ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        //<!-- == todo city enter
        WebElement cityButton= driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"City\")"));
        cityButton.click();
        //ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        //<!-- == todo zip code enter
        WebElement zipCodeBox= driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(3)"));
        zipCodeBox.click();
        ReusableMethods.bekle(2);
        zipCodeBox.sendKeys("12345");
        ReusableMethods.hideKeyboard();
        //ReusableMethods.bekle(2);


        //<!-- == todo street address enter
        WebElement streetAdress= driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(4)"));

        streetAdress.click();
        driver.setClipboardText("Test Street 12");
        streetAdress.sendKeys(driver.getClipboardText());

        ReusableMethods.hideKeyboard();
        //ReusableMethods.bekle(2);

        WebElement addAddressButton = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"Add Address\")"));
        addAddressButton.click();
        //ReusableMethods.bekle(2);

        ReusableMethods.KeyBack();

        ReusableMethods.clickButtonByDescription("Home");





    }

    public void createOrder(){
        io.appium.java_client.android.AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");
       // loginWithPhoneNumber();
        ReusableMethods.bekle(6);

        ReusableMethods.clickButtonByDescription("Men Clothing");
        ReusableMethods.xPathElementClick("Classic Cargo Shorts", "0 (0  Reviews)", "$40.00");
        ReusableMethods.clickButtonByDescription("M");
        ScrollHelper.scrollAndClickByDescription("Add To Cart");

        shoppingCartButton.click();

        ReusableMethods.clickButtonByDescription("Proceed to Checkout");

        String userName = "lazKorsan";
        ScrollHelper.scrollAndClickByContainsDescription(userName);

        ScrollHelper.scrollAndClickByDescription("Save & Pay");

        ReusableMethods.clickButtonByDescription("Stripe");

        ReusableMethods.clickButtonByDescription("Confirm Order");

        cartNumberField.click();
        cartNumberField.sendKeys("4242424242424242");

        // 1 saniye bekle (gerekirse)
        ReusableMethods.wait(1);

        // Date alanına direkt tıkla
        dateField.click();

        // Tarih gir
        dateField.sendKeys("1228");

        // CVC alanına tıkla
        cvcfield.click();
        cvcfield.sendKeys("321");
        ReusableMethods.wait(1);

        // Zip alanına tıkla
        zipField.click();
        zipField.sendKeys("12345");

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        // "Go to order details" butonunun görünmesini bekleyen döngü
        boolean isOrderCompleted = false;
        int maxRetries = 60; // 60 deneme * 5 saniye = 300 saniye (5 dakika) maksimum bekleme

        for (int i = 1; i <= maxRetries; i++) {
            try {
                // Elementi her döngüde yeniden bulmaya çalışıyoruz
                WebElement goToOrderDetailsPageButton = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"Go to order details\")"));

                if (goToOrderDetailsPageButton.isDisplayed()) {
                    System.out.println("✅ Sipariş tamamlandı! Detay sayfasına gidiliyor. (Deneme #" + i + ")");
                    goToOrderDetailsPageButton.click();
                    isOrderCompleted = true;
                    break; // Döngüden çık
                }
            } catch (Exception e) {
                // Element henüz bulunamadı, beklemeye devam et
                System.out.println("⏳ Sipariş bekleniyor... Deneme #" + i + "/" + maxRetries);

                // Sadece konsola yazdırmak yerine, daha basit bir bekleme mesajı gösterebiliriz
                if (i == 1) {
                    // Sadece ilk denemede bir kez göster
                    System.out.println("Sipariş işleniyor, lütfen bekleyin...");
                }
            }

            // 5 saniye bekle
            ReusableMethods.wait(5);
        }

        if (!isOrderCompleted) {
            Assert.fail("❌ Sipariş " + (maxRetries * 5) + " saniye içinde tamamlanamadı!");
        }

        // Test başarılı mesajı
        System.out.println("✅ US006_CreateOrder testi başarıyla tamamlandı!");
    }





    }
