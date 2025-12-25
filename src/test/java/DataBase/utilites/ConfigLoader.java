package DataBase.utilites;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ConfigLoader {


    private static final String DEFAULT_CONFIG = "src/test/resources/config/config.yaml";
    private static final String LOCAL_CONFIG = "src/test/resources/config/config-local.yaml";


    public static ConfigLoader getInstance() {
        return new ConfigLoader();  // constructor private ise yine sınıf içinde çağrılabilir
    }


    private Map<String, Object> config;

    // Constructor ile YAML dosyasını yükleme. otomatik olarak config.yaml dosyası okunacak ve Map’e yüklenecek.
    public ConfigLoader() {

        String fileToLoad;

        // Eğer local config varsa önce onu kullan
        if (new File(LOCAL_CONFIG).exists()) {
            fileToLoad = LOCAL_CONFIG;
        } else {
            fileToLoad = DEFAULT_CONFIG;
        }


        Yaml yaml = new Yaml();

        try (InputStream inputStream = new FileInputStream(fileToLoad)) {

            config = yaml.load(inputStream);
            System.out.println("Loaded config file: " + fileToLoad);

        } catch (IOException e) {
            throw new RuntimeException("config.yaml dosyasi okunamadi: " + fileToLoad, e);
        }

    }

    // Belirli bir kategori (api, database) ve anahtar (key) için yapılandırma değerini alır
    public String getConfigValue(String category, String key) {
        Map<String, Object> categoryMap = (Map<String, Object>) config.get(category);
        if (categoryMap != null) {
            return (String) categoryMap.get(key);
        } else {
            throw new RuntimeException(category + " kategorisi bulunamadı.");
        }
    }


    public String getApiConfig(String key) {
        return getConfigValue("api", key);
    }

    // Database yapılandırma değerlerini almak için metod
    public String getDatabaseConfig(String key) {
        return getConfigValue("database", key);
    }
}
