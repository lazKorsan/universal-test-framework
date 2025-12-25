#!/usr/bin/env python3
"""
EN BASİT TEST ÇALIŞTIRMA DOSYASI
Terminalde: python run_test.py
"""

from test_delete_courses_dinamik import US005DeleteCoursesDinamikMethod

def main():
    # API BİLGİLERİNİ BURAYA GİR
    BASE_URL = "http://localhost:8000"  # veya senin API URL'in
    TOKEN = "16120|KPHcoyX5N9dLVg9q6uFmWSGjPaX2MKKG3IZp9jy4"  # Token'ını buraya yapıştır

    print("=" * 60)
    print("PYTHON DELETE TESTİ BAŞLIYOR")
    print("=" * 60)

    # API client oluştur
    print(f"Base URL: {BASE_URL}")
    print(f"Token: {TOKEN[:20]}...")  # Token'ın sadece ilk 20 karakterini göster

    client = US005DeleteCoursesDinamikMethod(BASE_URL, TOKEN)

    # Testi çalıştır
    print("\n" + "=" * 60)
    print("TEST 1: Dinamik Create-Delete")
    print("=" * 60)

    success = client.test_dinamik_create_and_delete()

    if success:
        print("\n🎉 TEBRİKLER! TEST BAŞARILI!")
    else:
        print("\n😞 TEST BAŞARISIZ!")

    print("\n" + "=" * 60)
    print("TEST TAMAMLANDI")
    print("=" * 60)

if __name__ == "__main__":
    main()