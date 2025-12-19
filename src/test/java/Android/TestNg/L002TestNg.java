package Android.TestNg;

import org.testng.annotations.Test;

public class L002TestNg {

    PageClassOpenEmulator pageClassOpenEmulator = new PageClassOpenEmulator();


    @Test
    public void test01(){
        System.out.println("Test 01");

        pageClassOpenEmulator.openAppAndEmulator();
    }
}
