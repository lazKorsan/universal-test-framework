package DataBase.Stepdefinitions;

import DataBase.utilites.DB_Utils;
import config_Requirements.ConfigLoader;
import io.cucumber.java.en.*;
import org.junit.Assert;

import java.util.List;

public class DBSteps {

    ConfigLoader config = new ConfigLoader();
    String query;
    Object actualId;
    List<List<Object>> userList;

    @Given("database baglantisi kurulur")
    public void databaseBaglantisiKurulur() {
        String url = config.getConfigValue("database", "URL");
        String username = config.getConfigValue("database", "USERNAME");
        String password = config.getConfigValue("database", "PASSWORD");
        DB_Utils.createConnection(url, username, password);
    }

    @Then("{string} tablosusunun {string} sutununda {string} ve {string} rolleri dışında bir şey olmadigi dogrulanir")
    public void tablosusununSutunundaVeRolleriDısındaBirSeyOlmadigiDogrulanir(String tableName, String columnName, String role1, String role2) {

        // 1. SQL sorgusunu oluştur
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " NOT IN ('" + role1 + "', '" + role2 + "')";

        // 2. Sorguyu çalıştır ve sonucu al
        Object result = DB_Utils.getCellValue(query);

        // 3. Dönen sonucun "0" olduğunu doğrula
        long count = 0;
        if (result instanceof Number) {
            count = ((Number) result).longValue();
        } else if (result != null) {
            count = Long.parseLong(result.toString());
        }

        Assert.assertEquals("Tabloda beklenmeyen roller bulundu.", 0, count);
        
    }

    @And("database baglantisi kapatilir")
    public void databaseBaglantisiKapatilir() {
        DB_Utils.closeConnection();
    }
}
