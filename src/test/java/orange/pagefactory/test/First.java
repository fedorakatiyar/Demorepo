package orange.pagefactory.test;

import orange.component.login.Login;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by amitkk on 10-02-2019.
 */
public class First {

    public static void main (String args [])  {

//        First.testOrangeHrm();
    }

    public static void ReadExcel() throws IOException {

        File file = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\ExcelFiles\\Address.xlsx");

        FileInputStream fis  = new FileInputStream(file);

        Workbook workbook = new HSSFWorkbook(fis);

        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> iterator = sheet.iterator();

        while (iterator.hasNext())
        {
            Row nextrow = iterator.next();

        }
    }


























    public static void testOrangeHrm() {

        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\main\\resources\\Drivers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.get("https://demo.openmrs.org/openmrs/");

        Login login = PageFactory.initElements(driver, Login.class);
        System.out.println("--------"+login.getTextBoxUsername());
        login.getTextBoxUsername().sendKeys("Admin");
        login.getTextBoxPassword().sendKeys("Admin123");
        login.getLocation().click();
        login.getBtnLogin().click();
//        driver.quit();
    }
}
