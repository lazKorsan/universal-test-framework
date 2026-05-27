// SendKeyUtils.java
package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Gelişmiş yazma utilities sınıfı
 * Hem manuel testlerde hem de otomasyon framework'lerinde kullanılabilir
 */
public class SendKeyUtils {

    //  send.sendKeys(By.xpath(ExamplePage.mailBox),"ahmet.student@instulearn.com");
    // ayrıca sınıf seviyesinde

    //    SendKeyUtils send = new SendKeyUtils(driver);

    private final WebDriver driver;
    private Logger logger;
    private final JavascriptExecutor js;
    private final String highlightColor = "yellow";
    private String circleColor = "red";
    private int circleSize = 20;
    private final double defaultDelay = 0.1;
    private final Random random = new Random();

    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public SendKeyUtils(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        setupLogger();
    }

    /**
     * Logger yapılandırması
     */
    private void setupLogger() {
        logger = Logger.getLogger(SendKeyUtils.class.getName());
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
                            "circle.id = 'sendkey_utils_circle';" +
                            "var oldCircle = document.getElementById('sendkey_utils_circle');" +
                            "if (oldCircle) oldCircle.remove();" +
                            "document.body.appendChild(circle);" +
                            "setTimeout(function() {" +
                            "    var circle = document.getElementById('sendkey_utils_circle');" +
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
    public void highlight(WebElement element, String color, double duration,
                          boolean drawCircle, String circleColor) {
        try {
            String originalStyle = element.getAttribute("style");
            if (originalStyle == null) originalStyle = "";

            // Vurgulama
            js.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    element,
                    "border: 3px solid " + color + " !important; background: #ffff99 !important; box-shadow: 0 0 10px " + color + " !important;"
            );

            // Daire çiz
            if (drawCircle) {
                drawCircle(element, circleColor, circleSize, (int)duration);
            }

            Thread.sleep((long)(duration * 1000));

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
        highlight(element, highlightColor, 0.5, true, circleColor);
    }

    /**
     * GELİŞMİŞ YAZMA FONKSİYONU - Her türlü senaryo için
     * @param by Element locator
     * @param text Yazılacak metin
     * @param clearFirst Önce temizleme
     * @param pressEnter Enter tuşuna bas
     * @param humanLike İnsan gibi yavaş yaz
     * @param delay Karakter arası gecikme
     * @param highlightColor Vurgu rengi
     * @param timeout Bekleme süresi
     * @param drawCircle Daire çizilsin mi?
     * @param circleColor Daire rengi
     * @return boolean İşlem başarılı mı?
     */
    public boolean sendKeys(By by, String text, boolean clearFirst, boolean pressEnter,
                            boolean humanLike, double delay, String highlightColor,
                            int timeout, boolean drawCircle, String circleColor) {

        logger.info("\n" + "=".repeat(70));
        logger.info("🚀 Yazma işlemi başlıyor...");
        logger.info("    ├─ Metin: '" + text + "'");
        logger.info("    ├─ Locator: " + by);
        logger.info("    ├─ İnsan gibi: " + humanLike);
        logger.info("    ├─ Enter: " + pressEnter);
        logger.info("    ├─ Daire: " + drawCircle);
        logger.info("    └─ Daire Rengi: " + circleColor);
        logger.info("=".repeat(70));

        try {
            // Elementi bul ve etkileşime hazır olana kadar bekle
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));

            // Elementin etkileşime hazır olmasını bekle
            wait.until(ExpectedConditions.elementToBeClickable(by));

            // Element bilgilerini al
            String tag = element.getTagName();
            String elementType = element.getAttribute("type");
            boolean isDisplayed = element.isDisplayed();
            boolean isEnabled = element.isEnabled();
            String currentValue = element.getAttribute("value");
            if (currentValue == null || currentValue.isEmpty()) {
                currentValue = "[empty]";
            }

            logger.info("🔍 Element bilgileri:");
            logger.info("    ├─ Tag: <" + tag + ">");
            logger.info("    ├─ Type: " + (elementType != null ? elementType : "N/A"));
            logger.info("    ├─ Görünür: " + isDisplayed);
            logger.info("    ├─ Etkin: " + isEnabled);
            logger.info("    └─ Mevcut değer: '" + currentValue + "'");

            // Elementi vurgula ve daire çiz
            highlight(element, highlightColor, 0.3, drawCircle, circleColor);

            // ============= YAZMA YÖNTEMLERİ =============

            // YÖNTEM 1: Normal sendKeys
            try {
                if (clearFirst) {
                    element.clear();
                    Thread.sleep(200);
                }

                // Elemente tıkla (odaklan)
                try {
                    element.click();
                } catch (Exception e) {
                    js.executeScript("arguments[0].focus();", element);
                }

                if (humanLike) {
                    // İnsan gibi yazma
                    for (char c : text.toCharArray()) {
                        element.sendKeys(String.valueOf(c));
                        Thread.sleep((long)(random.nextDouble() * 100 + 50));
                    }
                } else {
                    element.sendKeys(text);
                }

                if (pressEnter) {
                    element.sendKeys(Keys.RETURN);
                    logger.info("✅ Enter tuşuna basıldı");
                }

                logger.info("✅ Yöntem 1 (Normal sendKeys) BAŞARILI");
                return true;

            } catch (ElementNotInteractableException e) {
                logger.warning("⚠ Yöntem 1 başarısız - Element etkileşime kapalı");
            } catch (Exception e) {
                logger.warning("⚠ Yöntem 1 başarısız - " + e.getMessage());
            }

            // YÖNTEM 2: JavaScript ile yazma
            try {
                if (clearFirst) {
                    js.executeScript("arguments[0].value = '';", element);
                }

                // Metni JavaScript ile yaz
                js.executeScript("arguments[0].value = arguments[1];", element, text);

                // Input event'ini tetikle
                js.executeScript(
                        "var event = new Event('input', { bubbles: true });" +
                                "arguments[0].dispatchEvent(event);" +
                                "var changeEvent = new Event('change', { bubbles: true });" +
                                "arguments[0].dispatchEvent(changeEvent);", element);

                if (pressEnter) {
                    js.executeScript(
                            "var event = new KeyboardEvent('keydown', {" +
                                    "    key: 'Enter'," +
                                    "    code: 'Enter'," +
                                    "    keyCode: 13," +
                                    "    which: 13," +
                                    "    bubbles: true" +
                                    "});" +
                                    "arguments[0].dispatchEvent(event);", element);
                }

                logger.info("✅ Yöntem 2 (JavaScript) BAŞARILI");
                return true;
            } catch (Exception e) {
                logger.warning("⚠ Yöntem 2 başarısız - " + e.getMessage());
            }

            // YÖNTEM 3: ActionChains ile
            try {
                Actions actions = new Actions(driver);
                actions.moveToElement(element).click();

                if (clearFirst) {
                    // Tüm metni seç ve sil
                    actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL);
                    actions.sendKeys(Keys.DELETE);
                }

                // Metni yaz
                if (humanLike) {
                    for (char c : text.toCharArray()) {
                        actions.sendKeys(String.valueOf(c));
                        actions.pause(Duration.ofMillis(random.nextLong(50, 150)));
                    }
                } else {
                    actions.sendKeys(text);
                }

                if (pressEnter) {
                    actions.sendKeys(Keys.RETURN);
                }

                actions.perform();
                logger.info("✅ Yöntem 3 (ActionChains) BAŞARILI");
                return true;
            } catch (Exception e) {
                logger.warning("⚠ Yöntem 3 başarısız - " + e.getMessage());
            }

