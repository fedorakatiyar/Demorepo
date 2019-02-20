package orange.smoke.test;

import orange.component.login.Login;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by amitkk on 12-02-2019.
 */
public class smoke {

    WebDriver driver = null;

    @Parameters("browser")
    @BeforeTest
public void openBrowser(String browser){
        try {
                if(browser.equalsIgnoreCase("edge"))
                {
                    System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")+"\\src\\main\\resources\\Drivers\\MicrosoftWebDriver.exe");
                    driver = new EdgeDriver();
                }
                else if(browser.equalsIgnoreCase("chrome"))
                {
                    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\main\\resources\\Drivers\\chromedriver.exe");
                    driver = new ChromeDriver();

                }else if (browser.equalsIgnoreCase("IE")) {
                    System.setProperty("webdriver.ie.driver",System.getProperty("user.dir")+"\\src\\main\\resources\\Drivers\\IEDriverServer.exe");
                    driver = new InternetExplorerDriver();
                }
                else {

                    System.out.println("No driver found ");
                }
        } catch (WebDriverException e) {
            System.out.println(e.getMessage());
        }

    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//    Dimension d = new Dimension(600,600);
//    //Resize current window to the set dimension
//    driver.manage().window().setSize(d);
        driver.manage().window().maximize();

    driver.get("https://demo.openmrs.org/openmrs/");
    }



    @AfterTest
    public void tearDown(){

        driver.quit();
    }

    @Test
    public void login() throws InterruptedException {

        Login login = PageFactory.initElements(driver,Login.class);
        System.out.println(login.getTextBoxUsername());
        Thread.sleep(3000);

        login.getTextBoxUsername().sendKeys("Admin");
//        Thread.sleep(3000);
        login.getTextBoxPassword().sendKeys("Admin123");
//        Thread.sleep(3000);


        login.getLocation().click();
//        Thread.sleep(3000);
        login.getBtnLogin().click();
//        Thread.sleep(6000);
    }




}
