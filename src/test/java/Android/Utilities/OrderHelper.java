// 📁 utilities/OrderHelper.java (Reusable utility)
package Android.Utilities;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderHelper {

    public static String extractOrderNumber(String description) {
        if (description == null || description.isEmpty()) {
            return null;
        }

        Pattern pattern = Pattern.compile("#(\\d{10})");
        Matcher matcher = pattern.matcher(description);

        return matcher.find() ? matcher.group(1) : null;
    }

    public static String getFirstOrderNumberFromPage() {
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        WebElement element = driver.findElement(
                By.xpath("//android.widget.ImageView[contains(@content-desc, '#')][1]")
        );
        return extractOrderNumber(element.getAttribute("content-desc"));
    }

    public static String extractOrderNumberFromContentDesc(String contentDesc) {
        if (contentDesc != null && contentDesc.contains("#")) {
            int startIndex = contentDesc.indexOf("#") + 1;
            int endIndex = contentDesc.indexOf("\n", startIndex);

            if (endIndex == -1) {
                return contentDesc.substring(startIndex).trim();
            } else {
                return contentDesc.substring(startIndex, endIndex).trim();
            }
        }
        return null;
    }

    /**
     * Belirtilen bounds (koordinat ve boyut) değerlerine sahip olan veya bu alanı içeren
     * elementin içeriğini (content-desc, text) konsola yazdırır.
     *
     * @param x      Elementin başlangıç x koordinatı.
     * @param y      Elementin başlangıç y koordinatı.
     * @param width  Elementin genişliği.
     * @param height Elementin yüksekliği.
     */
    public static void printElementContentByBounds(int x, int y, int width, int height) {
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Rectangle targetBounds = new Rectangle(x, y, height, width);

        System.out.println("Aranan Bounds Değerleri: " + targetBounds);

        try {
            // Sayfadaki tüm elementleri al
            List<WebElement> allElements = driver.findElements(By.xpath("//*"));
            WebElement bestMatch = null;

            for (WebElement element : allElements) {
                Rectangle elementBounds = element.getRect();

                // 1. Tam Eşleşme Kontrolü
                if (elementBounds.equals(targetBounds)) {
                    bestMatch = element;
                    System.out.println("\n--- TAM EŞLEŞME BULUNDU ---");
                    break; // Tam eşleşme varsa daha fazla arama yapma
                }

                // 2. Kapsama Kontrolü (Eğer tam eşleşme yoksa)
                // Verilen bounds'u içeren en küçük elementi bul
                if (elementBounds.x <= targetBounds.x &&
                        elementBounds.y <= targetBounds.y &&
                        (elementBounds.x + elementBounds.width) >= (targetBounds.x + targetBounds.width) &&
                        (elementBounds.y + elementBounds.height) >= (targetBounds.y + targetBounds.height)) {

                    if (bestMatch == null ||
                            (elementBounds.width * elementBounds.height) < (bestMatch.getRect().width * bestMatch.getRect().height)) {
                        bestMatch = element;
                    }
                }
            }

            if (bestMatch != null) {
                if (!bestMatch.getRect().equals(targetBounds)) {
                    System.out.println("\n--- En Yakın Kapsayan Element Bulundu ---");
                }
                printElementDetails(bestMatch);
            } else {
                System.out.println("\n--- SONUÇ ---");
                System.out.println("Belirtilen bounds değerlerine uyan veya bu alanı kapsayan bir element bulunamadı.");
            }

        } catch (Exception e) {
            System.out.println("Element aranırken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Bir WebElement'in detaylarını konsola yazdıran yardımcı metot.
     * @param element Detayları yazdırılacak element.
     */
    public static void printElementDetails(WebElement element) {
        Rectangle bounds = element.getRect();
        String contentDesc = element.getAttribute("content-desc");
        String text = element.getText();

        System.out.println("Element Detayları:");
        System.out.println("  - Class: " + element.getAttribute("class"));
        System.out.println("  - Bounds: " + bounds);

        if (contentDesc != null && !contentDesc.isEmpty()) {
            System.out.println("  - Content-Desc: " + contentDesc);
        } else {
            System.out.println("  - Content-Desc: (boş)");
        }

        if (text != null && !text.isEmpty()) {
            System.out.println("  - Text: " + text);
        } else {
            System.out.println("  - Text: (boş)");
        }
        System.out.println("---------------------------------");
    }


}