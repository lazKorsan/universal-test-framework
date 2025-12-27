package Android.TestNg;

import Android.Pages.MethodsPage;
import Android.Utilities.AndroidDriver;
import Android.Utilities.ReusableMethods;
import com.student.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class US003SignUpPhone extends BaseTest {

    MethodsPage methodsPage = new MethodsPage();


    @Test
    public void US003_SignUpWithPhoneNumber() {

        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");
        ReusableMethods.bekle(2);

        methodsPage.profileButton.click();
        ReusableMethods.bekle(2);

        methodsPage.signupButton.click();
        ReusableMethods.bekle(2);

        Actions actions = new Actions(driver);
        methodsPage.nameBox.click();
        methodsPage.nameBox.sendKeys("lazKorsan");
        ReusableMethods.bekle(2);

        String dynamicphoneNumber = String.valueOf(System.currentTimeMillis());
        String phoneNumber = dynamicphoneNumber;
        methodsPage.loginPhoneNumberBox.click();
        methodsPage.loginPhoneNumberBox.sendKeys(phoneNumber);
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

        methodsPage.signInLoginButton.click();
        ReusableMethods.bekle(2);

    }

    @Test
    public void signup(){
        methodsPage.signupWithPhoneNumber();
    }
}
