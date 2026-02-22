# sendkey_utils.py
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, ElementNotInteractableException
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.keys import Keys
import time
import random
import logging
from typing import Union, Tuple, Optional, Any


class SendKeyUtils:
    """
    Gelişmiş yazma utilities sınıfı
    Hem Robot Framework hem de Python testlerinde kullanılabilir
    """

    def __init__(self, driver=None, log_level=logging.INFO):
        """
        Args:
            driver: WebDriver instance (Robot Framework'te set edilmeli)
            log_level: Loglama seviyesi
        """
        self.driver = driver
        self.logger = self._setup_logger(log_level)
        self.highlight_color = "yellow"
        self.circle_color = "red"
        self.circle_size = 20
        self.default_delay = 0.1

    def _setup_logger(self, log_level):
        """Logger yapılandırması"""
        logger = logging.getLogger(__name__)
        logger.setLevel(log_level)
        if not logger.handlers:
            handler = logging.StreamHandler()
            formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
            handler.setFormatter(formatter)
            logger.addHandler(handler)
        return logger

    def set_driver(self, driver):
        """Driver'ı set et (Robot Framework için)"""
        self.driver = driver

    def _draw_circle(self, element, color="red", size=20, duration=1):
        """
        Elementin merkezine daire çizer

        Args:
            element: WebElement
            color: Daire rengi
            size: Daire boyutu (pixel)
            duration: Gösterim süresi
        """
        if not self.driver:
            raise ValueError("Driver set edilmemiş!")

        # Elementin pozisyonunu al
        location = element.location
        size_dict = element.size

        center_x = location['x'] + size_dict['width'] / 2
        center_y = location['y'] + size_dict['height'] / 2

        # Daire çizen JavaScript
        circle_script = f"""
            var circle = document.createElement('div');
            circle.style.position = 'absolute';
            circle.style.left = '{center_x - size / 2}px';
            circle.style.top = '{center_y - size / 2}px';
            circle.style.width = '{size}px';
            circle.style.height = '{size}px';
            circle.style.borderRadius = '50%';
            circle.style.border = '3px solid {color}';
            circle.style.backgroundColor = 'transparent';
            circle.style.zIndex = '9999';
            circle.style.pointerEvents = 'none';
            circle.style.boxShadow = '0 0 10px {color}';
            circle.id = 'sendkey_utils_circle';

            // Eski daireyi temizle
            var oldCircle = document.getElementById('sendkey_utils_circle');
            if (oldCircle) oldCircle.remove();

            document.body.appendChild(circle);

            // Belirtilen süre sonra daireyi kaldır
            setTimeout(function() {{
                var circle = document.getElementById('sendkey_utils_circle');
                if (circle) circle.remove();
            }}, {duration * 1000});
        """

        self.driver.execute_script(circle_script)
        self.logger.info(f"🔴 Daire çizildi - Renk: {color}, Boyut: {size}px")

    def highlight(self, element, color="yellow", duration=0.5, draw_circle=True, circle_color="red"):
        """
        Elementi vurgula ve isteğe bağlı daire çiz

        Args:
            element: WebElement
            color: Vurgu rengi
            duration: Gösterim süresi
            draw_circle: Daire çizilsin mi?
            circle_color: Daire rengi
        """
        if not self.driver:
            raise ValueError("Driver set edilmemiş!")

        original_style = element.get_attribute('style')

        # Vurgulama
        self.driver.execute_script(
            f"arguments[0].setAttribute('style', arguments[1]);",
            element,
            f"border: 3px solid {color}; background: #ffff99; box-shadow: 0 0 10px {color};"
        )

        # Daire çiz
        if draw_circle:
            self._draw_circle(element, circle_color, self.circle_size, duration)

        time.sleep(duration)

        # Orijinal stili geri yükle
        self.driver.execute_script(
            f"arguments[0].setAttribute('style', arguments[1]);",
            element,
            original_style
        )

    def send_keys(self, locator: Union[str, Tuple[By, str]], text: str,
                  clear_first: bool = True, press_enter: bool = False,
                  human_like: bool = False, delay: float = 0.1,
                  highlight_color: str = "yellow", timeout: int = 10,
                  draw_circle: bool = True, circle_color: str = "red") -> bool:
        """
        GELİŞMİŞ YAZMA FONKSİYONU - Her türlü senaryo için

        Args:
            locator: Element bulucu (XPATH string veya (By, value) tuple)
            text: Yazılacak metin
            clear_first: Önce temizleme
            press_enter: Enter tuşuna bas
            human_like: İnsan gibi yavaş yaz
            delay: Karakter arası gecikme
            highlight_color: Vurgu rengi
            timeout: Bekleme süresi
            draw_circle: Daire çizilsin mi?
            circle_color: Daire rengi

        Returns:
            bool: İşlem başarılı mı?
        """
        if not self.driver:
            raise ValueError("Driver set edilmemiş!")

        self.logger.info(f"\n{'=' * 70}")
        self.logger.info(f"🚀 Yazma işlemi başlıyor...")
        self.logger.info(f"    ├─ Metin: '{text}'")
        self.logger.info(f"    ├─ Locator: {locator}")
        self.logger.info(f"    ├─ İnsan gibi: {human_like}")
        self.logger.info(f"    ├─ Enter: {press_enter}")
        self.logger.info(f"    ├─ Daire: {draw_circle}")
        self.logger.info(f"    └─ Daire Rengi: {circle_color}")
        self.logger.info(f"{'=' * 70}")

        # Locator tipini belirle
        if isinstance(locator, str):
            by_type = By.XPATH
            locator_value = locator
        else:
            by_type, locator_value = locator

        try:
            # 1. Elementi bul ve etkileşime hazır olana kadar bekle
            wait = WebDriverWait(self.driver, timeout)
            element = wait.until(EC.presence_of_element_located((by_type, locator_value)))

            # Elementin etkileşime hazır olmasını bekle
            wait.until(EC.element_to_be_clickable((by_type, locator_value)))

            # Element bilgilerini al
            tag = element.tag_name
            element_type = element.get_attribute('type')
            is_displayed = element.is_displayed()
            is_enabled = element.is_enabled()
            current_value = element.get_attribute('value') or '[empty]'

            self.logger.info(f"🔍 Element bilgileri:")
            self.logger.info(f"    ├─ Tag: <{tag}>")
            self.logger.info(f"    ├─ Type: {element_type}")
            self.logger.info(f"    ├─ Görünür: {is_displayed}")
            self.logger.info(f"    ├─ Etkin: {is_enabled}")
            self.logger.info(f"    └─ Mevcut değer: '{current_value[:50]}...'")

            # Elementi vurgula ve daire çiz
            self.highlight(element, highlight_color, 0.3, draw_circle, circle_color)

            # ============= YAZMA YÖNTEMLERİ =============

            # YÖNTEM 1: Normal send_keys
            try:
                if clear_first:
                    element.clear()
                    time.sleep(0.2)

                # Elemente tıkla (odaklan)
                try:
                    element.click()
                except:
                    self.driver.execute_script("arguments[0].focus();", element)

                if human_like:
                    # İnsan gibi yazma
                    for char in text:
                        element.send_keys(char)
                        time.sleep(random.uniform(0.05, 0.15))
                else:
                    element.send_keys(text)

                if press_enter:
                    element.send_keys(Keys.RETURN)
                    self.logger.info(f"✅ Enter tuşuna basıldı")

                self.logger.info(f"✅ Yöntem 1 (Normal send_keys) BAŞARILI")
                return True

            except ElementNotInteractableException:
                self.logger.warning(f"⚠ Yöntem 1 başarısız - Element etkileşime kapalı")
            except Exception as e:
                self.logger.warning(f"⚠ Yöntem 1 başarısız - {str(e)[:50]}...")

            # YÖNTEM 2: JavaScript ile yazma
            try:
                if clear_first:
                    self.driver.execute_script("arguments[0].value = '';", element)

                # Metni JavaScript ile yaz
                self.driver.execute_script(f"arguments[0].value = arguments[1];", element, text)

                # Input event'ini tetikle
                self.driver.execute_script("""
                    var event = new Event('input', { bubbles: true });
                    arguments[0].dispatchEvent(event);

                    var changeEvent = new Event('change', { bubbles: true });
                    arguments[0].dispatchEvent(changeEvent);
                """, element)

                if press_enter:
                    self.driver.execute_script("""
                        var event = new KeyboardEvent('keydown', {
                            key: 'Enter',
                            code: 'Enter',
                            keyCode: 13,
                            which: 13,
                            bubbles: true
                        });
                        arguments[0].dispatchEvent(event);
                    """, element)

                self.logger.info(f"✅ Yöntem 2 (JavaScript) BAŞARILI")
                return True
            except Exception as e:
                self.logger.warning(f"⚠ Yöntem 2 başarısız - {str(e)[:50]}...")

            # YÖNTEM 3: ActionChains ile
            try:
                actions = ActionChains(self.driver)
                actions.move_to_element(element).click()

                if clear_first:
                    # Tüm metni seç ve sil
                    actions.key_down(Keys.CONTROL).send_keys('a').key_up(Keys.CONTROL)
                    actions.send_keys(Keys.DELETE)

                # Metni yaz
                if human_like:
                    for char in text:
                        actions.send_keys(char)
                        actions.pause(random.uniform(0.05, 0.15))
                else:
                    actions.send_keys(text)

                if press_enter:
                    actions.send_keys(Keys.RETURN)

                actions.perform()
                self.logger.info(f"✅ Yöntem 3 (ActionChains) BAŞARILI")
                return True
            except Exception as e:
                self.logger.warning(f"⚠ Yöntem 3 başarısız - {str(e)[:50]}...")

            # YÖNTEM 4: disabled/readonly kaldır + yaz
            try:
                # Disabled ve readonly attribute'lerini kaldır
                self.driver.execute_script("""
                    arguments[0].disabled = false;
                    arguments[0].removeAttribute('readonly');
                    arguments[0].removeAttribute('aria-disabled');
                """, element)
                time.sleep(0.2)

                if clear_first:
                    element.clear()

                # Odaklan ve yaz
                self.driver.execute_script("arguments[0].focus();", element)
                element.send_keys(text)

                self.logger.info(f"✅ Yöntem 4 (Disabled kaldır) BAŞARILI")
                return True
            except:
                pass

            # YÖNTEM 5: setAttribute ile yaz
            try:
                # Direkt value attribute'ünü set et
                self.driver.execute_script(f"arguments[0].setAttribute('value', arguments[1]);", element, text)

                # Event'leri tetikle
                self.driver.execute_script("""
                    ['input', 'change', 'blur'].forEach(eventType => {
                        var event = new Event(eventType, { bubbles: true });
                        arguments[0].dispatchEvent(event);
                    });
                """, element)

                self.logger.info(f"✅ Yöntem 5 (setAttribute) BAŞARILI")
                return True
            except:
                pass

            # YÖNTEM 6: Clipboard ile yapıştır
            try:
                import pyperclip
                pyperclip.copy(text)

                # Elemente odaklan
                self.driver.execute_script("arguments[0].focus();", element)
                element.click()

                if clear_first:
                    self.driver.execute_script("arguments[0].select();", element)
                    element.send_keys(Keys.DELETE)

                # Ctrl+V yapıştır
                actions = ActionChains(self.driver)
                actions.key_down(Keys.CONTROL).send_keys('v').key_up(Keys.CONTROL)
                actions.perform()

                self.logger.info(f"✅ Yöntem 6 (Clipboard) BAŞARILI")
                return True
            except ImportError:
                self.logger.warning(f"⚠ Yöntem 6 için pyperclip gerekli")
            except:
                pass

            # YÖNTEM 7: Karakter karakter JavaScript ile
            try:
                self.driver.execute_script("arguments[0].focus();", element)

                for char in text:
                    self.driver.execute_script("""
                        arguments[0].value += arguments[1];
                        var event = new Event('input', { bubbles: true });
                        arguments[0].dispatchEvent(event);
                    """, element, char)
                    time.sleep(0.05)

                self.logger.info(f"✅ Yöntem 7 (Karakter karakter JS) BAŞARILI")
                return True
            except:
                pass

            self.logger.error(f"❌ TÜM YÖNTEMLER BAŞARISIZ!")
            return False

        except TimeoutException:
            self.logger.error(f"❌ Element bulunamadı: {locator}")
            return self._try_alternative_locators(locator, text, clear_first, press_enter,
                                                  human_like, delay, highlight_color, timeout,
                                                  draw_circle, circle_color)
        except Exception as e:
            self.logger.error(f"❌ Beklenmeyen hata: {str(e)}")
            return False

    def _try_alternative_locators(self, original_locator: Union[str, Tuple], text: str,
                                  clear_first: bool, press_enter: bool, human_like: bool,
                                  delay: float, highlight_color: str, timeout: int,
                                  draw_circle: bool, circle_color: str) -> bool:
        """Alternatif locator'ları dener"""

        alt_locators = []

        if isinstance(original_locator, str):
            # XPATH alternatifleri oluştur
            alt_locators = [
                original_locator,
                f"{original_locator}[1]",
                f"({original_locator})[1]",
                original_locator.replace("input", "div"),
                original_locator.replace("@type='text'", ""),
                original_locator.replace("@type='email'", ""),
                original_locator.replace("@type='password'", ""),
            ]

            # Metin içeren alternatifler
            if "'" in original_locator:
                try:
                    extracted_text = original_locator.split("'")[1]
                    alt_locators.extend([
                        f"//*[contains(@placeholder, '{extracted_text}')]",
                        f"//*[contains(@name, '{extracted_text}')]",
                        f"//*[contains(@id, '{extracted_text}')]",
                    ])
                except:
                    pass

        for i, alt_loc in enumerate(set(alt_locators[:5]), 1):
            try:
                self.logger.info(f"🔄 Alternatif {i} deneniyor: {alt_loc}")

                if isinstance(original_locator, str):
                    by = By.XPATH
                    value = alt_loc
                else:
                    by, _ = original_locator
                    value = alt_loc

                element = WebDriverWait(self.driver, 3).until(
                    EC.presence_of_element_located((by, value))
                )

                self.highlight(element, "orange", 0.3, draw_circle, circle_color)

                if clear_first:
                    element.clear()

                element.send_keys(text)

                if press_enter:
                    element.send_keys(Keys.RETURN)

                self.logger.info(f"✅ Alternatif {i} BAŞARILI!")
                return True
            except:
                continue

        return False

    # ============= ÖZEL KULLANIM METODLARI =============

    def by_id(self, element_id: str, text: str, **kwargs) -> bool:
        """ID ile elemente yaz"""
        return self.send_keys((By.ID, element_id), text, **kwargs)

    def by_name(self, name: str, text: str, **kwargs) -> bool:
        """Name attribute ile elemente yaz"""
        return self.send_keys((By.NAME, name), text, **kwargs)

    def by_class(self, class_name: str, text: str, **kwargs) -> bool:
        """Class ile elemente yaz"""
        return self.send_keys((By.CLASS_NAME, class_name), text, **kwargs)

    def by_css(self, css_selector: str, text: str, **kwargs) -> bool:
        """CSS Selector ile elemente yaz"""
        return self.send_keys((By.CSS_SELECTOR, css_selector), text, **kwargs)

    def by_placeholder(self, placeholder_text: str, text: str, **kwargs) -> bool:
        """Placeholder attribute ile elemente yaz"""
        return self.send_keys(
            (By.XPATH, f"//input[@placeholder='{placeholder_text}'] | //textarea[@placeholder='{placeholder_text}']"),
            text, **kwargs
        )

    def by_label(self, label_text: str, text: str, **kwargs) -> bool:
        """Label metnine göre input bul ve yaz"""
        xpath = f"//label[contains(text(), '{label_text}')]/following::input[1] | //label[contains(text(), '{label_text}')]/following::textarea[1]"
        return self.send_keys(xpath, text, **kwargs)

    def random_text(self, locator: Union[str, Tuple], length: int = 10, **kwargs) -> bool:
        """Rastgele metin oluştur ve yaz"""
        import random
        import string

        random_text = ''.join(random.choices(string.ascii_letters + string.digits, k=length))
        self.logger.info(f"🎲 Rastgele metin: '{random_text}'")
        return self.send_keys(locator, random_text, **kwargs)

    def password(self, locator: Union[str, Tuple], **kwargs) -> bool:
        """Güçlü şifre oluştur ve yaz"""
        import random
        import string

        # Güçlü şifre oluştur (büyük harf, küçük harf, rakam, özel karakter)
        password_parts = [
            random.choice(string.ascii_uppercase),
            random.choice(string.ascii_lowercase),
            random.choice(string.digits),
            random.choice("!@#$%&*"),
            ''.join(random.choices(string.ascii_letters + string.digits, k=8))
        ]
        random.shuffle(password_parts)
        password = ''.join(password_parts)

        self.logger.info(f"🔐 Şifre oluşturuldu: {'*' * len(password)}")
        return self.send_keys(locator, password, **kwargs)

    def email(self, locator: Union[str, Tuple], prefix: str = "test", **kwargs) -> bool:
        """Rastgele email oluştur ve yaz"""
        import random
        import string

        domains = ["example.com", "test.com", "demo.com", "instulearn.com", "mail.com"]
        random_string = ''.join(random.choices(string.ascii_lowercase + string.digits, k=8))
        email = f"{prefix}.{random_string}@{random.choice(domains)}"

        self.logger.info(f"📧 Email oluşturuldu: {email}")
        return self.send_keys(locator, email, **kwargs)

    def phone(self, locator: Union[str, Tuple], **kwargs) -> bool:
        """Rastgele telefon numarası oluştur ve yaz"""
        import random

        phone = f"5{random.randint(10, 99)}{random.randint(100, 999)}{random.randint(1000, 9999)}"
        self.logger.info(f"📱 Telefon: {phone}")
        return self.send_keys(locator, phone, **kwargs)

    def date(self, locator: Union[str, Tuple], days_offset: int = 0,
             date_format: str = "%d.%m.%Y", **kwargs) -> bool:
        """Bugünün tarihini veya offset'li tarihi yaz"""
        from datetime import datetime, timedelta

        date = datetime.now() + timedelta(days=days_offset)
        date_str = date.strftime(date_format)

        self.logger.info(f"📅 Tarih: {date_str}")
        return self.send_keys(locator, date_str, **kwargs)

    def clear(self, locator: Union[str, Tuple], **kwargs) -> bool:
        """Elementi temizle"""
        return self.send_keys(locator, "", clear_first=True, **kwargs)

    def append(self, locator: Union[str, Tuple], text: str, **kwargs) -> bool:
        """Mevcut değerin sonuna ekle"""
        return self.send_keys(locator, text, clear_first=False, **kwargs)

    def set_circle_properties(self, color: str = "red", size: int = 20):
        """Daire özelliklerini ayarla"""
        self.circle_color = color
        self.circle_size = size
        self.logger.info(f"⚙ Daire özellikleri ayarlandı - Renk: {color}, Boyut: {size}px")


