package utilities;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;

public class AndroidClickUtils {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration SHORT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration HIGHLIGHT_DURATION = Duration.ofMillis(200);

    /**
     * Ana method - Elemente tıklar (description, xpath, id destekler)
     * @param driver AppiumDriver
     * @param selector Element bulma stratejisi (By.xpath, By.id, MobileBy.AccessibilityId)

     */
    public static void click(AndroidDriver driver, By selector) {
        click(driver, selector, DEFAULT_TIMEOUT);
    }

    public static void click(AndroidDriver driver, By selector, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            log("🔍 Element aranıyor: " + selector);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
            log("✅ Element görünür hale geldi");

            // Android için scroll
            scrollToElementAndroid(driver, element);

            // Android için tıklama dene
            boolean clicked = performClickWithFallback(driver, element);

            if (clicked) {
                log("✅ BUTONA TIKLANDI - " + selector);
            } else {
                throw new RuntimeException("❌ Tüm tıklama methodları başarısız oldu: " + selector);
            }

        } catch (TimeoutException e) {
            log("❌ ZAMAN AŞIMI: Element bulunamadı: " + selector);
            throw new RuntimeException("Element bulunamadı: " + selector, e);
        }
    }

    /**
     * Description (content-desc) ile tıklama - EN ÇOK KULLANILAN
     */
    public static void clickByDescription(AndroidDriver driver, String description) {
        String xpath = "//*[@content-desc='" + description + "']";
        click(driver, By.xpath(xpath));
    }

    /**
     * Text ile tıklama
     */
    public static void clickByText(AndroidDriver driver, String text) {
        String xpath = "//*[@text='" + text + "']";
        click(driver, By.xpath(xpath));
    }

    /**
     * Partial text ile tıklama
     */
    public static void clickByPartialText(AndroidDriver driver, String partialText) {
        String xpath = "//*[contains(@text,'" + partialText + "')]";
        click((AndroidDriver) driver, By.xpath(xpath));
    }

    /**
     * ID ile tıklama
     */
    public static void clickById(AndroidDriver driver, String id) {
        click(driver, By.id(id));
    }

    /**
     * Android'e özel scroll metodu
     */
    private static void scrollToElementAndroid(AndroidDriver driver, WebElement element) {
        try {
            log("   📜 Elemente scroll yapılıyor...");

            // Modern AppiumBy kullanımı
            if (driver instanceof AppiumDriver) {
                ((AppiumDriver) driver).findElement(
                        AppiumBy.androidUIAutomator(
                                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                                        "new UiSelector().description(\"" + element.getAttribute("content-desc") + "\"))"
                        )
                );
            }

            // Alternatif: JavaScript scroll
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(300);
            log("   ✓ Scroll başarılı");

        } catch (Exception e) {
            log("   ⚠️ Scroll başarısız: " + e.getMessage());
        }
    }

    /**
     * Aşamalı tıklama methodları - Android'e özel
     */
    private static boolean performClickWithFallback(AndroidDriver driver, WebElement element) {

        // METHOD 1: Normal Click
        try {
            log("   [1/6] Normal click deneniyor...");
            element.click();
            log("   ✓ Normal click başarılı");
            return true;
        } catch (Exception e) {
            log("   ✗ Normal click başarısız: " + e.getMessage());
        }

        // METHOD 2: Actions ile tıklama
        try {
            log("   [2/6] Actions click deneniyor...");
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().perform();
            log("   ✓ Actions click başarılı");
            return true;
        } catch (Exception e) {
            log("   ✗ Actions click başarısız: " + e.getMessage());
        }

        // METHOD 3: JavaScript click
        try {
            log("   [3/6] JavaScript click deneniyor...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            log("   ✓ JavaScript click başarılı");
            return true;
        } catch (Exception e) {
            log("   ✗ JavaScript click başarısız: " + e.getMessage());
        }

        // METHOD 4: Tap with coordinates (Android)
        try {
            log("   [4/6] Coordinate tap deneniyor...");
            Point point = element.getLocation();
            int centerX = point.x + (element.getSize().getWidth() / 2);
            int centerY = point.y + (element.getSize().getHeight() / 2);

            // W3C Pointer Input (TouchAction yerine geçen modern yöntem)
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            ((AppiumDriver) driver).perform(Collections.singletonList(tap));
            
            log("   ✓ Coordinate tap başarılı");
            return true;
        } catch (Exception e) {
            log("   ✗ Coordinate tap başarısız: " + e.getMessage());
        }

        // METHOD 5: SendKeys Enter
        try {
            log("   [5/6] SendKeys Enter deneniyor...");
            element.sendKeys(Keys.ENTER);
            log("   ✓ SendKeys Enter başarılı");
            return true;
        } catch (Exception e) {
            log("   ✗ SendKeys Enter başarısız: " + e.getMessage());
        }

        // METHOD 6: Press and release
        try {
            log("   [6/6] Press and release deneniyor...");
            if (driver instanceof AppiumDriver) {
                Point loc = element.getLocation();
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence press = new Sequence(finger, 1);
                press.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), loc.x, loc.y));
                press.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                press.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                ((AppiumDriver) driver).perform(Collections.singletonList(press));
            }
            log("   ✓ Press and release başarılı");
            return true;
        } catch (Exception e) {
            log("   ✗ Press and release başarısız: " + e.getMessage());
        }

        return false;
    }

    private static void log(String message) {
        System.out.println("[AndroidClickUtils] " + message);
    }
}