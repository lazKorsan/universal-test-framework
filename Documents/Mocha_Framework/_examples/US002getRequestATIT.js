const api = require('../utils/api_methods');
const { generateToken } = require('../utils/authentication');
const { describe, it, before } = require('mocha');
const { expect } = require('chai');

describe('USgetRequestATIT - Specific Course Test', function() {
    this.timeout(10000);
    let token;
    const targetCourseId = 3631;

    before(async function() {
        token = await generateToken();
        api.setToken(token);
    });

    it(`Should fetch details for course ID ${targetCourseId}`, async function() {
        // 1. Endpoint belirle
        api.pathParam(`api/course/${targetCourseId}`);

        // 2. İsteği gönder
        await api.sendRequest('GET');

        // 3. Assertion yap
        api.statusCodeAssert(200);

        // 4. Veriyi kontrol et
        const courseData = api.response.data.data || api.response.data;

        // ID Doğrulaması
        expect(String(courseData.id)).to.equal(String(targetCourseId));

        // 5. TÜM KURS BİLGİLERİNİ YAZDIR
        console.log('\n✅ TÜM KURS BİLGİLERİ:');
        console.log('='.repeat(50));

        // JSON formatında, okunaklı (pretty print) yazdır
        console.log(JSON.stringify(courseData, null, 2));

        console.log('='.repeat(50));
    });
});