            // YÖNTEM 4: disabled/readonly kaldır + yaz
            try {
                // Disabled ve readonly attribute'lerini kaldır
                js.executeScript(
                        "arguments[0].disabled = false;" +
                                "arguments[0].removeAttribute('readonly');" +
                                "arguments[0].removeAttribute('aria-disabled');", element);
                Thread.sleep(200);

                if (clearFirst) {
                    element.clear();
                }

                // Odaklan ve yaz
                js.executeScript("arguments[0].focus();", element);
                element.sendKeys(text);

                logger.info("✅ Yöntem 4 (Disabled kaldır) BAŞARILI");
                return true;
            } catch (Exception e) {
                // Ignore
            }

            // YÖNTEM 5: setAttribute ile yaz
            try {
                // Direkt value attribute'ünü set et
                js.executeScript("arguments[0].setAttribute('value', arguments[1]);", element, text);

                // Event'leri tetikle
                js.executeScript(
                        "['input', 'change', 'blur'].forEach(eventType => {" +
                                "    var event = new Event(eventType, { bubbles: true });" +
                                "    arguments[0].dispatchEvent(event);" +
                                "});", element);

                logger.info("✅ Yöntem 5 (setAttribute) BAŞARILI");
                return true;
            } catch (Exception e) {
                // Ignore
            }

