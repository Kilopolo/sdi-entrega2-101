package socialnetwork.pageobjects;


import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import socialnetwork.util.*;

import java.util.List;


public class PO_UserListAdminView extends PO_NavView {

    /**
     *
     */
    public static void accesoUserList(WebDriver driver) {
        PO_PrivateView.login(driver, "admin@email.com", "admin", "user-list");

        //Vamos a la lista de usuarios
        checkElementBy(driver, "text", "Gestión de usuarios").get(0).click();
        checkElementBy(driver, "@href", "user/adminList").get(0).click();

    }

    /**
     * @param expectedSize
     */
    public static void checkNumberOfUsersOnList(WebDriver driver, int expectedSize) {
        // Contamos el número de filas de notas
        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                getTimeout());
//		CUIDADO AL AÑADIR MAS USUARIOS EN PRUEBAS ANTERIORES
        Assertions.assertEquals(expectedSize, elementos.size());
    }

    /**
     * @param numBorrar
     */
    public static void deleteUser(WebDriver driver, int... numsBorrar) {
        for (int i = 0; i < numsBorrar.length; i++) {
            List<WebElement> filaBorrar = SeleniumUtils.waitLoadElementsBy(driver, "free", "//*[@id=\"deleteCheckbox\"]", getTimeout());
            filaBorrar.get(numsBorrar[i]).click();
        }

        List<WebElement> borrarBoton = SeleniumUtils.waitLoadElementsBy(driver, "free", "//*[@id=\"deleteButton\"]", getTimeout());
        borrarBoton.get(0).click();
    }

    /**
     * @param numBorrar
     */
    public static void reInitDB() {
//		InsertSampleDataService isds = new InsertSampleDataService();
//		isds.reinsertDeletedUsers();

    }


}
