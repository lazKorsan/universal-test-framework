package Android.TestNg;

import Android.Pages.MethodsPage;
import Android.Utilities.AndroidDriver;
import Android.Utilities.ReusableMethods;
import Android.Utilities.ScrollHelper;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class US006CreateOrder {

    MethodsPage methodsPage = new MethodsPage();

    @Test
    public void US006_CreateOrder() {
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");
        methodsPage.loginWithPhoneNumber();
        ReusableMethods.bekle(6);

        ReusableMethods.clickButtonByDescription("Men Clothing");
        ReusableMethods.xPathElementClick("Classic Cargo Shorts", "0 (0  Reviews)", "$40.00");
        ReusableMethods.clickButtonByDescription("M");
        ScrollHelper.scrollAndClickByDescription("Add To Cart");

        methodsPage.shoppingCartButton.click();

        ReusableMethods.clickButtonByDescription("Proceed to Checkout");

        String userName = "lazKorsan";
        ScrollHelper.scrollAndClickByContainsDescription(userName);

        ScrollHelper.scrollAndClickByDescription("Save & Pay");

        ReusableMethods.clickButtonByDescription("Stripe");

        ReusableMethods.clickButtonByDescription("Confirm Order");

        methodsPage.cartNumberField.click();
        methodsPage.cartNumberField.sendKeys("4242424242424242");

        // 1 saniye bekle (gerekirse)
        ReusableMethods.wait(1);

        // Date alanına direkt tıkla
        methodsPage.dateField.click();

        // Tarih gir
        methodsPage.dateField.sendKeys("1228");

        // CVC alanına tıkla
        methodsPage.cvcfield.click();
        methodsPage.cvcfield.sendKeys("321");
        ReusableMethods.wait(1);

        // Zip alanına tıkla
        methodsPage.zipField.click();
        methodsPage.zipField.sendKeys("12345");

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        // "Go to order details" butonunun görünmesini bekleyen döngü
        boolean isOrderCompleted = false;
        int maxRetries = 60; // 60 deneme * 5 saniye = 300 saniye (5 dakika) maksimum bekleme

        for (int i = 1; i <= maxRetries; i++) {
            try {
                // Elementi her döngüde yeniden bulmaya çalışıyoruz
                WebElement goToOrderDetailsPageButton = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"Go to order details\")"));

                if (goToOrderDetailsPageButton.isDisplayed()) {
                    System.out.println("✅ Sipariş tamamlandı! Detay sayfasına gidiliyor. (Deneme #" + i + ")");
                    goToOrderDetailsPageButton.click();
                    isOrderCompleted = true;
                    break; // Döngüden çık
                }
            } catch (Exception e) {
                // Element henüz bulunamadı, beklemeye devam et
                System.out.println("⏳ Sipariş bekleniyor... Deneme #" + i + "/" + maxRetries);

                // Sadece konsola yazdırmak yerine, daha basit bir bekleme mesajı gösterebiliriz
                if (i == 1) {
                    // Sadece ilk denemede bir kez göster
                    System.out.println("Sipariş işleniyor, lütfen bekleyin...");
                }
            }

            // 5 saniye bekle
            ReusableMethods.wait(5);
        }

        if (!isOrderCompleted) {
            Assert.fail("❌ Sipariş " + (maxRetries * 5) + " saniye içinde tamamlanamadı!");
        }

        // Test başarılı mesajı
        System.out.println("✅ US006_CreateOrder testi başarıyla tamamlandı!");
    }

    @Test
    public void createOrderTest(){
        methodsPage.createOrder();
    }
}