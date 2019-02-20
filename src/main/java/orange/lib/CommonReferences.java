package orange.lib;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by amitkk on 10-02-2019.
 */
public abstract class CommonReferences<T> extends Driver {

    private static final int REFRESH_RATE = 5;
    static String configFilePath = System.getProperty("user.dir") + "\\src\\config\\config.properties";
    static Properties properties = new Properties();
    static String cname = null;
    static String style = null;


    /**
     * Method to initialize the object of the desired class
     *
     * @param clazz
     * 				Name of the class to initialize
     *
     * @return
     * 				Object of the class
     *
     */
    public T openPage(Class<T> clazz, WebDriver driver) {
        T page = PageFactory.initElements(driver, clazz);
        ExpectedCondition pageLoadCondition = ((CommonReferences) page).getPageLoadCondition(driver);
        waitForPageToLoad(pageLoadCondition);
        return page;
    }



    /**
     * Method to check condition for condition to load completely
     *
     * @param pageLoadCondition
     * 				Method to wait for the condition to complete
     */
    private void waitForPageToLoad(ExpectedCondition pageLoadCondition) {
        try {
            FileInputStream fis = new FileInputStream(configFilePath);
            properties.load(fis);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            Wait wait = new FluentWait(driver)
                    .withTimeout(Integer.parseInt(properties.getProperty("implicit_load_wait", "60")), TimeUnit.SECONDS)
                    .pollingEvery(REFRESH_RATE, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class);
            wait.until(pageLoadCondition);
        } catch (Exception e) {

            //e.printStackTrace();
        }

    }



    /**
     * Abstract method to check for the respective condition
     *
     * @return
     * 			State of the condition to be verified
     */
    protected abstract ExpectedCondition getPageLoadCondition(WebDriver driver);



    /**
     * Method to set input values for the desired web element.
     *
     * @param inputObj
     * 				WebElement for which the value needs to be set
     *
     * @param Testdata
     * 				String value to be set for this element
     */
    public static void fnInput(Object inputObj, String Testdata) {
        String tagName = ((WebElement) inputObj).getTagName();
        if (tagName.equalsIgnoreCase("input")) {
            ((WebElement) inputObj).clear();
            ((WebElement) inputObj).sendKeys(Testdata);
        } else if (tagName.equalsIgnoreCase("textarea")) {
            ((WebElement) inputObj).clear();
            ((WebElement) inputObj).sendKeys(Testdata);
        } else if (tagName.equalsIgnoreCase("select")) {
            new Select((WebElement) inputObj).selectByVisibleText(Testdata);
        }
    }



    /**
     * Method to click on web elements.
     *
     * @param clickableObj
     * 				WebElement to be clicked.
     * @throws InterruptedException
     */
    public static void fnClick(WebDriver driver, Object clickableObj) throws InterruptedException {
        //waitForJQueryProcessing(driver);
        ((WebElement) clickableObj).click();
        //((WebElement) clickableObj).sendKeys(Keys.TAB);
        Thread.sleep(4000);
    }



    /**
     * Method to perform action from keyboard keys on web elements.
     *
     * @param element
     * 				WebElement on which the action needs to be performed.
     *
     * @param key
     * 				Action which needs to be taken on this element.
     */
    public static void SendKeysFromKeyboard(Object element, String key) {

        if (key.equalsIgnoreCase("TAB")) {
            ((WebElement) element).sendKeys(Keys.TAB);
        }
    }



    /**
     * Method to select Auto-suggested value
     *
     * @param inputElement
     * 					WebElement on which suggestion is to be shown
     *
     * @param suggestedElement
     * 					Suggested WebElement
     *
     * @param inputText
     * 					Text input from user which will generate the suggestion
     *
     * @throws InterruptedException
     */
    public static void fillAutoSuggest(Object inputElement, Object suggestedElement, String inputText, WebDriver driver) throws InterruptedException{
        ((WebElement) inputElement).sendKeys(inputText);
        Thread.sleep(5000);
        Actions actions = new Actions(driver);
        actions.click((WebElement) suggestedElement).perform();
    }



