package socialnetwork;


import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import socialnetwork.pageobjects.*;
import socialnetwork.util.SeleniumUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.springframework.boot.test.context.SpringBootTest;


//Ordenamos las pruebas por la anotación @Order de cada método
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SocialNetworkApplicationTests {


    //Pablo Diaz
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";
   //static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    //static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";

    //PabloRgz
    //static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    //static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";

    //PabloRgz
    // static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    // static String Geckodriver = "C:\\Users\\pablo\\Desktop\\uni\\cuartocurso\\segundo\\SDI\\sesion5\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";

    //Para MACOSX
    //static String PathFirefox = "/Applications/Firefox 2.app/Contents/MacOS/firefox-bin";
    //static String Geckodriver = "/Users/delacal/selenium/geckodriver-v0.30.0-macos";
    //Para Windows
//    static String Geckodriver = "C:\\Path\\geckodriver-v0.30.0-win64.exe";
//    static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";    //Común a Windows y a MACOSX

    //Común a Windows y a MACOSX
    static final String URL = "https://localhost:4000";
    static final String URLApiClient = "http://localhost:8081/apiclient/client.html";
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
        PO_View.setTimeout(3);
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
        PO_PrivateView.login(driver, "admin@email.com", "admin", "searchBtn");
        WebElement check = driver.findElement(By.id("checkbox_aaa@email.com"));
        check.click();
        WebElement eliminarBtn = driver.findElement(By.name("eliminar"));
        eliminarBtn.click();


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
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_RegisterView.fillForm(driver, "aaa@email.com", "Randall", "Boggs"
                , "user01", "user01");
        //PO_View.checkElementBy(driver, "id", "signup");
        PO_HomeView.desconect(driver);
        PO_PrivateView.login(driver, "admin@email.com", "admin", "searchBtn");
        WebElement check = driver.findElement(By.id("checkbox_aaa@email.com"));
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
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_RegisterView.fillForm(driver, "zzz@email.com", "Randall", "Boggs"
                , "user01", "user01");
        PO_HomeView.desconect(driver);
        PO_PrivateView.login(driver, "admin@email.com", "admin", "searchBtn");
        WebElement check = driver.findElement(By.id("checkbox_zzz@email.com"));
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
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_RegisterView.fillForm(driver, "zzz1@email.com", "Randall1", "Boggs1"
                , "user01", "user01");
        PO_HomeView.desconect(driver);
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_RegisterView.fillForm(driver, "zzz2@email.com", "Randall2", "Boggs2"
                , "user01", "user01");
        PO_HomeView.desconect(driver);
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_RegisterView.fillForm(driver, "zzz3@email.com", "Randall3", "Boggs3"
                , "user01", "user01");
        PO_HomeView.desconect(driver);
        PO_PrivateView.login(driver, "admin@email.com", "admin", "searchBtn");
        WebElement check = driver.findElement(By.id("checkbox_zzz1@email.com"));
        check.click();
        WebElement check1 = driver.findElement(By.id("checkbox_zzz2@email.com"));
        check1.click();
        WebElement check2 = driver.findElement(By.id("checkbox_zzz3@email.com"));
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
        for (int i = 1; i <= 4; i++) {
            pageBtn = driver.findElement(By.id("page" + i));
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
     * [Prueba16]Hacer  una  búsqueda  con  el  campo  vacío  y  comprobar  que  se  muestra  la  página  que corresponde
     * con el listado usuarios existentes en el sistema.
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
     * [Prueba17] Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
     * muestra la página que corresponde, con la lista de usuarios vacía.
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
        PO_PrivateView.checkTextNotInView(driver, "email");

    }

    /**
     * [Prueba18]Hacer  una  búsqueda  con  un texto  específico y  comprobar  que  se  muestra  la  página
     * que corresponde, con la lista de usuarios en los que el texto
     * especificado sea parte de su nombre, apellidos o de su email.
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
     * [Prueba19] Desde el listado de usuarios de la aplicación, enviar una invitación de amistad a un usuario.
     * Comprobar que la solicitud de amistad aparece en el listado de invitaciones (punto siguiente).
     */
    @Test
    @Order(19)
    void Prueba19() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_RegisterView.fillForm(driver, "a1@email.com", "Randall1", "Boggs1"
                , "user01", "user01");
        PO_HomeView.desconect(driver);
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_RegisterView.fillForm(driver, "a2@email.com", "Randall2", "Boggs2"
                , "user02", "user02");
        PO_HomeView.desconect(driver);
        PO_PrivateView.login(driver, "a1@email.com", "user01", "user-list");
        var pageBtn = driver.findElement(By.id("page"+3));
        pageBtn.click();
        pageBtn = driver.findElement(By.id("page"+5));
        pageBtn.click();
        var elements = PO_View.checkElementBy(driver, "text", "a2@email.com");
        elements = PO_View.checkElementBy(driver, "free", "//a[@href='/peticiones/enviar/a2@email.com']");
        elements.get(0).click();
        SeleniumUtils.waitSeconds(driver, 5);
        PO_HomeView.desconect(driver);
        PO_PrivateView.login(driver, "a2@email.com", "user02", "user-list");
        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/peticiones").get(0).click();
        String checkText = "a1@email.com";
        var result = PO_View.checkElementBy(driver, "text", "a1@email.com");
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * [Prueba20] Desde el listado de usuarios de la aplicación, enviar una invitación de amistad a un usuario al
     * que ya le habíamos enviado la invitación previamente. No debería dejarnos enviar la invitación, se podría
     * ocultar el botón de enviar invitación o notificar que ya había sido enviada previamente.
     */
    @Test
    @Order(20)
    void Prueba20() {
        PO_PrivateView.login(driver, "a1@email.com", "user01", "user-list");
        var pageBtn = driver.findElement(By.id("page"+3));
        pageBtn.click();
        pageBtn = driver.findElement(By.id("page"+5));
        pageBtn.click();
        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "//a[@href='/peticiones/enviar/a2@email.com']", PO_View.getTimeout());
        PO_HomeView.desconect(driver);
    }

    /**
     * [Prueba21] Mostrar el listado de invitaciones de amistad recibidas. Comprobar con un listado que
     * contenga varias invitaciones recibidas.
     */
    @Test
    @Order(21)
    void Prueba21() {
        PO_PrivateView.login(driver, "a2@email.com", "user02", "user-list");
        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/peticiones").get(0).click();
        //Esto para la vista nueva
        PO_Peticiones.checkListaDePeticiones(driver, 1);
    }

    /**
     * [Prueba22] Sobre el listado de invitaciones recibidas. Hacer clic en el botón/enlace de una de ellas y
     * comprobar que dicha solicitud desaparece del listado de invitaciones.
     */
    @Test
    @Order(22)
    void Prueba22() {
        PO_PrivateView.login(driver, "a2@email.com", "user02", "user-list");
        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/peticiones").get(0).click();
        //Esto para la vista nueva
        PO_Peticiones.checkListaDePeticiones(driver,1);
        var elements = PO_View.checkElementBy(driver, "free", "//a[@href='/peticiones/aceptar/a1@email.com']");
        elements.get(0).click();
        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "//a[@href='/peticion/aceptar/a1@email.com']", PO_View.getTimeout());
    }

    /**
     * [Prueba23] Mostrar el listado de amigos de un usuario. Comprobar que el listado contiene los amigos que
     * deben ser.
     */
    @Test
    @Order(23)
    void Prueba23() {
        PO_PrivateView.login(driver, "a1@email.com", "user01", "user-list");
        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/amistades").get(0).click();

        //Contamos el numero de filas de los usuarios
        List<WebElement> amistadesList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertEquals(1, amistadesList.size());

        PO_HomeView.desconect(driver);
        PO_PrivateView.login(driver, "admin@email.com", "admin", "searchBtn");
        WebElement check = driver.findElement(By.id("checkbox_a1@email.com"));
        check.click();
        WebElement check1 = driver.findElement(By.id("checkbox_a2@email.com"));
        check1.click();
        WebElement eliminarBtn = driver.findElement(By.name("eliminar"));
        eliminarBtn.click();
    }


    /**
     * [Prueba24] Ir al formulario crear publicaciones, rellenarla con datos válidos y pulsar el botón Submit.
     * Comprobar que la publicación sale en el listado de publicaciones de dicho usuario.
     * <p>
     * TODO IMPORTANTE: borrar publicación tras ejecutar las pruebas
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
        PO_View.checkElementBy(driver, "id", "tablePublications");

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
        PO_Publicaciones.fillForm(driver, "", "Contenido de test para PR25");

        // Comprobamos que seguimos en el formulario, y se muestra el error apropiado
        PO_Publicaciones.checkElementBy(driver, "text",
                "Ni el título ni el contenido de la publicación pueden estar vacíos");
    }

    /**
     * [Prueba26] Mostrar el listado de publicaciones de un usuario y comprobar que se muestran todas las que
     * existen para dicho usuario.
     */
    @Test
    @Order(26)
    void Prueba26() {
        // Entramos con datos válidos
        PO_PrivateView.login(driver, "user01@email.com", "user01", "user-list");

        //Navegamos al listado de publicaciones
        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/publications").get(0).click();

        // Comprobamos que accedemos a la página correcta
        PO_View.checkElementBy(driver, "id", "tablePublications");

        // Recuperamos una lista de tr
        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "id", "publiCount",
                PO_View.getTimeout());

        //Contamos el número de filas de los usuarios
        int count = 0;
        for (WebElement each : elementos) {
            count++;
        }

        //comprobamos que existe 1 (la que hemos añadido)
        assertEquals(1, count);
    }

    /**
     * [Prueba27] Mostrar el listado de publicaciones de un usuario amigo y comprobar que se muestran todas
     * las que existen para dicho usuario.
     * <p>
     * AVISO: su correcto funcionamiento depende de la prueba 24
     */
    @Test
    @Order(27)
    public void Prueba27() {
        PO_PrivateView.login(driver, "user00@email.com", "user00", "user-list");

        PO_HomeView.checkElementBy(driver, "text", "Opciones").get(0).click();
        PO_HomeView.checkElementBy(driver, "@href", "/amistades").get(0).click();

        // Accede a un amigo con publicaciones, user01
        PO_HomeView.checkElementBy(driver, "@href", "/publications/list/user01@email.com").get(0).click();

        // Se debería ver 1 filas (la cantidad de publicaciones de user00)
        List<WebElement> elementos = PO_Publicaciones.checkElementBy(driver, "free",
                "/html/body/div[1]/div[1]/table/tbody/tr");
        assertEquals(1, elementos.size());
    }

    /**
     * [Prueba28] Utilizando un acceso vía URL u otra alternativa, tratar de listar las publicaciones de un usuario
     * que no sea amigo del usuario identificado en sesión. Comprobar que el sistema da un error de autorización.
     */
    @Test
    @Order(28)
    public void Prueba28() {
        PO_PrivateView.login(driver, "user00@email.com", "user00", "user-list");

        // Intentamos acceder a la lista de publicaciones de un usuario no amigo, y debería impedirse
        driver.navigate().to("http://localhost:4000/publications/list/user07@email.com");
        //PO_View.checkElementBy(driver, "text", "Error de autorización");

        // Comprobamos que se nos ha enviado al listado de amistades con el error apropiado
        PO_Publicaciones.checkElementBy(driver, "text",
                "No tienes permiso para acceder a estas publicaciones");
    }

    /**
     * [Prueba29] Intentar acceder sin estar autenticado a la opción de listado de usuarios. Se deberá volver al
     * formulario de login.
     */
    @Test
    @Order(29)
    public void Prueba29() {
        // Intentamos acceder al listado de usuarios sin estar autenticados
        driver.navigate().to("http://localhost:4000/users");

        // Se nos debería enviar de vuelta al login
        PO_View.checkElementBy(driver, "id", "login");
    }

    /**
     * [Prueba30] Intentar acceder sin estar autenticado a la opción de listado de invitaciones de amistad recibida
     * de un usuario estándar. Se deberá volver al formulario de login.
     */
    @Test
    @Order(30)
    public void Prueba30() {
        // Intentamos acceder al listado de usuarios sin estar autenticados
        driver.navigate().to("http://localhost:4000/peticiones");

        // Se nos debería enviar de vuelta al login
        PO_View.checkElementBy(driver, "id", "login");
    }

    /**
     * [Prueba31] Intentar acceder estando autenticado como usuario standard a la lista de amigos de otro
     * usuario. Se deberá mostrar un mensaje de acción indebida.
     */
    @Test
    @Order(31)
    public void Prueba31() {
        // No es posible acceder a un listado de amistades ajeno, al no contener parámetros en la URL
        // Por tanto, testeamos aquí el funcionamiento del router de sesión para el listado de usuarios

        // Intentamos acceder al listado de usuarios sin estar autenticados
        driver.navigate().to("http://localhost:4000/amistades");

        // Se nos debería enviar de vuelta al login
        PO_View.checkElementBy(driver, "id", "login");
    }

    ///////////////////////////////////////////////////////////////////////////////
    //////////////    Parte 2B - Cliente - Aplicación jQuery       ////////////////
    ///////////////////////////////////////////////////////////////////////////////


    /**
     * [Prueba32] Inicio de sesión con datos válidos.
     */
    @Test
    @Order(32)
    void Prueba32() {
        driver.navigate().to(URLApiClient);
        PO_PrivateView.loginAPI(driver, "user01@email.com", "user01", "friend-list");
//        Assertions.fail("Not yet implemented");
    }

    /**
     * [Prueba33] Inicio de sesión con datos inválidos (usuario no existente en la aplicación).
     */
    @Test
    @Order(33)
    void Prueba33() {
        driver.navigate().to(URLApiClient);
        PO_PrivateView.loginAPI(driver, "usuarioNOexistente@email.com", "1111111", "login");
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
        PO_PrivateView.loginAPI(driver, user, "user01", "friend-list");
        PO_ClienteAPIFriendList.getCountMoreThan(driver, 6);
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
        PO_PrivateView.loginAPI(driver, user, "user01", "friend-list");
        PO_ClienteAPIFriendList.filter(driver, "nameUser00");
        PO_NavView.checkElementBy(driver, "id", "");
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
        PO_PrivateView.loginAPI(driver, user, "user01", "friend-list");
        PO_ClienteAPIFriendList.goToConversation(driver, "user00@email.com");
        PO_ClienteAPIChat.getCountMoreThan(driver, 3);
//        Assertions.fail("Not yet implemented");
    }


    /**
     * [Prueba37] Acceder a la lista de mensajes de un amigo y crear un nuevo mensaje. Validar que el mensaje
     * aparece en la lista de mensajes.
     */
    @Test
    @Order(37)
    void Prueba37() {
        driver.navigate().to(URLApiClient);
        String user = "user01@email.com";
        PO_PrivateView.loginAPI(driver, user, "user01", "friend-list");
        PO_ClienteAPIFriendList.goToConversation(driver, "user00@email.com");
        PO_ClienteAPIFriendList.createMessage(driver, "cceder a la lista de mensajes de un amigo y crear u");
        PO_ClienteAPIChat.getCountMoreThan(driver, PO_ClienteAPIChat.getCountMoreThan(driver, 0));
//        Assertions.fail("Not yet implemented");
    }


}
