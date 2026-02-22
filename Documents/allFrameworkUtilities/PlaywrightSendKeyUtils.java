// PlaywrightSendKeyUtils.java
package utilities;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.BoundingBox;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Playwright için gelişmiş yazma utilities sınıfı
 */
public class PlaywrightSendKeyUtils {

    private final Page page;
    private Logger logger;
    private final int circleSize = 20;
    private final Random random = new Random();

    public PlaywrightSendKeyUtils(Page page) {
        this.page = page;
        setupLogger();
    }

    private void setupLogger() {
        logger = Logger.getLogger(PlaywrightSendKeyUtils.class.getName());
        logger.setLevel(Level.INFO);
    }

    /**
     * Daire çiz
     */
    private void drawCircle(Locator locator, String color, int size, int duration) {
        try {
            BoundingBox box = locator.boundingBox();
            if (box == null) return;

            double centerX = box.x + box.width / 2;
            double centerY = box.y + box.height / 2;

            page.evaluate(String.format(
                    "([x, y]) => {" +
                            "  const circle = document.createElement('div');" +
                            "  circle.style.position = 'absolute';" +
                            "  circle.style.left = (x - %d/2) + 'px';" +
                            "  circle.style.top = (y - %d/2) + 'px';" +
                            "  circle.style.width = '%dpx';" +
                            "  circle.style.height = '%dpx';" +
                            "  circle.style.borderRadius = '50%%';" +
                            "  circle.style.border = '3px solid %s';" +
                            "  circle.style.backgroundColor = 'transparent';" +
                            "  circle.style.zIndex = '9999';" +
                            "  circle.style.pointerEvents = 'none';" +
                            "  circle.style.boxShadow = '0 0 10px %s';" +
                            "  circle.id = 'playwright_sendkey_circle';" +
                            "  document.body.appendChild(circle);" +
                            "  setTimeout(() => circle.remove(), %d);" +
                            "}", size, size, size, size, color, color, duration * 1000), Arrays.asList(centerX, centerY));

            logger.info("🔴 Daire çizildi");
        } catch (Exception e) {
            logger.warning("Daire çizilemedi: " + e.getMessage());
        }
    }

    /**
     * Gelişmiş yazma fonksiyonu
     */
    public boolean sendKeys(String selector, SendKeysOptions options) {
        logger.info("\n" + "=".repeat(70));
        logger.info("🚀 Yazma işlemi başlıyor...");
        logger.info("    ├─ Metin: '" + options.text + "'");
        logger.info("    ├─ Selector: " + selector);
        logger.info("    ├─ İnsan gibi: " + options.humanLike);
        logger.info("    ├─ Enter: " + options.pressEnter);
        logger.info("    └─ Daire: " + options.drawCircle);
        logger.info("=".repeat(70));

        try {
            Locator locator = page.locator(selector).first();

            // Element bilgilerini al
            String tag = locator.evaluate("el => el.tagName.toLowerCase()").toString();
            String type = locator.getAttribute("type");
            boolean isVisible = locator.isVisible();
            boolean isEnabled = locator.isEnabled();
            String currentValue = locator.inputValue();

            logger.info("🔍 Element bilgileri:");
            logger.info("    ├─ Tag: <" + tag + ">");
            logger.info("    ├─ Type: " + (type != null ? type : "N/A"));
            logger.info("    ├─ Görünür: " + isVisible);
            logger.info("    ├─ Etkin: " + isEnabled);
            logger.info("    └─ Mevcut değer: '" + currentValue + "'");

            // Vurgula ve daire çiz
            if (options.drawCircle) {
                drawCircle(locator, options.circleColor, circleSize, 1);
            }

            // ============= YAZMA YÖNTEMLERİ =============

            // YÖNTEM 1: fill() - En hızlı ve önerilen yöntem
            if (!options.humanLike && !options.pressEnter) {
                if (options.clearFirst) {
                    locator.fill(options.text);
                } else {
                    String current = locator.inputValue();
                    locator.fill(current + options.text);
                }
                logger.info("✅ fill() BAŞARILI");
                return true;
            }

            // YÖNTEM 2: pressSequentially() - İnsan gibi yazma
            if (options.humanLike) {
                locator.click(); // Önce odaklan
                if (options.clearFirst) {
                    locator.clear();
                }

                Locator.PressSequentiallyOptions pressOptions = new Locator.PressSequentiallyOptions()
                        .setDelay(options.delay > 0 ? options.delay / 2 : 50.0);

                locator.pressSequentially(options.text, pressOptions);

                if (options.pressEnter) {
                    locator.press("Enter");
                }

                logger.info("✅ pressSequentially() BAŞARILI");
                return true;
            }

            // YÖNTEM 3: Keyboard API
            if (options.useKeyboard) {
                locator.click(); // Odaklan

                if (options.clearFirst) {
                    // Tümünü seç ve sil
                    page.keyboard().press("Control+A");
                    page.keyboard().press("Backspace");
                }

                // Karakter karakter yaz
                for (char c : options.text.toCharArray()) {
                    page.keyboard().type(String.valueOf(c));
                    if (options.delay > 0) {
                        Thread.sleep((long)options.delay);
                    }
                }

                if (options.pressEnter) {
                    page.keyboard().press("Enter");
                }

                logger.info("✅ Keyboard API BAŞARILI");
                return true;
            }

            // YÖNTEM 4: JavaScript ile
            try {
                if (options.clearFirst) {
                    locator.evaluate("el => el.value = ''");
                }
                locator.evaluate("(el, text) => { el.value += text; el.dispatchEvent(new Event('input')); }", options.text);

                if (options.pressEnter) {
                    locator.press("Enter");
                }

                logger.info("✅ JavaScript BAŞARILI");
                return true;
            } catch (Exception e) {
                logger.warning("⚠ JavaScript başarısız");
            }

            logger.severe("❌ TÜM YÖNTEMLER BAŞARISIZ!");
            return false;

        } catch (Exception e) {
            logger.severe("❌ Yazma hatası: " + e.getMessage());
            return false;
        }
    }

