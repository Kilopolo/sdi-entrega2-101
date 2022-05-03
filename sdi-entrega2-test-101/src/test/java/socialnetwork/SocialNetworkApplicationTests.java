package socialnetwork;


import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import socialnetwork.pageobjects.*;
//import org.springframework.boot.test.context.SpringBootTest;


//Ordenamos las pruebas por la anotación @Order de cada método
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SocialNetworkApplicationTests {


    //Pablo Diaz
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";


    //Para MACOSX
    //static String PathFirefox = "/Applications/Firefox 2.app/Contents/MacOS/firefox-bin";
    //static String Geckodriver = "/Users/delacal/selenium/geckodriver-v0.30.0-macos";
    //Para Windows
//    static String Geckodriver = "C:\\Path\\geckodriver-v0.30.0-win64.exe";
//    static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";    //Común a Windows y a MACOSX

    //Común a Windows y a MACOSX
    static final String URL = "https://localhost:4000";
    static WebDriver driver = getDriver(PathFirefox, Geckodriver);

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp(){
        driver.navigate().to(URL);
    }
    //Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown(){
        driver.manage().deleteAllCookies();
    }
    //Antes de la primera prueba
    @BeforeAll
    static public void begin() {}
    //Al finalizar la última prueba
    @AfterAll
    static public void end() {
//Cerramos el navegador al finalizar las pruebas
        driver.quit();
    }

    @Test
    @Order(1)
    void Prueba00() {
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
    }

    /**
     * [Prueba1] Registro de Usuario con datos válidos.
     */
    @Test
    @Order(1)
    void Prueba01() {
        PO_RegisterView.registerUser(driver, "email@email.com", "password");

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

        Assertions.assertEquals(true, PO_HomeView.checkTextNotInView(driver, "nav.Desconectar"));
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
