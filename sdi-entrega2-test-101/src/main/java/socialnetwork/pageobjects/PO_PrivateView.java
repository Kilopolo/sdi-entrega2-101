package socialnetwork.pageobjects;

import org.openqa.selenium.WebDriver;

public class PO_PrivateView extends PO_NavView {

    static public void login(WebDriver driver, String email, String password, String check) {

        // Vamos al formulario de logueo.
        clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, email, password);
        // COmprobamos que entramos en la pagina privada
        checkElementBy(driver, "id", check);
    }

    public static void loginAPI(WebDriver driver, String email, String password, String check) {

        // Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, email, password);
        // COmprobamos que entramos en la pagina privada
        checkElementBy(driver, "id", check);
    }

}