// sendKeyUtils.js
const crypto = require('crypto');

/**
 * Playwright için gelişmiş yazma utilities sınıfı
 */
class SendKeyUtils {
    /**
     * @param {import('@playwright/test').Page} page - Playwright page nesnesi
     */
    constructor(page) {
        this.page = page;
        this.circleColor = 'red';
        this.circleSize = 20;
        this.random = Math.random;
        this.logger = console;
    }

    /**
     * Elementin merkezine daire çizer
     */
    async drawCircle(locator, color = 'red', size = 20, duration = 1000) {
        try {
            const box = await locator.boundingBox();
            if (!box) return;

            const centerX = box.x + box.width / 2;
            const centerY = box.y + box.height / 2;

            await this.page.evaluate(
                ({ centerX, centerY, size, color, duration }) => {
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
                    circle.id = 'playwright_sendkey_circle';
                    document.body.appendChild(circle);

                    setTimeout(() => circle.remove(), duration);
                },
                { centerX, centerY, size, color, duration }
            );

            this.logger.log('🔴 Daire çizildi');
        } catch (error) {
            this.logger.warn('Daire çizilemedi:', error.message);
        }
    }

    /**
     * Gelişmiş yazma fonksiyonu
     * @param {string} selector - Element selector
     * @param {Object} options - Yazma seçenekleri
     * @returns {Promise<boolean>}
     */
    async sendKeys(selector, options = {}) {
        const defaultOptions = {
            text: '',
            clearFirst: true,
            pressEnter: false,
            humanLike: false,
            delay: 50,
            useKeyboard: false,
            drawCircle: true,
            circleColor: 'red',
            timeout: 30000
        };

        const config = { ...defaultOptions, ...options };

        this.logger.log('\n' + '='.repeat(70));
        this.logger.log('🚀 Yazma işlemi başlıyor...');
        this.logger.log('    ├─ Metin: "' + config.text + '"');
        this.logger.log('    ├─ Selector:', selector);
        this.logger.log('    ├─ İnsan gibi:', config.humanLike);
        this.logger.log('    ├─ Enter:', config.pressEnter);
        this.logger.log('    └─ Daire:', config.drawCircle);
        this.logger.log('='.repeat(70));

        try {
            const locator = this.page.locator(selector).first();

            // Elementin görünür olmasını bekle
            await locator.waitFor({ state: 'visible', timeout: config.timeout });

            // Element bilgilerini al
            const tag = await locator.evaluate('el => el.tagName.toLowerCase()');
            const type = await locator.getAttribute('type');
            const isVisible = await locator.isVisible();
            const isEnabled = await locator.isEnabled();
            let currentValue = '';

            try {
                currentValue = await locator.inputValue();
            } catch (e) {
                currentValue = '[değer alınamadı]';
            }

            this.logger.log('🔍 Element bilgileri:');
            this.logger.log('    ├─ Tag: <' + tag + '>');
            this.logger.log('    ├─ Type:', type || 'N/A');
            this.logger.log('    ├─ Görünür:', isVisible);
            this.logger.log('    ├─ Etkin:', isEnabled);
            this.logger.log('    └─ Mevcut değer:', currentValue);

            // Vurgula ve daire çiz
            if (config.drawCircle) {
                await this.drawCircle(locator, config.circleColor, this.circleSize, 1000);
            }

            // ============= YAZMA YÖNTEMLERİ =============

            // YÖNTEM 1: fill() - En hızlı
            if (!config.humanLike && !config.pressEnter && !config.useKeyboard) {
                if (config.clearFirst) {
                    await locator.fill(config.text);
                } else {
                    const current = await locator.inputValue();
                    await locator.fill(current + config.text);
                }
                this.logger.log('✅ fill() BAŞARILI');
                return true;
            }

            // YÖNTEM 2: pressSequentially() - İnsan gibi yazma
            if (config.humanLike) {
                await locator.click(); // Önce odaklan

                if (config.clearFirst) {
                    await locator.clear();
                }

                await locator.pressSequentially(config.text, {
                    delay: config.delay
                });

                if (config.pressEnter) {
                    await locator.press('Enter');
                }

                this.logger.log('✅ pressSequentially() BAŞARILI');
                return true;
            }

            // YÖNTEM 3: type()
            if (!config.humanLike && config.delay > 0) {
                await locator.click(); // Odaklan

                if (config.clearFirst) {
                    await locator.clear();
                }

                await locator.type(config.text, { delay: config.delay });

                if (config.pressEnter) {
                    await locator.press('Enter');
                }

                this.logger.log('✅ type() BAŞARILI');
                return true;
            }

            // YÖNTEM 4: Keyboard API
            if (config.useKeyboard) {
                await locator.click(); // Odaklan

                if (config.clearFirst) {
                    await this.page.keyboard.press('Control+A');
                    await this.page.keyboard.press('Backspace');
                }

                for (const char of config.text) {
                    await this.page.keyboard.type(char);
                    if (config.delay > 0) {
                        await this.page.waitForTimeout(config.delay);
                    }
                }

                if (config.pressEnter) {
                    await this.page.keyboard.press('Enter');
                }

                this.logger.log('✅ Keyboard API BAŞARILI');
                return true;
            }

            // YÖNTEM 5: JavaScript ile
            try {
                if (config.clearFirst) {
                    await locator.evaluate('el => el.value = ""');
                }

                await locator.evaluate(
                    (el, text) => {
                        el.value += text;
                        el.dispatchEvent(new Event('input', { bubbles: true }));
                        el.dispatchEvent(new Event('change', { bubbles: true }));
                    },
                    config.text
                );

                if (config.pressEnter) {
                    await locator.press('Enter');
                }

                this.logger.log('✅ JavaScript BAŞARILI');
                return true;
            } catch (error) {
                this.logger.warn('⚠ JavaScript başarısız:', error.message);
            }

            this.logger.error('❌ TÜM YÖNTEMLER BAŞARISIZ!');
            return false;

        } catch (error) {
            this.logger.error('❌ Yazma hatası:', error.message);
            return false;
        }
    }

