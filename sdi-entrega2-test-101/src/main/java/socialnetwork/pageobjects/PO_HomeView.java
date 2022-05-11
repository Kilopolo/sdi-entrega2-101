package socialnetwork.pageobjects;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import socialnetwork.util.*;

import java.util.List;

public class PO_HomeView extends PO_NavView {

    public static void goHome(WebDriver driver, String dni, String password) {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, dni, password);

    }

    public static boolean checkRole(WebDriver driver, String role) {
        String checkText = role;
        List<WebElement> result = checkElementBy(driver, "text", checkText);
        return checkText.equals(result.get(0).getText());

    }

    public static boolean checkLoginError(WebDriver driver) {
        String checkId = "login";
        List<WebElement> result = checkElementBy(driver, "id", checkId);

        return (result.get(0).getText()) != null;

    }

    public static void desconect(WebDriver driver) {


        SeleniumUtils.waitLoadElementsBy(driver, "id", "btnDesconectar", getTimeout()).get(0).click();


    }


}
