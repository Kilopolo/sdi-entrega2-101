package socialnetwork;


import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import socialnetwork.db.InitDB;
import socialnetwork.pageobjects.*;
import socialnetwork.util.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.springframework.boot.test.context.SpringBootTest;


//Ordenamos las pruebas por la anotación @Order de cada método
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SocialNetworkApplicationTests {


    //Pablo Diaz
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckodriver = "C:\\Users\\UO270628\\Desktop\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";


    //Para MACOSX
    //static String PathFirefox = "/Applications/Firefox 2.app/Contents/MacOS/firefox-bin";
    //static String Geckodriver = "/Users/delacal/selenium/geckodriver-v0.30.0-macos";
    //Para Windows
//    static String Geckodriver = "C:\\Path\\geckodriver-v0.30.0-win64.exe";
//    static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";    //Común a Windows y a MACOSX

    //Común a Windows y a MACOSX
    static final String URL = "https://localhost:4000/users";
    static final String URLApiClient = "https://localhost:4000/apiclient/client.html";
    static WebDriver driver = getDriver(PathFirefox, Geckodriver);

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp() {
        driver.navigate().to(URL);
    }

    //Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    //Antes de la primera prueba
    @BeforeAll
    static public void begin() {

    }

    //Al finalizar la última prueba
    @AfterAll
    static public void end() {
//Cerramos el navegador al finalizar las pruebas
        driver.quit();
    }


    /**
     * [Prueba1] Registro de Usuario con datos válidos.
     */
    @Test
    @Order(1)
    void Prueba01() {
        String email = "email1@email.com";
        PO_RegisterView.registerUser(driver, email, "password");
        PO_DataBase.deleteUserByEmail(email);

    }

    /**
     * [Prueba2] Registro de Usuario con datos inválidos (email vacío, nombre vacío, apellidos vacíos).
     */
    @Test
    @Order(2)
    void Prueba02() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");

        PO_RegisterView.fillForm(driver, "", "", "", "password", "password");
        PO_View.checkElementBy(driver, "id", "signup");
    }

    /**
     * [Prueba3] Registro de Usuario con datos inválidos (repetición de contraseña inválida).
     */
    @Test
    @Order(3)
    void Prueba03() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");

        PO_RegisterView.fillForm(driver, "Mike@email.com", "Mike", "Wazowski"
                , "password", "password1");
        PO_View.checkElementBy(driver, "id", "signup");
    }

    /**
     * [Prueba4] Registro de Usuario con datos inválidos (email existente).
     */
    @Test
    @Order(4)
    void Prueba04() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");

        PO_RegisterView.fillForm(driver, "user01@email.com", "Randall", "Boggs"
                , "user01", "user01");
        PO_View.checkElementBy(driver, "id", "signup");
    }

    /**
     * [Prueba5] Inicio de sesión con datos válidos (administrador).
     */
    @Test
    @Order(5)
    void Prueba05() {


        PO_PrivateView.login(driver, "admin@email.com", "admin", "user-list");


    }

    /**
     * [Prueba6] Inicio de sesión con datos válidos (usuario estándar).
     */
    @Test
    @Order(6)
    void Prueba06() {
        PO_PrivateView.login(driver, "user01@email.com", "user01", "user-list");
    }

    /**
     * [Prueba7] Inicio de sesión con datos inválidos (usuario estándar, campo email y contraseña vacíos).
     */
    @Test
    @Order(7)
    void Prueba07() {
        //Comprobamos que no hemos entrado
        PO_PrivateView.login(driver, "", "", "login");
    }

    /**
     * [Prueba8] Inicio de sesión con datos válidos (usuario estándar, email existente, pero contraseña
     * incorrecta).
     */
    @Test
    @Order(8)
    void Prueba08() {
        //Compribamos que no hemos entrado
        PO_PrivateView.login(driver, "user01@email.com", "malapassword", "login");
    }

    /**
     * [Prueba9] Hacer clic en la opción de salir de sesión y comprobar que se redirige a la página de inicio de
     * sesión (Login).
     */
    @Test
    @Order(9)
    void Prueba09() {
        //Login como usuario
        PO_PrivateView.login(driver, "user01@email.com", "user01", "user-list");
        //Nos desconectamos
        PO_HomeView.desconect(driver);


    }

    /**
     * [Prueba10] Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
     */
    @Test
    @Order(10)
    void Prueba10() {

        assertEquals(true, PO_HomeView.checkTextNotInView(driver, "nav.Desconectar"));
    }
    /**
     * [Prueba11]Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema.
     */
    @Test
    @Order(11)
    void Prueba11() {
        PO_PrivateView.login(driver, "admin@email.com", "admin", "searchBtn");
        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "text", "email",
                PO_View.getTimeout());

        int count = 0;
        for (WebElement each : elementos) {
            count++;
        }

        assertEquals(16, count);
    }
    /**
     * [Prueba12]Ir a la lista de usuarios, borrar el primer usuario de la lista,
     * comprobar que la lista se actualiza y dicho usuario desaparece.
     */
    @Test
    @Order(12)
    void Prueba12() {
        InitDB.insertUser("aaa","aaa","aaa","USER","aaa");
        PO_PrivateView.login(driver, "admin@email.com", "admin", "searchBtn");
        WebElement check = driver.findElement(By.id("checkbox_aaa"));
        check.click();
        WebElement eliminarBtn = driver.findElement(By.name("eliminar"));
        eliminarBtn.click();
        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "text", "email",
                PO_View.getTimeout());

        int count = 0;
        for (WebElement each : elementos) {
            count++;
        }

        assertEquals(16, count);
    }
    /**
     * [Prueba13]Ir a la lista de usuarios, borrar el último usuario de la lista,
     * comprobar que la lista se actualiza y dicho usuario desaparece.
     */
    @Test
    @Order(13)
    void Prueba13() {
        InitDB.insertUser("zzz","zzz","zzz","USER","zzz");
        PO_PrivateView.login(driver, "admin@email.com", "admin", "searchBtn");
        WebElement check = driver.findElement(By.id("checkbox_zzz"));
        check.click();
        WebElement eliminarBtn = driver.findElement(By.name("eliminar"));
        eliminarBtn.click();
        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "text", "email",
                PO_View.getTimeout());

        int count = 0;
        for (WebElement each : elementos) {
            count++;
        }

        assertEquals(16, count);
    }
    /**
     * [Prueba14]Ir a la lista de usuarios, borrar 3 usuarios,
     * comprobar que la lista se actualiza y dichos usuarios desaparecen.
     */
    @Test
    @Order(14)
    void Prueba14() {
        InitDB.insertUser("zzz","zzz","zzz","USER","zzz");
        InitDB.insertUser("zzz1","zzz1","zzz1","USER","zzz1");
        InitDB.insertUser("zzz2","zzz2","zzz2","USER","zzz2");
        PO_PrivateView.login(driver, "admin@email.com", "admin", "searchBtn");
        WebElement check = driver.findElement(By.id("checkbox_zzz"));
        check.click();
        WebElement check1 = driver.findElement(By.id("checkbox_zzz1"));
        check1.click();
        WebElement check2 = driver.findElement(By.id("checkbox_zzz2"));
        check2.click();
        WebElement eliminarBtn = driver.findElement(By.name("eliminar"));
        eliminarBtn.click();
        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "text", "email",
                PO_View.getTimeout());

        int count = 0;
        for (WebElement each : elementos) {
            count++;
        }

        assertEquals(16, count);
    }
    /**
     * [Prueba15]Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema,
     * excepto el propio usuario y aquellos que sean Administradores.
     */
    @Test
    @Order(15)
    void Prueba15() {
        PO_PrivateView.login(driver, "user14@email.com", "user14", "searchBtn");
        WebElement pageBtn;
       for(int i = 1;i<=4;i++) {
           pageBtn = driver.findElement(By.id("page"+i));
           pageBtn.click();
           List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "text", "email",
                   PO_View.getTimeout());

           int count = 0;
           for (WebElement each : elementos) {
               count++;
           }
           if (i != 4) {
               assertEquals(4, count);
           } else {
               assertEquals(2, count);
           }
       }

    }
    /**
     *[Prueba16]Hacer  una  búsqueda  con  el  campo  vacío  y  comprobar  que  se  muestra  la  página  que corresponde
     *con el listado usuarios existentes en el sistema.
     */
    @Test
    @Order(16)
    void Prueba16() {
        PO_PrivateView.login(driver, "user14@email.com", "user14", "searchBtn");
        WebElement navBar = driver.findElement(By.id("search"));
        navBar.click();
        navBar.clear();
        WebElement navBarBtn = driver.findElement(By.id("searchBtn"));
        navBarBtn.click();
            List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "text", "email",
                    PO_View.getTimeout());
            int count = 0;
            for (WebElement each : elementos) {
                count++;
            }
            assertEquals(4, count);

    }
    /**
     *[Prueba16]Hacer  una  búsqueda  con  el  campo  vacío  y  comprobar  que  se  muestra  la  página  que corresponde
     *con el listado usuarios existentes en el sistema.
     */
    @Test
    @Order(17)
    void Prueba17() {
        PO_PrivateView.login(driver, "user14@email.com", "user14", "searchBtn");
        WebElement navBar = driver.findElement(By.id("search"));
        navBar.click();
        navBar.clear();
        navBar.sendKeys("According to all known laws of aviation, there is no way that a bee should be " +
                "able to fly. Its wings are too small to get its fat little body off the ground." +
                " The bee, of course, flies anyway");
        WebElement navBarBtn = driver.findElement(By.id("searchBtn"));
        navBarBtn.click();
        PO_PrivateView.checkTextNotInView(driver,"email");

    }
    /**
     [Prueba18]Hacer  una  búsqueda  con  un texto  específico y  comprobar  que  se  muestra  la  página
     que corresponde, con la lista de usuarios en los que el texto
     especificado sea parte de su nombre, apellidos o de su email.
     */
    @Test
    @Order(18)
    void Prueba18() {
        PO_PrivateView.login(driver, "user14@email.com", "user14", "searchBtn");
        WebElement navBar = driver.findElement(By.id("search"));
        navBar.click();
        navBar.clear();
        navBar.sendKeys("user02");
        WebElement navBarBtn = driver.findElement(By.id("searchBtn"));
        navBarBtn.click();
        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "text", "email",
                PO_View.getTimeout());
        int count = 0;
        for (WebElement each : elementos) {
            count++;
        }
        assertEquals(1, count);

    }
    /**
     * [Prueba10] Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
     */
    @Test
    @Order(19)
    void Prueba19() {
        PO_PrivateView.login(driver, "user11@email.com", "user11", "user-list");
        var elements = PO_View.checkElementBy(driver, "text", "user03@email.com");
        elements = PO_View.checkElementBy(driver, "free", "//a[@href='/peticiones/enviar/user03@email.com']");
        elements.get(0).click();
        PO_HomeView.desconect(driver);
        PO_PrivateView.login(driver, "user03@email.com", "user03", "user-list");
        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/peticiones").get(0).click();
        String checkText = "user11@email.com";
        var result = PO_View.checkElementBy(driver, "text", "user11@email.com");
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * [Prueba10] Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
     */
    @Test
    @Order(20)
    void Prueba20() {
        PO_PrivateView.login(driver, "user11@email.com", "user11", "user-list");
        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "//a[@href='/peticiones/enviar/user03@email.com']", PO_View.getTimeout());
        PO_HomeView.desconect(driver);
    }

    /**
     * [Prueba10] Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
     */
    @Test
    @Order(21)
    void Prueba21() {
        PO_PrivateView.login(driver, "user03@email.com", "user03", "user-list");
        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/peticiones").get(0).click();
        //Esto para la vista nueva
        PO_Peticiones.checkListaDePeticiones(driver,1);
    }

    /**
     * [Prueba10] Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
     */
    @Test
    @Order(22)
    void Prueba22() {
        PO_PrivateView.login(driver, "user03@email.com", "user03", "user-list");
        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/peticiones").get(0).click();
        //Esto para la vista nueva
        PO_Peticiones.checkListaDePeticiones(driver,1);
        var elements = PO_View.checkElementBy(driver, "free", "//a[@href='/peticion/aceptar/user11@email.com']");
        elements.get(0).click();
        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "//a[@href='/peticion/aceptar/user11@email.com']", PO_View.getTimeout());
    }

    /**
     * [Prueba10] Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
     */
    @Test
    @Order(23)
    void Prueba23() {
        PO_PrivateView.login(driver, "user11@email.com", "user11", "user-list");
        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/amistades").get(0).click();

        //Contamos el numero de filas de los usuarios
        List<WebElement> amistadesList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertEquals(1, amistadesList.size());
    }




    /**
     * [Prueba24] Ir al formulario crear publicaciones, rellenarla con datos válidos y pulsar el botón Submit.
     * Comprobar que la publicación sale en el listado de publicaciones de dicho usuario.
     *
     * IMPORTANTE: borrar publicación tras ejecutar las pruebas
     */
    @Test
    @Order(24)
    void Prueba24() {
        // Entramos con datos válidos
        PO_PrivateView.login(driver, "user01@email.com", "user01", "user-list");

        //Navegamos al formulario de creación de publicación
        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/publications/add").get(0).click();

        // Creamos la publicación de prueba PR24
        PO_Publicaciones.fillForm(driver, "PR24", "Contenido de test para PR24");

        // Comprobamos que se nos envía a la lista de publicaciones
        PO_View.checkElementBy(driver, "text", "Lista de publicaciones");

        // Comprobamos que la nueva publicación aparece
        PO_NavView.checkElementBy(driver, "text", "PR24");
    }

    /**
     * [Prueba25] Ir al formulario de crear publicaciones, rellenarla con datos inválidos (campo título vacío) y
     * pulsar el botón Submit. Comprobar que se muestra el mensaje de campo obligatorio.
     */
    @Test
    @Order(25)
    void Prueba25() {
        // Entramos con datos válidos
        PO_PrivateView.login(driver, "user01@email.com", "user01", "user-list");

        //Navegamos al formulario de creación de publicación
        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/publications/add").get(0).click();

        // Rellenamos el formulario con un título en blanco
        PO_Publicaciones.fillForm(driver, "   ", "Contenido de test para PR25");

        // Comprobamos que seguimos en el formulario de creacion
        PO_View.checkElementBy(driver, "text", "Añadir nueva publicación");
    }

    /**
     * [Prueba26] Mostrar el listado de publicaciones de un usuario y comprobar que se muestran todas las que
     * existen para dicho usuario.
     */
    void Prueba26() {
        // Entramos con datos válidos
        PO_PrivateView.login(driver, "user00@email.com", "user00", "user-list");

        //Navegamos al listad de publicaciones
        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/publications").get(0).click();

        // Comprobamos que accedemos la página correcta
        PO_View.checkElementBy(driver, "text", "Lista de publicaciones");

        // Recuperamos una lista de tr
        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "id", "publiCount",
                PO_View.getTimeout());

        //Contamos el numero de filas de los usuarios
        int count = 0;
        for (WebElement each : elementos) {
            count++;
        }

        //comprobamos que existen 2
        assertEquals(2, count);
    }

    /**
     * [Prueba27] Mostrar el listado de publicaciones de un usuario amigo y comprobar que se muestran todas
     * las que existen para dicho usuario.
     *
     * AVISO: su correcto funcionamiento depende de la prueba 24
     */
    @Test
    @Order(27)
    public void Prueba27() {
        PO_PrivateView.login(driver, "user00@uniovi.es", "user00", "user-list");

        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/amistades").get(0).click();

        // Accede a un amigo con publicaciones, user01
        PO_HomeView.checkElementBy(driver, "@href", "/publications/list/user01@email.com").get(0).click();

        // Se deberían ver 2 filas (la cantidad de publicaciones de user00)
        List<WebElement> elementos = PO_Publicaciones.checkElementBy(driver, "free",
                "/html/body/div[1]/div[1]/table/tbody/tr");
        assertEquals(2, elementos.size());
    }

    /**
     *
     */
    @Test
    @Order(28)
    public void Prueba28() {
        PO_PrivateView.login(driver, "user00@uniovi.es", "user00", "user-list");


    }

    /**
     * [Prueba32] Inicio de sesión con datos válidos.
     */
    @Test
    @Order(32)
    void Prueba32() {
        driver.navigate().to(URLApiClient);
        PO_PrivateView.login(driver, "user01@email.com", "user01", "user-list");
//        Assertions.fail("Not yet implemented");
    }

    /**
     * [Prueba33] Inicio de sesión con datos inválidos (usuario no existente en la aplicación).
     */
    @Test
    @Order(33)
    void Prueba33() {
        driver.navigate().to(URLApiClient);
        PO_PrivateView.loginAPI(driver, "usuarioNOexistente@email.com", "1111111", "friend-list");
//        Assertions.fail("Not yet implemented");
    }

    /**
     * [Prueba34] Acceder a la lista de amigos de un usuario, que al menos tenga tres amigos.
     */
    @Test
    @Order(34)
    void Prueba34() {
        driver.navigate().to(URLApiClient);
        String user = "user01@email.com";
        PO_PrivateView.login(driver, user, "user01", "user-list");
        PO_ClienteAPIFriendList.getCount(driver,user,6);
//        Assertions.fail("Not yet implemented");
    }

    /**
     * [Prueba35] Acceder a la lista de amigos de un usuario, y realizar un filtrado para encontrar a un amigo
     * concreto, el nombre a buscar debe coincidir con el de un amigo.
     */
    @Test
    @Order(35)
    void Prueba35() {
        driver.navigate().to(URLApiClient);
        String user = "user01@email.com";
        PO_PrivateView.login(driver, user, "user01", "user-list");
        PO_ClienteAPIFriendList.filter(driver,"user00@email.com");
        //2 por que las tr estan duplicadas por la linea de ultimo mensaje
        PO_ClienteAPIFriendList.getCount(driver,user,2);
//        Assertions.fail("Not yet implemented");
    }

    /**
     * [Prueba36] Acceder a la lista de mensajes de un amigo, la lista debe contener al menos tres mensajes.
     */
    @Test
    @Order(36)
    void Prueba36() {
        driver.navigate().to(URLApiClient);
        String user = "user01@email.com";
        PO_PrivateView.login(driver, user, "user01", "user-list");
        PO_ClienteAPIFriendList.goToConversation(driver,"user00@email.com");
        PO_ClienteAPIChat.getCountMessages(driver,3);
        Assertions.fail("Not yet implemented");
    }


    /**
     * [Prueba37] Acceder a la lista de mensajes de un amigo y crear un nuevo mensaje. Validar que el mensaje
     * aparece en la lista de mensajes.
     */
    @Test
    @Order(37)
    void Prueba37() {

        Assertions.fail("Not yet implemented");
    }


