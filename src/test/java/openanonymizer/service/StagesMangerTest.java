package openanonymizer.service;

import openanonymizer.config.Configuration;
import openanonymizer.core.stage.StageManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Locale;

public class StagesMangerTest {

    @Before
    public void before() {
        DOMConfigurator.configure("log4j.xml");
    }

    @Test
    public void test() {
        Configuration configuration = new Configuration();
        configuration.setStages(Arrays.asList("openanonymizer.service.SimpleTestStage", "test"));
        StageManager manager = StageManager.fromConfiguration(configuration);
        manager.processChain();
        Assert.assertEquals(Locale.CHINA, configuration.getLocale());
    }

}
