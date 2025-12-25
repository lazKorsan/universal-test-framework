package DataBase.utilites;

import config_Requirements.ConfigLoader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void setUpDB() {
        // ConfigLoader ile bilgileri çekmeye çalışıyoruz
        ConfigLoader config = ConfigLoader.getInstance();
        
        // Eğer config.yaml dosyanızda bu anahtarlar tanımlıysa oradan okur.
        // Tanımlı değilse veya null gelirse, aşağıda manuel olarak set edebilirsiniz.
        String url = config.getDatabaseConfig("URL");
        String username = config.getDatabaseConfig("USERNAME");
        String password = config.getDatabaseConfig("PASSWORD");

        // Eğer config'den null dönerse (henüz ayarlanmadıysa), manuel değerleri kullan:
        if (url == null || username == null) {
            url = "jdbc:mysql://195.35.59.18/u201212290_qainstulearn";
            username = "u201212290_qainstuser";
            password = "A/s&Yh[qU0";
        }

        System.out.println("🔌 Connecting to Database...");
        DB_Utils.createConnection(url, username, password);
    }

    @After
    public void tearDownDB(Scenario scenario) {
        // Senaryo bittiğinde bağlantıyı kapat
        System.out.println("🔌 Closing Database Connection...");
        DB_Utils.closeConnection();
    }
}
