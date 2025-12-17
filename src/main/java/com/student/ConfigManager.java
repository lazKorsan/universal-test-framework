package com.student;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigManager {

    private static ConfigManager instance;
    private Properties properties;
    private final String configFilePath = "src/main/resources/config.properties";
    private final Map<String, String> defaultConfigs = new HashMap<>();

    // Private constructor (Singleton pattern)
    private ConfigManager() {
        loadDefaultConfigs();
        loadProperties();
    }

    /**
     * Singleton instance döndürür
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Default ayarları yükler
     */
    private void loadDefaultConfigs() {
        defaultConfigs.put("browser", "chrome");
        defaultConfigs.put("environment", "test");
        defaultConfigs.put("headless", "false");
        defaultConfigs.put("timeout.implicit", "10");
        defaultConfigs.put("timeout.explicit", "15");
        defaultConfigs.put("timeout.pageLoad", "30");
        defaultConfigs.put("base.url", "https://www.google.com");
        defaultConfigs.put("api.base.url", "https://api.example.com");
        defaultConfigs.put("db.url", "jdbc:h2:mem:testdb");
        defaultConfigs.put("db.username", "sa");
        defaultConfigs.put("db.password", "");
        defaultConfigs.put("report.path", "target/reports");
        defaultConfigs.put("screenshot.on.failure", "true");
        defaultConfigs.put("log.level", "INFO");

        // Driver paths
        defaultConfigs.put("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        defaultConfigs.put("webdriver.gecko.driver", "src/test/resources/drivers/geckodriver.exe");
        defaultConfigs.put("webdriver.edge.driver", "src/test/resources/drivers/msedgedriver.exe");

        // Mobile settings
        defaultConfigs.put("appium.server.url", "http://127.0.0.1:4723");
        defaultConfigs.put("android.platform.version", "11.0");
        defaultConfigs.put("android.device.name", "emulator");
        defaultConfigs.put("android.app.path", "");

        // API settings
        defaultConfigs.put("api.key", "");
        defaultConfigs.put("api.secret", "");
        defaultConfigs.put("api.token", "");
    }

    /**
     * Properties dosyasını yükler
     */
    private void loadProperties() {
        properties = new Properties();

        try {
            // Önce external config dosyasını dene
            if (Files.exists(Paths.get(configFilePath))) {
                try (InputStream input = new FileInputStream(configFilePath)) {
                    properties.load(input);
                }
                System.out.println("✅ Config dosyası yüklendi: " + configFilePath);
            } else {
                // Config dosyası yoksa oluştur
                createDefaultConfigFile();
                System.out.println("⚠️ Config dosyası oluşturuldu: " + configFilePath);
            }

            // Default değerleri ekle (properties'te yoksa)
            for (Map.Entry<String, String> entry : defaultConfigs.entrySet()) {
                if (!properties.containsKey(entry.getKey())) {
                    properties.setProperty(entry.getKey(), entry.getValue());
                }
            }

        } catch (IOException e) {
            System.err.println("❌ Config dosyası yüklenemedi: " + e.getMessage());
            // Hata durumunda sadece default değerleri kullan
            for (Map.Entry<String, String> entry : defaultConfigs.entrySet()) {
                properties.setProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Default config dosyası oluşturur
     */
    private void createDefaultConfigFile() throws IOException {
        Properties defaultProps = new Properties();

        // Default değerleri ekle
        for (Map.Entry<String, String> entry : defaultConfigs.entrySet()) {
            defaultProps.setProperty(entry.getKey(), entry.getValue());
        }

        // Dosyayı kaydet
        try (FileOutputStream output = new FileOutputStream(configFilePath)) {
            defaultProps.store(output, "Universal Test Framework Configuration");
        }

        // Properties'i güncelle
        properties = defaultProps;
    }

    /**
     * Property değerini döndürür
     */
    public String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Property değerini döndürür (default ile)
     */
    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Integer değer döndürür
     */
    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key));
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }

    /**
     * Boolean değer döndürür
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Long değer döndürür
     */
    public long getLong(String key) {
        return Long.parseLong(get(key));
    }

    /**
     * Double değer döndürür
     */
    public double getDouble(String key) {
        return Double.parseDouble(get(key));
    }

    /**
     * Property değerini ayarlar (runtime'da)
     */
    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Değişiklikleri dosyaya kaydeder
     */
    public void save() throws IOException {
        try (FileOutputStream output = new FileOutputStream(configFilePath)) {
            properties.store(output, "Updated configuration");
            System.out.println("✅ Config dosyası güncellendi: " + configFilePath);
        }
    }

    /**
     * Tüm property'leri döndürür
     */
    public Properties getAllProperties() {
        return new Properties(properties);
    }

    /**
     * Environment'a göre property döndürür
     */
    public String getEnvProperty(String key) {
        String env = get("environment", "test");
        String envKey = env + "." + key;

        // Önce environment-specific property'yi dene
        String envValue = get(envKey);
        if (envValue != null) {
            return envValue;
        }

        // Yoksa genel property'yi döndür
        return get(key);
    }

    /**
     * Browser bilgisini döndürür
     */
    public String getBrowser() {
        return get("browser", "chrome").toLowerCase();
    }

    /**
     * Headless modda mı?
     */
    public boolean isHeadless() {
        return getBoolean("headless", false);
    }

    /**
     * Base URL döndürür
     */
    public String getBaseUrl() {
        return getEnvProperty("base.url");
    }

    /**
     * API base URL döndürür
     */
    public String getApiBaseUrl() {
        return getEnvProperty("api.base.url");
    }

    /**
     * Driver path döndürür
     */
    public String getDriverPath(String driverType) {
        String key = "webdriver." + driverType.toLowerCase() + ".driver";
        return get(key);
    }

    /**
     * Timeout değerlerini döndürür
     */
    public int getImplicitTimeout() {
        return getInt("timeout.implicit", 10);
    }

    public int getExplicitTimeout() {
        return getInt("timeout.explicit", 15);
    }

    public int getPageLoadTimeout() {
        return getInt("timeout.pageLoad", 30);
    }

    /**
     * Config dosyasını reload eder
     */
    public void reload() {
        loadProperties();
    }

    /**
     * Static get metodu (kolay erişim için)
     */
    public static String getProperty(String key) {
        return getInstance().get(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return getInstance().get(key, defaultValue);
    }

    public static int getIntProperty(String key) {
        return getInstance().getInt(key);
    }

    public static boolean getBooleanProperty(String key) {
        return getInstance().getBoolean(key);
    }
}