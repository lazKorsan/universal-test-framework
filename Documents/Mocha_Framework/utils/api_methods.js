const axios = require('axios');
const config = require('../config');
const { expect } = require('chai');

class ApiMethods {
    constructor() {
        this.token = null;
        this.response = null;
        this.fullPath = '';
        this.cookies = null;
    }

    setToken(token) {
        this.token = token;
    }

    setCookies(cookies) {
        this.cookies = cookies;
    }

    pathParam(rawPath) {
        if (rawPath.startsWith('/')) {
            rawPath = rawPath.substring(1);
        }
        this.fullPath = `${config.baseUrl}/${rawPath}`;
        console.log(`[ENDPOINT] ${this.fullPath}`);
    }

    async sendRequest(method, body = null) {
        const headers = {
            'Accept': 'application/json',
            'x-api-key': config.apiKey,
            'User-Agent': config.userAgent,
            'Referer': config.baseUrl + '/',
            'Origin': config.baseUrl
        };

        // Content-Type sadece body varsa veya POST/PUT/PATCH ise ekle
        if (body || ['POST', 'PUT', 'PATCH'].includes(method.toUpperCase())) {
            headers['Content-Type'] = 'application/json';
        }

        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }

        if (this.cookies) {
            headers['Cookie'] = this.cookies;
        }

        const options = {
            method: method.toUpperCase(),
            url: this.fullPath,
            headers: headers,
            validateStatus: () => true,
            maxRedirects: 5
        };

        // GET isteklerinde data parametresi gönderilmemeli (body null olsa bile)
        if (body !== null && ['POST', 'PUT', 'PATCH', 'DELETE'].includes(method.toUpperCase())) {
            options.data = body;
        }

        console.log(`[REQUEST] ${method} ${this.fullPath}`);

        try {
            this.response = await axios(options);

            if (this.response.headers['set-cookie']) {
                this.cookies = this.response.headers['set-cookie'];
            }

            if (this.response.status !== 200 && this.response.status !== 201) {
                console.log('⚠️ Request Failed Status:', this.response.status);
                const bodyStr = typeof this.response.data === 'string' ? this.response.data : JSON.stringify(this.response.data);
                console.log('⚠️ Response Body Preview:', bodyStr.substring(0, 200) + '...');
            }

            return this.response;
        } catch (error) {
            console.error('Request Failed:', error.message);
            throw error;
        }
    }

    statusCodeAssert(expectedCode) {
        expect(this.response).to.not.be.null;
        expect(this.response.status).to.equal(expectedCode);
    }

    assertBody(path, expectedValue) {
        expect(this.response).to.not.be.null;
        const actualValue = path.split('.').reduce((o, i) => (o ? o[i] : undefined), this.response.data);
        expect(String(actualValue)).to.equal(String(expectedValue));
    }
}

module.exports = new ApiMethods();