    /**
     * Method to wait for the JQuery to execute
     *
     * @param timeOutInSeconds
     * 					Wait for seconds till timeout
     *
     * @return
     * 					Returns TRUE when JQuery execution is completed; FALSE otherwise
     */
    public static boolean waitForJQueryProcessing(WebDriver driver) {
        boolean jQcondition = false;
        try{
            FileInputStream  fis = new FileInputStream(configFilePath);
            properties.load(fis);
            new WebDriverWait(driver, Integer.parseInt(properties.getProperty("js_load_wait", "30"))).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driverObject) {
                    return (Boolean) ((JavascriptExecutor) driverObject)
                            .executeScript("return jQuery.active == 0");
                }
            });
            jQcondition = (Boolean) ((JavascriptExecutor) driver).executeScript("return window.jQuery != undefined && jQuery.active === 0");
            return jQcondition;
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        catch (Exception e) {
        }
        return jQcondition;
    }



    /**
     * Method to mouse hover on the element and click on it
     *
     * @param elemetToClick
     * 					Element on which hover is required
     * @throws InterruptedException, ElementNotVisibleException
     */
    public void mousehoverclick(WebDriver driver, WebElement elemetToClick) throws InterruptedException, org.openqa.selenium.ElementNotVisibleException{
        //waitForJQueryProcessing(driver);
        driver.manage().window().maximize();
        Actions action = new Actions(driver);
        action.moveToElement(elemetToClick).click().build().perform();
        Thread.sleep(2000);
    }



    /**
     * Method to mouse hover over an element and hit Enter key on the next element
     *
     * eg. Mouse hover on action dropdown and Enter on any action.
     *
     * @param hoverOn
     * 				Mouse hover over this WebElement
     *
     * @param enterOn
     * 				Hit Enter key on this WebElement
     * @throws InterruptedException, org.openqa.selenium.ElementNotVisibleException
     */

    public void hoverAndHitEnter(WebDriver driver, WebElement hoverOn, WebElement enterOn) throws InterruptedException, org.openqa.selenium.ElementNotVisibleException{
        //	waitForJQueryProcessing(driver);
        Actions action = new Actions(driver);
        action.moveToElement(hoverOn).click().build().perform();
        (waitForElement(driver)).until(ExpectedConditions.visibilityOf(enterOn));
        //	enterOn.sendKeys(Keys.ENTER);
        enterOn.click();
        Thread.sleep(2000);
    }




    /**
     * Method for Implicit Wait function to be used in all components and pages
     *
     * @param driver
     * 				Driver instance on which action needs to be performed
     *
     * @return
     * 				Returns the WebElement
     * @throws IOException
     */
    public WebDriverWait waitForElement(WebDriver driver) {
        try{
            FileInputStream  fis = new FileInputStream(configFilePath);
            properties.load(fis);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return new WebDriverWait(driver, Integer.parseInt(properties.getProperty("element_load_wait", "60")));
    }



    private void getValues(WebElement element){
        cname = element.getAttribute("class");
        style = element.getAttribute("style.border");
    }



    private void setBorder(WebElement element, WebDriver dr){
        // draw a border around the found element
        if (dr instanceof JavascriptExecutor) {
            ((JavascriptExecutor)dr).executeScript("arguments[0].style.border='3px solid red'", element);
            ((JavascriptExecutor)dr).executeScript("arguments[0].className = 'automation'", element);
        }
    }



    /**
     * Method to get the text for provided WebElement.
     * This method also encircles the element with a Red Box that can be identified during Assertions in the scripts.
     *
     * @param element
     * 			WebElement whose text is required
     * @param dr
     * 			Instance of WebDriver
     * @return
     * 			String value of the text for provided WebElement
     */
    public String getText(WebElement element, WebDriver dr) {
        getValues(element);
        String text = element.getText();
        setBorder(element,dr);
        return text;
    }



    /**
     * Method to get status of element being displayed for provided WebElement.
     * This method also encircles the element with a Red Box that can be identified during Assertions in the scripts.
     *
     * @param element
     * 			WebElement which needs to be verified
     * @param dr
     * 			Instance of WebDriver
     * @return
     * 			TRUE if element is displayed ; FALSE otherwise
     */
    public Boolean isDisplayed(WebElement element, WebDriver dr) {
        getValues(element);
        Boolean value = element.isDisplayed();
        setBorder(element,dr);
        return value;
    }



    /**
     * Method to get the attribute value text for provided WebElement.
     * This method also encircles the element with a Red Box that can be identified during Assertions in the scripts.
     *
     * @param element
     * 			WebElement whose attribute is required
     * @param parameter
     * 			Attribute parameter required for the element
     * @param dr
     * 			Instance of WebDriver
     * @return
     * 			String value for the attribute of the element
     */
    public String getAttribute(WebElement element, String parameter, WebDriver dr) {
        getValues(element);
        String text = element.getAttribute(parameter);
        setBorder(element,dr);
        return text;
    }

    /**
     * Method to get status of element being enabled for provided WebElement.
     * This method also encircles the element with a Red Box that can be identified during Assertions in the scripts.
     *
     * @param element
     * 			WebElement which needs to be verified
     * @param dr
     * 			Instance of WebDriver
     * @return
     * 			TRUE if element is enabled ; FALSE otherwise
     */
    public Boolean isEnabled(WebElement element, WebDriver dr) {
        getValues(element);
        Boolean value = element.isEnabled();
        setBorder(element,dr);
        return value;
    }

    public Boolean isDisabled(WebElement element, WebDriver dr) {
        getValues(element);
        Boolean value = element.isDisplayed();
        setBorder(element,dr);
        return value;
    }

    /**
     * Method to scroll the page to desired WebElement
     *
     * @param element
     * 			Desired WebElement till which we need to scroll
     */
    public void scrollToWebElement(WebElement element){
        int elementPosition = element.getLocation().getY();
        String js = String.format("window.scroll(0, %s)", elementPosition);
        ((JavascriptExecutor)driver).executeScript(js);
    }

}
