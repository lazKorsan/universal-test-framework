import requests
import time
import uuid

# --- Konfigürasyon ---
BASE_URL = "https://qa.instulearn.com"
TOKEN = "15707|XCnyE8Ov4ZyvaMbZIbMfzOpRUbiyA1eUVPyUhXOB"

HEADERS = {
    "Accept": "application/json",
    "x-api-key": "1234",
    "Authorization": f"Bearer {TOKEN}"
}


def test_post_courses_tc01():
    """US003 TC01: Geçerli bilgilerle yeni course oluşturmak"""
    print("\n--- TC01 Başlatılıyor ---")

    endpoint = f"{BASE_URL}/api/addCourse"

    unique_slug = f"course-{uuid.uuid4()}"

    body = {
        "title": "course görmek güzeldir",
        "type": "course",
        "slug": unique_slug,
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
    assert json_data.get("Message") == "Successfully Added."

    course_id = json_data.get("Added Course ID")
    print(f"TC01 Başarılı. Oluşturulan Course ID: {course_id}")

    return course_id


def test_post_courses_tc02():
    """US003 TC02: Eksik teacher_id ile POST"""
    print("\n--- TC02 Başlatılıyor ---")

    endpoint = f"{BASE_URL}/api/addCourse"

    body = {
        "title": "course görmek güzeldir",
        "type": "course",
        "slug": f"course-{uuid.uuid4()}",
        "start_date": "2025-11-28",
        "duration": 40,
        "capacity": 40,
        "price": 0,
        "description": "Yeni oluşturulan kursun açıklamasıdır.",
        "category_id": 611
    }

    response = requests.post(endpoint, json=body, headers=HEADERS)

    assert response.status_code == 422
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
    """US004: Zincirleme Test (Create -> GET Verify)"""
    print("\n--- TC04 (Zincirleme Test) Başlatılıyor ---")

    course_id = test_post_courses_tc01()

    endpoint_get = f"{BASE_URL}/api/course/{course_id}"
    response_get = requests.get(endpoint_get, headers=HEADERS)

    assert response_get.status_code == 200

    data = response_get.json().get("data", {})
    assert data.get("id") == course_id

    translations = data.get("translations", [])
    assert translations, "Translations listesi boş döndü!"
    assert translations[0].get("title") == "course görmek güzeldir"

    print("TC04 Başarılı: Kayıt oluşturuldu ve doğrulandı.")


def run_tc01_with_delay():
    """TC01'i 5 saniye ara ile 10 defa çalıştırır"""
    for i in range(1, 11):
        print(f"\n===== TC01 ÇALIŞMA {i}/10 =====")
        test_post_courses_tc01()

        if i < 10:
            time.sleep(5)


if __name__ == "__main__":
    try:
        test_post_courses_tc01()
        test_post_courses_tc02()
        test_post_courses_tc03()
        test_post_courses_tc04()

        run_tc01_with_delay()

        print("\n=== TÜM TESTLER BAŞARILI ===")

    except AssertionError as e:
        print(f"\n!!! TEST HATASI !!!\n{e}")
    except Exception as e:
        print(f"\n!!! BEKLENMEYEN HATA !!!\n{e}")
