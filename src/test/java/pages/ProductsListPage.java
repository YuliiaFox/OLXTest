package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductsListPage extends BasePage{

    public ProductsListPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver, ProductsListPage.class);
    }

    @FindBy(xpath = "//a[contains(@class, 'detailsLink')]/strong")
    private WebElement itemsLabels;

}
