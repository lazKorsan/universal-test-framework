package utilities;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;

public class ScrollHelper {

    private AppiumDriver driver;
    private WebDriverWait wait;

    public ScrollHelper(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ⭐⭐⭐ ANA KAYDIRMA METODU (GÜNCEL) ⭐⭐⭐
    public void swipeUp() {
        Dimension size = driver.manage().window().getSize();

        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 0);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(swipe));

        // Kaydırma sonrası kısa bekle
        waitMillis(300);
    }

    // ⭐⭐⭐ AŞAĞI KAYDIR ⭐⭐⭐
    public void swipeDown() {
        Dimension size = driver.manage().window().getSize();

        int startX = size.width / 2;
        int startY = (int) (size.height * 0.2);
        int endY = (int) (size.height * 0.8);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 0);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(swipe));
        waitMillis(300);
    }

    // ⭐⭐⭐ KAYDIR VE TIKLA ⭐⭐⭐
    public void scrollAndClick(By locator, int maxSwipes) {
        boolean found = false;
        int swipeCount = 0;

        while (!found && swipeCount < maxSwipes) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                element.click();
                found = true;
                System.out.println("✅ Element bulundu ve tıklandı: " + locator);
                return;
            } catch (TimeoutException e) {
                System.out.println("↕️  Kaydırma " + (swipeCount + 1) + "/" + maxSwipes);
                swipeUp();
                swipeCount++;
                waitMillis(500);
            } catch (Exception e) {
                System.out.println("⚠️  Hata: " + e.getMessage());
                swipeUp();
                swipeCount++;
                waitMillis(500);
            }
        }

        if (!found) {
            throw new NoSuchElementException("❌ Element bulunamadı: " + locator);
        }
    }

    // ⭐⭐⭐ SENİN İÇİN ÖZEL METODLAR ⭐⭐⭐

    // 1. ImageView content-desc ile
    public void scrollAndClickImageView(String description) {
        By locator = By.xpath("//android.widget.ImageView[@content-desc=\"" + description + "\"]");
        scrollAndClick(locator, 15);
    }

    // 2. scrollAndClickByDescription - HERHANGİ BİR ELEMENT İÇİN
    public void scrollAndClickByDescription(String description) {
        By locator = By.xpath("//*[@content-desc=\"" + description + "\"]");
        scrollAndClick(locator, 15);
    }

    // 3. Text ile
    public void scrollAndClickByText(String text) {
        By locator = By.xpath("//*[@text=\"" + text + "\"]");
        scrollAndClick(locator, 15);
    }

    // 4. Resource-id ile
    public void scrollAndClickById(String resourceId) {
        By locator = By.id(resourceId);
        scrollAndClick(locator, 15);
    }

    // 5. Class name ile
    public void scrollAndClickByClassName(String className, int index) {
        By locator = By.xpath("(//" + className + ")[" + index + "]");
        scrollAndClick(locator, 15);
    }

    // 6. Contains text ile (kısmi eşleşme)
    public void scrollAndClickByContainsText(String partialText) {
        By locator = By.xpath("//*[contains(@text, '" + partialText + "')]");
        scrollAndClick(locator, 15);
    }

    // 7. Contains description ile (kısmi eşleşme)
    public void scrollAndClickByContainsDescription(String partialDesc) {
        By locator = By.xpath("//*[contains(@content-desc, '" + partialDesc + "')]");
        scrollAndClick(locator, 15);
    }

    // ⭐⭐⭐ TAP (TIKLAMA) METODU ⭐⭐⭐
    public void tapAtPoint(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 0);

        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(tap));
        waitMillis(200);
    }

    // ⭐⭐⭐ YARDIMCI METODLAR ⭐⭐⭐

    private void waitMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ⭐⭐⭐ SMART CLICK METODLARI ⭐⭐⭐

    public static void smartClick(String identifier, IdentifierType type) {
        ScrollHelper scrollHelper = new ScrollHelper(Driver.getAppiumDriver());

        switch (type) {
            case DESCRIPTION:
                scrollHelper.scrollAndClickByDescription(identifier);
                break;
            case TEXT:
                scrollHelper.scrollAndClickByText(identifier);
                break;
            case IMAGE_VIEW_DESC:
                scrollHelper.scrollAndClickImageView(identifier);
                break;
            case XPATH:
                scrollHelper.scrollAndClick(By.xpath(identifier), 10);
                break;
            case ID:
                scrollHelper.scrollAndClick(By.id(identifier), 10);
                break;
            default:
                throw new IllegalArgumentException("Geçersiz IdentifierType: " + type);
        }
    }

    // ⭐⭐⭐ SMART CLICK - OVERLOADED VERSİYONLAR ⭐⭐⭐

    public static void smartClick(String identifier) {
        // Otomatik tür algıla
        if (identifier.startsWith("//")) {
            smartClick(identifier, IdentifierType.XPATH);
        } else if (identifier.startsWith("com.") && identifier.contains(":id/")) {
            smartClick(identifier, IdentifierType.ID);
        } else {
            // Önce description ile dene, sonra text ile
            try {
                smartClick(identifier, IdentifierType.DESCRIPTION);
            } catch (Exception e) {
                smartClick(identifier, IdentifierType.TEXT);
            }
        }
    }

    // ⭐⭐⭐ ELEMENT GÖRÜNENE KADAR KAYDIR (TIKLAMA YOK) ⭐⭐⭐
    public void scrollToElement(By locator, int maxSwipes) {
        for (int i = 0; i < maxSwipes; i++) {
            try {
                if (driver.findElement(locator).isDisplayed()) {
                    System.out.println("✅ Element görünür oldu");
                    return;
                }
            } catch (Exception e) {
                swipeUp();
                waitMillis(500);
            }
        }
        throw new NoSuchElementException("Element görünür hale getirilemedi");
    }

    public void scrollToElementByDescription(String description) {
        By locator = By.xpath("//*[@content-desc=\"" + description + "\"]");
        scrollToElement(locator, 15);
    }

    public void scrollToElementByText(String text) {
        By locator = By.xpath("//*[@text=\"" + text + "\"]");
        scrollToElement(locator, 15);
    }

    // ⭐⭐⭐ SAYFA SONUNA/AŞAĞIYA KAYDIR ⭐⭐⭐
    public void scrollToBottom(int maxSwipes) {
        for (int i = 0; i < maxSwipes; i++) {
            swipeUp();
            waitMillis(300);
        }
    }

    public void scrollToTop(int maxSwipes) {
        for (int i = 0; i < maxSwipes; i++) {
            swipeDown();
            waitMillis(300);
        }
    }

    // ⭐⭐⭐ ELEMENT VAR MI KONTROL ET ⭐⭐⭐
    public boolean isElementPresent(By locator, int maxSwipes) {
        for (int i = 0; i < maxSwipes; i++) {
            try {
                if (driver.findElement(locator).isDisplayed()) {
                    return true;
                }
            } catch (Exception e) {
                swipeUp();
                waitMillis(500);
            }
        }
        return false;
    }

    public boolean isElementPresentByDescription(String description) {
        By locator = By.xpath("//*[@content-desc=\"" + description + "\"]");
        return isElementPresent(locator, 10);
    }

    public enum IdentifierType {
        DESCRIPTION, TEXT, IMAGE_VIEW_DESC, XPATH, ID
    }
}