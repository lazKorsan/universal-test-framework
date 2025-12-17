package com.student;

import Android.Utilities.AndroidDriver;
import Browser.Utilities.Driver;
import com.student.ConfigManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest {

    protected ConfigManager config;
    protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        System.out.println("=".repeat(50));
        System.out.println("🚀 UNIVERSAL TEST FRAMEWORK - TEST SUITE BAŞLIYOR");
        System.out.println("=".repeat(50));
        config = ConfigManager.getInstance();
        printConfiguration();
        createReportDirectory();
    }

    @BeforeClass(alwaysRun = true)
    public void classSetup() {
        String className = this.getClass().getSimpleName();
        System.out.println("\n📂 Test Sınıfı: " + className);
        System.out.println("-".repeat(30));
    }

    @BeforeMethod(alwaysRun = true)
    public void methodSetup(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        System.out.println("\n▶️  Test: " + methodName);
        System.out.println("   Başlangıç: " + getCurrentTimestamp());
    }

    @AfterMethod(alwaysRun = true)
    public void methodTeardown(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("   ❌ DURUM: BAŞARISIZ");
            captureScreenshot(methodName);
            logFailureDetails(result);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            System.out.println("   ✅ DURUM: BAŞARILI");
        } else {
            System.out.println("   ⚠️  DURUM: ATLANDI");
        }
        System.out.println("   Bitiş: " + getCurrentTimestamp());
        System.out.println("   Süre: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        cleanupAfterTest();
    }

    @AfterClass(alwaysRun = true)
    public void classTeardown() {
        System.out.println("\n🏁 Test Sınıfı Tamamlandı: " + this.getClass().getSimpleName());
        System.out.println("=".repeat(50));
    }

    @AfterSuite(alwaysRun = true)
    public void globalTeardown() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("✅ TEST SUITE TAMAMLANDI");
        System.out.println("=".repeat(50));
        generateSummaryReport();
    }

    protected void captureScreenshot(String testName) {
        if (!config.getBoolean("screenshot.on.failure", true)) {
            return;
        }
        try {
            WebDriver driver = null;
            if (isWebTest()) {
                driver = Driver.getDriver();
            } else if (isMobileTest()) {
                driver = (WebDriver) AndroidDriver.getDriver();
            }

            if (driver instanceof TakesScreenshot) {
                File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String timestamp = dateFormat.format(new Date());
                String screenshotDir = config.get("report.path", "target/reports") + "/screenshots";
                Path screenshotPath = Paths.get(screenshotDir, testName + "_" + timestamp + ".png");
                Files.createDirectories(screenshotPath.getParent());
                Files.copy(source.toPath(), screenshotPath);
                System.out.println("   📸 Screenshot kaydedildi: " + screenshotPath);
            }
        } catch (Exception e) {
            System.err.println("   ❌ Screenshot alınamadı: " + e.getMessage());
        }
    }

    protected void logFailureDetails(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            System.out.println("   🔍 HATA: " + throwable.getMessage());
        }
    }

    protected void cleanupAfterTest() {
        if (isWebTest()) {
            Driver.closeDriver();
        } else if (isMobileTest()) {
            AndroidDriver.closeDriver();
        }
    }

    protected void createReportDirectory() {
        try {
            Path path = Paths.get(config.get("report.path", "target/reports"));
            Files.createDirectories(path);
            System.out.println("📁 Rapor dizini: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("❌ Rapor dizini oluşturulamadı: " + e.getMessage());
        }
    }

    protected void printConfiguration() {
        System.out.println("\n⚙️  KONFİGÜRASYON:");
        if (isWebTest()) {
            System.out.println("   Platform: Web");
            System.out.println("   Browser: " + config.get("browser"));
            System.out.println("   Base URL: " + config.get("base.url"));
        } else if (isMobileTest()) {
            System.out.println("   Platform: " + config.get("platformName"));
            System.out.println("   Cihaz: " + config.get("android.device.name"));
            System.out.println("   Sürüm: " + config.get("android.platform.version"));
        }
        System.out.println("   Environment: " + config.get("environment"));
    }

    protected String getCurrentTimestamp() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    protected void generateSummaryReport() {
        System.out.println("\n📊 TEST ÖZET RAPORU");
        System.out.println("   Tarih: " + new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        System.out.println("   Saat: " + getCurrentTimestamp());
    }

    protected boolean isWebTest() {
        return this.getClass().getPackage().getName().contains("Browser");
    }

    protected boolean isMobileTest() {
        return this.getClass().getPackage().getName().contains("Android");
    }
}