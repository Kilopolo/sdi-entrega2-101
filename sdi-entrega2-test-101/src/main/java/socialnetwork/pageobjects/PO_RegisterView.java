package socialnetwork.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_RegisterView extends PO_NavView {
    static public void fillForm(WebDriver driver, String emailp, String namep, String lastnamep, String passwordp,
                                String passwordconfp) {
        WebElement email = driver.findElement(By.name("email"));
        email.click();
        email.clear();
        email.sendKeys(emailp);
        WebElement name = driver.findElement(By.name("name"));
        name.click();
        name.clear();
        name.sendKeys(namep);
        WebElement lastname = driver.findElement(By.name("lastName"));
        lastname.click();
        lastname.clear();
        lastname.sendKeys(lastnamep);
        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.clear();
        password.sendKeys(passwordp);
        WebElement passwordConfirm = driver.findElement(By.name("passwordConfirm"));
        passwordConfirm.click();
        passwordConfirm.clear();
        passwordConfirm.sendKeys(passwordconfp);
        // Pulsar el boton de Alta.
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }


    /**
     * @param email
     * @param password
     */
    public static void registerUser(WebDriver driver, String email, String password) {
        // Vamos al formulario de registro
        clickOption(driver, "signup", "class", "btn btn-primary");
        // Rellenamos el formulario.
        PO_RegisterView.fillForm(driver, email, "Nombre", "Apellido", password, password);
        // Comprobamos que entramos en la sección privada
        checkElementBy(driver, "id", "user-list");
    }
}
