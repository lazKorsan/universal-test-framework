import requests
import json

# --- Konfigürasyon ---
BASE_URL = "https://qa.instulearn.com"
# Token süresi dolduysa güncellemeyi unutmayın
TOKEN = "15707|XCnyE8Ov4ZyvaMbZIbMfzOpRUbiyA1eUVPyUhXOB"

HEADERS = {
    "Accept": "application/json",
    "x-api-key": "1234",
    "Authorization": f"Bearer {TOKEN}"
}

def test_post_courses_tc01():
    """US003 TC01: Geçerli Bilgilerle yeni course oluşturmak"""
    print("\n--- TC01 Başlatılıyor ---")
    endpoint = f"{BASE_URL}/api/addCourse"
    
    body = {
        "title": "course görmek güzeldir",
        "type": "course",
        "slug": "course-gormek-guzeldir",
        "start_date": "2025-11-28",
        "duration": 40,
        "capacity": 40,
        "price": 0,
        "description": "Yeni oluşturulan kursun açıklamasıdır.",
        "teacher_id": 1016,
        "category_id": 611
    }
    
    response = requests.post(endpoint, json=body, headers=HEADERS)
    
    assert response.status_code == 200, f"Beklenen 200, Gelen {response.status_code}"
    
    json_data = response.json()
    assert json_data.get("remark") == "success"
    # API bazen "Message" bazen "message" dönebilir, Java kodunda "Message" kullanılmış
    assert json_data.get("Message") == "Successfully Added."
    
    print("TC01 Başarılı.")

def test_post_courses_tc02():
    """US003 TC02: Eksik data (teacher_id) ile POST"""
    print("\n--- TC02 Başlatılıyor ---")
    endpoint = f"{BASE_URL}/api/addCourse"
    
    # teacher_id eksik
    body = {
        "title": "course görmek güzeldir",
        "type": "course",
        "slug": "course-gormek-guzeldir",
        "start_date": "2025-11-28",
        "duration": 40,
        "capacity": 40,
        "price": 0,
        "description": "Yeni oluşturulan kursun açıklamasıdır.",
        # "teacher_id": 1016,  <-- Yorum satırına alındı
        "category_id": 611
    }
    
    response = requests.post(endpoint, json=body, headers=HEADERS)
    
    assert response.status_code == 422, f"Beklenen 422, Gelen {response.status_code}"
    assert response.json().get("message") == "The teacher id field is required."
    
    print("TC02 Başarılı.")

def test_post_courses_tc03():
    """US003 TC03: Geçersiz Token ile POST"""
    print("\n--- TC03 Başlatılıyor ---")
    endpoint = f"{BASE_URL}/api/addCourse"
    
    body = {
        "title": "course deneme",
        "type": "course",
        "teacher_id": 1016,
        "category_id": 611
    }
    
    invalid_headers = HEADERS.copy()
    invalid_headers["Authorization"] = "Bearer invalid"
    
    response = requests.post(endpoint, json=body, headers=invalid_headers)
    
    assert response.status_code == 401
    assert response.json().get("message") == "Unauthenticated."
    
    print("TC03 Başarılı.")

def test_post_courses_tc04():
    """US004: Zincirleme Test (Create -> Extract ID -> Get Verify)"""
    print("\n--- TC04 (Zincirleme Test) Başlatılıyor ---")
    
    # 1. Adım: Kursu Oluştur (POST)
    test_post_courses_tc01() # TC01'deki aynı body ve işlemi kullanabiliriz veya tekrar yazabiliriz.
    # Burada mantığı açıkça görelim diye tekrar yazıyorum ama normalde fonksiyon çağrılabilir.
    
    endpoint_post = f"{BASE_URL}/api/addCourse"
    body = {
        "title": "course görmek güzeldir",
        "type": "course",
        "slug": "course-gormek-guzeldir",
        "start_date": "2025-11-28",
        "duration": 40,
        "capacity": 40,
        "price": 0,
        "description": "Yeni oluşturulan kursun açıklamasıdır.",
        "teacher_id": 1016,
        "category_id": 611
    }
    response_post = requests.post(endpoint_post, json=body, headers=HEADERS)
    assert response_post.status_code == 200
    
    # 2. Adım: ID'yi al
    # Python'da boşluklu key'leri almak için direkt string olarak yazarız
    new_course_id = response_post.json().get("Added Course ID")
    print(f"Yeni Oluşturulan Kurs ID: {new_course_id}")
    
    # 3. Adım: GET ile doğrula
    endpoint_get = f"{BASE_URL}/api/course/{new_course_id}"
    response_get = requests.get(endpoint_get, headers=HEADERS)
    
    assert response_get.status_code == 200
    
    data = response_get.json().get("data", {})
    assert data.get("id") == new_course_id
    
    # Nested yapı kontrolü: data.translations[0].title
    translations = data.get("translations", [])
    if translations:
        assert translations[0].get("title") == "course görmek güzeldir"
    else:
        raise AssertionError("Translations listesi boş döndü!")
        
    print("TC04 Başarılı: Kayıt oluşturuldu ve doğrulandı.")

if __name__ == "__main__":
    try:
        test_post_courses_tc01()
        test_post_courses_tc02()
        test_post_courses_tc03()
        test_post_courses_tc04()
        print("\n=== US003 TÜM TESTLER BAŞARILI ===")
    except AssertionError as e:
        print(f"\n!!! TEST HATASI !!!\n{e}")
    except Exception as e:
        print(f"\n!!! BEKLENMEYEN HATA !!!\n{e}")