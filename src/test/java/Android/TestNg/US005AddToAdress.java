package Android.TestNg;

import Android.Pages.MethodsPage;
import Android.Utilities.AndroidDriver;
import Android.Utilities.ReusableMethods;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class US005AddToAdress {

    MethodsPage methodsPage = new MethodsPage();


    @Test
    public void US005_AddToAdress(){

        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");
        ReusableMethods.bekle(2);

        methodsPage.loginWithPhoneNumber();

        //<!-- todo dynamic method için gerekli

        methodsPage.profileButton.click();
        ReusableMethods.bekle(2);

        methodsPage.AddressButton.click();
        ReusableMethods.bekle(2);

        methodsPage.addNewAddressButton.click();
        ReusableMethods.bekle(2);

        Actions actions = new Actions(driver);
        String fakeMail = "lazKorsan"+System.currentTimeMillis()+"@gmail.com";
        String mail = fakeMail;

        String fakePhoneNumber = String.valueOf(System.currentTimeMillis());

        methodsPage.fullNameBox.click();
        methodsPage.fullNameBox.sendKeys("lazKorsan");
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        ReusableMethods.bekle(2);
        actions.sendKeys(mail).perform();
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        ReusableMethods.bekle(2);

        WebElement fakePhoneNumberBox = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(2)"));
        fakePhoneNumberBox.click();
        fakePhoneNumberBox.sendKeys(fakePhoneNumber);
        ReusableMethods.bekle(2);


        WebElement countryButton= driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"Country\")"));
        countryButton.click();
        ReusableMethods.bekle(2);


        WebElement countryBox = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\")"));
        countryBox.click();
        ReusableMethods.bekle(2);

        //<!-- == todo country enter
        actions.sendKeys("Denmark").perform();
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();
        ReusableMethods.bekle(2);

        //<!-- == todo state enter
        WebElement stateButton= driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"State\")"));
        stateButton.click();
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        //<!-- == todo city enter
        WebElement cityButton= driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"City\")"));
        cityButton.click();
        ReusableMethods.bekle(2);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        //<!-- == todo zip code enter
        WebElement zipCodeBox= driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(3)"));
        zipCodeBox.click();
        ReusableMethods.bekle(1);
        zipCodeBox.sendKeys("12345");
        ReusableMethods.hideKeyboard();
        ReusableMethods.bekle(1);
        ReusableMethods.hideKeyboard();


        //<!-- == todo street address enter
        WebElement streetAdress= driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").instance(4)"));

        streetAdress.click();
        ReusableMethods.hideKeyboard();
        ReusableMethods.bekle(2);
        streetAdress.sendKeys("12");
        ReusableMethods.hideKeyboard();

        WebElement addAddressButton = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"Add Address\")"));
        addAddressButton.click();
        ReusableMethods.bekle(2);

    }
}
