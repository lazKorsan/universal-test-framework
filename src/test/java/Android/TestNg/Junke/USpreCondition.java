package Android.TestNg.Junke;

import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class USpreCondition   {

    private io.appium.java_client.android.AndroidDriver driver;


    @Test
    public void preCondition(){
        System.out.println("Pre-Condition metodu çalıştı.");
        this.driver = Android.Utilities.AndroidDriver.getDriver();
        Assert.assertNotNull(this.driver, "AndroidDriver başlatılamadı!");
        driver.findElement(AppiumBy.accessibilityId("Profile")).click();
    }
}
