const api = require('../utils/api_methods');
const { generateToken } = require('../utils/authentication');
const { describe, it, before } = require('mocha');

describe('API Tests', function() {
    // Timeout süresini artıralım (API yavaş olabilir)
    this.timeout(10000);

    let token;

    before(async function() {
        // Testler başlamadan önce token al
        token = await generateToken();
        api.setToken(token);
    });

    it('US001 - Get Courses Test', async function() {
        // 1. Endpoint belirle
        api.pathParam('api/courses');

        // 2. İsteği gönder
        await api.sendRequest('GET');

        // 3. Assertion yap
        api.statusCodeAssert(200);

        // 4. Kurs Listesini Yazdır
        console.log('\n✅ KURS LİSTESİ:');
        console.log('='.repeat(50));

        // API yanıt yapısına göre (data.courses veya direkt data olabilir)
        // Genelde response.data.data içinde liste olur
        const courses = api.response.data.data || api.response.data;

        if (Array.isArray(courses)) {
            courses.forEach((course, index) => {
                console.log(`${index + 1}. [ID: ${course.id}] ${course.title || course.name}`);
            });
            console.log('='.repeat(50));
            console.log(`Toplam ${courses.length} kurs bulundu.`);
        } else {
            console.log('⚠️ Kurs listesi dizi formatında değil veya boş.');
            console.log('Response:', JSON.stringify(courses, null, 2));
        }
    });
});