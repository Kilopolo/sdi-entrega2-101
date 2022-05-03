package socialnetwork.pageobjects;


import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import socialnetwork.util.SeleniumUtils;

import java.util.List;

public class PO_Peticiones extends PO_NavView {

    static public void checkListaDePeticiones(WebDriver driver, int expected) {
        List<WebElement> peticionesList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                getTimeout());
        Assertions.assertEquals(expected, peticionesList.size());
    }

}
