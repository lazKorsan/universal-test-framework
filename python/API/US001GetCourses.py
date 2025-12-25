import requests
import json

# --- Konfigürasyon ve Değişkenler ---
# Java projenizdeki config-local.yaml dosyasından alınan bilgiler
BASE_URL = "https://qa.instulearn.com"

# Java dosyasında (US001GetCourses.java) bulunan token. 
# Eğer süresi dolduysa güncel bir token ile değiştirmeniz gerekebilir.
TOKEN = "15707|XCnyE8Ov4ZyvaMbZIbMfzOpRUbiyA1eUVPyUhXOB"

HEADERS = {
    "Accept": "application/json",
    "x-api-key": "1234",
    "Authorization": f"Bearer {TOKEN}"
}

def test_get_courses_tc01():
    """
    US001 TC01: Admin kullanıcısı ile kursları getir (GET api/courses)
    """
    print("\n--- TC01 Başlatılıyor ---")
    endpoint = f"{BASE_URL}/api/courses"
    
    response = requests.get(endpoint, headers=HEADERS)
    
    # Status Code Doğrulama
    assert response.status_code == 200, f"Beklenen 200, Gelen {response.status_code}"
    
    # Body Doğrulama
    json_data = response.json()
    assert json_data.get("remark") == "success", f"Beklenen 'success', Gelen {json_data.get('remark')}"
    print("TC01 Başarılı: Status 200 ve remark 'success' doğrulandı.")

def test_get_courses_tc02():
    """
    US001 TC02: Belirli bir kursun detaylarını doğrula (GET api/course/1995)
    """
    print("\n--- TC02 Başlatılıyor ---")
    course_id = 1995
    endpoint = f"{BASE_URL}/api/course/{course_id}"
    
    response = requests.get(endpoint, headers=HEADERS)
    
    # Status Code
    assert response.status_code == 200, f"Beklenen 200, Gelen {response.status_code}"
    
    json_data = response.json()
    data = json_data.get("data", {})
    
    # Assertions (Java kodundaki doğrulamaların aynısı)
    assert data.get("teacher_id") == 1016, "Teacher ID eşleşmedi"
    assert data.get("creator_id") == 1016, "Creator ID eşleşmedi"
    assert str(data.get("category_id")) == "611", "Category ID eşleşmedi"
    assert data.get("type") == "course", "Type bilgisi eşleşmedi"
    
    print("TC02 Başarılı: Kurs detayları (ID: 1995) doğrulandı.")

def test_get_courses_tc03():
    """
    US001 TC03: Geçersiz token ile erişim denemesi (GET api/courses)
    """
    print("\n--- TC03 Başlatılıyor ---")
    endpoint = f"{BASE_URL}/api/courses"
    
    # Geçersiz token header'ı oluştur
    invalid_headers = HEADERS.copy()
    invalid_headers["Authorization"] = "Bearer invalid_token"
    
    response = requests.get(endpoint, headers=invalid_headers)
    
    # Status Code 401 Bekleniyor
    assert response.status_code == 401, f"Beklenen 401, Gelen {response.status_code}"
    
    json_data = response.json()
    assert json_data.get("message") == "Unauthenticated.", f"Beklenen mesaj 'Unauthenticated.', Gelen '{json_data.get('message')}'"
    print("TC03 Başarılı: 401 Unauthorized hatası doğrulandı.")

if __name__ == "__main__":
    # Dosya doğrudan çalıştırıldığında testleri sırayla koşar
    try:
        test_get_courses_tc01()
        test_get_courses_tc02()
        test_get_courses_tc03()
        print("\n=== TÜM TESTLER BAŞARIYLA TAMAMLANDI ===")
    except AssertionError as e:
        print(f"\n!!! TEST BAŞARISIZ !!!\nHata Detayı: {e}")
    except Exception as e:
        print(f"\n!!! BEKLENMEYEN HATA !!!\n{e}")