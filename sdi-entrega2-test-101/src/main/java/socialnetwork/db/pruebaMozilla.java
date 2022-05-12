package socialnetwork.db;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import socialnetwork.pageobjects.PO_View;

public class pruebaMozilla {


    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";
    static final String URL = "https://localhost:4000";
    static final String URLApiClient = "http://localhost:8081/apiclient/client.html";
//    static WebDriver driver = getDriver(PathFirefox, Geckodriver);

//    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
//        System.setProperty("webdriver.firefox.bin", PathFirefox);
//        System.setProperty("webdriver.gecko.driver", Geckodriver);
//        driver = new FirefoxDriver();
//        return driver;
//    }

    public static void main(String[] args) {
        pruebaMozilla p = new pruebaMozilla();
//        p.run();
        InitDB db = new InitDB();
        db.deleteUserByEmail("email1@email.com");

    }

    private void run() {
//        driver.navigate().to(URLApiClient);
        PO_View.setTimeout(3);


//        testSSL();

//        for (int i = 0; i < 10; i++)
        {
//            PO_PrivateView.loginAPI(driver, "user01@email.com", "user01", "friend-list");

        }


        //driver.manage().deleteAllCookies();
//        driver.quit();
    }

    private void testSSL() {
        //Creating an object of the FirefoxOptions Class
        FirefoxOptions firefoxOptions = new FirefoxOptions();

//Using the setAcceptInsecureCerts() method to pass parameter as False
        firefoxOptions.setAcceptInsecureCerts(true);

        WebDriver driver = new FirefoxDriver(firefoxOptions);

        driver.get(URLApiClient);
        System.out.println("The page title is : " +driver.getTitle());
//        driver.quit();
    }


}
