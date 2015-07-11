package SE.remote;

import SE.chrome.ConfigurationUtil;
import SE.WebDriverFactorySPI;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * WebDriver Factory for a Selenium Server.
 * 
 * @author joachim.bader
 * @see <a href="https://github.com/SeleniumHQ/selenium/wiki/Grid2">Selenium Server</a>
 */
public class RemoteWebdriverFactory implements WebDriverFactorySPI {

    private static final String REMOTE_URL = "remote.url";
    
    @Override
    public WebDriver create() {
        final String url = (String) ConfigurationUtil.getConfig().getString(REMOTE_URL);
                
        DesiredCapabilities capability = DesiredCapabilities.firefox();
        
        WebDriver driver;
        try {
            driver = new RemoteWebDriver(new URL(url), capability);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        
        return driver;
    }
    
}
