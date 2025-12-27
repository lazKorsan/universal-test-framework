package Android.TestNg;

import Android.Pages.MethodsPage;
import Android.Utilities.AndroidDriver;
import Android.Utilities.ReusableMethods;
import com.student.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class US002LoginMail extends BaseTest {

    MethodsPage methodsPage = new MethodsPage();

    @Test
    public void US002_LoginWithMail() {

        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
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

    @Test
    public void US002_LoginWithemail() {

        methodsPage.loginWithEMail();
    }



}
