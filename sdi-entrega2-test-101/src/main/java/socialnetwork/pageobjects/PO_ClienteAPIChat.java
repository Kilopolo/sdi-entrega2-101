package socialnetwork.pageobjects;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import socialnetwork.util.SeleniumUtils;

import java.util.List;

import static socialnetwork.pageobjects.PO_View.getTimeout;

public class PO_ClienteAPIChat {
    public static int getCountMoreThan(WebDriver driver, int count) {
        //contamos los tr
        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "id", "mensaje",
                getTimeout());

        Assertions.assertTrue(elementos.size() >= count, "Hay menos elementos de lo esperado");

        return elementos.size();
    }
}
