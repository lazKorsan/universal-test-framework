package Android.Stepdefinitions;

import Android.Pages.QueryCardPage;
import io.cucumber.java.en.Given;

import static Android.DailyJunke.AppiumServerController.startServer;
import static Android.DailyJunke.EmulatorController.startEmulator;

public class exampleSteps {

    QueryCardPage page = new QueryCardPage(Android.Utilities.AndroidDriver.getDriver());

    @Given("user open the QueryCar applications")
    public void user_open_the_query_car_applications() {

       page.kullaniciQueryCartUygulamasiniAcar();

    }


    @Given("User opner appiumServer and Emulator and app")
    public void userOpnerAppiumServerAndEmulatorAndApp() {


        page.userOpenTheAppiumServerAndEmulatorAndApp();

    }
}
