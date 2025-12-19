package Android.TestNg;


import org.testng.annotations.Test;

public class PageObjectDenemeleri {

    MethodsPage optionMet = new MethodsPage();



    @Test
    public void test01(){
        System.out.println("Test 01");

        optionMet.methodKolCalismasiWithTestNG();
    }
}
