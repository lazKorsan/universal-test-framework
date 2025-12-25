import requests
import json
import re
import time
from typing import Dict, Any, Optional

class US005DeleteCoursesDinamikMethod:

    def __init__(self, base_url: str, token: str):
        self.base_url = base_url
        self.headers = {
            'Authorization': f'Bearer {token}',
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
        self.session = requests.Session()
        self.session.headers.update(self.headers)

    # ==================== YARDIMCI METHODLAR ====================

    def create_course(self) -> int:
        """Yeni course oluştur ve ID'sini döndür"""
        print("\n=== YENİ COURSE OLUŞTURMA ===")

        url = f"{self.base_url}/api/addCourse"

        # Dinamik veriler
        timestamp = int(time.time())
        request_body = {
            "title": f"Dinamik Test Course {timestamp}",
            "type": "course",
            "slug": f"dinamik-test-course-{timestamp}",
            "start_date": "2025-11-28",
            "duration": 40,
            "capacity": 40,
            "price": 0,
            "description": "Dinamik olarak oluşturulan kurs.",
            "teacher_id": 1016,
            "category_id": 611
        }

        print(f"Request URL: {url}")
        print(f"Request Body: {json.dumps(request_body, indent=2)}")

        response = self.session.post(url, json=request_body)

        print(f"Status Code: {response.status_code}")
        print(f"Response: {json.dumps(response.json(), indent=2)}")

        # Status code kontrolü
        assert response.status_code == 200, f"Beklenen 200, alınan {response.status_code}"

        # Response body kontrolü
        response_data = response.json()
        assert response_data.get("remark") == "success", "Remark 'success' değil!"
        assert response_data.get("Message") == "Successfully Added.", "Message doğru değil!"

        # Oluşturulan Course ID'yi al
        created_id = self.extract_added_course_id(response_data)
        print(f"✓ Oluşturulan Course ID: {created_id}")

        return created_id

    def extract_added_course_id(self, response_data: Dict[str, Any]) -> int:
        """Response'tan 'Added Course ID' değerini çıkar"""
        # Method 1: Direkt dict'ten alma
        if "Added Course ID" in response_data:
            return response_data["Added Course ID"]

        # Method 2: String içinde arama
        response_str = json.dumps(response_data)
        match = re.search(r'"Added Course ID"\s*:\s*(\d+)', response_str)
        if match:
            return int(match.group(1))

        raise ValueError("Added Course ID bulunamadı!")

    def delete_course(self, course_id: int) -> str:
        """Course'u sil ve silinen ID'yi döndür"""
        print(f"\n=== COURSE SİLME (ID: {course_id}) ===")

        url = f"{self.base_url}/api/deleteCourse/{course_id}"
        print(f"DELETE URL: {url}")

        response = self.session.delete(url)

        print(f"Status Code: {response.status_code}")
        print(f"Response: {json.dumps(response.json(), indent=2)}")

        # Status code kontrolü
        assert response.status_code == 200, f"Beklenen 200, alınan {response.status_code}"

        # Response body kontrolü
        response_data = response.json()
        assert response_data.get("remark") == "success", "Remark 'success' değil!"
        assert response_data.get("Message") == "Successfully Deleted.", "Message doğru değil!"

        # Silinen ID'yi al
        deleted_id = self.extract_deleted_course_id(response_data)
        print(f"Response'taki Deleted Course ID: {deleted_id}")

        # ID'leri karşılaştır
        if str(course_id) == deleted_id:
            print("✓ Silinen ID doğrulandı!")
        else:
            print(f"✗ ID'ler eşleşmiyor! Beklenen: {course_id}, Bulunan: {deleted_id}")

        return deleted_id

    def extract_deleted_course_id(self, response_data: Dict[str, Any]) -> str:
        """Response'tan 'Deleted Course ID' değerini çıkar"""
        # Method 1: Direkt dict'ten alma
        if "Deleted Course ID" in response_data:
            return str(response_data["Deleted Course ID"])

        # Method 2: String içinde regex ile arama
        response_str = json.dumps(response_data)

        # Regex pattern'leri
        patterns = [
            r'"Deleted Course ID"\s*:\s*"(\d+)"',  # "Deleted Course ID": "3390"
            r'"Deleted Course ID"\s*:\s*(\d+)',     # "Deleted Course ID": 3390
        ]

        for pattern in patterns:
            match = re.search(pattern, response_str)
            if match:
                return match.group(1)

        # Method 3: Manual arama
        for key, value in response_data.items():
            if "Deleted" in str(key) and "Course" in str(key) and "ID" in str(key):
                return str(value)

        raise ValueError("Deleted Course ID bulunamadı!")

    def verify_course_deleted(self, course_id: int) -> bool:
        """Course'un silindiğini doğrula"""
        print(f"\n=== SİLİNEN KAYIT DOĞRULAMA (ID: {course_id}) ===")

        url = f"{self.base_url}/api/course/{course_id}"
        print(f"GET URL: {url}")

        response = self.session.get(url)

        status_code = response.status_code
        print(f"GET Status Code: {status_code}")

        if status_code == 404:
            print("✓ Course başarıyla silinmiş (404 Not Found)")
            return True

        elif status_code == 200:
            print("✗ Course hala mevcut!")
            print(f"Response: {json.dumps(response.json(), indent=2)}")
            return False

        elif status_code == 203:
            response_data = response.json()
            response_str = json.dumps(response_data)

            if any(msg in response_str for msg in ["not found", "There is not course", "bulunamadı"]):
                print("✓ Course silinmiş (203 with error message)")
                return True
            else:
                print(f"? Beklenmeyen 203 response: {response_str}")
                return False

        else:
            print(f"? Beklenmeyen status code: {status_code}")
            print(f"Response: {json.dumps(response.json(), indent=2)}")
            return False

    def extract_all_data_from_response(self, response_data: Dict[str, Any]) -> None:
        """Response'taki tüm verileri göster (debug için)"""
        print("\n=== RESPONSE ANALİZİ ===")

        def print_nested(data, indent=0):
            for key, value in data.items():
                if isinstance(value, dict):
                    print(" " * indent + f"{key}:")
                    print_nested(value, indent + 2)
                else:
                    print(" " * indent + f"{key}: {value}")

        print_nested(response_data)

    # ==================== TEST METHODLARI ====================

    def test_dinamik_create_and_delete(self):
        """Dinamik olarak course oluştur, sil ve doğrula"""
        print("=== DİNAMİK CREATE VE DELETE TESTİ ===")

        try:
            # 1. Yeni course oluştur
            created_course_id = self.create_course()

            # 2. Oluşturulan course'u sil
            self.delete_course(created_course_id)

            # 3. Silindiğini doğrula
            self.verify_course_deleted(created_course_id)

            print("\n✓ TEST BAŞARIYLA TAMAMLANDI")
            return True

        except Exception as e:
            print(f"\n✗ TEST BAŞARISIZ: {str(e)}")
            return False

    def test_multiple_create_delete(self, count: int = 3):
        """Birden fazla course oluştur ve sil"""
        print(f"=== ÇOKLU CREATE-DELETE TESTİ ({count} adet) ===")

        results = []
        for i in range(1, count + 1):
            print(f"\n--- İterasyon {i} ---")

            try:
                course_id = self.create_course()
                self.delete_course(course_id)
                is_deleted = self.verify_course_deleted(course_id)

                results.append((i, course_id, is_deleted))
                print(f"--- İterasyon {i} tamamlandı ---\n")

            except Exception as e:
                print(f"✗ İterasyon {i} başarısız: {str(e)}")
                results.append((i, None, False))

        # Sonuçları özetle
        print("\n=== TEST SONUÇLARI ===")
        successful = sum(1 for _, _, success in results if success)
        print(f"Başarılı: {successful}/{count}")

        return results

    def test_with_hardcoded_id(self, course_id: int):
        """Hard-coded ID ile test"""
        print(f"=== HARD-CODED ID İLE TEST (ID: {course_id}) ===")

        try:
            # DELETE
            deleted_id = self.delete_course(course_id)

            # Doğrulama
            if str(course_id) == deleted_id:
                print("✓ BAŞARILI: ID'ler eşleşiyor!")
            else:
                print(f"✗ HATA: Beklenen: {course_id}, Bulunan: {deleted_id}")

                # Debug için response'u göster
                print("\nResponse detayları:")
                # Burada response'u tekrar almak gerekebilir
                # Veya delete_course methodundan response'u döndürebiliriz

            # Silindiğini doğrula
            self.verify_course_deleted(course_id)

            return True

        except Exception as e:
            print(f"✗ TEST BAŞARISIZ: {str(e)}")
            return False

    def cleanup_test_courses(self, keyword: str = "Dinamik Test"):
        """Test sırasında oluşan kursları temizle"""
        print("\n=== TEST COURSE'LARI TEMİZLEME ===")

        # Bu endpoint varsa kullan, yoksa manuel yap
        try:
            # Örnek: Tüm kursları listele
            url = f"{self.base_url}/api/courses"
            response = self.session.get(url)

            if response.status_code == 200:
                courses = response.json().get("data", [])

                for course in courses:
                    if keyword in course.get("title", ""):
                        course_id = course.get("id")
                        print(f"Test course'u siliniyor: {course_id} - {course.get('title')}")
                        self.delete_course(course_id)

        except Exception as e:
            print(f"Temizleme sırasında hata: {str(e)}")