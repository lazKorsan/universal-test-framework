package Android.TestNg;

import Android.Pages.MethodsPage;
import Android.Utilities.OrderHelper;
import Android.Utilities.OrderInfo;
import Android.Utilities.ReusableMethods;
import Android.Utilities.ScrollHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Android.Utilities.OrderInfo.clickFirstOrderByStatus;

public class US008ConfirmDeleteOrder {

    MethodsPage methodsPage = new MethodsPage();


    @Test
    public void US008_getOrderNumber(){
        io.appium.java_client.android.AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();
        methodsPage.loginWithPhoneNumber();
        ReusableMethods.clickButtonByDescription("Profile");
        ReusableMethods.clickButtonByDescription("Order History");
        String orderNumber = OrderHelper.getFirstOrderNumberFromPage();
        System.out.println(OrderHelper.getFirstOrderNumberFromPage());

        clickFirstOrderByStatus( driver, "Pending");



        String detailOrderNumber =
                OrderInfo.getOrderNumberFromOrderDetailPage(driver);

        System.out.println("Sipariş Detay Numarası: " + detailOrderNumber);

        ScrollHelper.scrollAndClickByDescription("Cancel Order");

        ReusableMethods.KeyBack();

        String canceledOrderNumber =  OrderHelper.getFirstOrderNumberFromPage();

        System.out.println("silinenSipaiş Numarasi"+canceledOrderNumber);


        Assert.assertEquals(orderNumber, canceledOrderNumber,
                "Order number history ve detail sayfasında aynı değil!");











    }


}
