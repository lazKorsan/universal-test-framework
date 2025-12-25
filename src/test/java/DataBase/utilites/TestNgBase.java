package DataBase.utilites;

import config_Requirements.ConfigLoader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class TestNgBase {

    @BeforeMethod
    public void setUpDB() {
        ConfigLoader config = ConfigLoader.getInstance();

        // Bilgileri Config dosyasından çekiyoruz (Gizlilik)
        String url = config.getDatabaseConfig("URL");
        String username = config.getDatabaseConfig("USERNAME");
        String password = config.getDatabaseConfig("PASSWORD");

        // Eğer config dosyasında sorun varsa, hardcoded fallback (Yedek) değerler
        if (url == null || username == null) {
            System.out.println("⚠️ Config dosyasından veri okunamadı, yedek bilgiler kullanılıyor.");
            url = "jdbc:mysql://195.35.59.18/u201212290_qainstulearn";
            username = "u201212290_qainstuser";
            password = "A/s&Yh[qU0";
        }

        System.out.println("🔌 Connecting to Database (TestNG Base)...");
        DB_Utils.createConnection(url, username, password);
    }

    @AfterMethod
    public void tearDownDB() {
        System.out.println("🔌 Closing Database Connection (TestNG Base)...");
        DB_Utils.closeConnection();
    }
}
