package Android.TestNg;

import Android.Pages.MethodsPage;
import Android.Utilities.AndroidDriver;
import Android.Utilities.OrderHelper;
import Android.Utilities.ReusableMethods;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class US007OrderNumber {

    MethodsPage methodsPage = new MethodsPage();

    @Test
    public void USOO7_getOrderNumber(){
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


        String orderNumber = orderInfoContainer.getText();

        System.out.println("Sipariş Numarası: " + orderNumber);

        //OrderHelper.getFirstOrderNumberFromPage();

        //OrderHelper.extractOrderNumber(orderNumber);
        //System.out.println("Sipariş Numarası: " + orderNumber);

        //OrderHelper.extractOrderNumberFromContentDesc("#2712251718\\n08:24 PM, 27-12-2025\\nInfo: \\n1 Product\\nDelivery Status: \\nPending\\nPayment Status: \\nPaid\\nTotal: \\n$50.00");


        OrderHelper.printElementContentByBounds(64,586,1376,1372);

        OrderHelper.printElementDetails(orderInfoContainer);



    }



}
