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

    public static void filter(WebDriver driver, String username) {




    }

    public static void goToConversation(WebDriver driver, String nombre) {


        PO_NavView.checkElementBy(driver, "id","testClienteOfertasView");

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
}
