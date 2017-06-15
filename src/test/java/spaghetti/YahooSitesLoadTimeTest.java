package spaghetti;

import org.joda.time.LocalTime;
import org.joda.time.Seconds;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Automate following scenario: Goto www.yahoo.com and fetch the Yahoo sites (Mail, Autos etc) in the Left dynamically
 * and click over each site to verify if the page loads in 7 seconds
 *
 */
public class YahooSitesLoadTimeTest {

    private WebDriver driver;

    private final int expectedMaxLoadTime = 7;

    private Map<String, Integer> loadTimeResults;

    private final String urlMainPage = "https://www.yahoo.com/";
    private final By links = By.xpath("//a");

    public YahooSitesLoadTimeTest() {
        loadTimeResults = new HashMap<String, Integer>();
    }

    @BeforeClass
    public void beforeTestClass() {
        System.setProperty("webdriver.gecko.driver",
                getClass().getClassLoader().getResource("geckodriver/geckodriver.exe").getPath());
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver/chromedriver.exe");

        driver = new FirefoxDriver();
        driver.manage().window().maximize();
    }

    @AfterClass
    public void afterTestClass() {
        driver.quit();
    }

    @Test
    public void checkYahooSitesLoadTime() {

        // find out load time for main Yahoo site
        // loadTimeResults.put(yahooPage.getUrlMainPage(), getSiteLoadTime(""));
        driver.get(urlMainPage);

        // Get sites
        String href;
        List<String> yahooSites = new ArrayList<String>();
        List<WebElement> elementList = driver.findElements(links);

        for (WebElement element : elementList) {
            href = element.getAttribute("href");
            if (href != null && !yahooSites.contains(href) && href.contains("yahoo.com") && !href.contains("#")
                    && !href.contains("=") && !href.contains(".html") && !href.contains(".htm") && !href.contains("-")
                    && !href.equals("https://www.yahoo.com/"))
                yahooSites.add(href);
        }

        // find out load time for each Yahoo site
        for (String url : yahooSites)
            loadTimeResults.put(url, getSiteLoadTime(url));

        // print test results
        for (String key : loadTimeResults.keySet())
            System.out.println(key + " was loaded in " + loadTimeResults.get(key) + " seconds");

        // verify test results with expected time
        for (String key : loadTimeResults.keySet())
            assertThat(loadTimeResults.get(key)).as("Load time of " + key + " page")
                    .isLessThanOrEqualTo(expectedMaxLoadTime);
    }

    private int getSiteLoadTime(String url) {
        LocalTime startTime = LocalTime.now();

        driver.get(url.isEmpty() ? urlMainPage : url);
        LocalTime endTime = LocalTime.now();

        return Seconds.secondsBetween(startTime, endTime).getSeconds();
    }

}
