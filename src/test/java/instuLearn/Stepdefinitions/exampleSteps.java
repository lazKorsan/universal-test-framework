package instuLearn.Stepdefinitions;

import Browser.Utilities.Driver;
import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;
import utilities.ClickUtils;

public class exampleSteps {

    WebDriver driver= Driver.getDriver();

    @Given("Kullanici loginPage gider")
    public void kullanici_login_page_gider() {
        System.out.println("Kullanici loginPage gider");
        ClickUtils clickUtils = new ClickUtils();
        driver.get("https://www.qa.instuLearn.com/login");
        clickUtils.clickByCss("//*[@id=\"app\"]/div[3]/div/div[2]/div/form/button");


    }
}
