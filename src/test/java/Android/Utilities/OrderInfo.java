package Android.Utilities;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import io.appium.java_client.android.AndroidDriver;

public class OrderInfo {


    public static String extractOrderNumberFromContentDesc(String contentDesc) {
        // #2712251718 → 2712251718
        Pattern pattern = Pattern.compile("#(\\d+)");
        Matcher matcher = pattern.matcher(contentDesc);

        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Sipariş numarası bulunamadı!");
    }

    public static WebElement getFirstOrderByStatus(AndroidDriver driver, String status) {

        List<WebElement> orders = driver.findElements(
                AppiumBy.className("android.widget.ImageView")
        );

        for (WebElement order : orders) {
            String desc = order.getAttribute("content-desc");
            if (desc != null && desc.contains("Delivery Status:") && desc.contains(status)) {
                return order;
            }
        }

        throw new RuntimeException(status + " statüsünde sipariş bulunamadı!");
    }

    public static void clickFirstOrderByStatus(AndroidDriver driver, String status) {

        List<WebElement> orders = driver.findElements(
                AppiumBy.className("android.widget.ImageView")
        );

        for (WebElement order : orders) {
            String desc = order.getAttribute("content-desc");

            if (desc != null
                    && desc.contains("Delivery Status:")
                    && desc.contains(status)) {

                order.click();
                return;
            }
        }

        throw new RuntimeException(
                status + " statüsünde tıklanacak sipariş bulunamadı!"
        );
    }

    public static String getOrderNumberFromOrderDetailPage(AndroidDriver driver) {

        List<WebElement> views = driver.findElements(
                AppiumBy.className("android.view.View")
        );

        for (WebElement view : views) {
            String desc = view.getAttribute("content-desc");

            if (desc != null && desc.startsWith("#")) {
                // #2712251718 → 2712251718
                return desc.replace("#", "").trim();
            }
        }

        throw new RuntimeException("Sipariş detay sayfasında order number bulunamadı!");
    }
}


