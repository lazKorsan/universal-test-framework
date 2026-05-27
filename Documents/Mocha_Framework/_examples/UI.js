const puppeteer = require('puppeteer');

describe('Google Testleri', () => {
    let browser;
    let page;

    beforeAll(async () => {
        browser = await puppeteer.launch({ headless: false });
        page = await browser.newPage();
    });

    afterAll(async () => {
        await browser.close();
    });

    test('Google sayfasını aç ve 12 saniye bekle', async () => {
        await page.goto('https://www.google.com');
        await page.waitForTimeout(12000); // 12 saniye bekle
        console.log('12 saniye geçti');
    });
});