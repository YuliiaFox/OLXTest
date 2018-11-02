package pages;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import sun.jvm.hotspot.debugger.Page;

public class ProductDetailsPage extends BasePage {
    public ProductDetailsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver, ProductDetailsPage.class);
    }

    @FindBy(xpath = "//h1")
    private WebElement productName;

    public boolean isProductNameOnProductDetailsPageAndInSearchPageTheSame(String){
        return Assert.assertEquals(productName.getText(), );
    }
}
