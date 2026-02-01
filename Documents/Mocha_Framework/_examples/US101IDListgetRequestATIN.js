const api = require('../utils/api_methods');
const { generateToken } = require('../utils/authentication');
const { describe, it, before } = require('mocha');

describe('US102 - Get Webinar IDs', function() {
    this.timeout(20000);
    let token;

    before(async function() {
        token = await generateToken();
        api.setToken(token);
    });

    it('Should list all webinar IDs', async function() {
        // 1. Endpoint
        api.pathParam('api/courses');

        // 2. Request
        await api.sendRequest('GET');
        api.statusCodeAssert(200);

        // 3. Response Analizi
        const responseData = api.response.data;

        let webinars = [];

        if (responseData.data && responseData.data.webinars) {
            webinars = responseData.data.webinars;
        } else if (responseData.webinars) {
            webinars = responseData.webinars;
        } else if (responseData.AddedCourseID && responseData.AddedCourseID.webinars) {
             webinars = responseData.AddedCourseID.webinars;
        } else if (Array.isArray(responseData.data)) {
            webinars = responseData.data;
        }

        // 4. ID'leri Listele (Alt alta)
        console.log('\n✅ WEBINAR ID LİSTESİ:');
        console.log('='.repeat(50));

        if (webinars.length > 0) {
            webinars.forEach((w, index) => {
                // Her ID'yi yeni satıra yazdır
                console.log(`${index + 1}. Webinar ID: ${w.id}`);
            });

            console.log('='.repeat(50));
            console.log(`Toplam ${webinars.length} adet webinar bulundu.`);
        } else {
            console.log('⚠️ Webinar listesi bulunamadı.');
            console.log('Response Keys:', Object.keys(responseData));
        }
    });
});