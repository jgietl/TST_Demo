package SE.htmlunit;

import SE.chrome.ConfigurationUtil;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.WebClient;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import SE.WebDriverFactorySPI;

/**
 * HTMLUnitDriver Factory.
 *
 * @author joachim.bader
 * @see <a href="https://code.google.com/p/selenium/wiki/HtmlUnitDriver">Html Unit Driver </a>
 */
public class HTMLUnitDriverFactory implements WebDriverFactorySPI {

    final String NO_PROXY_HOSTS = "localhost";

    @Override
    public WebDriver create() {

        HtmlUnitDriver driver = new HtmlUnitDriver() {

            @Override
            protected WebClient modifyWebClient(WebClient client) {
                if (isProxyEnabled()) {
                    DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();

                    credentialsProvider.addNTLMCredentials(
                            ConfigurationUtil.getConfig().getString(ConfigurationUtil.PROXY_USER),
                            ConfigurationUtil.getConfig().getString(ConfigurationUtil.PROXY_PASSWORD),
                            ConfigurationUtil.getConfig().getString(ConfigurationUtil.PROXY_HOST),
                            ConfigurationUtil.getConfig().getInt(ConfigurationUtil.PROXY_PORT),
                            ConfigurationUtil.getConfig().getString(ConfigurationUtil.PROXY_NTLM_HOST),
                            ConfigurationUtil.getConfig().getString(ConfigurationUtil.PROXY_NTLM_DOMAIN));
                    client.setCredentialsProvider(credentialsProvider);

                    return client;
                } else {
                    return super.modifyWebClient(client);
                }
            }

        };

        configure(driver);

        return driver;
    }

    protected boolean isProxyEnabled() {
        return ConfigurationUtil.getConfig().getBoolean(ConfigurationUtil.PROXY);
    }

    protected void configure(final HtmlUnitDriver driver) {
        if (isProxyEnabled()) {
            Proxy proxy = new Proxy();

            proxy.setProxyType(Proxy.ProxyType.MANUAL);
            proxy.setHttpProxy(ConfigurationUtil.getConfig().getString(ConfigurationUtil.PROXY_HOST) + ":" + ConfigurationUtil.getConfig().getInt(ConfigurationUtil.PROXY_PORT));
            proxy.setSslProxy(ConfigurationUtil.getConfig().getString(ConfigurationUtil.PROXY_HOST) + ":" + ConfigurationUtil.getConfig().getInt(ConfigurationUtil.PROXY_PORT));
            proxy.setNoProxy(ConfigurationUtil.getConfig().getString(ConfigurationUtil.PROXY_NO_PROXY_HOSTS));

            driver.setProxySettings(proxy);
        }
    }

}
