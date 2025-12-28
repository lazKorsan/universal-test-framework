package Android.TestNg;

import Android.Pages.MethodsPage;
import Android.Utilities.AndroidDriver;
import Android.Utilities.ReusableMethods;
import Android.Utilities.ScrollHelper;
import Android.Utilities.TestInfoTables;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.swing.*;

public class US006CreateOrder {

    MethodsPage methodsPage = new MethodsPage();

    @Test
    public void US006_CreateOrder(){
        io.appium.java_client.android.AndroidDriver driver = AndroidDriver.getDriver();
        Assert.assertNotNull(driver, "AndroidDriver başlatılamadı!");
        methodsPage.loginWithPhoneNumber();
        ReusableMethods.bekle(6);

        ReusableMethods.clickButtonByDescription("Men Clothing");
        ReusableMethods.xPathElementClick("Classic Cargo Shorts","0 (0  Reviews)","$40.00");
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
        // yukarıdaki işlemlerden sonra confrim butona basılıyor.
        // fakat sitenin bir hastalığı var ki.
        // siparişin düşmesi 120 saniye ile 240 saniye aralığına kadar sürebiliyor.
        // BURADAN SONRA ŞÖYLE BİR ŞEY YAPMALIYIZ
        // HER BEŞ SANİYEDE BİR ŞU BUTONUN "Go to order details"
        // GORUNURLUĞUNU TEST ETMELİ . GORUNMUYORSA TEKRARK 5 SANİYE BEKLETME VERMELİ
        // "Go to order details" BUTONU görünüyorsa basmalı ve detay sayfasına gitmeli
        // elimde çok iyi bir bekletme metodu var . tam buraya uyacak .



        WebElement goToOrderDetailsPageButton = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"Go to order details\")"));

        for (int i = 1; i <= 100; i++) {
            System.out.println("Çalışma #" + i);
            if(goToOrderDetailsPageButton.isDisplayed()){
                goToOrderDetailsPageButton.click();
                break;
            }else {
                ReusableMethods.wait(2);
                SwingUtilities.invokeLater(() -> {
                    TestInfoTables.InProgress(
                            "5 SANİYE BEKLEME"
                    );
                });ReusableMethods.wait(6);
            }


        };













    }


}
