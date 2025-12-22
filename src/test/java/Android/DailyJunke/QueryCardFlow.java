package Android.DailyJunke;

import Android.Pages.QueryCardPage;
import io.appium.java_client.android.AndroidDriver;

public class QueryCardFlow {

    private QueryCardPage page;

    public QueryCardFlow(AndroidDriver driver) {
        this.page = new QueryCardPage(driver);
    }

    public void openAppAndClickProfile() {
        page.userHasBeenOpenAppAutomaticaly();
    }
}
