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

        // 2. Webinars Listesini Yakala
            // Yanıt yapına göre: response.AddedCourseID.webinars
            const webinars = api.response.AddedCourseID.webinars;

            if (webinars && Array.isArray(webinars)) {
                console.log('\n' + '='.repeat(30));
                console.log('🚀 WEBİNAR ID LİSTESİ');
                console.log('='.repeat(30));

                // Sadece ID'leri içeren yeni bir liste oluştur ve yazdır
                const idList = webinars.map(w => w.id);

                console.log('IDler:', idList.join(', '));
                console.log(`📊 Toplam ${idList.length} adet ID listelendi.`);
                console.log('='.repeat(30));
            } else {
                console.log('⚠️ Webinars listesi bulunamadı veya boş.');
            }









         // AddedCourseID.webinars altındaki id'leri yazdır
                if (api.response.AddedCourseID && Array.isArray(api.response.AddedCourseID.webinars)) {
                    console.log('\n✅ WEBİNAR LİSTESİ:');
                    console.log('='.repeat(50));
                    api.response.AddedCourseID.webinars.forEach((webinar, index) => {
                        console.log(`${index + 1}. [ID: ${webinar.id}]`);
                    });
                    console.log('='.repeat(50));
                    console.log(`Toplam ${api.response.AddedCourseID.webinars.length} webinar bulundu.`);
                } else {
                    console.log('⚠️ Webinar listesi dizi formatında değil veya boş.');
                    console.log('Response:', JSON.stringify(api.response.AddedCourseID, null, 2));
                }

        // todo terminalde dosya dizinine in
        // todo     npx mocha test ismi

        // AddedCourseID.webinars[0].id
    });
});