# conftest.py
import pytest
from test_delete_courses_dinamik import US005DeleteCoursesDinamikMethod

@pytest.fixture
def api_client():
    # Config'den bilgileri al
    base_url = "http://your-api-url.com"
    token = "your-token-here"

    client = US005DeleteCoursesDinamikMethod(base_url, token)
    yield client

    # Test sonrası temizlik
    client.cleanup_test_courses()

# test_delete_courses.py
def test_dinamik_create_delete(api_client):
    """Test 1: Dinamik create ve delete"""
    result = api_client.test_dinamik_create_and_delete()
    assert result is True

def test_multiple_operations(api_client):
    """Test 2: Çoklu create-delete"""
    results = api_client.test_multiple_create_delete(3)

    # En az 2 başarılı olmalı
    successful = sum(1 for _, _, success in results if success)
    assert successful >= 2

def test_hardcoded_id(api_client):
    """Test 3: Hard-coded ID ile"""
    # Önce bir course oluştur
    course_id = api_client.create_course()

    # Sonra sil
    result = api_client.test_with_hardcoded_id(course_id)
    assert result is True

# Tek başına çalıştırma
if __name__ == "__main__":
    # Config
    BASE_URL = "http://localhost:8000"
    TOKEN = "15820|Y8dhv0NMmJFSoYOZQPlDXY691VJf7VHaiadiper5"

    # Client oluştur
    client = US005DeleteCoursesDinamikMethod(BASE_URL, TOKEN)

    # Testleri çalıştır
    print("=" * 50)
    print("TEST 1: Dinamik Create-Delete")
    print("=" * 50)
    client.test_dinamik_create_and_delete()

    print("\n" + "=" * 50)
    print("TEST 2: Çoklu İşlem")
    print("=" * 50)
    client.test_multiple_create_delete(2)

    print("\n" + "=" * 50)
    print("TEST 3: Hard-coded ID")
    print("=" * 50)
    # Hard-coded ID ile test etmek için
    # client.test_with_hardcoded_id(3390)