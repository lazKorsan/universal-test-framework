
Feature:

@DBUS015
Scenario:
Given database baglantisi kurulur
Then Admin kullanicisi "products" tablosunda "unlimited_inventory" sutunu ve "inventory" sutunlarındaki degerleri birlestirerek stockda olmayan urunleri listeler yada consola "stockda bulunmayam urun yok" yazdırır
And database baglantisi kapatilir