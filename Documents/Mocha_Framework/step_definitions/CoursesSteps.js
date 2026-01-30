const { Given, When, Then } = require('@cucumber/cucumber');
const { expect } = require('chai');
const api = require('../utils/api_methods');
const { generateToken } = require('../utils/authentication');

const { setWorldConstructor, World } = require('@cucumber/cucumber');

class CustomWorld extends World {
  constructor(options) {
    super(options);
    this.token = null;
    this.response = null;
  }
}
setWorldConstructor(CustomWorld);


// --- Step Definitions ---

Given('I am an authenticated user', async function () {
  this.token = await generateToken();
  api.setToken(this.token);
});

When('I send a GET request to {string}', async function (path) {
  api.pathParam(path);
  this.response = await api.sendRequest('GET');
});

Then('the response status code should be {int}', function (statusCode) {
  expect(this.response.status).to.equal(statusCode);
});

Then('the response should contain a list of courses', function () {
  const responseData = this.response.data;

  // DEBUG: Gelen yanıtın anahtarlarını yazdır
  console.log('\n🔍 API Yanıtının Anahtarları:', Object.keys(responseData));

  let coursesList = [];
  if (responseData.data && Array.isArray(responseData.data.webinars)) {
    coursesList = responseData.data.webinars;
  } else if (responseData.webinars && Array.isArray(responseData.webinars)) {
    coursesList = responseData.webinars;
  } else if (Array.isArray(responseData.data)) {
    coursesList = responseData.data;
  }

  // Hata mesajını daha anlaşılır hale getir
  expect(coursesList, `Kurs listesi bulunamadı! Gelen yanıtın anahtarları: ${Object.keys(responseData)}`).to.be.an('array').and.to.not.be.empty;

  console.log(`\n✅ Toplam ${coursesList.length} kurs bulundu.`);
});