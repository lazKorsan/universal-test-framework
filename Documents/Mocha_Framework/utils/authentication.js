const api = require('./api_methods');
const config = require('../config');

async function generateToken() {
    console.log('Token üretiliyor...');

    api.pathParam('api/token');

    const body = {
        email: config.adminEmail,
        password: config.adminPassword
    };

    await api.sendRequest('POST', body);

    api.statusCodeAssert(200);

    // Header'ları kontrol et (Cookie var mı?)
    console.log('Login Response Headers:', api.response.headers);

    const token = api.response.data.data.access_token;
    console.log('Token alındı:', token);

    return token;
}

module.exports = { generateToken };