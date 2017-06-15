package spaghetti;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Automate following steps: Goto www.nascar.com Register Login with the registered credentials Logout
 *
 */

public class FunctionalTest {

    private WebDriver driver;
    private WebDriverWait driverWait;

    private static final String urlMainPage = "http://www.nascar.com/en_us/sprint-cup-series.html";

    private static final By hrefMyProfile = By.xpath("//ul[@class='primary active']//a[@id='myProfileLink']");
    private static final By hrefLogin = By.xpath("//a[@class='gigyaLoginDialog tab tab-button']");
    private static final By hrefRegister = By
            .xpath("//div[@id='gigya-login-screen']//a[@data-switch-screen='gigya-register-screen']");
    private static final By buttonLogOut = By
            .xpath("//div[@class='pageContainer']//input[@class='gigya-input-submit logout']");

    private static final By dialog = By.xpath("//div[@class='gigya-screen-dialog-inner']");
    private static final By textUser = dialog.name("username");
    private static final By password = dialog.name("password");
    private static final By submit = dialog.xpath("//input[@class='gigya-input-submit']/..");

    private static final By textEmail = By.xpath("//div[@class='gigya-screen-dialog-inner']//input[@name='email']");
    private static final By textZIP = dialog.name("zip");
    private static final By checkboxPrivacyPolicy = By
            .xpath("//div[@class='gigya-screen-dialog-inner']//input[@name='data.terms']");

    private static final By divRaceCenter = By.xpath("//div[@class='race-center-container clearfix ']");
    private static final By aEntryList = By.xpath("//div[@id='tab-1']//div[@class='button']/a");

    @BeforeClass
    public void beforeTestClass() {
        System.setProperty("webdriver.gecko.driver",
                getClass().getClassLoader().getResource("geckodriver/geckodriver.exe").getPath());
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver/chromedriver.exe");

        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driverWait = new WebDriverWait(driver, 30);
    }

    @AfterClass
    public void afterTestClass() {
        driver.quit();
    }

    @Test
    public void loginByRegisteredUser() {
        // test data
        String registeredUserEmail = "shynkarenkoden@yandex.ru";
        String registeredUserPassword = "Denys123456789";

        // Open page
        driver.get(urlMainPage);
        String originalHandle = driver.getWindowHandle();
        waitForJSandJQueryToLoad();
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(divRaceCenter));
        System.out.println(driver.getTitle());

        // Go to login
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(aEntryList));
        driver.findElement(aEntryList).click();
        waitForJSandJQueryToLoad();
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(hrefLogin));
        driver.findElement(hrefLogin).click();

        waitForJSandJQueryToLoad();

        // Login as registered user
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(dialog));
        driver.findElement(textUser).sendKeys(registeredUserEmail);
        driver.findElement(password).sendKeys(registeredUserPassword);
        driver.findElement(submit).submit();
        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(submit));

        waitForJSandJQueryToLoad();
        closeTabs(originalHandle);

        // Go to user profile
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(hrefMyProfile));
        driver.findElement(hrefMyProfile).click();

        waitForJSandJQueryToLoad();

        // Logout
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(buttonLogOut));
        driver.findElement(buttonLogOut).click();
        closeTabs(originalHandle);
    }

    @Test
    public void registerNewUser() {
        // test data
        String newUserEmail = "myemail@gmail.com";
        String newUserPassword = "MySuperPass_word";
        String newUserZip = "40211";

        // Open page
        driver.get(urlMainPage);
        waitForJSandJQueryToLoad();
        String originalHandle = driver.getWindowHandle();
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(divRaceCenter));
        System.out.println(driver.getTitle());

        // Go to login
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(aEntryList));
        driver.findElement(aEntryList).click();
        waitForJSandJQueryToLoad();
        closeTabs(originalHandle);
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(hrefLogin));
        waitForJSandJQueryToLoad();
        driver.findElement(hrefLogin).click();

        waitForJSandJQueryToLoad();

        // Open register page
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(hrefRegister));
        driver.findElement(hrefRegister).click();

        waitForJSandJQueryToLoad();

        // Fill the fields
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(textEmail));
        driver.findElement(textEmail).sendKeys(newUserEmail);
        driver.findElement(password).sendKeys(newUserPassword);
        driver.findElement(textZIP).sendKeys(newUserZip);
        if (!driver.findElement(checkboxPrivacyPolicy).isSelected())
            driver.findElement(checkboxPrivacyPolicy).click();

        // Submit
        driver.findElement(submit).click();
        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(submit));

        waitForJSandJQueryToLoad();
        closeTabs(originalHandle);

        // Go to user profile
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(hrefMyProfile));
        driver.findElement(hrefMyProfile).click();
        waitForJSandJQueryToLoad();

        // Logout
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(buttonLogOut));
        driver.findElement(buttonLogOut).click();
        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(buttonLogOut));
        closeTabs(originalHandle);
    }

    private boolean waitForJSandJQueryToLoad() {
        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    // no jQuery present
                    return true;
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
                        .equals("complete");
            }
        };

        return driverWait.until(jQueryLoad) && driverWait.until(jsLoad);
    }

    private void closeTabs(String exceptThisTab) {
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(exceptThisTab)) {
                driver.switchTo().window(handle);
                driver.close();
            }
        }

        driver.switchTo().window(exceptThisTab);
    }
}
