package Android.TestNg.Junke;

import Browser.Utilities.Driver;
import org.testng.annotations.Test;

public class browserTest {

    @Test
    public static void browserTes2t(){

        Driver.getDriver().get("https://www.google.com");
    }
}
