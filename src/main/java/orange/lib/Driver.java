package orange.lib;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amitkk on 10-02-2019.
 */
public class Driver {

    public static DesiredCapabilities cap= new DesiredCapabilities();
    protected static WebDriver driver;
    private static String URL;
    private static String configFilePath = System.getProperty("user.dir") + "\\src\\config\\config.properties";
    private static Properties properties = new Properties();


    /**
     * Launching Browser using WebDriver
     *
     * @return
     * 			Returns the instance of Browser launched
     */
    public static WebDriver Launch_App() {
        String browser = System.getProperty("browser");
        String URL = System.getProperty("environment");
        driver = openBrowser(browser);
        driver.manage().timeouts().implicitlyWait(wait_for_seconds(), TimeUnit.SECONDS);
        driver.get(URL);
        driver.manage().window().maximize();
        return driver;
    }


    private static int wait_for_seconds(){
        try{
            FileInputStream fis = new FileInputStream(configFilePath);
            properties.load(fis);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return Integer.parseInt(properties.getProperty("implicit_load_wait", "60"));
    }



    private static WebDriver openBrowser(String BrowserName) {
        if (BrowserName.startsWith("F")) {
            cap.setCapability("initialBrowserUrl", URL);
            driver = new FirefoxDriver(cap);
            String version = getVersion((String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;"), "Firefox/");
            System.setProperty("browser", "Firefox "+version);
        }
        else if (BrowserName.startsWith("C")) {
            System.setProperty("webdriver.chrome.driver","src/main/java/exzeo/lib/Drivers/chromedriver.exe");
            cap.setCapability("initialBrowserUrl", URL);
            driver = new ChromeDriver(cap);
            String version = getVersion((String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;"), "Chrome/");
            System.setProperty("browser", "Chrome "+version);
        }
        else if (BrowserName.startsWith("I")) {
            System.setProperty("webdriver.ie.driver","src/main/java/exzeo/lib/Drivers/IEDriverServer.exe");
            cap.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
            cap.setCapability("initialBrowserUrl", URL);
            driver = new InternetExplorerDriver(cap);
            String version = getVersion((String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;"), "MSIE ");
            System.setProperty("browser", "IE "+version);
        }
        else{
//            Reporter.log("<b><align=\"center\">"+"------------------ INVALID PARAMETERS ------------------"+ "</b><br />");
//            Reporter.log("<b><align=\"center\">"+"    Please provide any of the 3 options: IE, FF, CH     "+ "</b><br />");
        }

        return driver;
    }



    private static String getVersion(String statement, String match){
        Pattern pattern = Pattern.compile(match+"(\\d+\\.\\d+).*");
        Matcher m = pattern.matcher(statement);
        m.find();
        return m.group(1);
    }

}

