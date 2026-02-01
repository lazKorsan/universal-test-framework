const api = require('../utils/api_methods');
const { generateToken } = require('../utils/authentication');
const { describe, it, before } = require('mocha');
const { expect } = require('chai');

describe('US005 - Delete Course Test (Dynamic)', function() {
    this.timeout(20000); // Süreyi artırdık çünkü 2 istek atacağız
    let token;
    let createdCourseId;

    before(async function() {
        token = await generateToken();
        api.setToken(token);
    });

    it('Should create a new course and then delete it', async function() {
        // ==========================================
        // ADIM 1: YENİ KURS OLUŞTUR (POST)
        // ==========================================
        console.log('\n🚀 ADIM 1: Yeni Kurs Oluşturuluyor...');
        api.pathParam('api/addCourse');

        const newCourseBody = {
            "title": "Mocha Test Course " + Date.now(), // Benzersiz isim
            "type": "course",
            "slug": "mocha-test-course-" + Date.now(),
            "start_date": "2025-11-28",
            "duration": 40,
            "capacity": 40,
            "price": 0,
            "description": "Mocha ile otomatik oluşturulan test kursu.",
            "teacher_id": 1016,
            "category_id": 611
        };

        await api.sendRequest('POST', newCourseBody);
        api.statusCodeAssert(200);

        // Oluşan ID'yi al (API yanıtında 'Added Course ID' olarak dönüyor)
        // Not: Key içinde boşluk olduğu için ['Added Course ID'] şeklinde erişiyoruz
        createdCourseId = api.response.data['Added Course ID'];

        console.log(`✅ Kurs Oluşturuldu! ID: ${createdCourseId}`);
        expect(createdCourseId).to.be.a('number');

        // ==========================================
        // ADIM 2: OLUŞAN KURSU SİL (DELETE)
        // ==========================================
        console.log(`\n🗑️ ADIM 2: Kurs Siliniyor (ID: ${createdCourseId})...`);

        api.pathParam(`api/deleteCourse/${createdCourseId}`);
        await api.sendRequest('DELETE');

        // ==========================================
        // ADIM 3: DOĞRULAMA
        // ==========================================
        api.statusCodeAssert(200);
        api.assertBody('remark', 'success');

        const responseData = api.response.data;
        const message = responseData.message || responseData.Message;
        expect(message).to.equal('Successfully Deleted.');

        console.log('\n✅ SİLME İŞLEMİ BAŞARILI');
        console.log('='.repeat(50));
        console.log(`Silinen Course ID: ${createdCourseId}`);
        console.log(`Remark: ${responseData.remark}`);
        console.log(`Message: ${message}`);
        console.log('='.repeat(50));
    });
});