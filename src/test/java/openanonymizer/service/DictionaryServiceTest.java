package openanonymizer.service;

import openanonymizer.core.dict.DictionaryService;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.Optional;

/**
 * Test for {@link DictionaryService}. Tests if dictionaries in default and another locales are loaded correctly.
 */
public class DictionaryServiceTest {

    private final static String DIR = "src/test/resources";

    @Before
    public void before() {
        DOMConfigurator.configure("log4j.xml");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyPathTest() {
        DictionaryService.getDictionaryValue("", "random");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyDictNameTest() {
        DictionaryService.getDictionaryValue("random", "");
    }

    @Test
    public void emptyDictionaryTest() {
        Assert.assertFalse(DictionaryService.getDictionaryValue("random", "random").isPresent());
    }

    @Test
    public void emptyLocalizedDictionaryTest() {
        Assert.assertFalse(DictionaryService.getDictionaryValue("random", "random", Locale.CHINA).isPresent());
    }

    @Test
    public void defaultDictTest() {
        Optional<String> v1 = DictionaryService.getDictionaryValue(DIR, "default");
        Optional<String> v2 = DictionaryService.getDictionaryValue(DIR, "default");
        Optional<String> v3 = DictionaryService.getDictionaryValue(DIR, "default");
        Assert.assertTrue(v1.isPresent());
        Assert.assertTrue(v2.isPresent());
        Assert.assertTrue(v3.isPresent());
    }

    @Test
    public void localizedDictTest() {
        Optional<String> v1 = DictionaryService.getDictionaryValue(DIR, "default", Locale.ENGLISH);
        Optional<String> v2 = DictionaryService.getDictionaryValue(DIR, "default", Locale.ENGLISH);
        Optional<String> v3 = DictionaryService.getDictionaryValue(DIR, "default", Locale.ENGLISH);
        Assert.assertTrue(v1.isPresent());
        Assert.assertTrue(v2.isPresent());
        Assert.assertTrue(v3.isPresent());
    }
}
