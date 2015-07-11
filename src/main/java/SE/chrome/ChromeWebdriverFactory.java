package SE.chrome;

import SE.WebDriverFactorySPI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;

/**
 * Chrome WebDriver Factory.
 *
 * @author joachim.bader
 * @see <a href="https://sites.google.com/a/chromium.org/chromedriver">Chrome Driver</a>
 */
public class ChromeWebdriverFactory implements WebDriverFactorySPI {

    private static final String DRIVER_PACKAGE_PATH = "chrome.webdriver.path";

    @Override
    public WebDriver create() {
        final String chromeDriverProp = ConfigurationUtil.getConfig().getString(DRIVER_PACKAGE_PATH);
        final String platformDirectory = getPlatformDirectory();
        final String platformExecutable = getPlatformExecutable();

        String driverExecutablePath = chromeDriverProp;
        if (!driverExecutablePath.endsWith(File.separator)) {
            driverExecutablePath += File.separator;
        }

        driverExecutablePath += platformDirectory + File.separator + platformExecutable;

        System.setProperty("webdriver.chrome.driver", driverExecutablePath);
        ChromeDriver driver = new ChromeDriver();

        configure(driver);

        return driver;
    }

    protected void configure(final ChromeDriver driver) {
        // todo: proxy?
    }

    private String getPlatformExecutable() {
        final String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("windows")) {
            return "chromedriver.exe";
        } else {
            return "chromedriver";
        }
    }

    /**
     * @return
     * @throws IllegalAccessException may throw a illegal argument exception if the current platform is not supported
     */
    private String getPlatformDirectory() {
        final String osName = System.getProperty("os.name").toLowerCase();
        final String osArch = System.getProperty("os.arch");

        String platformDirectory;

        if (osName.contains("windows")) {
            // there's no special windows 64bit driver for chrome, so take the 32bit version for both
            platformDirectory = "chromedriver_win32";
        } else if (osName.contains("linux") && osArch.contains("64")) {
            platformDirectory = "chromedriver_linux64";
        } else if (osName.contains("linux") && osArch.contains("32")) {
            platformDirectory = "chromedriver_linux32";
        } else if (osName.contains("os x")) {
            // there's no special os x 64bit driver for chrome, so take the 32bit version for both
            platformDirectory = "chromedriver_mac32";
        } else {
            throw new IllegalStateException(String.format("Platform is not supported: os.name: %s  os.arch: %s", osName, osArch));
        }

        return platformDirectory;
    }
}
