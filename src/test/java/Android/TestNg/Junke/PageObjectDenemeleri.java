package Android.TestNg.Junke;


import org.testng.annotations.Test;

public class PageObjectDenemeleri {

    OlderJunkeMethodsPage optionMet = new OlderJunkeMethodsPage();



    @Test
    public void test01(){
        System.out.println("Test 01");

        optionMet.methodKolCalismasiWithTestNG();
    }
}