    /**
     * Basit kullanım
     */
    public boolean sendKeys(String selector, String text) {
        return sendKeys(selector, new SendKeysOptions().setText(text));
    }

    /**
     * Placeholder ile bul ve yaz
     */
    public boolean byPlaceholder(String placeholder, String text) {
        return sendKeys("[placeholder='" + placeholder + "']", text);
    }

    /**
     * Label ile bul ve yaz
     */
    public boolean byLabel(String labelText, String text) {
        return sendKeys("xpath=//label[contains(text(), '" + labelText + "')]/following::input[1]", text);
    }

    /**
     * Rastgele email oluştur ve yaz
     */
    public boolean randomEmail(String selector, String prefix) {
        String[] domains = {"example.com", "test.com", "demo.com"};
        String randomStr = UUID.randomUUID().toString().substring(0, 8);
        String email = prefix + "." + randomStr + "@" + domains[random.nextInt(domains.length)];

        logger.info("📧 Email: " + email);
        return sendKeys(selector, email);
    }

    /**
     * Rastgele şifre oluştur ve yaz
     */
    public boolean randomPassword(String selector) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%";

        StringBuilder password = new StringBuilder();
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        for (int i = 0; i < 8; i++) {
            String all = upper + lower + digits;
            password.append(all.charAt(random.nextInt(all.length())));
        }

        logger.info("🔐 Şifre oluşturuldu: " + "*".repeat(password.length()));
        return sendKeys(selector, password.toString());
    }

    /**
     * Rastgele telefon
     */
    public boolean randomPhone(String selector) {
        String phone = "5" +
                (random.nextInt(90) + 10) +
                (random.nextInt(900) + 100) +
                (random.nextInt(9000) + 1000);

        logger.info("📱 Telefon: " + phone);
        return sendKeys(selector, phone);
    }

    /**
     * Tarih gir
     */
    public boolean date(String selector, int daysOffset, String format) {
        LocalDate date = LocalDate.now().plusDays(daysOffset);
        String dateStr = date.format(DateTimeFormatter.ofPattern(format));
        return sendKeys(selector, dateStr);
    }

    /**
     * Options class
     */
    public static class SendKeysOptions {
        public String text = "";
        public boolean clearFirst = true;
        public boolean pressEnter = false;
        public boolean humanLike = false;
        public double delay = 50;
        public boolean useKeyboard = false;
        public boolean drawCircle = true;
        public String circleColor = "red";

        public SendKeysOptions setText(String text) {
            this.text = text;
            return this;
        }

        public SendKeysOptions setHumanLike(boolean humanLike) {
            this.humanLike = humanLike;
            return this;
        }

        public SendKeysOptions setPressEnter(boolean pressEnter) {
            this.pressEnter = pressEnter;
            return this;
        }

        public SendKeysOptions setDelay(double delay) {
            this.delay = delay;
            return this;
        }

        public SendKeysOptions setDrawCircle(boolean drawCircle) {
            this.drawCircle = drawCircle;
            return this;
        }
    }
}