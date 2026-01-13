package Browser.TestNg;

import Browser.Utilities.Driver;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.*;
import java.time.Duration;
import java.util.*;

public class AdvancedElementExplorer {


    @Test
    public  void testElementExplorer() throws IOException {

            // Scanner konsol girdisi beklediği için test takılı kalıyordu.
            // URL doğrudan tanımlandı.
            String url = "https://qa.loyalfriendcare.com/en/register";

            Driver.getDriver().get(url);

            // Sayfanın yüklenmesini bekle
           // wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

            // Farklı selector türlerine göre elementleri topla
            Map<String, List<String>> elementMap = new LinkedHashMap<>();

            // Tüm element tipleri için
            String[] tags = {"input", "button", "a", "div", "span", "form", "select", "textarea", "img", "h1", "h2", "h3", "h4", "h5", "h6"};

            PrintWriter writer = new PrintWriter("element_raporu.txt", "UTF-8");

            writer.println("=".repeat(100));
            writer.println("ELEMENT EXPLORER RAPORU");
            writer.println("Tarih: " + new Date());
            writer.println("URL: " + url);
            writer.println("=".repeat(100) + "\n");

            // Her tag için analiz
            for (String tag : tags) {
                List<WebElement> elements = Driver.getDriver().findElements(By.tagName(tag));
                writer.println("\n" + tag.toUpperCase() + " ELEMENTLERİ (" + elements.size() + " adet)");
                writer.println("-".repeat(50));

                for (int i = 0; i < elements.size(); i++) {
                    WebElement el = elements.get(i);

                    writer.println("\n[" + tag + " #" + (i+1) + "]");

                    // Ortak attribute'lar
                    writeAttribute(writer, "id", el.getAttribute("id"));
                    writeAttribute(writer, "class", el.getAttribute("class"));
                    writeAttribute(writer, "name", el.getAttribute("name"));
                    writeAttribute(writer, "type", el.getAttribute("type"));
                    writeAttribute(writer, "placeholder", el.getAttribute("placeholder"));
                    writeAttribute(writer, "href", el.getAttribute("href"));

                    // Test attribute'ları
                    writeAttribute(writer, "data-cy", el.getAttribute("data-cy"));
                    writeAttribute(writer, "data-test", el.getAttribute("data-test"));
                    writeAttribute(writer, "data-testid", el.getAttribute("data-testid"));
                    writeAttribute(writer, "data-qa", el.getAttribute("data-qa"));

                    // Text (kısaltılmış)
                    String text = el.getText().trim();
                    if (!text.isEmpty() && text.length() < 200) {
                        writer.println("  Text: \"" + text + "\"");
                    }

                    // Görünür mü?
                    writer.println("  Görünür: " + el.isDisplayed());

                    // Cypress selector önerileri
                    writer.println("  CYPRESS SELECTOR ÖNERİLERİ:");

                    String id = el.getAttribute("id");
                    if (id != null && !id.isEmpty()) {
                        writer.println("    - cy.get('#" + id + "')");
                    }

                    String dataCy = el.getAttribute("data-cy");
                    if (dataCy != null && !dataCy.isEmpty()) {
                        writer.println("    - cy.get('[data-cy=\"" + dataCy + "\"]')");
                    }

                    // Text-based (son çare)
                    if (!text.isEmpty() && text.length() < 50) {
                        writer.println("    - cy.contains('" + text.replace("'", "\\'") + "')");
                    }
                }
            }

            // ÖZEL BÖLÜM: FORM ELEMENTLERİ
            writer.println("\n" + "=".repeat(100));
            writer.println("FORM ELEMENTLERİ ÖZETİ");
            writer.println("=".repeat(100));

            List<WebElement> formElements = Driver.getDriver().findElements(By.xpath("//input | //select | //textarea"));
            for (WebElement el : formElements) {
                String type = el.getAttribute("type") != null ? el.getAttribute("type") : el.getTagName();
                writer.println("\n- " + el.getTagName() +
                        " [type=" + type +
                        ", name=" + el.getAttribute("name") +
                        ", id=" + el.getAttribute("id") +
                        ", placeholder=" + el.getAttribute("placeholder") + "]");
            }

            writer.close();

            // Konsola özet
            System.out.println("\n✅ RAPOR HAZIR!");
            System.out.println("Dosya: element_raporu.txt");
            System.out.println("Toplam element analiz edildi.");


            Driver.getDriver().quit();

    }

    private static void writeAttribute(PrintWriter writer, String attrName, String attrValue) {
        if (attrValue != null && !attrValue.trim().isEmpty()) {
            writer.println("  " + attrName + ": " + attrValue);
        }
    }
}