// cypressSendKeyUtils.js
const crypto = require('crypto');

/**
 * Cypress için gelişmiş yazma utilities sınıfı
 */
class CypressSendKeyUtils {
    constructor() {
        this.circleColor = 'red';
        this.circleSize = 20;
        this.logger = console;
    }

    /**
     * Elementin merkezine daire çizer
     * @param {string} selector - Element selector
     * @param {Object} options - Seçenekler
     */
    drawCircle(selector, options = {}) {
        const { color = 'red', size = 20, duration = 1000 } = options;

        cy.get(selector).then($el => {
            const rect = $el[0].getBoundingClientRect();
            const centerX = rect.left + rect.width / 2;
            const centerY = rect.top + rect.height / 2;

            cy.document().then(doc => {
                // Önceki daireyi temizle
                const oldCircle = doc.getElementById('cypress_sendkey_circle');
                if (oldCircle) oldCircle.remove();

                // Yeni daire çiz
                const circle = doc.createElement('div');
                circle.id = 'cypress_sendkey_circle';
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
                doc.body.appendChild(circle);

                setTimeout(() => {
                    const circle = doc.getElementById('cypress_sendkey_circle');
                    if (circle) circle.remove();
                }, duration);
            });

            cy.log('🔴 Daire çizildi');
        });
    }

    /**
     * Gelişmiş yazma fonksiyonu
     * @param {string} selector - Element selector
     * @param {Object} options - Yazma seçenekleri
     * @returns {Cypress.Chainable}
     */
    sendKeys(selector, options = {}) {
        const defaultOptions = {
            text: '',
            clearFirst: true,
            pressEnter: false,
            humanLike: false,
            delay: 50,
            drawCircle: true,
            circleColor: 'red',
            timeout: 30000,
            log: true,
            parseSpecialCharSequences: true,
            force: false
        };

        const config = { ...defaultOptions, ...options };

        if (config.log) {
            cy.log('\n' + '='.repeat(70));
            cy.log('🚀 Yazma işlemi başlıyor...');
            cy.log('    ├─ Metin: "' + config.text + '"');
            cy.log('    ├─ Selector: ' + selector);
            cy.log('    ├─ İnsan gibi: ' + config.humanLike);
            cy.log('    ├─ Enter: ' + config.pressEnter);
            cy.log('    ├─ Temizle: ' + config.clearFirst);
            cy.log('    └─ Daire: ' + config.drawCircle);
            cy.log('='.repeat(70));
        }

        // Element bilgilerini al
        cy.get(selector, { timeout: config.timeout }).should('exist').then($el => {
            const tag = $el.prop('tagName').toLowerCase();
            const type = $el.attr('type');
            const isVisible = $el.is(':visible');
            const isEnabled = !$el.prop('disabled');
            let currentValue = $el.val() || '[empty]';

            if (config.log) {
                cy.log('🔍 Element bilgileri:');
                cy.log('    ├─ Tag: <' + tag + '>');
                cy.log('    ├─ Type: ' + (type || 'N/A'));
                cy.log('    ├─ Görünür: ' + isVisible);
                cy.log('    ├─ Etkin: ' + isEnabled);
                cy.log('    └─ Mevcut değer: "' + currentValue + '"');
            }
        });

        // Vurgula ve daire çiz
        if (config.drawCircle) {
            this.drawCircle(selector, { color: config.circleColor, size: this.circleSize, duration: 1000 });
        }

        // ============= YAZMA YÖNTEMLERİ =============

        // YÖNTEM 1: type() - Cypress'in doğal methodu
        if (!config.humanLike) {
            let typeOptions = {
                delay: config.delay,
                parseSpecialCharSequences: config.parseSpecialCharSequences,
                force: config.force,
                timeout: config.timeout
            };

            if (config.clearFirst) {
                return cy.get(selector)
                    .clear({ force: config.force })
                    .type(config.text, typeOptions)
                    .then(() => {
                        if (config.pressEnter) {
                            return cy.get(selector).type('{enter}');
                        }
                    })
                    .then(() => {
                        if (config.log) cy.log('✅ type() BAŞARILI');
                    });
            } else {
                return cy.get(selector)
                    .type(config.text, typeOptions)
                    .then(() => {
                        if (config.pressEnter) {
                            return cy.get(selector).type('{enter}');
                        }
                    })
                    .then(() => {
                        if (config.log) cy.log('✅ type() BAŞARILI');
                    });
            }
        }

        // YÖNTEM 2: İnsan gibi yazma
        if (config.humanLike) {
            return cy.get(selector).then($el => {
                if (config.clearFirst) {
                    $el.val('');
                }

                // Her karakteri tek tek yaz
                const chars = config.text.split('');
                let chain = cy.wrap($el).click();

                chars.forEach((char, index) => {
                    chain = chain.then(() => {
                        return cy.wrap($el).type(char, { delay: config.delay });
                    });
                });

                if (config.pressEnter) {
                    chain = chain.then(() => {
                        return cy.wrap($el).type('{enter}');
                    });
                }

                return chain.then(() => {
                    if (config.log) cy.log('✅ İnsan gibi yazma BAŞARILI');
                });
            });
        }

        // YÖNTEM 3: invoke ile val() set et
        if (config.force) {
            return cy.get(selector)
                .invoke('val', config.text)
                .trigger('input')
                .trigger('change')
                .then(() => {
                    if (config.pressEnter) {
                        return cy.get(selector).trigger('keydown', { key: 'Enter' });
                    }
                })
                .then(() => {
                    if (config.log) cy.log('✅ invoke val() BAŞARILI');
                });
        }
    }

