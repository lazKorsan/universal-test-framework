package DataBase.TestNg;

import DataBase.utilites.DB_Utils;
import config_Requirements.ConfigLoader;
import org.junit.Assert;

import org.testng.annotations.Test;

import java.util.List;

public class US014DBrole {

   // ConfigLoader config = new ConfigLoader();
    String query;
    Object actualId;
    List<List<Object>> userList;




    @Test
    public static void TC014(){

        DB_Utils.createConnection("jdbc:mysql://195.35.59.18/u201212290_qainstulearn", "u201212290_qainstuser", "A/s&Yh[qU0");

        //String url = config.getConfigValue("database", "URL");
        //String username = config.getConfigValue("database", "USERNAME");
        //String password = config.getConfigValue("database", "PASSWORD");
        //DB_Utils.createConnection(url, username, password);


          String tableName = "become_instructors";
          String role = "role";
          String teacher = "teacher";
          String organization = "organization";


        // --- EKLENEN KISIM: Rolleri Konsola Yazdırma ---
        System.out.println("=== " + tableName + " Tablosundaki Mevcut Roller ===");
        String listQuery = "SELECT " + role + " FROM " + tableName;
        List<List<Object>> roleList = DB_Utils.getQueryResultList(listQuery);

        for (List<Object> row : roleList) {
            // getQueryResultList her satırı bir liste olarak döner, tek sütun seçtiğimiz için 0. indexi alıyoruz
            System.out.println("Rol: " + row.get(0));
        }
        System.out.println("==============================================");
        // -----------------------------------------------


        // variable : tableName  roles: teacher and organizations
        // become_instructors tablosuna teacher ve organization dısında baska bir rol ile veri eklenemediğini dogrulayın

        //  @DBUS014TC01
        //  Scenario:
        //    Given database baglantisi kurulur
        //    Then "become_instructors" tablosusunun "role" sutununda "teacher" ve "organization" rolleri dışında bir şey olmadigi dogrulanir
        //    And database baglantisi kapatilir


        // 1. SQL sorgusunu oluştur
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + role + " NOT IN ('" + teacher + "', '" + organization + "')";

        // 2. Sorguyu çalıştır ve sonucu al
        Object result = DB_Utils.getCellValue(query);

        // 3. Dönen sonucun "0" olduğunu doğrula
        long count = 0;
        if (result instanceof Number) {
            count = ((Number) result).longValue();
        } else if (result != null) {
            count = Long.parseLong(result.toString());
        }

        // Test bitmeden bağlantıyı kapatalım
        DB_Utils.closeConnection();

        Assert.assertEquals("Tabloda beklenmeyen roller bulundu.", 0, count);

        // Başarılı doğrulama mesajı
        System.out.println("\"become_instructors\" tablosusunun \"role\" sutununda \"teacher\" ve \"organization\" rolleri dışında bir şey olmadigi dogrulandi");
    }


}
