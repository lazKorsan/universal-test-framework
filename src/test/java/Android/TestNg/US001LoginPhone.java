package Android.TestNg;

import Android.Pages.MethodsPage;
import com.student.BaseTest;
import org.testng.annotations.Test;

public class US001LoginPhone extends BaseTest {

    @Test
    public void US001_LoginWithPhoneNumber() {
        // MethodsPage nesnesini test metodu içinde oluşturuyoruz
        // Constructor içinde driver otomatik olarak alınıyor
        MethodsPage methodsPage = new MethodsPage();
        
        methodsPage.loginWithPhoneNumber();
    }
}