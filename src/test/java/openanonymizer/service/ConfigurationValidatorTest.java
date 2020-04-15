package openanonymizer.service;

import openanonymizer.config.Configuration;
import openanonymizer.config.loader.ConfigurationLoader;
import openanonymizer.config.loader.JsonConfigurationLoader;
import openanonymizer.core.validation.ConfigurationValidationException;
import openanonymizer.core.validation.ConfigurationValidator;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class ConfigurationValidatorTest {

    private static final String DIR = "src/test/resources";
    private static final ConfigurationLoader loader = new JsonConfigurationLoader();

    @Before
    public void before() {
        DOMConfigurator.configure("log4j.xml");
    }

    @Test(expected = ConfigurationValidationException.class)
    public void invalidConfig1() throws ConfigurationValidationException {
        ConfigurationValidator.validateConfiguration(loader.readConfiguration(DIR + "/invalid1.json"));
    }

    @Test(expected = ConfigurationValidationException.class)
    public void invalidConfig2() throws ConfigurationValidationException {
        ConfigurationValidator.validateConfiguration(loader.readConfiguration(DIR + "/invalid2.json"));
    }

    @Test(expected = ConfigurationValidationException.class)
    public void invalidConfig3() throws ConfigurationValidationException {
        ConfigurationValidator.validateConfiguration(loader.readConfiguration(DIR + "/invalid3.json"));
    }

    @Test(expected = ConfigurationValidationException.class)
    public void invalidConfig4() throws ConfigurationValidationException {
        ConfigurationValidator.validateConfiguration(loader.readConfiguration(DIR + "/invalid4.json"));
    }

    @Test
    public void validConfig() throws ConfigurationValidationException {
        Configuration configuration = loader.readConfiguration(DIR + "/valid.json");
        ConfigurationValidator.validateConfiguration(configuration);
        Assert.assertEquals(new Integer(100), configuration.getPageSize());
        Assert.assertEquals(new Integer(2), configuration.getThreads());
        Assert.assertEquals(Locale.ENGLISH, configuration.getLocale());
        Assert.assertEquals(2, configuration.getEntities().size());
    }

}
