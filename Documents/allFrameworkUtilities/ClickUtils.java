// ClickUtils.java
package utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Gelişmiş tıklama utilities sınıfı
 * Hem manuel testlerde hem de otomasyon framework'lerinde kullanılabilir
 */
public class ClickUtils {

    private final WebDriver driver;
    private Logger logger;
    private final String highlightColor = "yellow";
    private String circleColor = "red";
    private int circleSize = 20;
    private final JavascriptExecutor js;

    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public ClickUtils(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        setupLogger();
    }

    /**
     * Logger yapılandırması
     */
    private void setupLogger() {
        logger = Logger.getLogger(ClickUtils.class.getName());
        logger.setLevel(Level.INFO);
    }

    /**
     * Elementin merkezine daire çizer
     * @param element WebElement
     * @param color Daire rengi
     * @param size Daire boyutu
     * @param duration Gösterim süresi (saniye)
     */
    private void drawCircle(WebElement element, String color, int size, int duration) {
        try {
            Point location = element.getLocation();
            Dimension dimension = element.getSize();

            int centerX = location.getX() + dimension.getWidth() / 2;
            int centerY = location.getY() + dimension.getHeight() / 2;

            String circleScript = String.format(
                    "var circle = document.createElement('div');" +
                            "circle.style.position = 'absolute';" +
                            "circle.style.left = '%dpx';" +
                            "circle.style.top = '%dpx';" +
                            "circle.style.width = '%dpx';" +
                            "circle.style.height = '%dpx';" +
                            "circle.style.borderRadius = '50%%';" +
                            "circle.style.border = '3px solid %s';" +
                            "circle.style.backgroundColor = 'transparent';" +
                            "circle.style.zIndex = '9999';" +
                            "circle.style.pointerEvents = 'none';" +
                            "circle.style.boxShadow = '0 0 10px %s';" +
                            "circle.id = 'click_utils_circle';" +
                            "var oldCircle = document.getElementById('click_utils_circle');" +
                            "if (oldCircle) oldCircle.remove();" +
                            "document.body.appendChild(circle);" +
                            "setTimeout(function() {" +
                            "    var circle = document.getElementById('click_utils_circle');" +
                            "    if (circle) circle.remove();" +
                            "}, %d);",
                    centerX - size/2, centerY - size/2, size, size, color, color, duration * 1000
            );

            js.executeScript(circleScript);
            logger.info("🔴 Daire çizildi - Renk: " + color + ", Boyut: " + size + "px");
        } catch (Exception e) {
            logger.warning("Daire çizilemedi: " + e.getMessage());
        }
    }

