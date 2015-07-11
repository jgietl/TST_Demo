package SE.ie;

import SE.chrome.ConfigurationUtil;
import SE.WebDriverFactorySPI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.File;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Internet Explorer WebDriver factory.
 * <p/>
 * It's neccessary to reduce the Internet Explorer Security Settings to get
 * access to the remote control. Otherwise the webdriver will not find any elemetns.
 *
 * @author joachim.bader
 * @see <a href="https://code.google.com/p/selenium/wiki/InternetExplorerDriver">IE Driver</a>
 */
public class IEWebdriverFactory implements WebDriverFactorySPI {
    private static final String DRIVER_PACKAGE_PATH = "ie.webdriver.path";

    @Override
    public WebDriver create() {
        final String ieDriverProp = ConfigurationUtil.getConfig().getString(DRIVER_PACKAGE_PATH);
        final String platformDirectory = getPlatformDirectory();

        String driverExecutablePath = ieDriverProp;
        if (!driverExecutablePath.endsWith(File.separator)) {
            driverExecutablePath += File.separator;
        }

        driverExecutablePath += platformDirectory + File.separator + "IEDriverServer.exe";

        System.setProperty("webdriver.ie.driver", driverExecutablePath);
        
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        
        InternetExplorerDriver driver = new InternetExplorerDriver(capabilities);

        configure(driver);

        return driver;
    }

    /**
     * @return
     * @throws IllegalAccessException may throw a illegal argument exception if the current platform is not supported
     */
    private String getPlatformDirectory() {
        final String osName = System.getProperty("os.name").toLowerCase();
        final String osArch = System.getProperty("os.arch");

        String platformDirectory;

        if (osName.contains("windows") && osArch.contains("64")) {
            platformDirectory = "win64";
        } else if (osName.contains("windows") && osArch.contains("32")) {
            platformDirectory = "win32";
        } else {
            throw new IllegalStateException(String.format("Platform is not supported: os.name: %s  os.arch: %s", osName, osArch));
        }

        return platformDirectory;
    }

    protected void configure(final InternetExplorerDriver driver) {
        // todo: proxy?
    }
}
