package socialnetwork.pageobjects;


import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import socialnetwork.util.*;

import java.util.List;

public class PO_ClienteAPIFriendList extends PO_NavView {

    public static int getCount(WebDriver driver, String userLogged, int count) {
        //contamos los tr
        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                getTimeout());
        //para cada elemento
        for (WebElement each : elementos) {
            //buscamos el nombre del email
            String actualUser = each.findElement(By.id("userEmail")).getText();
            //comprobacion
            if (!actualUser.equals(userLogged) || !actualUser.equals("admin@email.com"))
                count++;
            else Assertions.fail("Esta presente un usuario que no deberia");
        }
        return count;
    }

    public static void filter(WebDriver driver, String busquedap) {
        WebElement busqueda = driver.findElement(By.name("busqueda"));
        busqueda.click();
        busqueda.clear();
        busqueda.sendKeys(busquedap);

        //pulsamos el boton de busqueda
//		List<WebElement> elementos = SeleniumUtils.EsperaCargaPaginaxpath(driver, "//*[@id=\"search\"]", PO_View.getTimeout());
//		elementos.get(0).click();

//        By boton = By.className("btn");
//        driver.findElement(boton).click();
        // assert que estamos en la pagina correcta
//            PO_NavView.checkIdOnView(driver, "testVistaTienda");
        PO_NavView.checkElementBy(driver, "id", "friend-list");


    }

    public static void goToConversation(WebDriver driver, String nombre) {


        PO_NavView.checkElementBy(driver, "id", "testClienteOfertasView");

        List<WebElement> ofertas = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());

        for (WebElement webElement : ofertas) {
            List<WebElement> hijos = webElement.findElements(By.xpath("./child::*"));
            if (hijos.get(0).getText().equals(nombre)) {
                hijos.get(3).findElements(By.xpath("./child::*")).get(0).click();
                return;
            }

        }


        PO_View.checkElementBy(driver, "text", nombre);
    }

    public static void createMessage(WebDriver driver, String mensaje) {


    }

    public static int getCountMoreThan(WebDriver driver, int count) {
        //contamos los tr
        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                getTimeout());

        Assertions.assertTrue(elementos.size() < count, "Hay menos elementos de lo esperado");

        return elementos.size();
    }
}
