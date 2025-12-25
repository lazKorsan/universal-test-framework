package Android.Stepdefinitions;

import Android.DailyJunke.DailyJungelTest;
import Android.Pages.QueryCardPage;
import Android.TestNg.MethodsPage;
import Android.Utilities.AppiumServerController;
import Android.Utilities.RunTimeEmulatorStarter;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;


public class exampleSteps {

    MethodsPage optionMet = new MethodsPage();
    AppiumServerController serverer = new AppiumServerController();
    RunTimeEmulatorStarter emulator = new RunTimeEmulatorStarter();
    QueryCardPage page = new QueryCardPage(null);



    public void QueryCardFlow(AndroidDriver driver) {
        this.page = new QueryCardPage(driver);
    }



    @Given("user open the QueryCar applications")
    public void user_open_the_query_car_applications() {

       page.kullaniciQueryCartUygulamasiniAcar();

    }


    @Given("User opner appiumServer and Emulator and app")
    public void userOpnerAppiumServerAndEmulatorAndApp()  {

       // AppiumServerController.startServer();
        //RunTimeEmulatorStarter.startEmulator("pixel_7_pro");

      optionMet.methodKolCalismasiWithTestNG();


    }

    @Given("XXXX")
    public void xxxx() {
        //DailyJungelTest.testStartServer();

        page.userHasBeenOpenAppAutomaticaly();


    }



}
