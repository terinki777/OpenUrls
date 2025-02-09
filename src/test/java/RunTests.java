import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import config.LoadProperties;
import config.LoadURLs;
import config.Log;
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
import java.util.logging.Logger;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@Execution(ExecutionMode.CONCURRENT)
public class RunTests {
    static Logger LOGGER = Log.getLOGGER(RunTests.class.getName());
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
        LOGGER.info("-------------------------------------------------------------------");
        LOGGER.info("The launch is completed");
        LOGGER.info("-------------------------------------------------------------------");
    }

    @Test
    public void runTest1() {
        execution();
    }

    void execution() {
        Random random = new Random();
        long pauseBeforeOpen = random.nextLong(maxOpen - minOpen) + minOpen;
        long pauseBeforeClose = random.nextLong(maxClose - minClose) + minClose;
        int index = random.nextInt(urls.size());
        sleep(pauseBeforeOpen);

        for (int i = 0; i < rerunCount; i++) {
            LOGGER.info("-------------------------------------------------------------------");
            LOGGER.info(String.format("THREAD '%s': OPEN browser %s/%s", Thread.currentThread().getName(), i + 1, rerunCount));
            LOGGER.info("-------------------------------------------------------------------");

            try {
                open(urls.get(index));

                pageElements.videoPlayer.shouldBe(visible, Duration.ofSeconds(20));
                Selenide.actions().sendKeys("M").perform();

                if (pageElements.popup.exists()) {
                    try {
                        pageElements.popupSvg.click();
                        pageElements.popup.shouldNotBe(visible);
                    } catch (Error e) {
                        LOGGER.info(e.getMessage());
                    }
                }

                for (int j = 0; j < 3; j++) {
                    if (pageElements.soundOn.exists()) {
                        Selenide.actions().sendKeys("M").perform();
                        break;
                    } else if (pageElements.soundOff.exists())
                        break;
                    sleep(10 * 1000);
                }

            } catch (Error e) {
                LOGGER.info(e.getMessage());
            } finally {
                sleep(pauseBeforeClose);
                Selenide.closeWindow();
                LOGGER.info("-------------------------------------------------------------------");
                LOGGER.info(String.format("THREAD '%s': CLOSE browser %s/%s", Thread.currentThread().getName(), i + 1, rerunCount));
                LOGGER.info("-------------------------------------------------------------------");
            }
        }
        LOGGER.info("*******************************************************************");
        LOGGER.info(String.format("\nTHREAD\n'%s'\nIS\nCOMPLETED", Thread.currentThread().getName().toUpperCase()));
        LOGGER.info("*******************************************************************");
    }
}
