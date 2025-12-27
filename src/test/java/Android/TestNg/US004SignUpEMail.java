package Android.TestNg;

import Android.Pages.MethodsPage;
import Android.Utilities.AndroidDriver;
import Android.Utilities.ReusableMethods;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class US004SignUpEMail {

    MethodsPage methodsPage = new MethodsPage();

    @Test
    public void US004_SignUpwithEmail(){

        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");
        ReusableMethods.bekle(2);

        methodsPage.profileButton.click();
        ReusableMethods.bekle(2);

        methodsPage.signupButton.click();
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

    @Test
    public void signup(){
        methodsPage.signupWithEMail();
    }


}
