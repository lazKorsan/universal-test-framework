package Android.DailyJunke;

import Android.Pages.QueryCardPage;
import io.appium.java_client.android.AndroidDriver;

public class Flows {

    private final QueryCardPage page;

    public Flows(AndroidDriver driver) {
        this.page = new QueryCardPage(driver);
    }

    public void openQueryCart() {
        page.kullaniciQueryCartUygulamasiniAcar();
    }

    public void clickProfile() {
       // page.profileTikla();
    }
}