# ==================== ROBOT FRAMEWORK KEYWORD'LERİ ====================

class RobotSendKeyUtils:
    """Robot Framework için SendKeyUtils keywordleri"""

    def __init__(self):
        self.sendkey_utils = SendKeyUtils()

    def set_driver(self, driver):
        """Driver'ı set et"""
        self.sendkey_utils.set_driver(driver)

    def input_text(self, locator: str, text: str, clear_first: bool = True,
                   press_enter: bool = False, human_like: bool = False,
                   delay: float = 0.1, highlight_color: str = "yellow",
                   timeout: int = 10, draw_circle: bool = True,
                   circle_color: str = "red") -> bool:
        """Elemente metin gir (Robot Framework keyword)"""
        return self.sendkey_utils.send_keys(
            locator, text,
            clear_first=self._str_to_bool(clear_first),
            press_enter=self._str_to_bool(press_enter),
            human_like=self._str_to_bool(human_like),
            delay=float(delay),
            highlight_color=highlight_color,
            timeout=int(timeout),
            draw_circle=self._str_to_bool(draw_circle),
            circle_color=circle_color
        )

    def input_by_id(self, element_id: str, text: str, **kwargs) -> bool:
        """ID ile elemente yaz"""
        return self.sendkey_utils.by_id(element_id, text, **kwargs)

    def input_by_name(self, name: str, text: str, **kwargs) -> bool:
        """Name ile elemente yaz"""
        return self.sendkey_utils.by_name(name, text, **kwargs)

    def input_by_placeholder(self, placeholder: str, text: str, **kwargs) -> bool:
        """Placeholder ile elemente yaz"""
        return self.sendkey_utils.by_placeholder(placeholder, text, **kwargs)

    def input_by_label(self, label: str, text: str, **kwargs) -> bool:
        """Label ile elemente yaz"""
        return self.sendkey_utils.by_label(label, text, **kwargs)

    def input_random_text(self, locator: str, length: int = 10, **kwargs) -> bool:
        """Rastgele metin gir"""
        return self.sendkey_utils.random_text(locator, int(length), **kwargs)

    def input_random_email(self, locator: str, prefix: str = "test", **kwargs) -> bool:
        """Rastgele email gir"""
        return self.sendkey_utils.email(locator, prefix, **kwargs)

    def input_random_phone(self, locator: str, **kwargs) -> bool:
        """Rastgele telefon gir"""
        return self.sendkey_utils.phone(locator, **kwargs)

    def input_random_password(self, locator: str, **kwargs) -> bool:
        """Rastgele şifre gir"""
        return self.sendkey_utils.password(locator, **kwargs)

    def input_date(self, locator: str, days_offset: int = 0,
                   date_format: str = "%d.%m.%Y", **kwargs) -> bool:
        """Tarih gir"""
        return self.sendkey_utils.date(locator, int(days_offset), date_format, **kwargs)

    def clear_element(self, locator: str, **kwargs) -> bool:
        """Elementi temizle"""
        return self.sendkey_utils.clear(locator, **kwargs)

    def append_text(self, locator: str, text: str, **kwargs) -> bool:
        """Mevcut metnin sonuna ekle"""
        return self.sendkey_utils.append(locator, text, **kwargs)

    def set_circle_options(self, color: str = "red", size: int = 20):
        """Daire seçeneklerini ayarla"""
        self.sendkey_utils.set_circle_properties(color, int(size))

    def _str_to_bool(self, value):
        """String değeri boolean'a çevir"""
        if isinstance(value, bool):
            return value
        if isinstance(value, str):
            return value.lower() in ['true', 'yes', '1', 'on', 'evet']
        return bool(value)


