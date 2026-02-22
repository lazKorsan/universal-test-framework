// clickUtils.js
const { expect } = require('@playwright/test');

/**
 * Playwright için gelişmiş tıklama utilities sınıfı
 */
class ClickUtils {
    /**
     * @param {import('@playwright/test').Page} page - Playwright page nesnesi
     */
    constructor(page) {
        this.page = page;
        this.circleColor = 'red';
        this.circleSize = 20;
        this.lastCircle = null;
        this.logger = console;
    }

    /**
     * Elementin merkezine daire çizer
     * @param {import('@playwright/test').Locator} locator - Element locator
     * @param {string} color - Daire rengi
     * @param {number} size - Daire boyutu
     * @param {number} duration - Gösterim süresi (ms)
     */
    async drawCircle(locator, color = 'red', size = 20, duration = 1000) {
        try {
            const box = await locator.boundingBox();
            if (!box) return;

            const centerX = box.x + box.width / 2;
            const centerY = box.y + box.height / 2;

            // Önceki daireyi temizle
            if (this.lastCircle) {
                await this.page.evaluate('circle => circle?.remove()', this.lastCircle);
            }

            // Yeni daire çiz
            this.lastCircle = await this.page.evaluateHandle(
                ({ centerX, centerY, size, color }) => {
                    const circle = document.createElement('div');
                    circle.style.position = 'absolute';
                    circle.style.left = (centerX - size/2) + 'px';
                    circle.style.top = (centerY - size/2) + 'px';
                    circle.style.width = size + 'px';
                    circle.style.height = size + 'px';
                    circle.style.borderRadius = '50%';
                    circle.style.border = '3px solid ' + color;
                    circle.style.backgroundColor = 'transparent';
                    circle.style.zIndex = '9999';
                    circle.style.pointerEvents = 'none';
                    circle.style.boxShadow = '0 0 10px ' + color;
                    circle.id = 'playwright_click_circle';
                    document.body.appendChild(circle);
                    return circle;
                },
                { centerX, centerY, size, color }
            );

            this.logger.log('🔴 Daire çizildi - Renk:', color, 'Boyut:', size + 'px');

            // Belirtilen süre sonra daireyi kaldır
            if (duration > 0) {
                setTimeout(async () => {
                    await this.page.evaluate('document.getElementById("playwright_click_circle")?.remove()');
                    this.lastCircle = null;
                }, duration);
            }
        } catch (error) {
            this.logger.warn('Daire çizilemedi:', error.message);
        }
    }

    /**
     * Elementi vurgula
     * @param {import('@playwright/test').Locator} locator - Element locator
     * @param {Object} options - Seçenekler
     */
    async highlight(locator, options = {}) {
        const {
            color = 'yellow',
            duration = 1000,
            drawCircle = true,
            circleColor = 'red'
        } = options;

        // Playwright'ın highlight özelliği
        await locator.highlight();

        // Daire çiz
        if (drawCircle) {
            await this.drawCircle(locator, circleColor, this.circleSize, duration);
        }
    }

    /**
     * Gelişmiş tıklama fonksiyonu
     * @param {string} selector - CSS selector veya XPath
     * @param {Object} options - Tıklama seçenekleri
     * @returns {Promise<boolean>}
     */
    async click(selector, options = {}) {
        const {
            button = 'left',
            force = false,
            timeout = 30000,
            drawCircle = true,
            circleColor = 'red',
            highlightColor = 'yellow',
            clickCount = 1,
            trial = false,
            modifiers = []
        } = options;

        this.logger.log('\n' + '='.repeat(60));
        this.logger.log('🚀 Playwright tıklama başlıyor...');
        this.logger.log('    ├─ Selector:', selector);
        this.logger.log('    ├─ Button:', button);
        this.logger.log('    ├─ Force:', force);
        this.logger.log('    └─ Daire:', drawCircle);
        this.logger.log('='.repeat(60));

        try {
            const locator = this.page.locator(selector).first();

            // Element bilgilerini al
            const isVisible = await locator.isVisible();
            const isEnabled = await locator.isEnabled();
            let text = await locator.textContent();
            if (!text || !text.trim()) {
                text = await locator.getAttribute('value');
            }
            if (!text || !text.trim()) {
                text = 'NoText';
            }
            const tag = await locator.evaluate('el => el.tagName.toLowerCase()');

            this.logger.log('🔍 Element bilgileri:');
            this.logger.log('    ├─ Tag: <' + tag + '>');
            this.logger.log('    ├─ Text: "' + text + '"');
            this.logger.log('    ├─ Görünür:', isVisible);
            this.logger.log('    └─ Etkin:', isEnabled);

            // Vurgula ve daire çiz
            if (drawCircle) {
                await this.highlight(locator, {
                    color: highlightColor,
                    drawCircle: true,
                    circleColor: circleColor
                });
            }

            // ============= TIKLAMA YÖNTEMLERİ =============

            // YÖNTEM 1: Normal click
            try {
                await locator.click({
                    button,
                    force,
                    timeout,
                    clickCount,
                    modifiers: modifiers.map(m => this.getModifier(m))
                });
                this.logger.log('✅ Normal click BAŞARILI');
                return true;
            } catch (error) {
                this.logger.warn('⚠ Normal click başarısız:', error.message);
            }

            // YÖNTEM 2: Force click
            if (!force) {
                try {
                    await locator.click({ button, force: true, timeout, clickCount });
                    this.logger.log('✅ Force click BAŞARILI');
                    return true;
                } catch (error) {
                    this.logger.warn('⚠ Force click başarısız:', error.message);
                }
            }

            // YÖNTEM 3: JavaScript click
            try {
                await locator.evaluate('el => el.click()');
                this.logger.log('✅ JavaScript click BAŞARILI');
                return true;
            } catch (error) {
                this.logger.warn('⚠ JavaScript click başarısız:', error.message);
            }

            // YÖNTEM 4: Dispatch event
            try {
                await locator.dispatchEvent('click');
                this.logger.log('✅ Dispatch event BAŞARILI');
                return true;
            } catch (error) {
                this.logger.warn('⚠ Dispatch event başarısız:', error.message);
            }

            // YÖNTEM 5: Page.click
            try {
                await this.page.click(selector, { button, force: true, timeout });
                this.logger.log('✅ Page.click BAŞARILI');
                return true;
            } catch (error) {
                this.logger.warn('⚠ Page.click başarısız:', error.message);
            }

            this.logger.error('❌ TÜM YÖNTEMLER BAŞARISIZ!');
            return false;

        } catch (error) {
            this.logger.error('❌ Tıklama hatası:', error.message);
            return await this.tryAlternativeSelectors(selector, options);
        }
    }

