package socialnetwork.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_Publicaciones extends PO_NavView {

    static public void fillForm(WebDriver driver, String titlep, String contentp) {
        WebElement title = driver.findElement(By.name("titulo"));
        title.click();
        title.clear();
        title.sendKeys(titlep);
        WebElement content = driver.findElement(By.name("texto"));
        content.click();
        content.clear();
        content.sendKeys(contentp);

        // Botón de añadido de publicación
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }

}
