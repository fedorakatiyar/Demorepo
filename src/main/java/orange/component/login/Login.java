package orange.component.login;

import orange.lib.CommonReferences;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created by amitkk on 10-02-2019.
 */
public class Login extends CommonReferences<Login> {


    


    @FindBy(id="username")
    private WebElement textBoxUsername;

    @FindBy(id="password")
    private WebElement textBoxPassword;

    @FindBy(id="Pharmacy")
    private WebElement location;

    @FindBy(id="loginButton")
    private WebElement btnLogin;


    public WebElement getLocation() {
        return location;
    }

    public WebElement getTextBoxUsername() {
        return textBoxUsername;
    }

    public WebElement getTextBoxPassword() {
        return textBoxPassword;
    }

    public WebElement getBtnLogin() {

        return btnLogin;
    }

    /**
     * Abstract method to check for the respective condition
     *
     * @param driver
     * @return State of the condition to be verified
     */
    protected ExpectedCondition getPageLoadCondition(WebDriver driver) {

        (waitForElement(driver)).until(ExpectedConditions.presenceOfElementLocated(By.id("loginButton")));
        waitForJQueryProcessing(driver);
        return ExpectedConditions.visibilityOf(btnLogin);
    }



}
