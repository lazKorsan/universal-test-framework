package Android.TestNg;

import Android.Pages.MethodsPage;
import Android.Utilities.AndroidDriver;
import Android.Utilities.ReusableMethods;
import org.testng.Assert;
import org.testng.annotations.Test;

public class US006CreateOrder {

    MethodsPage methodsPage = new MethodsPage();

    @Test
    public void US006_CreateOrder(){
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");
       // methodsPage.loginWithPhoneNumber();

        ReusableMethods.clickButtonByDescription("Men Clothing");
        ReusableMethods.xPathElementClick("Classic Cargo Shorts","0 (0  Reviews)","$40.00");
        ReusableMethods.clickButtonByDescription("M");
        ReusableMethods.clickButtonByDescription("Add To Cart");




    }


}
