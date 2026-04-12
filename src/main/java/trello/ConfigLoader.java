package trello;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;

public class ConfigLoader {
    private static Map<String, Object> configData;

    static {
        try {
            Yaml yaml = new Yaml();
            // ClassLoader kullanarak dosyayı nerede olursa olsun (main veya test resources) buluruz
            InputStream inputStream = ConfigLoader.class
                    .getClassLoader()
                    .getResourceAsStream("trello/config.yaml");

            if (inputStream == null) {
                throw new RuntimeException("Hata: trello/config.yaml dosyası bulunamadı!");
            }
            configData = yaml.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Config dosyası yüklenirken hata oluştu!");
        }
    }

    public static String getProp(String key) {
        return configData.get(key).toString();
    }
}