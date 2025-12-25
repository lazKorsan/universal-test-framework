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

def test_patch_courses_tc01():
    """US01: Geçerli ID ile güncelleme (PATCH)"""
    print("\n--- TC01 Başlatılıyor ---")
    # Java kodundaki ID: 3279
    course_id = 3279
    endpoint = f"{BASE_URL}/api/updateCourse/{course_id}"
    
    body = {
        "duration": 40,
        "capacity": 40,
        "price": 0,
        "title": "course bilgileri güncellendi "
    }
    
    response = requests.patch(endpoint, json=body, headers=HEADERS)
    
    assert response.status_code == 200
    assert response.json().get("remark") == "success"
    assert response.json().get("Message") == "Successfully Updated."
    
    print("TC01 Başarılı.")

def test_patch_courses_tc02():
    """US02: Data içermeyen (Empty Body) PATCH isteği"""
    print("\n--- TC02 Başlatılıyor ---")
    course_id = 3280
    endpoint = f"{BASE_URL}/api/updateCourse/{course_id}"
    
    body = {} # Boş body
    
    response = requests.patch(endpoint, json=body, headers=HEADERS)
    
    assert response.status_code == 203
    assert response.json().get("remark") == "failed"
    # Nested message kontrolü
    assert response.json().get("data", {}).get("message") == "There is no information to update."
    
    print("TC02 Başarılı.")

def test_patch_courses_tc03_a01():
    """US03A01: Kaydı olmayan ID ile güncelleme"""
    print("\n--- TC03 A01 Başlatılıyor ---")
    course_id = 13280
    endpoint = f"{BASE_URL}/api/updateCourse/{course_id}"
    
    body = {
        "duration": 40,
        "capacity": 40,
        "price": 0,
        "title": "course bilgileri güncellendi "
    }
    
    response = requests.patch(endpoint, json=body, headers=HEADERS)
    
    assert response.status_code == 203
    assert response.json().get("remark") == "failed"
    assert response.json().get("data", {}).get("message") == "There is not course for this id."
    
    print("TC03 A01 Başarılı.")

def test_patch_courses_tc03_a02():
    """US03A02: ID olmadan güncelleme denemesi"""
    print("\n--- TC03 A02 Başlatılıyor ---")
    # Endpoint sonunda ID yok
    endpoint = f"{BASE_URL}/api/updateCourse"
    
    body = {
        "duration": 40,
        "capacity": 40,
        "price": 0,
        "title": "course bilgileri güncellendi"
    }
    
    response = requests.patch(endpoint, json=body, headers=HEADERS)
    
    assert response.status_code == 203
    assert response.json().get("remark") == "failed"
    assert response.json().get("data", {}).get("message") == "No id"
    
    print("TC03 A02 Başarılı.")

def test_patch_courses_tc04():
    """US04: Geçersiz Token ile PATCH"""
    print("\n--- TC04 Başlatılıyor ---")
    endpoint = f"{BASE_URL}/api/updateCourse/1995"
    
    body = {"title": "deneme"}
    
    inv_headers = HEADERS.copy()
    inv_headers["Authorization"] = "Bearer invalid"
    
    response = requests.patch(endpoint, json=body, headers=inv_headers)
    
    assert response.status_code == 401
    assert response.json().get("message") == "Unauthenticated."
    
    print("TC04 Başarılı.")

def test_patch_courses_tc05():
    """US05: Response ID ile URL ID eşleşmesi"""
    print("\n--- TC05 Başlatılıyor ---")
    course_id = 3279
    endpoint = f"{BASE_URL}/api/updateCourse/{course_id}"
    
    body = {
        "duration": 40,
        "capacity": 40,
        "price": 0,
        "title": "course bilgileri güncellendi"
    }
    
    response = requests.patch(endpoint, json=body, headers=HEADERS)
    
    assert response.status_code == 200
    # Python'da key'i direkt string olarak veriyoruz
    updated_id = response.json().get("Updated Course ID")
    
    assert updated_id == course_id, f"URL ID ({course_id}) ile Response ID ({updated_id}) eşleşmedi!"
    
    print("TC05 Başarılı.")

def test_patch_courses_tc06():
    """US06: Zincirleme Test (Update -> Extract ID -> Get Verify)"""
    print("\n--- TC06 Başlatılıyor ---")
    course_id = 3279
    endpoint_patch = f"{BASE_URL}/api/updateCourse/{course_id}"
    
    updated_title = "course bilgileri güncellendi - Verified via GET"
    
    body = {
        "duration": 55,
        "capacity": 55,
        "price": 0,
        "title": updated_title
    }
    
    # 1. Adım: Güncelle (PATCH)
    response_patch = requests.patch(endpoint_patch, json=body, headers=HEADERS)
    assert response_patch.status_code == 200
    
    # 2. Adım: ID'yi al
    updated_course_id = response_patch.json().get("Updated Course ID")
    
    # 3. Adım: GET ile doğrula
    endpoint_get = f"{BASE_URL}/api/course/{updated_course_id}"
    response_get = requests.get(endpoint_get, headers=HEADERS)
    
    assert response_get.status_code == 200
    
    data = response_get.json().get("data", {})
    
    # Title kontrolü (translations içinde olabilir)
    translations = data.get("translations", [])
    if translations:
        actual_title = translations[0].get("title")
        assert actual_title == updated_title, f"Title güncellenmedi! Beklenen: {updated_title}, Gelen: {actual_title}"
    
    assert data.get("duration") == 55
    assert data.get("capacity") == 55
    
    print("TC06 Başarılı: Güncelleme GET ile doğrulandı.")

if __name__ == "__main__":
    try:
        test_patch_courses_tc01()
        test_patch_courses_tc02()
        test_patch_courses_tc03_a01()
        test_patch_courses_tc03_a02()
        test_patch_courses_tc04()
        test_patch_courses_tc05()
        test_patch_courses_tc06()
        print("\n=== US004 TÜM TESTLER BAŞARILI ===")
    except AssertionError as e:
        print(f"\n!!! TEST HATASI !!!\n{e}")
    except Exception as e:
        print(f"\n!!! BEKLENMEYEN HATA !!!\n{e}")