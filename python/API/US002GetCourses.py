import requests
import json

# --- Konfigürasyon ---
BASE_URL = "https://qa.instulearn.com"
TOKEN = "15707|XCnyE8Ov4ZyvaMbZIbMfzOpRUbiyA1eUVPyUhXOB"

HEADERS = {
    "Accept": "application/json",
    "x-api-key": "1234",
    "Authorization": f"Bearer {TOKEN}"
}

def test_get_course_tc01():
    """US002 TC01: Geçerli ID (1995) ile belirli bir kursu getir"""
    print("\n--- TC01 Başlatılıyor ---")
    endpoint = f"{BASE_URL}/api/course/1995"
    
    response = requests.get(endpoint, headers=HEADERS)
    
    assert response.status_code == 200, f"Status 200 bekleniyordu, {response.status_code} geldi."
    assert response.json().get("remark") == "success", "Remark 'success' olmalı."
    print("TC01 Başarılı.")

def test_get_course_tc02():
    """US002 TC02: Bilinen ID ile detaylı doğrulama"""
    print("\n--- TC02 Başlatılıyor ---")
    endpoint = f"{BASE_URL}/api/course/1995"
    
    response = requests.get(endpoint, headers=HEADERS)
    assert response.status_code == 200
    
    # Response içindeki 'data' objesini alıyoruz
    data = response.json().get("data", {})
    
    # Java'daki assertBody satırlarının Python karşılığı
    # Veri tiplerine (int vs string) Python'da da dikkat ediyoruz
    assert data.get("creator_id") == 1016, "Creator ID hatalı"
    assert data.get("slug") == "Become-a-Project-Manager", "Slug hatalı"
    assert data.get("duration") == 150, "Duration hatalı"
    assert data.get("support") == 1, "Support hatalı"
    assert data.get("timezone") == "America/New_York", "Timezone hatalı"
    assert data.get("thumbnail") == "/store/1/product-management-.jpg", "Thumbnail hatalı"
    assert data.get("image_cover") == "/store/1/product-management.jpg", "Image Cover hatalı"
    assert data.get("downloadable") == 1, "Downloadable hatalı"
    assert data.get("partner_instructor") == 0, "Partner Instructor hatalı"
    assert data.get("subscribe") == 0, "Subscribe hatalı"
    assert data.get("forum") == 0, "Forum hatalı"
    assert data.get("created_at") == 1624867858, "Created At hatalı"
    assert data.get("updated_at") == 1711967895, "Updated At hatalı"
    
    print("TC02 Başarılı: Tüm detaylar doğrulandı.")

def test_get_course_tc03_a01():
    """US002 TC03 A01: Geçersiz ID (11995)"""
    print("\n--- TC03 A01 Başlatılıyor ---")
    endpoint = f"{BASE_URL}/api/course/11995"
    
    response = requests.get(endpoint, headers=HEADERS)
    
    assert response.status_code == 203, f"Status 203 bekleniyordu, {response.status_code} geldi."
    assert response.json().get("remark") == "failed"
    # Nested (iç içe) JSON kontrolü: data -> message
    assert response.json().get("data", {}).get("message") == "There is not course for this id."
    print("TC03 A01 Başarılı.")

def test_get_course_tc03_a02():
    """US002 TC03 A02: ID olmadan istek"""
    print("\n--- TC03 A02 Başlatılıyor ---")
    endpoint = f"{BASE_URL}/api/course/"
    
    response = requests.get(endpoint, headers=HEADERS)
    
    assert response.status_code == 203
    assert response.json().get("remark") == "failed"
    assert response.json().get("data", {}).get("message") == "No id"
    print("TC03 A02 Başarılı.")

def test_get_course_tc04():
    """US002 TC04: Geçersiz Token"""
    print("\n--- TC04 Başlatılıyor ---")
    endpoint = f"{BASE_URL}/api/course/1995"
    
    # Geçersiz header kopyası oluştur
    inv_headers = HEADERS.copy()
    inv_headers["Authorization"] = "Bearer invalid"
    
    response = requests.get(endpoint, headers=inv_headers)
    
    assert response.status_code == 401
    assert response.json().get("message") == "Unauthenticated."
    print("TC04 Başarılı.")

if __name__ == "__main__":
    try:
        test_get_course_tc01()
        test_get_course_tc02()
        test_get_course_tc03_a01()
        test_get_course_tc03_a02()
        test_get_course_tc04()
        print("\n=== US002 TÜM TESTLER BAŞARILI ===")
    except AssertionError as e:
        print(f"\n!!! TEST HATASI !!!\n{e}")
    except Exception as e:
        print(f"\n!!! BEKLENMEYEN HATA !!!\n{e}")