package spaghetti;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * Automate following scenario: Goto www.yahoo.com and fetch the Yahoo sites (Mail, Autos etc) in the Left dynamically
 * and click over each site to verify if the page loads in 7 seconds
 *
 */
public class YahooSitesLoadTimeTest {

    private static final int EXPECTED_MAX_LOAD_TIME = 10;
    private static final String URL_MAIN_PAGE = "https://www.yahoo.com/";

    private WebDriver driver;

    private Map<String, Long> loadTimeResults;

    private final By allLinks = By.xpath("//a");

    public YahooSitesLoadTimeTest() {
        loadTimeResults = new HashMap<>();
    }

    @BeforeClass
    public void beforeTestClass() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1700, 850));
    }

    @AfterClass
    public void afterTestClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void checkYahooSitesLoadTime() {

        // find out load time for main Yahoo site
        driver.get(URL_MAIN_PAGE);

        // get sites
        String href;
        Set<String> yahooSites = new HashSet<>();
        List<WebElement> elements = driver.findElements(allLinks);

        for (WebElement element : elements) {
            href = element.getAttribute("href");
            if (href != null && href.contains("yahoo.com")
                    && !href.contains("#") && !href.contains("=") && !href.contains(".html")
                    && !href.contains(".htm") && !href.contains("-") && !href.equals(URL_MAIN_PAGE))
                yahooSites.add(href);
        }

        // find out load time for each Yahoo site
        yahooSites.forEach(siteUrl -> loadTimeResults.put(siteUrl, getSiteLoadTime(siteUrl)));

        // verify test results
        SoftAssertions.assertSoftly(softly -> loadTimeResults.forEach((key, value) -> {
            System.out.println(String.format("%s was loaded in %s seconds", key, value));
            softly.assertThat(value).as("Load time of %s page", key)
                    .isLessThanOrEqualTo(EXPECTED_MAX_LOAD_TIME);
        }));
    }

    private long getSiteLoadTime(String url) {
        LocalTime startTime = LocalTime.now();

        driver.get(url.isEmpty() ? URL_MAIN_PAGE : url);
        LocalTime endTime = LocalTime.now();

        return startTime.until(endTime, SECONDS);
    }
}