    /**
     * Modifier string'ini Playwright formatına çevir
     */
    getModifier(mod) {
        const modifiers = {
            'alt': 'Alt',
            'control': 'Control',
            'ctrl': 'Control',
            'meta': 'Meta',
            'shift': 'Shift'
        };
        return modifiers[mod.toLowerCase()] || mod;
    }

    /**
     * Metin ile tıkla
     * @param {string} text - Buton metni
     * @param {string} role - Element rolü (button, link, etc.)
     */
    async clickByText(text, role = 'button') {
        const selector = `role=${role}[name="${text}"]`;
        return await this.click(selector);
    }

    /**
     * CSS Selector ile tıkla
     */
    async clickByCss(cssSelector, options = {}) {
        return await this.click(`css=${cssSelector}`, options);
    }

    /**
     * XPath ile tıkla
     */
    async clickByXPath(xpath, options = {}) {
        return await this.click(`xpath=${xpath}`, options);
    }

    /**
     * Alternatif selector'ları dener
     */
    async tryAlternativeSelectors(originalSelector, options) {
        const alternatives = [
            originalSelector,
            originalSelector + ' >> nth=0',
            `text=${originalSelector}`,
            `css=${originalSelector}`,
            `xpath=${originalSelector}`,
            `role=button[name="${originalSelector}"]`,
            `role=link[name="${originalSelector}"]`
        ];

        for (let i = 0; i < alternatives.length; i++) {
            try {
                this.logger.log(`🔄 Alternatif ${i + 1} deneniyor:`, alternatives[i]);
                const locator = this.page.locator(alternatives[i]).first();
                const count = await locator.count();

                if (count > 0) {
                    await locator.click({ timeout: 3000 });
                    this.logger.log('✅ Alternatif BAŞARILI!');
                    return true;
                }
            } catch (error) {
                // Devam et
            }
        }
        return false;
    }

    /**
     * Checkbox tıkla
     * @param {string} selector - Checkbox selector
     */
    async checkCheckbox(selector) {
        try {
            const checkbox = this.page.locator(selector).first();

            // Vurgula
            await this.drawCircle(checkbox, 'blue', this.circleSize, 1000);

            // Method 1: check()
            const isChecked = await checkbox.isChecked();
            if (!isChecked) {
                await checkbox.check();
                this.logger.log('✅ Checkbox check() BAŞARILI');
                return true;
            }

            // Method 2: setChecked()
            await checkbox.setChecked(true);
            this.logger.log('✅ Checkbox setChecked() BAŞARILI');
            return true;

        } catch (error) {
            this.logger.warn('⚠ Checkbox hatası:', error.message);

            // Method 3: JavaScript
            try {
                await this.page.evaluate((sel) => {
                    document.querySelector(sel).checked = true;
                }, selector);
                this.logger.log('✅ JavaScript ile checkbox seçildi');
                return true;
            } catch (jsError) {
                return false;
            }
        }
    }

    /**
     * Radio button seç
     */
    async selectRadio(selector) {
        try {
            const radio = this.page.locator(selector).first();
            await radio.check();
            this.logger.log('✅ Radio button seçildi');
            return true;
        } catch (error) {
            this.logger.error('❌ Radio button seçilemedi:', error.message);
            return false;
        }
    }

    /**
     * Daire özelliklerini ayarla
     */
    setCircleProperties(color, size) {
        this.circleColor = color;
        this.circleSize = size;
        this.logger.log('⚙ Daire özellikleri ayarlandı - Renk:', color, 'Boyut:', size + 'px');
    }

    /**
     * Elementin ekranda olduğunu doğrula ve tıkla
     */
    async waitAndClick(selector, timeout = 10000) {
        const locator = this.page.locator(selector).first();
        await locator.waitFor({ state: 'visible', timeout });
        return await this.click(selector);
    }

    /**
     * Çift tıkla
     */
    async doubleClick(selector, options = {}) {
        return await this.click(selector, { ...options, clickCount: 2 });
    }

    /**
     * Sağ tıkla
     */
    async rightClick(selector, options = {}) {
        return await this.click(selector, { ...options, button: 'right' });
    }
}

module.exports = ClickUtils;