package socialnetwork.pageobjects;


import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import socialnetwork.util.*;


import java.util.List;

public class PO_NavView extends PO_View {


    /**
     * Clic en una de las opciones principales (a href) y comprueba que se vaya a la vista con el elemento de
     * tipo type con el texto Destino
     *
     * @param driver:     apuntando al navegador abierto actualmente.
     * @param textOption: Texto de la opción principal.
     * @param criterio:   "id" or "class" or "text" or "@attribute" or "free". Si el valor de criterio es free es una
     *                    expresion xpath completa.
     * @param targetText: texto correspondiente a la búsqueda de la página destino.
     */
    public static void clickOption(WebDriver driver, String textOption, String criterio, String targetText) {
//CLickamos en la opción de registro y esperamos a que se cargue el enlace de Registro.
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "@href", textOption,
                getTimeout());
//Tiene que haber un sólo elemento.
        Assertions.assertEquals(1, elements.size());
//Ahora lo clickamos
        elements.get(0).click();
//Esperamos a que sea visible un elemento concreto
        elements = SeleniumUtils.waitLoadElementsBy(driver, criterio, targetText, getTimeout());
//Tiene que haber un sólo elemento.
        Assertions.assertEquals(1, elements.size());
    }



    static public void checkWelcomeToPage(WebDriver driver, int language) {
//Esperamos a que se cargue el saludo de bienvenida en Español
        SeleniumUtils.waitLoadElementsBy(driver, "text", p.getString("welcome.message", language),
                getTimeout());
    }


    static public List<WebElement> getWelcomeMessageText(WebDriver driver, int language) {
//Esperamos a que se cargue el saludo de bienvenida en Español
        return SeleniumUtils.waitLoadElementsBy(driver, "text", p.getString("welcome.message", language),
                getTimeout());
    }



    public static boolean checkTextNotInView(WebDriver driver, String texto) {


        String busqueda = "//*[contains(text(),'" + texto + "')]";

        Boolean resultado;

        resultado = (new WebDriverWait(driver, 2))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(busqueda)));

        return resultado;

    }


    public static PO_Properties getP() {
        return p;
    }
}