    /**
     * Basit kullanım
     */
    async sendKeys(selector, text) {
        return await this.sendKeys(selector, { text });
    }

    /**
     * ID ile yaz
     */
    async byId(id, text, options = {}) {
        return await this.sendKeys(`#${id}`, { ...options, text });
    }

    /**
     * Name ile yaz
     */
    async byName(name, text, options = {}) {
        return await this.sendKeys(`[name="${name}"]`, { ...options, text });
    }

    /**
     * Placeholder ile yaz
     */
    async byPlaceholder(placeholder, text, options = {}) {
        return await this.sendKeys(`[placeholder="${placeholder}"]`, { ...options, text });
    }

    /**
     * Label ile yaz
     */
    async byLabel(labelText, text, options = {}) {
        const selector = `xpath=//label[contains(text(), '${labelText}')]/following::input[1]`;
        return await this.sendKeys(selector, { ...options, text });
    }

    /**
     * Test ID ile yaz
     */
    async byTestId(testId, text, options = {}) {
        return await this.sendKeys(`[data-testid="${testId}"]`, { ...options, text });
    }

    /**
     * Rastgele email oluştur ve yaz
     */
    async randomEmail(selector, prefix = 'test', options = {}) {
        const domains = ['example.com', 'test.com', 'demo.com', 'mail.com'];
        const randomStr = crypto.randomBytes(4).toString('hex');
        const email = `${prefix}.${randomStr}@${domains[Math.floor(Math.random() * domains.length)]}`;

        this.logger.log('📧 Email:', email);
        return await this.sendKeys(selector, { ...options, text: email });
    }