# ==================== GERİYE UYUMLULUK İÇİN FONKSİYONLAR ====================

_sendkey_utils_instance = None


def _get_instance(driver=None):
    """Singleton instance oluştur veya mevcut instance'ı kullan"""
    global _sendkey_utils_instance
    if _sendkey_utils_instance is None:
        _sendkey_utils_instance = SendKeyUtils(driver)
    elif driver and _sendkey_utils_instance.driver is None:
        _sendkey_utils_instance.set_driver(driver)
    return _sendkey_utils_instance


def highlight(driver, element, color="yellow", duration=0.5):
    """Geriye uyumluluk için highlight fonksiyonu"""
    utils = _get_instance(driver)
    utils.highlight(element, color, duration, draw_circle=False)


def sendKey_utils(driver, locator, text, clear_first=True, press_enter=False,
                  human_like=False, delay=0.1, highlight_color="yellow", timeout=10):
    """Geriye uyumluluk için sendKey_utils fonksiyonu"""
    utils = _get_instance(driver)
    return utils.send_keys(locator, text, clear_first, press_enter, human_like,
                           delay, highlight_color, timeout, draw_circle=False)


def sendKey_utils_by_id(driver, element_id, text, **kwargs):
    """Geriye uyumluluk için sendKey_utils_by_id fonksiyonu"""
    utils = _get_instance(driver)
    return utils.by_id(element_id, text, **kwargs)


