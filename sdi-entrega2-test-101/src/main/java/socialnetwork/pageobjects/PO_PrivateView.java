package socialnetwork.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
        fillLoginForm(driver, email, password);
        // COmprobamos que entramos en la pagina privada
        checkElementBy(driver, "id", check);
    }

    private static void fillLoginForm(WebDriver driver, String emailp, String passwordp) {
        WebElement email = driver.findElement(By.name("email"));
        email.click();
        email.clear();
        email.sendKeys(emailp);

        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.clear();
        password.sendKeys(passwordp);

        // Pulsar el boton de Login.
        By boton = By.className("boton-login");
//        By boton = By.className("btn");

        driver.findElement(boton).click();
                System.out.println(boton.toString());
    }
}