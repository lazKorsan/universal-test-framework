from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import time

# Chrome driver'ı başlat
driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))

# Google'ı aç
driver.get("https://www.google.com")

time.sleep(2)  # Sayfanın açılmasını bekle

# Arama kutusunu bul
search_box = driver.find_element(By.NAME, "q")

# Arama yapılacak metin
search_box.send_keys("Python selenium örneği")
search_box.send_keys(Keys.ENTER)

time.sleep(5)  # Sonuçları gör