def sendKey_utils_by_name(driver, name, text, **kwargs):
    """Geriye uyumluluk için sendKey_utils_by_name fonksiyonu"""
    utils = _get_instance(driver)
    return utils.by_name(name, text, **kwargs)


def sendKey_utils_by_placeholder(driver, placeholder_text, text, **kwargs):
    """Geriye uyumluluk için sendKey_utils_by_placeholder fonksiyonu"""
    utils = _get_instance(driver)
    return utils.by_placeholder(placeholder_text, text, **kwargs)


def sendKey_utils_by_label(driver, label_text, text, **kwargs):
    """Geriye uyumluluk için sendKey_utils_by_label fonksiyonu"""
    utils = _get_instance(driver)
    return utils.by_label(label_text, text, **kwargs)


def sendKey_utils_random_text(driver, locator, length=10, **kwargs):
    """Geriye uyumluluk için sendKey_utils_random_text fonksiyonu"""
    utils = _get_instance(driver)
    return utils.random_text(locator, length, **kwargs)


def sendKey_utils_password(driver, locator, **kwargs):
    """Geriye uyumluluk için sendKey_utils_password fonksiyonu"""
    utils = _get_instance(driver)
    return utils.password(locator, **kwargs)


def sendKey_utils_email(driver, locator, prefix="test", **kwargs):
    """Geriye uyumluluk için sendKey_utils_email fonksiyonu"""
    utils = _get_instance(driver)
    return utils.email(locator, prefix, **kwargs)


def sendKey_utils_phone(driver, locator, **kwargs):
    """Geriye uyumluluk için sendKey_utils_phone fonksiyonu"""
    utils = _get_instance(driver)
    return utils.phone(locator, **kwargs)


def sendKey_utils_date(driver, locator, days_offset=0, **kwargs):
    """Geriye uyumluluk için sendKey_utils_date fonksiyonu"""
    utils = _get_instance(driver)
    return utils.date(locator, days_offset, **kwargs)


def sendKey_utils_clear(driver, locator, **kwargs):
    """Geriye uyumluluk için sendKey_utils_clear fonksiyonu"""
    utils = _get_instance(driver)
    return utils.clear(locator, **kwargs)


def sendKey_utils_append(driver, locator, text, **kwargs):
    """Geriye uyumluluk için sendKey_utils_append fonksiyonu"""
    utils = _get_instance(driver)
    return utils.append(locator, text, **kwargs)