    /**
     * Basit kullanım
     */
    type(selector, text, options = {}) {
        return this.sendKeys(selector, { ...options, text });
    }

    /**
     * ID ile yaz
     */
    byId(id, text, options = {}) {
        return this.sendKeys('#' + id, { ...options, text });
    }

    /**
     * Name ile yaz
     */
    byName(name, text, options = {}) {
        return this.sendKeys(`[name="${name}"]`, { ...options, text });
    }

    /**
     * Class ile yaz
     */
    byClass(className, text, options = {}) {
        return this.sendKeys(`.${className}`, { ...options, text });
    }

    /**
     * Placeholder ile yaz
     */
    byPlaceholder(placeholder, text, options = {}) {
        return this.sendKeys(`[placeholder="${placeholder}"]`, { ...options, text });
    }

    /**
     * Label ile yaz
     */
    byLabel(labelText, text, options = {}) {
        return cy.contains('label', labelText)
            .invoke('attr', 'for')
            .then(forId => {
                if (forId) {
                    return this.sendKeys('#' + forId, { ...options, text });
                } else {
                    // Label'ın yanındaki input'u bul
                    return cy.contains('label', labelText)
                        .parent()
                        .find('input, textarea')
                        .then($input => {
                            const id = $input.attr('id');
                            if (id) {
                                return this.sendKeys('#' + id, { ...options, text });
                            } else {
                                return this.sendKeys($input, { ...options, text });
                            }
                        });
                }
            });
    }

    /**
     * Test ID ile yaz
     */
    byTestId(testId, text, options = {}) {
        return this.sendKeys(`[data-testid="${testId}"]`, { ...options, text });
    }

    /**
     * Form alanı adına göre yaz
     */
    byFormLabel(label, text, options = {}) {
        return this.byLabel(label, text, options);
    }

    /**
     * Rastgele email oluştur ve yaz
     */
    randomEmail(selector, prefix = 'test', options = {}) {
        const domains = ['example.com', 'test.com', 'demo.com', 'cypress.com', 'mail.com'];
        const randomStr = crypto.randomBytes(4).toString('hex');
        const email = `${prefix}.${randomStr}@${domains[Math.floor(Math.random() * domains.length)]}`;

        cy.log('📧 Email: ' + email);
        return this.sendKeys(selector, { ...options, text: email });
    }

