package socialnetwork.pageobjects;


import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import socialnetwork.util.SeleniumUtils;

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

    /**
     * Selecciona el enlace de idioma correspondiente al texto textLanguage
     *
     * @param driver:       apuntando al navegador abierto actualmente.
     * @param textLanguage: el texto que aparece en el enlace de idioma ("English" o "Spanish")
     */
    public static void changeLanguage(WebDriver driver, String textLanguage) {
//clickamos la opción Idioma.
        List<WebElement> languageButton = SeleniumUtils.waitLoadElementsBy(driver, "id", "btnLanguage",
                getTimeout());
        languageButton.get(0).click();
//Esperamos a que aparezca el menú de opciones.
        SeleniumUtils.waitLoadElementsBy(driver, "id", "languageDropdownMenuButton", getTimeout());
//CLickamos la opción Inglés partiendo de la opción Español
        List<WebElement> Selectedlanguage = SeleniumUtils.waitLoadElementsBy(driver, "id", textLanguage,
                getTimeout());
        Selectedlanguage.get(0).click();
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

    static public void checkChangeLanguage(WebDriver driver, String textLanguage1, String textLanguage,
                                           int locale1, int locale2) {
//Esperamos a que se cargue el saludo de bienvenida en Español
        PO_HomeView.checkWelcomeToPage(driver, locale1);
//Cambiamos a segundo idioma
        PO_HomeView.changeLanguage(driver, textLanguage);
//Comprobamos que el texto de bienvenida haya cambiado a segundo idioma
        PO_HomeView.checkWelcomeToPage(driver, locale2);
//Volvemos a Español.
        PO_HomeView.changeLanguage(driver, textLanguage1);
//Esperamos a que se cargue el saludo de bienvenida en Español
        PO_HomeView.checkWelcomeToPage(driver, locale1);
    }

    public static boolean checkTextNotInView(WebDriver driver, String texto) {

        int locale = PO_Properties.getSPANISH();

        String text = PO_View.getP().getString(texto, locale);
        String busqueda = "//*[contains(text(),'" + text + "')]";

        Boolean resultado;

        resultado = (new WebDriverWait(driver, 2))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(busqueda)));

        return resultado;

    }

    public static void checkStringInLanguage(WebDriver driver, String text, int language) {
        //Esperamos a que se cargue el saludo de bienvenida en Español
        SeleniumUtils.waitLoadElementsBy(driver, "text", p.getString(text, language),
                getTimeout());
    }

    public static void checkTextInAllLanguages(WebDriver driver, String text) {
        //Cambiamos a ingles
        changeLanguage(driver, "btnEnglish");
        //Comprobamos que el texto se haya cambiado a ingles
        checkStringInLanguage(driver, text, PO_Properties.getENGLISH());
        //Cambiamos a español
        changeLanguage(driver, "btnSpanish");
        //Comprobamos que el texto se haya cambiado a español
        checkStringInLanguage(driver, text, PO_Properties.getSPANISH());
    }

    public static PO_Properties getP() {
        return p;
    }
}