            // YÖNTEM 6: Karakter karakter JavaScript ile
            try {
                js.executeScript("arguments[0].focus();", element);

                for (char c : text.toCharArray()) {
                    js.executeScript(
                            "arguments[0].value += arguments[1];" +
                                    "var event = new Event('input', { bubbles: true });" +
                                    "arguments[0].dispatchEvent(event);", element, String.valueOf(c));
                    Thread.sleep(50);
                }

                logger.info("✅ Yöntem 6 (Karakter karakter JS) BAŞARILI");
                return true;
            } catch (Exception e) {
                // Ignore
            }

            logger.severe("❌ TÜM YÖNTEMLER BAŞARISIZ!");
            return false;

        } catch (Exception e) {
            logger.severe("❌ Element bulunamadı: " + by);
            return tryAlternativeLocators(by, text, clearFirst, pressEnter,
                    humanLike, delay, highlightColor, timeout,
                    drawCircle, circleColor);
        }
    }

    /**
     * sendKeys metodunun overload versiyonu (varsayılan değerlerle)
     */
    public boolean sendKeys(By by, String text) {
        return sendKeys(by, text, true, false, false, defaultDelay,
                highlightColor, 10, true, circleColor);
    }

    /**
     * Alternatif locator'ları dener
     */
    private boolean tryAlternativeLocators(By originalBy, String text,
                                           boolean clearFirst, boolean pressEnter,
                                           boolean humanLike, double delay,
                                           String highlightColor, int timeout,
                                           boolean drawCircle, String circleColor) {

        List<By> altLocators = new ArrayList<>();
        altLocators.add(originalBy);

        if (originalBy instanceof By.ByXPath) {
            String xpath = originalBy.toString().replace("By.xpath: ", "");
            altLocators.add(By.xpath(xpath + "[1]"));
            altLocators.add(By.xpath("(" + xpath + ")[1]"));
            altLocators.add(By.xpath(xpath.replace("input", "div")));
            altLocators.add(By.xpath(xpath.replace("@type='text'", "")));
            altLocators.add(By.xpath(xpath.replace("@type='email'", "")));
            altLocators.add(By.xpath(xpath.replace("@type='password'", "")));

            // Metin içeren alternatifler
            if (xpath.contains("'")) {
                try {
                    String extractedText = xpath.split("'")[1];
                    altLocators.add(By.xpath("//*[contains(@placeholder, '" + extractedText + "')]"));
                    altLocators.add(By.xpath("//*[contains(@name, '" + extractedText + "')]"));
                    altLocators.add(By.xpath("//*[contains(@id, '" + extractedText + "')]"));
                } catch (Exception e) {
                    // Ignore
                }
            }
        }

        int attempt = 1;
        for (By altBy : altLocators.stream().distinct().limit(5).toList()) {
            try {
                logger.info("🔄 Alternatif " + attempt + " deneniyor: " + altBy);

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(altBy));

                highlight(element, "orange", 0.3, drawCircle, circleColor);

                if (clearFirst) {
                    element.clear();
                }

                element.sendKeys(text);

                if (pressEnter) {
                    element.sendKeys(Keys.RETURN);
                }

                logger.info("✅ Alternatif " + attempt + " BAŞARILI!");
                return true;
            } catch (Exception e) {
                attempt++;
            }
        }

        return false;
    }

    // ============= ÖZEL KULLANIM METODLARI =============

    /**
     * ID ile elemente yaz
     */
    public boolean byId(String elementId, String text, Object... options) {
        return sendKeys(By.id(elementId), text);
    }

    /**
     * Name attribute ile elemente yaz
     */
    public boolean byName(String name, String text, Object... options) {
        return sendKeys(By.name(name), text);
    }

    /**
     * Class name ile elemente yaz
     */
    public boolean byClassName(String className, String text, Object... options) {
        return sendKeys(By.className(className), text);
    }

    /**
     * CSS Selector ile elemente yaz
     */
    public boolean byCss(String cssSelector, String text, Object... options) {
        return sendKeys(By.cssSelector(cssSelector), text);
    }

    /**
     * Placeholder ile elemente yaz
     */
    public boolean byPlaceholder(String placeholderText, String text, Object... options) {
        return sendKeys(
                By.xpath("//input[@placeholder='" + placeholderText + "'] | //textarea[@placeholder='" + placeholderText + "']"),
                text
        );
    }

    /**
     * Label metnine göre input bul ve yaz
     */
    public boolean byLabel(String labelText, String text, Object... options) {
        String xpath = "//label[contains(text(), '" + labelText + "')]/following::input[1] | " +
                "//label[contains(text(), '" + labelText + "')]/following::textarea[1]";
        return sendKeys(By.xpath(xpath), text);
    }

    /**
     * Rastgele metin oluştur ve yaz
     */
    public boolean randomText(By by, int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        String randomText = sb.toString();
        logger.info("🎲 Rastgele metin: '" + randomText + "'");
        return sendKeys(by, randomText);
    }

    /**
     * Rastgele metin oluştur ve yaz (varsayılan length=10)
     */
    public boolean randomText(By by) {
        return randomText(by, 10);
    }

    /**
     * Güçlü şifre oluştur ve yaz
     */
    public boolean password(By by) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%&*";

        StringBuilder password = new StringBuilder();
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        // 8 karakter daha ekle
        String allChars = upper + lower + digits;
        for (int i = 0; i < 8; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Karakterleri karıştır
        List<Character> chars = new ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            chars.add(c);
        }
        Collections.shuffle(chars);

        StringBuilder finalPassword = new StringBuilder();
        for (char c : chars) {
            finalPassword.append(c);
        }

        logger.info("🔐 Şifre oluşturuldu: " + "*".repeat(finalPassword.length()));
        return sendKeys(by, finalPassword.toString());
    }

    /**
     * Rastgele email oluştur ve yaz
     */
    public boolean email(By by, String prefix) {
        String[] domains = {"example.com", "test.com", "demo.com", "instulearn.com", "mail.com"};
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder randomStr = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            randomStr.append(chars.charAt(random.nextInt(chars.length())));
        }

        String email = prefix + "." + randomStr.toString() + "@" +
                domains[random.nextInt(domains.length)];

        logger.info("📧 Email oluşturuldu: " + email);
        return sendKeys(by, email);
    }

    /**
     * Rastgele email oluştur ve yaz (varsayılan prefix=test)
     */
    public boolean email(By by) {
        return email(by, "test");
    }

    /**
     * Rastgele telefon numarası oluştur ve yaz
     */
    public boolean phone(By by) {
        int part1 = 50 + random.nextInt(50); // 50-99 arası
        int part2 = 100 + random.nextInt(900); // 100-999 arası
        int part3 = 1000 + random.nextInt(9000); // 1000-9999 arası

        String phone = "5" + part1 + part2 + part3;
        logger.info("📱 Telefon: " + phone);
        return sendKeys(by, phone);
    }

    /**
     * Tarih gir (bugün veya offset'li)
     */
    public boolean date(By by, int daysOffset, String dateFormat) {
        LocalDate date = LocalDate.now().plusDays(daysOffset);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String dateStr = date.format(formatter);

        logger.info("📅 Tarih: " + dateStr);
        return sendKeys(by, dateStr);
    }

    /**
     * Tarih gir (varsayılan format: dd.MM.yyyy)
     */
    public boolean date(By by, int daysOffset) {
        return date(by, daysOffset, "dd.MM.yyyy");
    }

    /**
     * Bugünün tarihini gir
     */
    public boolean today(By by) {
        return date(by, 0);
    }

    /**
     * Elementi temizle
     */
    public boolean clear(By by) {
        return sendKeys(by, "", true, false, false, defaultDelay,
                highlightColor, 10, true, circleColor);
    }

    /**
     * Mevcut değerin sonuna ekle
     */
    public boolean append(By by, String text) {
        return sendKeys(by, text, false, false, false, defaultDelay,
                highlightColor, 10, true, circleColor);
    }

    /**
     * Daire özelliklerini ayarla
     */
    public void setCircleProperties(String color, int size) {
        this.circleColor = color;
        this.circleSize = size;
        logger.info("⚙ Daire özellikleri ayarlandı - Renk: " + color + ", Boyut: " + size + "px");
    }
}