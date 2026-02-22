// cypressClickUtils.js
/**
 * Cypress için gelişmiş tıklama utilities sınıfı
 */
class CypressClickUtils {
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

            // Önceki daireyi temizle
            cy.document().then(doc => {
                const oldCircle = doc.getElementById('cypress_click_circle');
                if (oldCircle) oldCircle.remove();
            });

            // Yeni daire çiz
            cy.document().then(doc => {
                const circle = doc.createElement('div');
                circle.id = 'cypress_click_circle';
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

                // Belirtilen süre sonra daireyi kaldır
                setTimeout(() => {
                    const circle = doc.getElementById('cypress_click_circle');
                    if (circle) circle.remove();
                }, duration);
            });

            cy.log('🔴 Daire çizildi - Renk: ' + color + ', Boyut: ' + size + 'px');
        });
    }

    /**
     * Elementi vurgula
     * @param {string} selector - Element selector
     * @param {Object} options - Seçenekler
     */
    highlight(selector, options = {}) {
        const { color = 'yellow', duration = 1000, drawCircle = true, circleColor = 'red' } = options;

        cy.get(selector).then($el => {
            const originalStyle = $el.attr('style') || '';

            // Vurgula
            $el.css({
                'border': '3px solid ' + color,
                'background': '#ffff99',
                'box-shadow': '0 0 10px ' + color,
                'transition': 'all 0.3s ease'
            });

            // Daire çiz
            if (drawCircle) {
                this.drawCircle(selector, { color: circleColor, size: this.circleSize, duration });
            }

            // Belirtilen süre sonra eski haline döndür
            setTimeout(() => {
                $el.attr('style', originalStyle);
            }, duration);
        });

        cy.log('✨ Element vurgulandı - Renk: ' + color);
    }

    /**
     * Gelişmiş tıklama fonksiyonu
     * @param {string} selector - Element selector
     * @param {Object} options - Tıklama seçenekleri
     * @returns {Cypress.Chainable}
     */
    click(selector, options = {}) {
        const {
            button = 'left',
            force = false,
            timeout = 30000,
            drawCircle = true,
            circleColor = 'red',
            highlightColor = 'yellow',
            clickCount = 1,
            multiple = false,
            scrollBehavior = 'top',
            waitForAnimation = true
        } = options;

        cy.log('\n' + '='.repeat(60));
        cy.log('🚀 Cypress tıklama başlıyor...');
        cy.log('    ├─ Selector: ' + selector);
        cy.log('    ├─ Button: ' + button);
        cy.log('    ├─ Force: ' + force);
        cy.log('    ├─ Click Count: ' + clickCount);
        cy.log('    └─ Daire: ' + drawCircle);
        cy.log('='.repeat(60));

        // Element bilgilerini al
        cy.get(selector, { timeout }).should('exist').then($el => {
            const tag = $el.prop('tagName').toLowerCase();
            const text = $el.text().trim() || $el.val() || 'NoText';
            const isVisible = $el.is(':visible');
            const isEnabled = !$el.prop('disabled');

            cy.log('🔍 Element bilgileri:');
            cy.log('    ├─ Tag: <' + tag + '>');
            cy.log('    ├─ Text: "' + text + '"');
            cy.log('    ├─ Görünür: ' + isVisible);
            cy.log('    └─ Etkin: ' + isEnabled);
        });

        // Vurgula ve daire çiz
        if (drawCircle) {
            this.highlight(selector, {
                color: highlightColor,
                drawCircle: true,
                circleColor: circleColor,
                duration: 500
            });
        }

        // ============= TIKLAMA YÖNTEMLERİ =============

        // YÖNTEM 1: Normal click
        if (!force && !multiple && clickCount === 1) {
            return cy.get(selector, { timeout })
                .scrollIntoView({ behavior: scrollBehavior === 'smooth' ? 'smooth' : 'auto' })
                .should('be.visible')
                .click({ button, timeout, waitForAnimations: waitForAnimation })
                .then(() => {
                    cy.log('✅ Normal click BAŞARILI');
                });
        }

        // YÖNTEM 2: Force click
        if (force) {
            return cy.get(selector, { timeout })
                .scrollIntoView({ behavior: scrollBehavior === 'smooth' ? 'smooth' : 'auto' })
                .click({ button, force: true, timeout })
                .then(() => {
                    cy.log('✅ Force click BAŞARILI');
                });
        }

        // YÖNTEM 3: Multiple click
        if (multiple || clickCount > 1) {
            return cy.get(selector, { timeout })
                .scrollIntoView({ behavior: scrollBehavior === 'smooth' ? 'smooth' : 'auto' })
                .click({ button, multiple, timeout })
                .then(() => {
                    cy.log('✅ Multiple click BAŞARILI');
                });
        }

        // YÖNTEM 4: JavaScript click (Cypress wrap ile)
        return cy.get(selector, { timeout }).then($el => {
            try {
                $el[0].click();
                cy.log('✅ JavaScript click BAŞARILI');
            } catch (error) {
                cy.log('⚠ JavaScript click başarısız:', error.message);
                throw error;
            }
        });
    }

    /**
     * Metin ile tıkla
     * @param {string} text - Buton metni
     * @param {Object} options - Seçenekler
     */
    clickByText(text, options = {}) {
        const { tag = 'button', exact = false } = options;
        const selector = exact
            ? `${tag}:contains("${text}")`
            : `${tag}:contains("${text}")`;

        cy.log('🔍 Metin ile buton aranıyor: "' + text + '"');
        return this.click(selector, options);
    }

    /**
     * CSS Selector ile tıkla
     */
    clickByCss(cssSelector, options = {}) {
        return this.click(cssSelector, options);
    }

    /**
     * XPath ile tıkla (Cypress XPath plugin gerekli)
     */
    clickByXPath(xpath, options = {}) {
        cy.log('🔍 XPath ile tıklama: ' + xpath);
        return this.click(xpath, options);
    }

    /**
     * Test ID ile tıkla
     */
    clickByTestId(testId, options = {}) {
        return this.click(`[data-testid="${testId}"]`, options);
    }

    /**
     * İçerik ile tıkla (contains)
     */
    clickByContains(text, options = {}) {
        const selector = `*:contains("${text}")`;
        return this.click(selector, options);
    }

    /**
     * Checkbox tıkla
     * @param {string} selector - Checkbox selector
     */
    checkCheckbox(selector, options = {}) {
        const { check = true, force = false } = options;

        cy.log('\n' + '='.repeat(70));
        cy.log('🎯 Checkbox tıklama başlıyor...');
        cy.log('='.repeat(70));

        // Vurgula
        this.drawCircle(selector, { color: 'blue', size: this.circleSize, duration: 1000 });

        // Method 1: check() / uncheck()
        try {
            if (check) {
                return cy.get(selector).check({ force }).then(() => {
                    cy.log('✅ Checkbox check() BAŞARILI');
                });
            } else {
                return cy.get(selector).uncheck({ force }).then(() => {
                    cy.log('✅ Checkbox uncheck() BAŞARILI');
                });
            }
        } catch (error) {
            cy.log('⚠ Checkbox hatası:', error.message);

            // Method 2: Click ile
            return cy.get(selector).click({ force }).then(() => {
                cy.log('✅ Checkbox click() BAŞARILI');
            });
        }
    }

    /**
     * Radio button seç
     */
    selectRadio(selector, options = {}) {
        cy.log('🎯 Radio button seçiliyor...');
        this.drawCircle(selector, { color: 'green', size: this.circleSize, duration: 1000 });

        return cy.get(selector).check({ force: options.force }).then(() => {
            cy.log('✅ Radio button seçildi');
        });
    }

    /**
     * Elementin görünmesini bekle ve tıkla
     */
    waitAndClick(selector, timeout = 10000, options = {}) {
        return cy.get(selector, { timeout })
            .should('be.visible')
            .then(() => this.click(selector, options));
    }

    /**
     * Çift tıkla
     */
    doubleClick(selector, options = {}) {
        cy.log('🖱️ Çift tıklama yapılıyor...');
        this.drawCircle(selector, { color: 'purple', size: this.circleSize, duration: 500 });

        return cy.get(selector).dblclick(options).then(() => {
            cy.log('✅ Çift tıklama BAŞARILI');
        });
    }

    /**
     * Sağ tıkla
     */
    rightClick(selector, options = {}) {
        cy.log('🖱️ Sağ tıklama yapılıyor...');
        this.drawCircle(selector, { color: 'orange', size: this.circleSize, duration: 500 });

        return cy.get(selector).rightclick(options).then(() => {
            cy.log('✅ Sağ tıklama BAŞARILI');
        });
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
     * Rastgele bir element bul ve tıkla
     */
    clickRandom(selector, options = {}) {
        cy.get(selector).then($elements => {
            const randomIndex = Math.floor(Math.random() * $elements.length);
            const randomSelector = `${selector}:eq(${randomIndex})`;
            cy.log('🎲 Rastgele element tıklanıyor: ' + randomSelector);
            return this.click(randomSelector, options);
        });
    }

    /**
     * Element üzerinde mouse hareketi yap
     */
    hover(selector, options = {}) {
        const { drawCircle = true, circleColor = 'blue' } = options;

        if (drawCircle) {
            this.drawCircle(selector, { color: circleColor, size: this.circleSize, duration: 500 });
        }

        return cy.get(selector).trigger('mouseover').then(() => {
            cy.log('🖱️ Mouse hover yapıldı');
        });
    }
}

module.exports = CypressClickUtils;