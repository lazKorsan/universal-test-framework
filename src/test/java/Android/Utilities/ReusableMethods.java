package Android.Utilities;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class ReusableMethods {

    private static final int DEFAULT_TIMEOUT = 15;

    // ==================================================
    // TEMEL ETKİLEŞİMLER (CLICK, SENDKEYS, GETTEXT)
    // ==================================================

    /**
     * Bir elemente tıklamadan önce görünür ve tıklanabilir olmasını bekler.
     * @param locator Elementin konumu (By)
     */
    public static void click(By locator) {
        waitForClickability(locator, DEFAULT_TIMEOUT).click();
    }

    /**
     * Bir elemente metin göndermeden önce görünür olmasını bekler, temizler ve yazar.
     * @param locator Elementin konumu (By)
     * @param text Gönderilecek metin
     */
    public static void sendKeys(By locator, String text) {
        WebElement element = waitForVisibility(locator, DEFAULT_TIMEOUT);
        element.clear();
        element.sendKeys(text);
        hideKeyboard();
    }

    /**
     * Bir elementin metnini alır.
     * @param locator Elementin konumu (By)
     * @return Elementin metni
     */
    public static String getText(By locator) {
        return waitForVisibility(locator, DEFAULT_TIMEOUT).getText();
    }

    /**
     * Klavyeyi kapatır.
     */
    public static void hideKeyboard() {
        try {
            getDriver().hideKeyboard();
        } catch (Exception ignored) {
            // Klavye zaten kapalıysa hata verebilir, yoksayıyoruz.
        }
    }

    // ==================================================
    // BEKLEME METODLARI (WAITS)
    // ==================================================

    /**
     * Belirtilen saniye kadar bekler (Hard Wait).
     * @param saniye Beklenecek süre
     */
    public static void bekle(int saniye) {
        try {
            Thread.sleep(saniye * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wait(int saniye) {
        bekle(saniye);
    }

    public static WebElement waitForVisibility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickability(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ==================================================
    // KAYDIRMA (SCROLL) METODLARI
    // ==================================================

    /**
     * UiScrollable kullanarak belirli bir metne sahip elemente kadar kaydırır.
     * @param text Aranacak metin
     * @return Bulunan element
     */
    public static WebElement scrollToText(String text) {
        String automatorString = String.format(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"%s\").instance(0))",
                text
        );
        return getDriver().findElement(AppiumBy.androidUIAutomator(automatorString));
    }

    /**
     * UiScrollable kullanarak belirli bir ID'ye sahip elemente kadar kaydırır.
     * @param id Aranacak elementin resource-id'si
     */
    public static WebElement scrollToId(String id) {
        String automatorString = String.format(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().resourceId(\"%s\").instance(0))",
                id
        );
        return getDriver().findElement(AppiumBy.androidUIAutomator(automatorString));
    }

    // ==================================================
    // DİĞER YARDIMCI METODLAR
    // ==================================================

    /**
     * Toast mesajını yakalar (Android için).
     * @return Toast mesajının içeriği
     */
    public static String getToastMessage() {
        return waitForVisibility(AppiumBy.xpath("//android.widget.Toast"), 5).getText();
    }

    /**
     * Elementin ekranda var olup olmadığını kontrol eder (Hata fırlatmaz).
     * @param locator Elementin konumu
     * @return Varsa true, yoksa false
     */
    public static boolean isElementPresent(By locator) {
        try {
            getDriver().findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Helper method to get driver easily
    private static AndroidDriver getDriver() {
        return Android.Utilities.AndroidDriver.getDriver();
    }

    public static void clickButtonByDescription(String description) {
        AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();
        WebElement button = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiSelector().description(\"" + description + "\")"));
        button.click();
    }

    public static void scrollWithUiScrollableContentDesc(String elementText) {

        AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();
        driver.findElement(
                AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector()).scrollIntoView(new UiSelector().description(\"" + elementText + "\"));"
                )
        );
    }

    public static void xPathElementClick(String itemName, String reviews, String price) {
        // Inspector'ın verdiği element genellikle tüm bilgiyi içeren View'dır.
        // Bu yüzden direkt o View'ı hedefliyoruz.
        String xpathExpression = String.format("//android.view.View[contains(@content-desc, '%s') and contains(@content-desc, '%s') and contains(@content-desc, '%s')]", itemName, reviews, price);

        AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();
        WebElement element = driver.findElement(AppiumBy.xpath(xpathExpression));
        element.click();
    }

    public static void scrollWithUiScrollableAndClick(String elementText) {
        AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();
          driver.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\"" + elementText + "\"))"));
        driver.findElement(By.xpath("//*[@text='" + elementText + "']")).click();

    }

    public static void KeyBack() {
        AndroidDriver driver = Android.Utilities.AndroidDriver.getDriver();
        // Geri tuşuna basın
        driver.pressKey(new KeyEvent(AndroidKey.BACK));

    }

    public static void goToHomePage(AndroidDriver driver) {

        // Android HOME tuşu
        driver.pressKey(new KeyEvent(AndroidKey.HOME));

        // App’i yeniden foreground’a al
        driver.activateApp(driver.getCurrentPackage());
    }

    // reusable method içine elementin tanımı ile ile uzerine scroll yapıp tıklyacak bir method eklermsiin


}