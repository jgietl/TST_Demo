package SE;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

/**
 *
 * @author 10BADEJ
 */
public class WebDriverFactory {

    /**
     * Properties file (classpath) which consists of a mapping of browser
     * abbreviations and class names which implements the
     * {@link WebDriverFactorySPI}
     */
    private static final String BROWSER_FACTORY_CONFIG = "browser-factories.properties";

    public WebDriver create(final String browserName) {
        final String className = findClassName(browserName.toLowerCase());

        try {
            Class clazz = Class.forName(className);
            WebDriverFactorySPI factory = (WebDriverFactorySPI) clazz.newInstance();

            WebDriver webDriver = factory.create();

            init(webDriver);

            return webDriver;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected String findClassName(final String browserName) {
        final Properties factories = loadFactoriesConfig();

        String className = factories.getProperty(browserName);
        if (className == null) {
            String alias = factories.getProperty("alias." + browserName);
            className = factories.getProperty(alias);
        }

        if (className == null) {
            throw new RuntimeException("no driver factory found for " + browserName);
        }

        return className;
    }

    protected void init(WebDriver driver) {
        //driver.manage().window().setSize(new Dimension(100, 100));
        //driver.manage().window().setPosition(new Point(0, 0));
    }

    protected Properties loadFactoriesConfig() {
        try {
            InputStream in = this.getClass().getResourceAsStream(BROWSER_FACTORY_CONFIG);
            Properties factories = new Properties();
            factories.load(in);

            return factories;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
