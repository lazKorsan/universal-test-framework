package Android.Utilities;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

public class ScrollHelper {

    private AppiumDriver driver;
    private WebDriverWait wait;

    // Constructor
    public ScrollHelper() {
        this.driver = AndroidDriver.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public ScrollHelper(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // ⭐⭐⭐ ANA KAYDIRMA METODU (INSTANCE) ⭐⭐⭐
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

        driver.perform(Collections.singletonList(swipe));
        waitMillis(300);
    }

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

        driver.perform(Collections.singletonList(swipe));
        waitMillis(300);
    }

    // ⭐⭐⭐ KAYDIR VE TIKLA (INSTANCE) ⭐⭐⭐
    public void scrollAndClick(By locator, int maxSwipes) {
        boolean found = false;
        int swipeCount = 0;

        while (!found && swipeCount < maxSwipes) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                element.click();
                found = true;
                return;
            } catch (TimeoutException | NoSuchElementException e) {
                swipeUp();
                swipeCount++;
            } catch (Exception e) {
                swipeUp();
                swipeCount++;
            }
        }

        if (!found) {
            throw new NoSuchElementException("❌ Element bulunamadı (Max swipe: " + maxSwipes + "): " + locator);
        }
    }

    // ⭐⭐⭐ STATIC WRAPPER METODLAR (KOLAY KULLANIM İÇİN) ⭐⭐⭐

    // 1. ImageView content-desc ile
    public static void scrollAndClickImageView(String description) {
        By locator = By.xpath("//android.widget.ImageView[@content-desc=\"" + description + "\"]");
        new ScrollHelper().scrollAndClick(locator, 15);
    }

    // 2. scrollAndClickByDescription - HERHANGİ BİR ELEMENT İÇİN
    public static void scrollAndClickByDescription(String description) {
        By locator = By.xpath("//*[@content-desc=\"" + description + "\"]");
        new ScrollHelper().scrollAndClick(locator, 15);
    }

    // 3. Text ile
    public static void scrollAndClickByText(String text) {
        By locator = By.xpath("//*[@text=\"" + text + "\"]");
        new ScrollHelper().scrollAndClick(locator, 15);
    }

    // 4. Resource-id ile
    public static void scrollAndClickById(String resourceId) {
        By locator = By.id(resourceId);
        new ScrollHelper().scrollAndClick(locator, 15);
    }

    // 5. Class name ile
    public static void scrollAndClickByClassName(String className, int index) {
        By locator = By.xpath("(//" + className + ")[" + index + "]");
        new ScrollHelper().scrollAndClick(locator, 15);
    }

    // 6. Contains text ile
    public static void scrollAndClickByContainsText(String partialText) {
        By locator = By.xpath("//*[contains(@text, '" + partialText + "')]");
        new ScrollHelper().scrollAndClick(locator, 15);
    }

    // 7. Contains description ile
    public static void scrollAndClickByContainsDescription(String partialDesc) {
        By locator = By.xpath("//*[contains(@content-desc, '" + partialDesc + "')]");
        new ScrollHelper().scrollAndClick(locator, 15);
    }

    // ⭐⭐⭐ TAP (TIKLAMA) METODU (INSTANCE) ⭐⭐⭐
    public void tapAtPoint(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 0);

        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
        waitMillis(200);
    }

    private void waitMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ⭐⭐⭐ SMART CLICK METODLARI (STATIC) ⭐⭐⭐

    public static void smartClick(String identifier, IdentifierType type) {
        switch (type) {
            case DESCRIPTION:
                scrollAndClickByDescription(identifier);
                break;
            case TEXT:
                scrollAndClickByText(identifier);
                break;
            case IMAGE_VIEW_DESC:
                scrollAndClickImageView(identifier);
                break;
            case XPATH:
                new ScrollHelper().scrollAndClick(By.xpath(identifier), 10);
                break;
            case ID:
                scrollAndClickById(identifier);
                break;
            default:
                throw new IllegalArgumentException("Geçersiz IdentifierType: " + type);
        }
    }

    public static void smartClick(String identifier) {
        if (identifier.startsWith("//") || identifier.startsWith("(")) {
            smartClick(identifier, IdentifierType.XPATH);
        } else if (identifier.startsWith("com.") && identifier.contains(":id/")) {
            smartClick(identifier, IdentifierType.ID);
        } else {
            try {
                smartClick(identifier, IdentifierType.DESCRIPTION);
            } catch (Exception e) {
                try {
                    smartClick(identifier, IdentifierType.TEXT);
                } catch (Exception ex) {
                    throw new NoSuchElementException("Element ne description ne de text ile bulunamadı: " + identifier);
                }
            }
        }
    }

    // ⭐⭐⭐ ELEMENT GÖRÜNENE KADAR KAYDIR (INSTANCE) ⭐⭐⭐
    public void scrollToElement(By locator, int maxSwipes) {
        for (int i = 0; i < maxSwipes; i++) {
            try {
                if (driver.findElement(locator).isDisplayed()) {
                    return;
                }
            } catch (Exception e) {
                swipeUp();
                waitMillis(500);
            }
        }
        throw new NoSuchElementException("Element görünür hale getirilemedi: " + locator);
    }

    // Static Wrappers for ScrollToElement
    public static void scrollToElementByDescription(String description) {
        By locator = By.xpath("//*[@content-desc=\"" + description + "\"]");
        new ScrollHelper().scrollToElement(locator, 15);
    }

    public static void scrollToElementByText(String text) {
        By locator = By.xpath("//*[@text=\"" + text + "\"]");
        new ScrollHelper().scrollToElement(locator, 15);
    }

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

    public enum IdentifierType {
        DESCRIPTION, TEXT, IMAGE_VIEW_DESC, XPATH, ID
    }
}