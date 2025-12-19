package Android.Stepdefinitions;

import Android.Pages.QueryCardPage;
import Android.TestNg.MethodsPage;
import Android.Utilities.AppiumServerController;
import Android.Utilities.RunTimeEmulatorStarter;
import io.cucumber.java.en.*;


public class exampleSteps {

    MethodsPage optionMet = new MethodsPage();
    AppiumServerController serverer = new AppiumServerController();
    RunTimeEmulatorStarter emulator = new RunTimeEmulatorStarter();
    QueryCardPage page = new QueryCardPage(null);



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
}
