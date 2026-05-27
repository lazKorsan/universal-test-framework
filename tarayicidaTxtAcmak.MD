# Chrome Tarayıcıda Geçici Metin Alanı Açma

Bu belge, Chrome tarayıcısında hızlıca düzenlenebilir bir metin alanı veya HTML sayfası açmak için kullanılabilecek pratik yöntemleri açıklamaktadır. Bu yöntemler, özellikle geçici notlar almak, HTML/CSS denemeleri yapmak veya web elementlerinin `locator`'larını test etmek için faydalıdır.

## Nasıl Kullanılır?

Aşağıdaki komutları Chrome tarayıcınızın adres çubuğuna yapıştırıp Enter tuşuna basarak ilgili sayfaları açabilirsiniz.

### 1. Beyaz Arka Planlı Düzenlenebilir HTML Sayfası

Bu komut, beyaz arka plan rengine sahip, basit ve düzenlenebilir bir HTML sayfası açar. İçine metin, HTML etiketleri veya element `locator`'ları yapıştırıp üzerinde çalışabilirsiniz.

```
data:text/html, <html contenteditable>
```

**Kullanım Alanları:**
* Hızlı not alma
* Basit HTML/CSS denemeleri
* Web element `locator`'larını test etme

### 2. Koyu Tema (Siyah Arka Plan) ile Düzenlenebilir HTML Sayfası

Bu komut, koyu tema (siyah arka plan, açık renk metin) ile düzenlenebilir bir HTML sayfası açmayı amaçlar. Bu, özellikle uzun süreli kullanımlarda veya koyu tema tercih edenler için daha konforlu bir deneyim sunabilir.

```
data:text/html, <html contenteditable style="background:#1e1e1e;color:#d4d4d4;font-family:monospace;padding:20px;line-height:1.5;">
```

**Not:** Bu komut bazı sistemlerde veya Chrome sürümlerinde beklenen koyu temayı uygulamayabilir. Eğer bu komut sizde çalışmazsa, ilk komutu kullanarak beyaz temalı sayfayı kullanmaya devam edebilirsiniz.

---

Bu yöntemler, geliştiriciler ve test mühendisleri için pratik ve hızlı çözümler sunar.
