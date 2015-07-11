package SE.chrome;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;

/**
 * Configuration for Selenium Tests.
 *
 * @author joachim.bader
 */
public class ConfigurationUtil {

    public static final String PROXY = "proxy";
    public static final String PROXY_HOST = "proxy.host";
    public static final String PROXY_PORT = "proxy.port";
    public static final String PROXY_USER = "proxy.user";
    public static final String PROXY_PASSWORD = "proxy.password";
    public static final String PROXY_NO_PROXY_HOSTS = "proxy.noProxyHosts";

    public static final String PROXY_NTLM_HOST = "proxy.ntlm.host";
    public static final String PROXY_NTLM_DOMAIN = "proxy.ntlm.domain";

    private static final ConfigurationUtil instance = new ConfigurationUtil();

    private final CompositeConfiguration compositeConfiguration;

    private ConfigurationUtil() {
        try {
            Configuration defaults = new PropertiesConfiguration("default-config.properties");
            Configuration configProperties = createConfigLazy("bcom-selenium.properties");
            Configuration configUserProperties = createConfigLazy("bcom-selenium." + System.getProperty("user.name") + ".properties");

            this.compositeConfiguration = new CompositeConfiguration();

            addConfigLazy(this.compositeConfiguration, configUserProperties);
            addConfigLazy(this.compositeConfiguration, configProperties);

            this.compositeConfiguration.addConfiguration(defaults);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void addConfigLazy(CompositeConfiguration compositeConfiguration, Configuration config) {
        if (config != null) {
            compositeConfiguration.addConfiguration(config);
        }
    }

    private Configuration createConfigLazy(final String filename) {
        Configuration config = null;

        File file = new File(filename);

        if (file.exists()) {
            try {
                config = new PropertiesConfiguration(file);
            } catch (ConfigurationException ex) {
                throw new RuntimeException(ex);
            }
        }

        return config;
    }

    protected static ConfigurationUtil getInstance() {
        return instance;
    }

    public Configuration getCompositeConfiguration() {
        return this.compositeConfiguration;
    }

    public static Configuration getConfig() {
        return getInstance().getCompositeConfiguration();
    }

    public static String getString(final String key){
        return ConfigurationUtil.getConfig().getString(key);
    }
}
