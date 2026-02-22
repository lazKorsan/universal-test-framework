// PlaywrightClickUtils.java
package utilities;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.MouseButton;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Playwright için gelişmiş tıklama utilities sınıfı
 */
public class PlaywrightClickUtils {

    private final Page page;
    private Logger logger;
    private final int circleSize = 20;
    private JSHandle lastCircle;

    public PlaywrightClickUtils(Page page) {
        this.page = page;
        setupLogger();
    }

    private void setupLogger() {
        logger = Logger.getLogger(PlaywrightClickUtils.class.getName());
        logger.setLevel(Level.INFO);
    }

    /**
     * Elementin merkezine daire çizer
     */
    private void drawCircle(Locator locator, String color, int size, int duration) {
        try {
            // Elementin bounding box'ını al
            BoundingBox box = locator.boundingBox();
            if (box == null) return;

            double centerX = box.x + box.width / 2;
            double centerY = box.y + box.height / 2;

            // Önceki daireyi temizle
            if (lastCircle != null) {
                page.evaluate("circle => circle?.remove()", lastCircle);
            }

            // Yeni daire çiz
            lastCircle = page.evaluateHandle(String.format(
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
                            "  circle.id = 'playwright_click_circle';" +
                            "  document.body.appendChild(circle);" +
                            "  return circle;" +
                            "}", size, size, size, size, color, color), Arrays.asList(centerX, centerY));

            logger.info("🔴 Daire çizildi - Renk: " + color + ", Boyut: " + size + "px");

            // Belirtilen süre sonra daireyi kaldır
            if (duration > 0) {
                Thread.sleep(duration * 1000L);
                page.evaluate("document.getElementById('playwright_click_circle')?.remove()");
                lastCircle = null;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Elementi vurgula ve daire çiz
     */
    public void highlight(Locator locator, String color, int duration, boolean drawCircle, String circleColor) {
        // Playwright'ın highlight özelliği
        locator.highlight();

        // Daire çiz
        if (drawCircle) {
            drawCircle(locator, circleColor, circleSize, duration);
        }
    }

    /**
     * Gelişmiş tıklama fonksiyonu
     */
    public boolean click(String selector, ClickOptions options) {
        logger.info("\n" + "=".repeat(60));
        logger.info("🚀 Playwright tıklama başlıyor...");
        logger.info("    ├─ Selector: " + selector);
        logger.info("    ├─ Button: " + (options != null ? options.button : "left"));
        logger.info("    └─ Force: " + (options != null ? options.force : false));
        logger.info("=".repeat(60));

        try {
            Locator locator = page.locator(selector).first();

            // Element bilgilerini al
            boolean isVisible = locator.isVisible();
            boolean isEnabled = locator.isEnabled();
            String text = locator.textContent();
            if (text == null || text.trim().isEmpty()) {
                text = locator.getAttribute("value");
            }
            if (text == null || text.trim().isEmpty()) {
                text = "NoText";
            }
            String tag = locator.evaluate("el => el.tagName.toLowerCase()").toString();

            logger.info("🔍 Element bilgileri:");
            logger.info("    ├─ Tag: <" + tag + ">");
            logger.info("    ├─ Text: '" + text + "'");
            logger.info("    ├─ Görünür: " + isVisible);
            logger.info("    └─ Etkin: " + isEnabled);

            // Vurgula ve daire çiz
            if (options != null && options.drawCircle) {
                highlight(locator, options.highlightColor, 1, true, options.circleColor);
            }

            // ============= TIKLAMA YÖNTEMLERİ =============

            // YÖNTEM 1: Normal click (Playwright otomatik bekler)
            if (options == null || !options.force) {
                locator.click(new Locator.ClickOptions()
                        .setButton(options != null ? options.button : MouseButton.LEFT)
                        .setTimeout(options != null ? options.timeout * 1000 : 30000));
                logger.info("✅ Normal click BAŞARILI");
                return true;
            }

            // YÖNTEM 2: Force click
            if (options != null && options.force) {
                locator.click(new Locator.ClickOptions()
                        .setForce(true)
                        .setButton(options.button));
                logger.info("✅ Force click BAŞARILI");
                return true;
            }

            // YÖNTEM 3: JavaScript click
            try {
                locator.evaluate("el => el.click()");
                logger.info("✅ JavaScript click BAŞARILI");
                return true;
            } catch (Exception e) {
                logger.warning("⚠ JavaScript click başarısız");
            }

            // YÖNTEM 4: Dispatch event
            try {
                locator.dispatchEvent("click");
                logger.info("✅ Dispatch event BAŞARILI");
                return true;
            } catch (Exception e) {
                logger.warning("⚠ Dispatch event başarısız");
            }

            logger.severe("❌ TÜM YÖNTEMLER BAŞARISIZ!");
            return false;

        } catch (Exception e) {
            logger.severe("❌ Tıklama hatası: " + e.getMessage());
            return tryAlternativeSelectors(selector, options);
        }
    }

    /**
     * Click overload
     */
    public boolean click(String selector) {
        return click(selector, new ClickOptions());
    }

    /**
     * Metin ile tıkla
     */
    public boolean clickByText(String text, String role) {
        String selector = role != null ?
                "role=" + role + "[name='" + text + "']" :
                "text=" + text;
        return click(selector);
    }

    public boolean clickByText(String text) {
        return clickByText(text, "button");
    }

    /**
     * Alternatif selector'ları dener
     */
    private boolean tryAlternativeSelectors(String originalSelector, ClickOptions options) {
        String[] alternatives = {
                originalSelector,
                originalSelector + " >> nth=0",
                "text=" + originalSelector,
                "css=" + originalSelector,
                "xpath=" + originalSelector,
                "role=button[name='" + originalSelector + "']"
        };

        for (int i = 0; i < alternatives.length; i++) {
            try {
                logger.info("🔄 Alternatif " + (i+1) + " deneniyor: " + alternatives[i]);
                Locator loc = page.locator(alternatives[i]).first();
                if (loc.count() > 0) {
                    loc.click(new Locator.ClickOptions().setTimeout(3000));
                    logger.info("✅ Alternatif BAŞARILI!");
                    return true;
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        return false;
    }

    /**
     * Checkbox tıklama
     */
    public boolean checkCheckbox(String selector) {
        try {
            Locator checkbox = page.locator(selector).first();

            // Elementi vurgula
            drawCircle(checkbox, "blue", circleSize, 1);

            // Method 1: check()
            if (!checkbox.isChecked()) {
                checkbox.check();
                logger.info("✅ Checkbox check() BAŞARILI");
                return true;
            }

            // Method 2: setChecked()
            checkbox.setChecked(true);
            logger.info("✅ Checkbox setChecked() BAŞARILI");
            return true;

        } catch (Exception e) {
            logger.warning("⚠ Checkbox hatası: " + e.getMessage());

            // Method 3: JavaScript
            try {
                page.evaluate("document.querySelector('" + selector + "').checked = true");
                logger.info("✅ JavaScript ile checkbox seçildi");
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * Options class for click configuration
     */
    public static class ClickOptions {
        public MouseButton button = MouseButton.LEFT;
        public boolean force = false;
        public int timeout = 30;
        public boolean drawCircle = true;
        public String highlightColor = "yellow";
        public String circleColor = "red";
        public int clickCount = 1;
        public boolean trial = false;

        public ClickOptions setButton(MouseButton button) {
            this.button = button;
            return this;
        }

        public ClickOptions setForce(boolean force) {
            this.force = force;
            return this;
        }

        public ClickOptions setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public ClickOptions setDrawCircle(boolean drawCircle) {
            this.drawCircle = drawCircle;
            return this;
        }

        public ClickOptions setCircleColor(String color) {
            this.circleColor = color;
            return this;
        }
    }
}