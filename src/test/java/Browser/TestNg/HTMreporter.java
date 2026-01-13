package Browser.TestNg;

import Browser.Utilities.Driver;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HTMreporter {

    @Test
    public void testHTMreport() throws IOException {

        String url = "https://qa.loyalfriendcare.com/en/register";

        Driver.getDriver().get(url);

        // Sayfanın HTML kaynağını al
        String pageSource = Driver.getDriver().getPageSource();

        // Dosya yolunu belirle
        String filePath = "C:\\Users\\user\\IdeaProjects\\universal-test-framework\\Documents\\pageHTMLRaopr.html";
        File file = new File(filePath);

        // Klasör yoksa oluştur
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // Dosyaya yaz
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(pageSource);
            System.out.println("HTML raporu başarıyla kaydedildi: " + filePath);
        } catch (IOException e) {
            System.err.println("Dosya yazılırken hata oluştu: " + e.getMessage());
            throw e;
        }

        Driver.getDriver().quit();
    }
}