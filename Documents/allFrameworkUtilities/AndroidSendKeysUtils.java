package utilities;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AndroidSendKeysUtils {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    /**
     * Ana method - XPath veya ID gibi standart selectorlar ile metin gönderir.
     */
    public static void sendKeys(WebDriver driver, By selector, String text) {
        performAgnosticSendKeys((AndroidDriver) driver, selector, text, DEFAULT_TIMEOUT);
    }

    /**
     * Android için en stabil yöntem olan Accessibility ID (content-desc) ile metin gönderir.
     */
    public static void sendByAccessibilityId(AndroidDriver driver, String accessibilityId, String text) {
        By selector = AppiumBy.accessibilityId(accessibilityId);
        performAgnosticSendKeys(driver, selector, text, DEFAULT_TIMEOUT);
    }

    /**
     * Ortak metin gönderme mantığı - Mobil uyumlu aşamaları içerir.
     */
    private static void performAgnosticSendKeys(AndroidDriver driver, By selector, String text, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            log("🔍 Metin girişi için element aranıyor: " + selector);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(selector));

            // 1. Görünürlük ve Focus işlemleri
            scrollToElement(driver, element);
            element.click();
            Thread.sleep(300); // Odaklanma için kısa bekleme

            // 2. Alanı temizle
            clearFieldWithFallback(driver, element);

            // 3. Metni gönder
            boolean sent = performSendKeysWithFallback(driver, element, text);

            if (sent) {
                log("✅ TEXT GÖNDERİLDİ - '" + text + "' -> " + selector);

                // 4. Doğrulama
                verifyTextEntered((AndroidDriver) element, text);

                // 5. Klavyeyi kapat (Diğer elementlerin üzerini kapatmaması için)
                hideKeyboard(driver);
            } else {
                throw new RuntimeException("❌ Tüm metin gönderme yöntemleri başarısız oldu.");
            }

        } catch (TimeoutException e) {
            log("❌ ZAMAN AŞIMI: Element bulunamadı: " + selector);
            throw e;
        } catch (Exception e) {
            log("❌ HATA: " + e.getMessage());
            throw new RuntimeException("Text gönderme başarısız: " + selector, e);
        }
    }

    private static void clearFieldWithFallback(AndroidDriver driver, WebElement element) {
        try {
            element.clear();
            if (element.getText().isEmpty()) {
                log("   ✓ Standart temizleme başarılı");
                return;
            }
        } catch (Exception e) {
            log("   ⚠️ Standart temizleme başarısız, alternatif deneniyor...");
        }

        // Mobil Fallback: Backspace ile silme
        try {
            String currentText = element.getText();
            if (currentText != null && !currentText.isEmpty()) {
                element.click();
                for (int i = 0; i < currentText.length(); i++) {
                    element.sendKeys(Keys.BACK_SPACE);
                }
                log("   ✓ Backspace ile temizleme yapıldı");
            }
        } catch (Exception e) {
            log("   ⚠️ Backspace temizleme başarısız");
        }
    }

    private static boolean performSendKeysWithFallback(AndroidDriver driver, WebElement element, String text) {
        // METHOD 1: Normal sendKeys
        try {
            log("   [1/4] Normal sendKeys deneniyor...");
            element.sendKeys(text);
            return true;
        } catch (Exception e) {
            log("   ✗ Normal sendKeys başarısız: " + e.getMessage());
        }

        // METHOD 2: Actions sendKeys
        try {
            log("   [2/4] Actions sendKeys deneniyor...");
            new Actions(driver).sendKeys(element, text).perform();
            return true;
        } catch (Exception e) {
            log("   ✗ Actions sendKeys başarısız");
        }

        // METHOD 3: Karakter karakter (En stabil mobil yöntem)
        try {
            log("   [3/4] Karakter karakter gönderimi deneniyor...");
            for (char c : text.toCharArray()) {
                element.sendKeys(String.valueOf(c));
                Thread.sleep(50);
            }
            return true;
        } catch (Exception e) {
            log("   ✗ Karakter karakter başarısız");
        }

        // METHOD 4: JavaScript (Mobil Web/Hybrid desteği için)
        try {
            log("   [4/4] JS value atama deneniyor...");
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1];" +
                            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                    element, text
            );
            return true;
        } catch (Exception e) {
            log("   ✗ JS başarısız");
        }

        return false;
    }

    private static void verifyTextEntered(AndroidDriver element, String expectedText) {
        try {
            String actualText = String.valueOf(element.context(expectedText));
            if (actualText != null && actualText.contains(expectedText)) {
                log("   ✓ Text doğrulandı: '" + actualText + "'");
            } else {
                log("   ⚠️ Doğrulama Uyarısı: Beklenen='" + expectedText + "', Mevcut='" + actualText + "'");
            }
        } catch (Exception e) {
            log("   ⚠️ Doğrulama sırasında hata: " + e.getMessage());
        }
    }

    private static void hideKeyboard(WebDriver driver) {
        try {
            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).hideKeyboard();
                log("   ⌨️ Klavye gizlendi");
            }
        } catch (Exception e) {
            // Klavye zaten kapalı olabilir, sessiz geç
        }
    }

    private static void scrollToElement(AndroidDriver driver, WebElement element) {
        try {
            if (driver instanceof AppiumDriver) {
                String desc = element.getAttribute("content-desc");
                if (desc != null && !desc.isEmpty()) {
                    ((AppiumDriver) driver).findElement(AppiumBy.androidUIAutomator(
                            "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                                    "new UiSelector().description(\"" + desc + "\"))"));
                }
            }
        } catch (Exception e) {
            // Scroll yapılamazsa devam et
        }
    }

    private static void log(String message) {
        System.out.println("[SendKeysUtils] " + message);
    }
}