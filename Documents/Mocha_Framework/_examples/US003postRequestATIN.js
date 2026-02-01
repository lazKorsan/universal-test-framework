const api = require('../utils/api_methods');
const { generateToken } = require('../utils/authentication');
const { describe, it, before, after } = require('mocha');
const { expect } = require('chai');

describe('US003 - Post Course Test', function() {
    this.timeout(20000);
    let token;
    let createdCourseId;

    before(async function() {
        token = await generateToken();
        api.setToken(token);
    });

    it('Should create a new course successfully', async function() {
        // 1. Endpoint
        api.pathParam('api/addCourse');

        // 2. Request Body Hazırla
        const requestBody = {
            "title": "Mocha Post Test " + Date.now(),
            "type": "course",
            "slug": "mocha-post-test-" + Date.now(),
            "start_date": "2025-12-01",
            "duration": 45,
            "capacity": 50,
            "price": 0,
            "description": "Bu kurs Mocha testi ile oluşturulmuştur.",
            "teacher_id": 1016,
            "category_id": 611
        };

        // 3. POST İsteği Gönder
        await api.sendRequest('POST', requestBody);

        // 4. Assertions (Doğrulamalar)
        api.statusCodeAssert(200);
        api.assertBody('remark', 'success');

        const message = api.response.data.message || api.response.data.Message;
        expect(message).to.equal('Successfully Added.');

        // ID'yi al (Temizlik için)
        createdCourseId = api.response.data['Added Course ID'];
        console.log(`\n✅ Kurs Başarıyla Oluşturuldu! ID: ${createdCourseId}`);
    });

    // Test bittikten sonra oluşan veriyi temizle (Best Practice)
    after(async function() {
        if (createdCourseId) {
            console.log(`\n🧹 Temizlik: Oluşturulan kurs siliniyor (ID: ${createdCourseId})...`);
            api.pathParam(`api/deleteCourse/${createdCourseId}`);
            await api.sendRequest('DELETE');
            api.statusCodeAssert(200);
            console.log('✅ Temizlik tamamlandı.');
        }
    });
});