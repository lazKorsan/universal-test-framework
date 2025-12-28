package Android.TestNg;

import Android.Pages.MethodsPage;
import Android.Utilities.AndroidDriver;
import Android.Utilities.OrderHelper;
import Android.Utilities.OrderInfo;
import Android.Utilities.ReusableMethods;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

public class US007OrderNumber {

    MethodsPage methodsPage = new MethodsPage();

    @Test
    public void USOO7_getOrderNumber(){
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();

        methodsPage.loginWithPhoneNumber();
        ReusableMethods.clickButtonByDescription("Profile");
        ReusableMethods.clickButtonByDescription("Order History");


        //<!-- todo elementin degerleri


        // 1 numaralı işlem
        WebElement orderInfoContainer = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"#2712251718\n" +
                "08:24 PM, 27-12-2025\n" +
                "Info: \n" +
                "1 Product\n" +
                "Delivery Status: \n" +
                "Pending\n" +
                "Payment Status: \n" +
                "Paid\n" +
                "Total: \n" +
                "$50.00\")"));

        String contentDesc = orderInfoContainer.getAttribute("content-desc");
        String orderNumber = OrderInfo.extractOrderNumberFromContentDesc(contentDesc);

        System.out.println("Sipariş Numarası: " + orderNumber);

        OrderInfo.getFirstOrderByStatus(driver,"Pending");
        System.out.println(orderInfoContainer); // ÇIKTISI AŞAĞIDAKİ GİBİ

    }

    @Test
    public void US007DinamikOrderNumber(){
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();

        methodsPage.loginWithPhoneNumber();
        ReusableMethods.clickButtonByDescription("Profile");
        ReusableMethods.clickButtonByDescription("Order History");

        WebElement orderInfoContainer = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"#2712251718\n" +
                "08:24 PM, 27-12-2025\n" +
                "Info: \n" +
                "1 Product\n" +
                "Delivery Status: \n" +
                "Pending\n" +
                "Payment Status: \n" +
                "Paid\n" +
                "Total: \n" +
                "$50.00\")"));

        List<WebElement> orders = driver.findElements(
                AppiumBy.className("android.widget.ImageView")
        );

        WebElement firstOrder = orders.get(0);
        String contentDesc = firstOrder.getAttribute("content-desc");
        String orderNumber = OrderInfo.extractOrderNumberFromContentDesc(contentDesc);
        System.out.println("Sipariş Numarası: " + orderNumber);



    }

    @Test
   public void US007Deneme3(){
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        methodsPage.loginWithPhoneNumber();
        ReusableMethods.clickButtonByDescription("Profile");
        ReusableMethods.clickButtonByDescription("Order History");

        WebElement orderInfoContainer = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"#2712251718\n" +
                "08:24 PM, 27-12-2025\n" +
                "Info: \n" +
                "1 Product\n" +
                "Delivery Status: \n" +
                "Pending\n" +
                "Payment Status: \n" +
                "Paid\n" +
                "Total: \n" +
                "$50.00\")"));

        List<WebElement> orders = driver.findElements(
                AppiumBy.className("android.widget.ImageView")
        );

        WebElement firstOrder = orders.get(0);
        String contentDesc = firstOrder.getAttribute("content-desc");
        String orderNumber = OrderHelper.extractOrderNumberFromContentDesc(contentDesc);

        OrderHelper.getFirstOrderNumberFromPage();

    }


}
