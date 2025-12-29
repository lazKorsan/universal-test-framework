package Android.TestNg.Junke;

import Android.Utilities.ReusableMethods;
import Browser.Utilities.Driver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import java.util.List;

import static org.openqa.selenium.Keys.TAB;

public class US01DeliveredTest {

    @Test
    public static void queryCartChangeStatusDelivered(){
        //WebDriverManager.firefoxdriver().setup();
        Driver.getDriver().get("https://querycart.com/#/login");



        WebElement emailBox = Driver.getDriver().findElement(By.xpath("//input[@id='formEmail']"));
        emailBox.click();
        emailBox.sendKeys("ahmeteldes.manager@querycart.com");
        Actions actions = new Actions(Driver.getDriver());
        actions.sendKeys(TAB).perform();
        actions.sendKeys("Query.202020").perform();

        WebElement signInButton = Driver.getDriver().findElement(By.xpath("//button[@type='submit']"));
        signInButton.click();
        ReusableMethods.bekle(8);

        WebElement profileButton = Driver.getDriver().findElement(By.xpath("//button[@class='lab-line-user text-xl py-5']"));

        actions.moveToElement(profileButton).perform();

       WebElement dashBoard = Driver.getDriver().findElement(By.xpath("//*[@class='text-sm font-medium capitalize whitespace-nowrap'][1]"));
       dashBoard.click();

       WebElement onlineOrders = Driver.getDriver().findElement(By.xpath("//*[@href='#/admin/online-orders']"));
       onlineOrders.click();

        WebElement orderButton = Driver.getDriver().findElement(
                By.xpath("//*[contains(@href,'#/admin/online-orders/show/')]")
        );
        orderButton.click();

        WebElement actionButton = Driver.getDriver().findElement(By.xpath("//button[@class='flex items-center justify-center text-white gap-2 px-4 h-[38px] rounded shadow-db-card bg-[#2AC769]']"));
        actionButton.click();

        WebElement acceptButton = Driver.getDriver().findElement(By.xpath("//button[@class='swal2-confirm swal2-styled']"));
        acceptButton.click();

        ReusableMethods.bekle(2);


        WebElement confirmButton = Driver.getDriver().findElement(By.xpath("//*[@id=\"app\"]/div/main/div[2]/div[3]/div/div/div[2]/div[2]/select"));
        confirmButton.click();

        WebElement deliveredButton = Driver.getDriver().findElement(By.xpath("//*[@id=\"app\"]/div/main/div[2]/div[3]/div/div/div[2]/div[2]/select/option[3]"));
        deliveredButton.click();
















    }
}
