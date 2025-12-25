package DataBase.TestNg;

import DataBase.utilites.DB_Utils;
import DataBase.utilites.TestNgBase;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.List;

// TestNgBase sınıfından miras alarak bağlantı yönetimini oraya devrediyoruz
public class US014DBrole extends TestNgBase {

    String tableName = "become_instructors";
    String role = "role";
    String teacher = "teacher";
    String organization = "organization";

    @Test
    public void TC014() {
        // Bağlantı @BeforeMethod (TestNgBase içinde) sayesinde zaten açık!

        // --- Rolleri Konsola Yazdırma ---
        System.out.println("=== " + tableName + " Tablosundaki Mevcut Roller ===");
        String listQuery = "SELECT " + role + " FROM " + tableName;
        List<List<Object>> roleList = DB_Utils.getQueryResultList(listQuery);

        for (List<Object> row : roleList) {
            System.out.println("Rol: " + row.get(0));
        }
        System.out.println("==============================================");
        // -----------------------------------------------

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

        // Assert işlemi
        Assert.assertEquals("Tabloda beklenmeyen roller bulundu.", 0, count);

        // Başarılı doğrulama mesajı
        System.out.println("\"become_instructors\" tablosusunun \"role\" sutununda \"teacher\" ve \"organization\" rolleri dışında bir şey olmadigi dogrulandi");
        
        // Bağlantı @AfterMethod (TestNgBase içinde) sayesinde otomatik kapanacak!
    }
}
