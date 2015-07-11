package SE.firefox;

import SE.WebDriverFactorySPI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Chrome WebDriver Factory.
 * <p/>
 * Firefox is good Broser! No additional binary necessary to get access to
 * the webdriver interface of the browser.
 *
 * @author joachim.bader
 */
public class FirefoxWebdriverFactory implements WebDriverFactorySPI {

    @Override
    public WebDriver create() {
        FirefoxDriver driver = new FirefoxDriver();

        configure(driver);

        return driver;
    }

    protected void configure(final FirefoxDriver driver) {
        // todo: proxy?
    }

}
