package application;

import application.config.Configuration;
import application.config.loader.ConfigurationLoader;
import application.config.loader.JsonConfigurationLoader;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

public class ConfigurationLoaderTest {

    private final static URL PATH = ConfigurationLoaderTest.class.getClassLoader().getResource("config.json");

    @Test
    public void test() {
        DOMConfigurator.configure("log4j.xml");
        ConfigurationLoader loader = new JsonConfigurationLoader();
        Configuration configuration = loader.readConfiguration(PATH.getPath());
        Assert.assertNotNull(configuration.getInputSource());
        Assert.assertNotNull(configuration.getOutputSource());
        Assert.assertNotNull(configuration.getEntities());
        Assert.assertNotNull(configuration.getDictionaryPath());
        Assert.assertNotNull(configuration.getLocale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyTest() {
        ConfigurationLoader loader = new JsonConfigurationLoader();
        loader.readConfiguration("");
    }

}
