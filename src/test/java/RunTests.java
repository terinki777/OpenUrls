import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import config.LoadProperties;
import config.LoadURLs;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.chrome.ChromeOptions;
import widgets.PageElements;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@Execution(ExecutionMode.CONCURRENT)
public class RunTests {
    static Properties properties = LoadProperties.getProperties();
    PageElements pageElements = new PageElements();

    int rerunCount = Integer.parseInt(properties.getProperty("rerun.count"));

    List<String> urls = new LoadURLs().getURLs();

    long minOpen = Long.parseLong(properties.getProperty("open.min.sec")) * 1000;
    long maxOpen = Long.parseLong(properties.getProperty("open.max.sec")) * 1000;

    long minClose = Long.parseLong(properties.getProperty("close.min.sec")) * 1000;
    long maxClose = Long.parseLong(properties.getProperty("close.max.sec")) * 1000;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        Configuration.browserSize = properties.getProperty("browser.size");
        Selenide.webdriver().driver().config().browserCapabilities().setCapability(ChromeOptions.CAPABILITY, options);
    }

    @AfterAll
    static void closeDriver() {
        webdriver().driver().close();
        System.out.println("*******************************************************************");
        System.out.println("The launch is completed");
        System.out.println("*******************************************************************");
    }

    @Test
    public void runTest1() {
        execution();
    }

    @Test
    public void runTest2() {
        execution();
    }

    @Test
    public void runTest3() {
        execution();
    }

    @Test
    public void runTest4() {
        execution();
    }

    void execution() {
        Random random = new Random();
        long pauseBeforeOpen = random.nextLong(maxOpen - minOpen) + minOpen;
        long pauseBeforeClose = random.nextLong(maxClose - minClose) + minClose;
        int index = random.nextInt(urls.size());
        sleep(pauseBeforeOpen);

        for (int i = 0; i < rerunCount; i++) {
            System.out.println("*******************************************************************");
            System.out.printf("THREAD '%s': OPEN browser %s/%s%n", Thread.currentThread().getName(), i + 1, rerunCount);
            System.out.println("*******************************************************************");
            open(urls.get(index));

            try {
                pageElements.popup.shouldBe(visible, Duration.ofSeconds(10));
                pageElements.popupSvg.click();
                pageElements.popup.shouldNotBe(visible);
            } catch (AssertionError e) {
                e.getMessage();
            }

            pageElements.videoPlayer.shouldBe(visible, Duration.ofSeconds(10));
            Selenide.actions().sendKeys("M").perform();

            for (int j = 0; j < 3; j++) {
                if (pageElements.soundOn.exists()) {
                    Selenide.actions().sendKeys("M").perform();
                    break;
                } else if (pageElements.soundOff.exists())
                    break;
                sleep(10 * 1000);
            }

            sleep(pauseBeforeClose);
            Selenide.closeWindow();
            System.out.println("*******************************************************************");
            System.out.printf("THREAD '%s': CLOSE browser %s/%s%n", Thread.currentThread().getName(), i + 1, rerunCount);
            System.out.println("*******************************************************************");
        }
        System.out.println("*******************************************************************");
        System.out.printf("THREAD '%s' is completed%n", Thread.currentThread().getName());
        System.out.println("*******************************************************************");
    }
}
