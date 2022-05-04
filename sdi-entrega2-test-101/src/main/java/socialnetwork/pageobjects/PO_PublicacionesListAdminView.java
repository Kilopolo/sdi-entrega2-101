package socialnetwork.pageobjects;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_PublicacionesListAdminView extends PO_NavView {

    /**
     *
     */
    public static void accesoPublicationList(WebDriver driver) {
        PO_PrivateView.login(driver, "admin@email.com", "admin", "user-list");

        //Vamos a la lista de usuarios
        checkElementBy(driver, "text", "Gestión de usuarios").get(0).click();
        checkElementBy(driver, "@href", "publicaciones/adminList").get(0).click();

    }

    public static void changeStateTo(WebDriver driver, String userEmail, String state) {

        Assertions.fail("Not yet implemented");

    }

    public static void changeSomePublicationStateToAnother(WebDriver driver, String state) {

//        List<WebElement> elements = PO_HomeView.checkElementBy(driver, "id","index" );//String.valueOf(publication)
//        WebElement rButton = driver.findElement(By.name(state +" "+ userEmail));
//        System.out.println(rButton.getTagName());

        //vamos a la ultima pagina
        driver.findElement(By.id("page-link-last")).click();

//        List<WebElement> elements =  driver.findElements(By.className("Aceptada Pepe@uniovi.es"));


        //de todos los elementos con el id index
        List<WebElement> elements = checkElementBy(driver, "id", "index");//String.valueOf(publication)
        String selectedClass = null;
        for (WebElement e : elements) {
            if (e.getAttribute("class").equals("Pepe@uniovi.es"))
                switch (state) {
                    case "Aceptada":
                        e.findElement(By.id("Moderada")).click();
                        ;
                    case "Moderada":
                        e.findElement(By.id("Censurada")).click();
                        ;
                    case "Censurada":
                        e.findElement(By.id("Aceptada")).click();
                        ;
                }
            selectedClass = e.getAttribute("class");

        }


        checkElementBy(driver, "id", "submit").get(0).click();


        //vamos a la ultima pagina
        driver.findElement(By.id("page-link-last")).click();

        Assertions.assertTrue(null != selectedClass);
        WebElement l = driver.findElement(By.className(selectedClass));
        l.getAttribute("checked");


    }

    public static void checkStateOf(WebDriver driver, int publication, String state) {
        Assertions.fail("Not yet implemented");
    }
}
