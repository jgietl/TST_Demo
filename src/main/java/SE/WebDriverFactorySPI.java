package SE;

import org.openqa.selenium.WebDriver;

/**
 * Interface for WebDriver Factory implementations.
 *
 * @author joachim.bader
 */
public interface WebDriverFactorySPI {

    /**
     * Create a WebDriver instance for a specific browser.
     *
     * @return Browser Specific WebDriver implementation
     */
    WebDriver create();
}
