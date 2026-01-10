package PlaywrightWeb.InstuLearnTest;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.Test;

public class US01InstuLearn {

    static String url = "https://qa.instulearn.com/";

    public static void openChrome(){

        Playwright playwright = Playwright.create();

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        Page page = browser.newPage();
        page.navigate(url);


    }

    @Test
    public void tc01(){
        openChrome();

    }
}