    /**
     * Elementi vurgula ve isteğe bağlı daire çiz
     * @param element WebElement
     * @param color Vurgu rengi
     * @param duration Gösterim süresi
     * @param drawCircle Daire çizilsin mi?
     * @param circleColor Daire rengi
     */
    public void highlight(WebElement element, String color, int duration,
                          boolean drawCircle, String circleColor) {
        try {
            String originalStyle = element.getAttribute("style");

            // Vurgulama
            js.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    element,
                    "border: 3px solid " + color + "; background: #ffff99; box-shadow: 0 0 10px " + color + ";"
            );

            // Daire çiz
            if (drawCircle) {
                drawCircle(element, circleColor, circleSize, duration);
            }

            Thread.sleep(duration * 1000L);

            // Orijinal stili geri yükle
            js.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    element,
                    originalStyle
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warning("Highlight kesintiye uğradı: " + e.getMessage());
        }
    }

    /**
     * Elementi vurgula (varsayılan değerlerle)
     */
    public void highlight(WebElement element) {
        highlight(element, highlightColor, 1, true, circleColor);
    }

    /**
     * İnatçı butonlar için gelişmiş tıklama fonksiyonu
     * @param by Element locator
     * @param color Vurgu rengi
     * @param timeout Bekleme süresi (saniye)
     * @param drawCircle Daire çizilsin mi?
     * @param circleColor Daire rengi
     * @return boolean Tıklama başarılı mı?
     */
    public boolean click(By by, String color, int timeout, boolean drawCircle, String circleColor) {
        logger.info("\n" + "=".repeat(60));
        logger.info("🚀 Buton tıklama denemesi başlıyor...");
        logger.info("    ├─ Locator: " + by);
        logger.info("    ├─ Renk: " + color);
        logger.info("    └─ Timeout: " + timeout + "sn");
        logger.info("=".repeat(60));

        try {
            // Elementi bul ve tıklanabilir olana kadar bekle
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));

            // Element bilgilerini al
            boolean visible = element.isDisplayed();
            boolean enabled = element.isEnabled();
            String text = element.getText().trim();
            if (text.isEmpty()) {
                text = element.getAttribute("value");
            }
            if (text == null || text.isEmpty()) {
                text = element.getAttribute("innerText");
            }
            if (text == null || text.isEmpty()) {
                text = "NoText";
            }
            String tag = element.getTagName();

            logger.info("🔍 Buton bilgileri:");
            logger.info("    ├─ Tag: <" + tag + ">");
            logger.info("    ├─ Text: '" + text + "'");
            logger.info("    ├─ Görünür: " + visible);
            logger.info("    └─ Tıklanabilir: " + enabled);

            // Elementi vurgula ve daire çiz
            highlight(element, color, 1, drawCircle, circleColor);

            // ============= TIKLAMA YÖNTEMLERİ =============

            // YÖNTEM 1: Normal click
            try {
                element.click();
                logger.info("✅ Yöntem 1 (Normal click) BAŞARILI: '" + text + "'");
                return true;
            } catch (ElementClickInterceptedException e) {
                logger.warning("⚠ Yöntem 1 başarısız - Element başka element tarafından engelleniyor");
            } catch (Exception e) {
                logger.warning("⚠ Yöntem 1 başarısız - " + e.getMessage());
            }

            // YÖNTEM 2: JavaScript click
            try {
                js.executeScript("arguments[0].click();", element);
                logger.info("✅ Yöntem 2 (JavaScript click) BAŞARILI: '" + text + "'");
                return true;
            } catch (Exception e) {
                logger.warning("⚠ Yöntem 2 başarısız - " + e.getMessage());
            }

            // YÖNTEM 3: ActionChains
            try {
                Actions actions = new Actions(driver);
                actions.moveToElement(element).click().perform();
                logger.info("✅ Yöntem 3 (ActionChains) BAŞARILI: '" + text + "'");
                return true;
            } catch (Exception e) {
                logger.warning("⚠ Yöntem 3 başarısız - " + e.getMessage());
            }

            // YÖNTEM 4: Scroll + click
            try {
                js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
                Thread.sleep(500);
                element.click();
                logger.info("✅ Yöntem 4 (Scroll + click) BAŞARILI: '" + text + "'");
                return true;
            } catch (Exception e) {
                logger.warning("⚠ Yöntem 4 başarısız - " + e.getMessage());
            }

            // YÖNTEM 5: Submit (eğer form elementi ise)
            if (tag.equals("button") || tag.equals("input")) {
                try {
                    element.submit();
                    logger.info("✅ Yöntem 5 (Submit) BAŞARILI: '" + text + "'");
                    return true;
                } catch (Exception e) {
                    // Ignore
                }
            }

            // YÖNTEM 6: Parent click
            try {
                WebElement parent = (WebElement) js.executeScript("return arguments[0].parentNode;", element);
                if (parent != null) {
                    js.executeScript("arguments[0].click();", parent);
                    logger.info("✅ Yöntem 6 (Parent click) BAŞARILI: '" + text + "'");
                    return true;
                }
            } catch (Exception e) {
                // Ignore
            }

            // YÖNTEM 7: disabled kaldır + click
            try {
                js.executeScript("arguments[0].disabled = false;", element);
                Thread.sleep(200);
                element.click();
                logger.info("✅ Yöntem 7 (Disabled kaldır + click) BAŞARILI: '" + text + "'");
                return true;
            } catch (Exception e) {
                // Ignore
            }

            // YÖNTEM 8: readonly kaldır + click
            try {
                js.executeScript("arguments[0].removeAttribute('readonly');", element);
                Thread.sleep(200);
                element.click();
                logger.info("✅ Yöntem 8 (Readonly kaldır + click) BAŞARILI: '" + text + "'");
                return true;
            } catch (Exception e) {
                // Ignore
            }

            // YÖNTEM 9: Mouse event
            try {
                js.executeScript(
                        "var event = new MouseEvent('click', {" +
                                "    view: window," +
                                "    bubbles: true," +
                                "    cancelable: true" +
                                "});" +
                                "arguments[0].dispatchEvent(event);", element);
                logger.info("✅ Yöntem 9 (Mouse Event) BAŞARILI: '" + text + "'");
                return true;
            } catch (Exception e) {
                // Ignore
            }

            logger.severe("❌ TÜM YÖNTEMLER BAŞARISIZ! Butona tıklanamadı: '" + text + "'");
            return false;

        } catch (Exception e) {
            logger.severe("❌ Element bulunamadı veya tıklanabilir değil: " + by);
            return tryAlternativeLocators(by, drawCircle, circleColor);
        }
    }

    /**
     * Click metodunun overload versiyonu (varsayılan değerlerle)
     */
    public boolean click(By by) {
        return click(by, highlightColor, 10, true, circleColor);
    }

    /**
     * Alternatif locator'ları dener
     */
    private boolean tryAlternativeLocators(By originalBy, boolean drawCircle, String circleColor) {
        String originalXpath = originalBy.toString();
        String extractedText = "";

        if (originalXpath.contains("'")) {
            try {
                extractedText = originalXpath.split("'")[1];
            } catch (Exception e) {
                // Ignore
            }
        }

        List<By> altLocators = new ArrayList<>();
        altLocators.add(originalBy);

        // XPATH alternatifleri
        if (originalBy instanceof By.ByXPath) {
            String xpath = originalXpath.replace("By.xpath: ", "");
            altLocators.add(By.xpath(xpath + "[1]"));
            altLocators.add(By.xpath("(" + xpath + ")[1]"));
            altLocators.add(By.xpath(xpath.replace("button", "div")));
            altLocators.add(By.xpath(xpath.replace("@type='submit'", "@type='button'")));

            if (!extractedText.isEmpty()) {
                altLocators.add(By.xpath("//*[contains(text(), '" + extractedText + "')]"));
                altLocators.add(By.xpath("//*[contains(@value, '" + extractedText + "')]"));
            }
        }

        int attempt = 1;
        for (By altBy : altLocators.stream().distinct().limit(5).toList()) {
            try {
                logger.info("🔄 Alternatif " + attempt + " deneniyor: " + altBy);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(altBy));

                highlight(element, "orange", 1, drawCircle, circleColor);
                element.click();

                logger.info("✅ Alternatif " + attempt + " BAŞARILI!");
                return true;
            } catch (Exception e) {
                attempt++;
            }
        }

        return false;
    }

    /**
     * Buton metnine göre tıkla
     */
    public boolean clickByText(String text, String tag) {
        By by = By.xpath("//" + tag + "[contains(text(), '" + text + "')]");
        logger.info("🔍 Metin ile buton aranıyor: '" + text + "'");
        return click(by, "purple", 10, true, "red");
    }

    /**
     * Buton metnine göre tıkla (varsayılan tag=button)
     */
    public boolean clickByText(String text) {
        return clickByText(text, "button");
    }

    /**
     * CSS Selector ile tıkla
     */
    public boolean clickByCss(String cssSelector) {
        logger.info("🔍 CSS Selector ile buton aranıyor: " + cssSelector);

        try {
            By by = By.cssSelector(cssSelector);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));

            highlight(element, "blue", 1, true, "red");
            element.click();

            logger.info("✅ CSS Selector tıklama başarılı!");
            return true;
        } catch (Exception e) {
            return click(By.xpath("//*[@id='" + cssSelector + "']"), "blue", 10, true, "red");
        }
    }

    /**
     * Checkbox tıklama
     */
    public boolean clickCheckbox(By by) {
        return clickCheckbox(by, null, "green", 10, true, "blue");
    }

    /**
     * Checkbox tıklama (gelişmiş)
     */
    public boolean clickCheckbox(By by, WebElement element, String color,
                                 int timeout, boolean drawCircle, String circleColor) {
        logger.info("\n" + "=".repeat(70));
        logger.info("🎯 Checkbox tıklama başlıyor...");
        logger.info("=".repeat(70));

        WebElement targetElement = element;

        // Element verilmemişse By'dan bul
        if (targetElement == null && by != null) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
                targetElement = wait.until(ExpectedConditions.presenceOfElementLocated(by));
            } catch (Exception e) {
                logger.severe("❌ Checkbox bulunamadı: " + by);
                return false;
            }
        }

        if (targetElement == null) {
            return false;
        }

        // Lambda ifadelerinde kullanmak için final değişken
        final WebElement finalElement = targetElement;

        // Elementi vurgula ve daire çiz
        highlight(finalElement, color, 1, drawCircle, circleColor);

        String elementId = finalElement.getAttribute("id");

        // Tüm tıklama yöntemlerini dene
        List<ClickMethod> methods = Arrays.asList(
                // 1. Direkt checkbox'a tıkla
                finalElement::click,

                // 2. JavaScript ile tıkla
                () -> js.executeScript("arguments[0].click();", finalElement),

                // 3. Label'a tıkla
                () -> {
                    if (elementId != null && !elementId.isEmpty()) {
                        driver.findElement(By.xpath("//label[@for='" + elementId + "']")).click();
                    } else {
                        throw new Exception("Element ID yok");
                    }
                },

                // 4. Parent click
                () -> js.executeScript("arguments[0].parentNode.click();", finalElement),

                // 5. Grandparent click
                () -> js.executeScript("arguments[0].parentNode.parentNode.click();", finalElement),

                // 6. ActionChains
                () -> new Actions(driver).moveToElement(finalElement).click().perform(),

                // 7. Space tuşu
                () -> finalElement.sendKeys(Keys.SPACE),

                // 8. Enter tuşu
                () -> finalElement.sendKeys(Keys.ENTER),

                // 9. Manuel set et
                () -> js.executeScript(
                        "arguments[0].checked = true; " +
                                "arguments[0].dispatchEvent(new Event('change', {bubbles: true})); " +
                                "arguments[0].dispatchEvent(new Event('click', {bubbles: true}));", finalElement),

                // 10. Merkezden tıkla
                () -> {
                    Dimension size = finalElement.getSize();
                    new Actions(driver)
                            .moveToElement(finalElement, size.getWidth()/2, size.getHeight()/2)
                            .click()
                            .perform();
                }
        );

        int methodIndex = 1;
        for (ClickMethod method : methods) {
            try {
                method.execute();
                Thread.sleep(200);

                // Checkbox seçildi mi kontrol et
                Boolean isChecked = (Boolean) js.executeScript("return arguments[0].checked;", finalElement);

                if (isChecked != null && isChecked) {
                    logger.info("✅ Yöntem " + methodIndex + " BAŞARILI! Checkbox seçildi.");
                    highlight(finalElement, "green", 1, drawCircle, "green");
                    return true;
                } else {
                    logger.warning("⚠ Yöntem " + methodIndex + " çalıştı ama checkbox seçilmedi?");
                }
            } catch (Exception e) {
                logger.warning("⚠ Yöntem " + methodIndex + " başarısız: " + e.getMessage());
            }
            methodIndex++;
        }

        // SON ÇARE: Tüm checkbox'ları dene
        return tryAllCheckboxes(drawCircle, circleColor);
    }

    /**
     * Son çare olarak tüm checkbox'ları dener
     */
    private boolean tryAllCheckboxes(boolean drawCircle, String circleColor) {
        try {
            logger.info("🔄 SON ÇARE: Tüm checkbox'lar taranıyor...");
            List<WebElement> allCheckboxes = driver.findElements(By.xpath("//input[@type='checkbox']"));

            int index = 0;
            for (WebElement cb : allCheckboxes) {
                try {
                    js.executeScript(
                            "arguments[0].checked = true; " +
                                    "arguments[0].dispatchEvent(new Event('change', {bubbles: true}));", cb);

                    if (drawCircle) {
                        drawCircle(cb, circleColor, circleSize, 1);
                    }

                    logger.info("✅ Son çare - Checkbox " + (index + 1) + " seçildi!");
                    return true;
                } catch (Exception e) {
                    index++;
                }
            }
        } catch (Exception e) {
            // Ignore
        }

        logger.severe("❌ TÜM YÖNTEMLER BAŞARISIZ!");
        return false;
    }

    /**
     * Terms checkbox'ı için özel metod
     */
    public boolean clickTermsCheckbox() {
        List<By> xpaths = Arrays.asList(
                By.xpath("//input[@type='checkbox' and @id='term']"),
                By.xpath("//input[@type='checkbox' and @name='term']"),
                By.xpath("//label[@for='term']"),
                By.xpath("//div[contains(@class, 'custom-checkbox')]"),
                By.xpath("//span[contains(@class, 'custom-control')]"),
                By.xpath("//label[contains(text(), 'I agree')]"),
                By.xpath("//label[contains(text(), 'terms')]/.."),
                By.xpath("//label[contains(text(), 'kabul ediyorum')]/.."),
                By.xpath("(//input[@type='checkbox'])[1]"),
                By.xpath("(//input[@type='checkbox'])[last()]")
        );

        for (By xpath : xpaths) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(xpath));

                // Eğer element label veya div ise, içindeki checkbox'ı bul
                String tagName = element.getTagName();
                if (tagName.equals("label") || tagName.equals("div") || tagName.equals("span")) {
                    List<WebElement> checkboxes = element.findElements(By.xpath(".//input[@type='checkbox']"));
                    if (!checkboxes.isEmpty()) {
                        return clickCheckbox(null, checkboxes.get(0), "green", 10, true, "blue");
                    }
                }

                // Direkt tıkla
                return clickCheckbox(null, element, "green", 10, true, "blue");

            } catch (Exception e) {
                // Ignore
            }
        }

        return false;
    }

    /**
     * Daire özelliklerini ayarla
     */
    public void setCircleProperties(String color, int size) {
        this.circleColor = color;
        this.circleSize = size;
        logger.info("⚙ Daire özellikleri ayarlandı - Renk: " + color + ", Boyut: " + size + "px");
    }

    /**
     * Functional interface for click methods
     */
    @FunctionalInterface
    private interface ClickMethod {
        void execute() throws Exception;
    }
}