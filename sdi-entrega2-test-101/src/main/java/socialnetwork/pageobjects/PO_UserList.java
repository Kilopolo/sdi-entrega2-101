package socialnetwork.pageobjects;


import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import socialnetwork.util.*;

import java.util.List;

public class PO_UserList extends PO_NavView {
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
}
