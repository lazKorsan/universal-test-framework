var http = require("https");

var options = {
  "method": "GET",
  "hostname": "api.collectapi.com",
  "port": null,
  "path": "/health/dutyPharmacy?ilce=Sarayköy&il=Denizli",
  "headers": {
    "content-type": "application/json",
    "authorization": "apikey 43mgAenlx77SRyuHHuCijD:5lBqbHdHNrl0uWfYlIMSaJ"
  }
};

var req = http.request(options, function (res) {
  var chunks = [];

  res.on("data", function (chunk) {
    chunks.push(chunk);
  });

  res.on("end", function () {
    var body = Buffer.concat(chunks);
    console.log(body.toString());
  });
});

req.end();