    /**
     * Rastgele şifre oluştur ve yaz
     */
    async randomPassword(selector, options = {}) {
        const upper = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
        const lower = 'abcdefghijklmnopqrstuvwxyz';
        const digits = '0123456789';
        const special = '!@#$%&*';

        let password = '';

        // En az bir büyük harf
        password += upper[Math.floor(Math.random() * upper.length)];
        // En az bir küçük harf
        password += lower[Math.floor(Math.random() * lower.length)];
        // En az bir rakam
        password += digits[Math.floor(Math.random() * digits.length)];
        // En az bir özel karakter
        password += special[Math.floor(Math.random() * special.length)];

        // 8 karakter daha ekle
        const allChars = upper + lower + digits;
        for (let i = 0; i < 8; i++) {
            password += allChars[Math.floor(Math.random() * allChars.length)];
        }

        // Karakterleri karıştır
        password = password.split('').sort(() => Math.random() - 0.5).join('');

        this.logger.log('🔐 Şifre oluşturuldu:', '*'.repeat(password.length));
        return await this.sendKeys(selector, { ...options, text: password });
    }

    /**
     * Rastgele telefon numarası
     */
    async randomPhone(selector, options = {}) {
        const part1 = Math.floor(Math.random() * 90) + 10; // 10-99
        const part2 = Math.floor(Math.random() * 900) + 100; // 100-999
        const part3 = Math.floor(Math.random() * 9000) + 1000; // 1000-9999

        const phone = `5${part1}${part2}${part3}`;

        this.logger.log('📱 Telefon:', phone);
        return await this.sendKeys(selector, { ...options, text: phone });
    }

    /**
     * Tarih gir
     */
    async date(selector, daysOffset = 0, format = 'DD.MM.YYYY', options = {}) {
        const date = new Date();
        date.setDate(date.getDate() + daysOffset);

        let dateStr;
        if (format === 'DD.MM.YYYY') {
            const day = String(date.getDate()).padStart(2, '0');
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const year = date.getFullYear();
            dateStr = `${day}.${month}.${year}`;
        } else if (format === 'YYYY-MM-DD') {
            dateStr = date.toISOString().split('T')[0];
        } else {
            dateStr = date.toLocaleDateString('tr-TR');
        }

        this.logger.log('📅 Tarih:', dateStr);
        return await this.sendKeys(selector, { ...options, text: dateStr });
    }

    /**
     * Bugünün tarihi
     */
    async today(selector, options = {}) {
        return await this.date(selector, 0, 'DD.MM.YYYY', options);
    }

    /**
     * Rastgele metin oluştur
     */
    async randomText(selector, length = 10, options = {}) {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let text = '';
        for (let i = 0; i < length; i++) {
            text += chars[Math.floor(Math.random() * chars.length)];
        }

        this.logger.log('🎲 Rastgele metin:', text);
        return await this.sendKeys(selector, { ...options, text });
    }

    /**
     * Elementi temizle
     */
    async clear(selector, options = {}) {
        return await this.sendKeys(selector, { ...options, text: '', clearFirst: true });
    }

    /**
     * Mevcut değere ekle
     */
    async append(selector, text, options = {}) {
        return await this.sendKeys(selector, { ...options, text, clearFirst: false });
    }

    /**
     * İnsan gibi yaz
     */
    async humanLikeType(selector, text, delay = 100, options = {}) {
        return await this.sendKeys(selector, {
            ...options,
            text,
            humanLike: true,
            delay
        });
    }

    /**
     * Enter tuşuna bas
     */
    async pressEnter(selector, options = {}) {
        return await this.sendKeys(selector, { ...options, text: '', pressEnter: true });
    }

    /**
     * Daire özelliklerini ayarla
     */
    setCircleProperties(color, size) {
        this.circleColor = color;
        this.circleSize = size;
        this.logger.log('⚙ Daire özellikleri ayarlandı - Renk:', color, 'Boyut:', size + 'px');
    }
}

module.exports = SendKeyUtils;