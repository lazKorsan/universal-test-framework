module.exports = {
  default: {
    requireModule: [],
    require: [
      'step_definitions/**/*.js' // Step definition dosyalarının yolu
    ],
    format: [
      'summary',
      'progress-bar'
    ],
    publishQuiet: true
  }
};