    /**
     * Rastgele şifre oluştur ve yaz
     */
    randomPassword(selector, options = {}) {
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

        cy.log('🔐 Şifre oluşturuldu: ' + '*'.repeat(password.length));
        return this.sendKeys(selector, { ...options, text: password });
    }

    /**
     * Rastgele telefon numarası
     */
    randomPhone(selector, options = {}) {
        const part1 = Math.floor(Math.random() * 90) + 10; // 10-99
        const part2 = Math.floor(Math.random() * 900) + 100; // 100-999
        const part3 = Math.floor(Math.random() * 9000) + 1000; // 1000-9999

        const phone = `5${part1}${part2}${part3}`;

        cy.log('📱 Telefon: ' + phone);
        return this.sendKeys(selector, { ...options, text: phone });
    }

    /**
     * Tarih gir
     */
    date(selector, daysOffset = 0, format = 'DD.MM.YYYY', options = {}) {
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
        } else if (format === 'MM/DD/YYYY') {
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const year = date.getFullYear();
            dateStr = `${month}/${day}/${year}`;
        } else {
            dateStr = date.toLocaleDateString('tr-TR');
        }

        cy.log('📅 Tarih: ' + dateStr);
        return this.sendKeys(selector, { ...options, text: dateStr });
    }

    /**
     * Bugünün tarihi
     */
    today(selector, options = {}) {
        return this.date(selector, 0, 'DD.MM.YYYY', options);
    }

    /**
     * Rastgele metin oluştur
     */
    randomText(selector, length = 10, options = {}) {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let text = '';
        for (let i = 0; i < length; i++) {
            text += chars[Math.floor(Math.random() * chars.length)];
        }

        cy.log('🎲 Rastgele metin: ' + text);
        return this.sendKeys(selector, { ...options, text });
    }

    /**
     * Rastgele sayı oluştur
     */
    randomNumber(selector, min = 0, max = 1000, options = {}) {
        const number = Math.floor(Math.random() * (max - min + 1)) + min;
        cy.log('🔢 Rastgele sayı: ' + number);
        return this.sendKeys(selector, { ...options, text: number.toString() });
    }

    /**
     * Elementi temizle
     */
    clear(selector, options = {}) {
        return this.sendKeys(selector, { ...options, text: '', clearFirst: true });
    }

    /**
     * Mevcut değere ekle
     */
    append(selector, text, options = {}) {
        return this.sendKeys(selector, { ...options, text, clearFirst: false });
    }

    /**
     * İnsan gibi yaz
     */
    humanLikeType(selector, text, delay = 100, options = {}) {
        return this.sendKeys(selector, {
            ...options,
            text,
            humanLike: true,
            delay
        });
    }

    /**
     * Enter tuşuna bas
     */
    pressEnter(selector, options = {}) {
        return this.sendKeys(selector, { ...options, text: '', pressEnter: true });
    }

    /**
     * Tab tuşuna bas
     */
    pressTab(selector, options = {}) {
        return cy.get(selector).trigger('keydown', { key: 'Tab' });
    }

    /**
     * Escape tuşuna bas
     */
    pressEscape(selector, options = {}) {
        return cy.get(selector).trigger('keydown', { key: 'Escape' });
    }

    /**
     * Daire özelliklerini ayarla
     */
    setCircleProperties(color, size) {
        this.circleColor = color;
        this.circleSize = size;
        cy.log('⚙ Daire özellikleri ayarlandı - Renk: ' + color + ', Boyut: ' + size + 'px');
    }

    /**
     * Elementin değerini doğrula
     */
    shouldHaveValue(selector, expectedValue, options = {}) {
        const { timeout = 10000 } = options;
        return cy.get(selector, { timeout }).should('have.value', expectedValue);
    }

    /**
     * Elementin placeholder'ını doğrula
     */
    shouldHavePlaceholder(selector, expectedPlaceholder, options = {}) {
        const { timeout = 10000 } = options;
        return cy.get(selector, { timeout }).should('have.attr', 'placeholder', expectedPlaceholder);
    }
}

module.exports = CypressSendKeyUtils;