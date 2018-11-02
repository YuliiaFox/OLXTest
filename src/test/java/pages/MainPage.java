package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage extends BasePage {
    public MainPage(WebDriver driver) {
        super( driver);
        PageFactory.initElements(this.driver,MainPage.class);
    }
    @FindBy(id = "headerLogo")
    private WebElement logo;
    @FindBy(id = "headerSearch")
    private WebElement searchField;
    @FindBy(id ="submit-searchmain")
    private WebElement searchButton;

}
