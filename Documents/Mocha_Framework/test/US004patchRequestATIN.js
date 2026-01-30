const api = require('../utils/api_methods');
const { generateToken } = require('../utils/authentication');
const { describe, it, before } = require('mocha');
const { expect } = require('chai');

describe('US004 - Patch Course Test (Dynamic CRUD)', function() {
    this.timeout(30000); // 4 istek atılacağı için süreyi artırdık
    let token;
    let courseId;
    const initialTitle = "Mocha Original Title " + Date.now();
    const updatedTitle = "Mocha Updated Title " + Date.now();

    before(async function() {
        token = await generateToken();
        api.setToken(token);
    });

    it('Should create, update, verify and delete a course', async function() {
        // ==========================================
        // ADIM 1: YENİ KURS OLUŞTUR (POST)
        // ==========================================
        console.log('\n🚀 ADIM 1: Yeni Kurs Oluşturuluyor...');
        api.pathParam('api/addCourse');

        const newCourseBody = {
            "title": initialTitle,
            "type": "course",
            "slug": "mocha-patch-test-" + Date.now(),
            "start_date": "2025-11-28",
            "duration": 30,
            "capacity": 30,
            "price": 0,
            "description": "Patch testi için oluşturuldu.",
            "teacher_id": 1016,
            "category_id": 611
        };

        await api.sendRequest('POST', newCourseBody);
        api.statusCodeAssert(200);
        
        courseId = api.response.data['Added Course ID'];
        console.log(`✅ Kurs Oluşturuldu! ID: ${courseId}`);

        // ==========================================
        // ADIM 2: KURSU GÜNCELLE (PATCH)
        // ==========================================
        console.log(`\n🔄 ADIM 2: Kurs Güncelleniyor (ID: ${courseId})...`);
        api.pathParam(`api/updateCourse/${courseId}`);

        const updateBody = {
            "duration": 60,
            "capacity": 60,
            "price": 100,
            "title": updatedTitle
        };

        await api.sendRequest('PATCH', updateBody);
        
        // Assertions
        api.statusCodeAssert(200);
        api.assertBody('remark', 'success');
        
        const message = api.response.data.message || api.response.data.Message;
        expect(message).to.equal('Successfully Updated.');
        
        console.log('✅ Güncelleme İsteği Başarılı.');

        // ==========================================
        // ADIM 3: GÜNCELLEMEYİ DOĞRULA (GET)
        // ==========================================
        console.log(`\n🔍 ADIM 3: Güncelleme Doğrulanıyor...`);
        api.pathParam(`api/course/${courseId}`);
        
        await api.sendRequest('GET');
        api.statusCodeAssert(200);

        const courseData = api.response.data.data || api.response.data;
        
        // Title kontrolü (API yapısına göre değişebilir)
        const actualTitle = courseData.title || (courseData.translations && courseData.translations[0].title);
        
        console.log(`Beklenen Başlık: ${updatedTitle}`);
        console.log(`Gelen Başlık:    ${actualTitle}`);
        
        expect(actualTitle).to.equal(updatedTitle);
        expect(String(courseData.duration)).to.equal('60'); // API string dönebilir
        expect(String(courseData.capacity)).to.equal('60');

        // ==========================================
        // ADIM 4: TEMİZLİK - SİLME (DELETE)
        // ==========================================
        console.log(`\n🗑️ ADIM 4: Test Verisi Temizleniyor...`);
        api.pathParam(`api/deleteCourse/${courseId}`);
        await api.sendRequest('DELETE');
        api.statusCodeAssert(200);
        
        console.log('✅ Test Başarıyla Tamamlandı!');
    });
});