//
//    @Test
//    @Order(11)
//    public void PR09() {
//    }
//
//    @Test
//    @Order(12)
//    public void PR10() {
//    }
//
//    @Test
//    @Order(13)
//    public void PR11() {
//    }
//
//    //PR12. Loguearse, comprobar que se visualizan 4 filas de notas y desconectarse usando el rol de
//    @Test
//    @Order(14)
//    public void PR12() {
//        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
//        PO_LoginView.fillLoginForm(driver, "99999990A", "123456");
//        String checkText = "Notas del usuario";
//        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
//
//        //Contamos el número de filas de notas
//        List<WebElement> markList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
//        Assertions.assertEquals(4, markList.size());
//
//        //Ahora nos desconectamos comprobamas que aparece el menu de registrarse
//        String loginText = PO_HomeView.getP().getString("signup.message", PO_Properties.getSPANISH());
//        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
//    }
//
//    //PR13. Loguearse como estudiante y ver los detalles de la nota con Descripcion = Nota A2.
//    @Test
//    @Order(15)
//    public void PR13() {
//        //Comprobamos que entramos en la pagina privada de Alumno
//        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
//        PO_LoginView.fillLoginForm(driver, "99999990A", "123456");
//        String checkText = "Notas del usuario";
//
//        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
//
//        //SeleniumUtils.esperarSegundos(driver, 1);
//        //Contamos las notas
//        By enlace = By.xpath("//td[contains(text(), 'Nota A2')]/following-sibling::*[2]");
//        driver.findElement(enlace).click();
//        //Esperamos por la ventana de detalle
//
//        checkText = "Detalles de la nota";
//        result = PO_View.checkElementBy(driver, "text", checkText);
//        Assertions.assertEquals(checkText, result.get(0).getText());
//
//        //Ahora nos desconectamos comprobamas que aparece el menu de registrarse
//        String loginText = PO_HomeView.getP().getString("signup.message", PO_Properties.getSPANISH());
//        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
//    }

    //P14. Loguearse como profesor y Agregar Nota A2.
    //P14. Esta prueba podría encapsularse mejor ...
    @Test
    @Order(16)
    public void PR14() {
//        //Vamos al formulario de login.
//        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
//            PO_LoginView.fillLoginForm(driver, "99999993D", "123456");
//        //Cmmprobamos que entramos en la pagina privada del Profesor
//        PO_View.checkElementBy(driver, "text", "99999993D");
//
//        //Pinchamos en la opción de menu de Notas: //li[contains(@id, 'marks-menu')]/a
//        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//li[contains(@id, 'marks-menu')]/a");
//        elements.get(0).click();
//
//        //Esperamos a aparezca la opción de añadir nota: //a[contains(@href, 'mark/add')]
//        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@href, 'mark/add')]");
//        //Pinchamos en agregar Nota.
//        elements.get(0).click();
//
//        //Ahora vamos a rellenar la nota. //option[contains(@value, '4')]
//        String checkText = "Nota Nueva 3";
//        PO_PrivateView.fillFormAddMark(driver, 3, checkText, "8");
//        //Esperamos a que se muestren los enlaces de paginación la lista de notas
//        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");
//        //Nos vamos a la última página
//        elements.get(3).click();
//        //Comprobamos que aparece la nota en la pagina
//        elements = PO_View.checkElementBy(driver, "text", checkText);
//        Assertions.assertEquals(checkText, elements.get(0).getText());
//
//        //Ahora nos desconectamos comprobamas que aparece el menu de registrarse
//        String loginText = PO_HomeView.getP().getString("signup.message", PO_Properties.getSPANISH());
//        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
    }

    //PRN. Loguearse como profesor, vamos a la ultima página y Eliminamos la Nota Nueva 1.
    //PRN. Ver la lista de Notas.
    @Test
    @Order(17)
    public void PR15() {
        //Vamos al formulario de logueo.
        //PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //PO_LoginView.fillLoginForm(driver, "99999993D", "123456");

        //Comprobamos que entramos en la pagina privada del Profesor
        //PO_View.checkElementBy(driver, "text", "99999993D");
        //Pinchamos en la opción de menu de Notas: //li[contains(@id, 'marks-menu')]/a
    }

}
