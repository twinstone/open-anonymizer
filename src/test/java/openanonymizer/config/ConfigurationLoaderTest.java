package openanonymizer.config;

import openanonymizer.config.loader.ConfigurationLoader;
import openanonymizer.config.loader.JsonConfigurationLoader;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

/**
 * Tests configuration loading from file. Test file iss located in resources directory.
 */
public class ConfigurationLoaderTest {

    private final static URL PATH = ConfigurationLoaderTest.class.getClassLoader().getResource("config.json");

    private ApplicationConfiguration configuration;

    @Before
    public void before() {
        DOMConfigurator.configure("log4j.xml");
    }

    @After
    public void after() {
        if (configuration != null) {
            Assert.assertNotNull(configuration.getInputSource());
            Assert.assertNotNull(configuration.getOutputSource());
            Assert.assertNotNull(configuration.getEntities());
            Assert.assertNotNull(configuration.getDictionaryPath());
            Assert.assertNotNull(configuration.getLocale());
        }
    }

    @Test
    public void dumpAndCsvTest() {
        ConfigurationLoader loader = new JsonConfigurationLoader();
        configuration = loader.readConfiguration(ConfigurationLoaderTest.class.getClassLoader().getResource("config_dump_csv.json").getPath());
    }

    @Test
    public void mongoAndNeoTest() {
        ConfigurationLoader loader = new JsonConfigurationLoader();
        configuration = loader.readConfiguration(ConfigurationLoaderTest.class.getClassLoader().getResource("config_mongo_neo.json").getPath());
    }

    @Test
    public void sqlTest() {
        ConfigurationLoader loader = new JsonConfigurationLoader();
        configuration = loader.readConfiguration(ConfigurationLoaderTest.class.getClassLoader().getResource("config_sql.json").getPath());
        Assert.assertEquals(1, configuration.getRelationEntities().size());
        Assert.assertNotNull(configuration.getSecret());
        Assert.assertNotNull(configuration.getThreads());
        Assert.assertNotNull(configuration.getPageSize());
        Assert.assertNotNull(configuration.getStages());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyTest() {
        ConfigurationLoader loader = new JsonConfigurationLoader();
        loader.readConfiguration("");
    }

}
