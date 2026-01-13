import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.*;
import java.util.List;

public class PageElementExplorer {
    public static void main(String[] args) {
        // ChromeDriver setup
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {
            // 1. Sayfaya git
            driver.get("https://qa.loyalfriendcare.com/en/register");

            // 2. Tüm elementleri bul
            List<WebElement> allElements = driver.findElements(By.xpath("//*"));

            // 3. Dosyaya yaz
            PrintWriter writer = new PrintWriter("sayfa_elementleri.txt", "UTF-8");

            writer.println("=== SAYFA ELEMENT ANALİZİ ===");
            writer.println("URL: " + driver.getCurrentUrl());
            writer.println("Toplam Element: " + allElements.size());
            writer.println("\n" + "=".repeat(80) + "\n");

            // 4. Her elementi analiz et
            for (int i = 0; i < allElements.size(); i++) {
                WebElement element = allElements.get(i);

                try {
                    writer.println("\n--- ELEMENT #" + (i+1) + " ---");

                    // Tag ve ID
                    String tagName = element.getTagName();
                    writer.println("Tag: " + tagName);

                    // ID
                    String id = element.getAttribute("id");
                    if (id != null && !id.isEmpty()) {
                        writer.println("ID: #" + id);
                    }

                    // Class
                    String className = element.getAttribute("class");
                    if (className != null && !className.isEmpty()) {
                        writer.println("Class(es): " + className);
                    }

                    // Name
                    String name = element.getAttribute("name");
                    if (name != null && !name.isEmpty()) {
                        writer.println("Name: " + name);
                    }

                    // Data attributes (Cypress için önemli)
                    String dataCy = element.getAttribute("data-cy");
                    String dataTest = element.getAttribute("data-test");
                    String dataTestid = element.getAttribute("data-testid");

                    if (dataCy != null && !dataCy.isEmpty())
                        writer.println("data-cy: " + dataCy);
                    if (dataTest != null && !dataTest.isEmpty())
                        writer.println("data-test: " + dataTest);
                    if (dataTestid != null && !dataTestid.isEmpty())
                        writer.println("data-testid: " + dataTestid);

                    // Text (ilk 100 karakter)
                    String text = element.getText();
                    if (text != null && !text.trim().isEmpty()) {
                        writer.println("Text: " +
                                (text.length() > 100 ? text.substring(0, 100) + "..." : text));
                    }

                    // XPath (basit)
                    writer.println("Basit XPath: //" + tagName +
                            (id != null && !id.isEmpty() ? "[@id='" + id + "']" : "") +
                            (className != null && !className.isEmpty() ? "[contains(@class, '" + className.split(" ")[0] + "')]" : ""));

                    // CSS Selector önerisi
                    if (id != null && !id.isEmpty()) {
                        writer.println("CSS Selector: #" + id);
                    } else if (className != null && !className.isEmpty()) {
                        writer.println("CSS Selector: ." + className.replace(" ", "."));
                    }

                } catch (StaleElementReferenceException e) {
                    writer.println("Element artık DOM'da yok (stale)");
                }
            }

            writer.close();
            System.out.println("✓ Analiz tamamlandı: sayfa_elementleri.txt");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}