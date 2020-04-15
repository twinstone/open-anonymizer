package openanonymizer.service;

import openanonymizer.core.anonymizer.AnonymizationException;
import openanonymizer.core.anonymizer.AnonymizationService;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import openanonymizer.model.wrapper.EntityWrapperImpl;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnonymizationServiceTest {

    private EntityWrapper wrapper;

    @Before
    public void before() {
        DOMConfigurator.configure("log4j.xml");
        EntityDescriber describer = new EntityDescriber();
        FieldDescriber.AnonymizationConfiguration configuration = new FieldDescriber.AnonymizationConfiguration();
        configuration.setAnonymizationClass("java.lang.String");
        FieldDescriber f1 = new FieldDescriber();
        f1.setName("name");
        f1.setConfiguration(configuration);
        FieldDescriber f2 = new FieldDescriber();
        f2.setName("age");
        f2.setConfiguration(configuration);
        describer.setFields(Arrays.asList(f1, f2));
        Map<String, Object> map = new HashMap<>();
        map.put("name", "TestName");
        map.put("age", 10);
        wrapper = new EntityWrapperImpl(map, describer);
    }

    @Test(expected = NullPointerException.class)
    public void nullEntityTest() throws AnonymizationException {
        AnonymizationService.anonymizeEntity(null, null, null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullLevelTest() throws AnonymizationException {
        AnonymizationService.anonymizeEntity(wrapper, null, null, null, null, null);
    }

    @Test
    public void randomAnonymizationClassInfoTest() throws AnonymizationException {
        AnonymizationService.anonymizeEntity(wrapper, null, null, null, openanonymizer.config.Configuration.ValidationLevel.INFO, null);
    }

    @Test
    public void randomAnonymizationClassWarnTest() throws AnonymizationException {
        AnonymizationService.anonymizeEntity(wrapper, null, null, null, openanonymizer.config.Configuration.ValidationLevel.WARN, null);
    }

    @Test(expected = AnonymizationException.class)
    public void randomAnonymizationClassErrorTest() throws AnonymizationException {
        AnonymizationService.anonymizeEntity(wrapper, null, null, null, openanonymizer.config.Configuration.ValidationLevel.ERROR, null);
    }

    @Test
    public void anonymizationTest() throws AnonymizationException {
        FieldDescriber.AnonymizationConfiguration configuration = new FieldDescriber.AnonymizationConfiguration();
        configuration.setAnonymizationClass("openanonymizer.service.TestAnonymizer");
        wrapper.describeEntity().getFields().forEach(f -> f.setConfiguration(configuration));
        wrapper = AnonymizationService.anonymizeEntity(wrapper, null, null, null, openanonymizer.config.Configuration.ValidationLevel.ERROR, this.getClass().getClassLoader());
        Assert.assertEquals("anonymized", wrapper.getValue("name"));
        Assert.assertEquals("anonymized", wrapper.getValue("age"));
    }
}
