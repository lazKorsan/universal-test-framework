import mysql.connector

def test_tc014():
    # Bağlantı Bilgileri (Java'daki ConfigLoader yerine dictionary yapısı)
    db_config = {
        'host': "195.35.59.18",
        'user': "u201212290_qainstuser",
        'password': "A/s&Yh[qU0",
        'database': "u201212290_qainstulearn"
    }

    connection = None
    cursor = None

    try:
        # 1. Bağlantı Kurma (Java'daki @BeforeMethod mantığı)
        print("🔌 Connecting to Database (Python)...")
        connection = mysql.connector.connect(**db_config)
        cursor = connection.cursor()

        table_name = "become_instructors"
        role_col = "role"
        teacher = "teacher"
        organization = "organization"

        # --- Rolleri Konsola Yazdırma ---
        print(f"=== {table_name} Tablosundaki Mevcut Roller ===")
        
        # Python'da f-string kullanımı Java'daki (+) operatöründen daha pratiktir
        list_query = f"SELECT {role_col} FROM {table_name}"
        cursor.execute(list_query)
        
        # Tüm satırları çek
        rows = cursor.fetchall()

        for row in rows:
            # row bir tuple döner (örneğin: ('teacher',)) o yüzden row[0] diyoruz
            print(f"Rol: {row[0]}")
            
        print("==============================================")
        # -----------------------------------------------

        # 2. Doğrulama Sorgusu
        query = f"SELECT COUNT(*) FROM {table_name} WHERE {role_col} NOT IN ('{teacher}', '{organization}')"

        # 3. Sorguyu Çalıştır
        cursor.execute(query)
        result = cursor.fetchone()
        count = result[0] # COUNT(*) sonucu ilk sütundadır

        # 4. Assert İşlemi (Java'daki Assert.assertEquals)
        # Python'da assert anahtar kelimesi kullanılır
        if count == 0:
            print("✅ TEST BAŞARILI")
            print('"become_instructors" tablosusunun "role" sutununda "teacher" ve "organization" rolleri dışında bir şey olmadigi dogrulanir')
        else:
            # Hata fırlat
            raise AssertionError(f"❌ TEST BAŞARISIZ: Tabloda beklenmeyen {count} adet rol bulundu.")

    except mysql.connector.Error as err:
        print(f"⚠️ Veritabanı Hatası: {err}")
    except AssertionError as ae:
        print(ae)
    finally:
        # 5. Bağlantıyı Kapatma (Java'daki @AfterMethod mantığı)
        # try-finally bloğu sayesinde hata olsa bile bağlantı kapanır
        if cursor:
            cursor.close()
        if connection and connection.is_connected():
            connection.close()
            print("🔌 Closing Database Connection (Python)...")

# Script doğrudan çalıştırıldığında testi başlat
if __name__ == "__main__":
    test_tc014()
