package spaghetti;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SpaghettiFunctionalTest {

    private WebDriver driver;
    private WebDriverWait driverWait;

    private static final String MAIN_PAGE_URL = "https://www.olx.ua/";

    private static final By LOGO_LINK = By.id("headerLogo");
    private static final By SEARCH_INPUT = By.id("headerSearch");
    private static final By SEARCH_BUTTON = By.id("submit-searchmain");
    private static final By FOUND_ITEMS_LABELS = By.xpath("//a[contains(@class, 'detailsLink')]/strong");

    private static final By PRODUCT_WITH_DELIVERY_HIGHLIGHT_CLOSE_BUTTON = By.xpath("//span[@class='highlight-close']");
    private static final By GEO_SUGGESTIONS_CLOSE_BUTTON = By.id("geo-suggestions-close");
    private static final By COOKIES_BAR_CLOSE_BUTTON = By.xpath("//a[contains(@class, 'cookiesBarClose')][contains(@class, 'close')]");
    private static final By ITEM_LABEL = By.xpath("//h1");

    private static final String CATEGORY_LINK_XPATH_LOCATOR = "//a[@data-id][..//span[text()='%s']]";

    private static final By CHANGE_LANGUAGE_LINK = By.id("changeLang");

    @BeforeMethod
    public void beforeTestMethod() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1700, 850));
        driverWait = new WebDriverWait(driver, 20);
    }

    @AfterMethod
    public void afterTestMethod() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void checkSearchingProductByName() {
        // test data
        String productName = "Lenovo L520";

        // Open Main page
        driver.get(MAIN_PAGE_URL);
        waitForJSandJQueryToLoad();
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(LOGO_LINK));

        driverWait.until(ExpectedConditions.visibilityOfElementLocated(COOKIES_BAR_CLOSE_BUTTON));
        driver.findElement(COOKIES_BAR_CLOSE_BUTTON).click();
        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(COOKIES_BAR_CLOSE_BUTTON));

        // Search a item
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT));
        driver.findElement(SEARCH_INPUT).sendKeys(productName);
        waitForJSandJQueryToLoad();
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BUTTON));
        driver.findElement(SEARCH_BUTTON).click();

        waitForJSandJQueryToLoad();

        // Open Product Details page of a first matched found item
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(FOUND_ITEMS_LABELS));

        driverWait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_WITH_DELIVERY_HIGHLIGHT_CLOSE_BUTTON));
        driver.findElement(PRODUCT_WITH_DELIVERY_HIGHLIGHT_CLOSE_BUTTON).click();
        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(PRODUCT_WITH_DELIVERY_HIGHLIGHT_CLOSE_BUTTON));

        driverWait.until(ExpectedConditions.visibilityOfElementLocated(GEO_SUGGESTIONS_CLOSE_BUTTON));
        driver.findElement(GEO_SUGGESTIONS_CLOSE_BUTTON).click();
        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(GEO_SUGGESTIONS_CLOSE_BUTTON));

        List<WebElement> items = driver.findElements(FOUND_ITEMS_LABELS).stream()
                .filter(item -> item.getText().contains(productName))
                .collect(Collectors.toList());
        assertThat(items.size()).as("Check number of matched found items").isGreaterThan(0);

        String productNameOnSearchPage = items.get(0).getText();
        items.get(0).click();

        waitForJSandJQueryToLoad();

        driverWait.until(ExpectedConditions.visibilityOfElementLocated(ITEM_LABEL));
        assertThat(driver.findElement(ITEM_LABEL).getText())
                .as("Check product names are the same on Details and Search pages")
                .isEqualTo(productNameOnSearchPage);
    }

    @Test
    public void checkSearchingProductByCategory() {
        // test data
        String categoryName = "Електроніка";
        String subCategoryName = "Ноутбуки та аксесуари";
        String productName = "Ноутбук";

        // Open Main page
        driver.get(MAIN_PAGE_URL);
        waitForJSandJQueryToLoad();
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(LOGO_LINK));

        driverWait.until(ExpectedConditions.visibilityOfElementLocated(COOKIES_BAR_CLOSE_BUTTON));
        driver.findElement(COOKIES_BAR_CLOSE_BUTTON).click();
        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(COOKIES_BAR_CLOSE_BUTTON));

        // Change language
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(CHANGE_LANGUAGE_LINK));
        driver.findElement(CHANGE_LANGUAGE_LINK).click();

        // Open category and sub-category
        By category = By.xpath(String.format(CATEGORY_LINK_XPATH_LOCATOR, categoryName));
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(category));
        driver.findElement(category).click();
        By subCategory = By.xpath(String.format(CATEGORY_LINK_XPATH_LOCATOR, subCategoryName));
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(subCategory));
        driver.findElement(subCategory).click();

        waitForJSandJQueryToLoad();

        // Open Product Details page of a first matched found item
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(FOUND_ITEMS_LABELS));

        driverWait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_WITH_DELIVERY_HIGHLIGHT_CLOSE_BUTTON));
        driver.findElement(PRODUCT_WITH_DELIVERY_HIGHLIGHT_CLOSE_BUTTON).click();
        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(PRODUCT_WITH_DELIVERY_HIGHLIGHT_CLOSE_BUTTON));

        driverWait.until(ExpectedConditions.visibilityOfElementLocated(GEO_SUGGESTIONS_CLOSE_BUTTON));
        driver.findElement(GEO_SUGGESTIONS_CLOSE_BUTTON).click();
        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(GEO_SUGGESTIONS_CLOSE_BUTTON));

        List<WebElement> items = driver.findElements(FOUND_ITEMS_LABELS).stream()
                .filter(item -> item.getText().contains(productName))
                .collect(Collectors.toList());
        assertThat(items.size()).as("Check number of matched found items").isGreaterThan(0);

        String productNameOnSearchPage = items.get(0).getText();
        items.get(0).click();

        waitForJSandJQueryToLoad();

        driverWait.until(ExpectedConditions.visibilityOfElementLocated(ITEM_LABEL));
        assertThat(driver.findElement(ITEM_LABEL).getText())
                .as("Check product names are the same on Details and Search pages")
                .isEqualTo(productNameOnSearchPage);
    }

    private boolean waitForJSandJQueryToLoad() {
        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = jsExecutor -> ((Long) ((JavascriptExecutor) jsExecutor)
                .executeScript("return jQuery.active")) == 0;

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = jsExecutor -> ((JavascriptExecutor) jsExecutor)
                .executeScript("return document.readyState").toString().equals("complete");

        return driverWait.until(jQueryLoad) && driverWait.until(jsLoad);
    }
}
