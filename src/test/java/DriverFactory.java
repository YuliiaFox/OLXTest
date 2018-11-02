import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public final class DriverFactory {
    private static WebDriver driver = null;

    private DriverFactory() {
    }

    public static WebDriver getWebDriver() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }
        return driver;
    }
    public static void closeWebDriver(){
        if(getWebDriver()!=null){
            driver.close();
        }
    }
}
