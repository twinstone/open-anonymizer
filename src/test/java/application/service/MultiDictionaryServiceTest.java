package application.service;

import application.core.dict.MultiDictionaryService;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class MultiDictionaryServiceTest {

    private final static String DIR = "src/test/resources";

    @Before
    public void before() {
        DOMConfigurator.configure("log4j.xml");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyPatternTest() {
        MultiDictionaryService.generate("", "random", new String[]{}, new String[]{}, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyPathTest() {
        MultiDictionaryService.generate("random", "", new String[]{}, new String[]{}, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyDictionariesTest() {
        MultiDictionaryService.generate("${0}", "random", new String[]{}, new String[]{}, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyDictionariesTest2() {
        MultiDictionaryService.generate("Some text ${0}", "random", new String[]{}, new String[]{}, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyPatternsTest() {
        MultiDictionaryService.generate("#{0}", "random", new String[]{}, new String[]{}, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyPatternsTest2() {
        MultiDictionaryService.generate("Some text #{0}", "random", new String[]{}, new String[]{}, null);
    }

    @Test
    public void testDefault() {
        String p = "${0}/${0}=#{0}";
        String r = MultiDictionaryService.generate(p, DIR, new String[]{"default"}, new String[]{"d{2-2}"}, null);
        Assert.assertEquals("Test/Test=2", r);
    }

    @Test
    public void testMultiDictEn() {
        String p = "${0} from first = ${1} from second";
        String r = MultiDictionaryService.generate(p, DIR, new String[]{"default", "default2"}, new String[]{}, Locale.ENGLISH);
        Assert.assertEquals("Test1 from first = Test1 from second", r);
    }

    @Test
    public void randomAddressTest() {
        String regex = "\\w+\\s\\d+,\\s\\d{5}\\s\\w+";
        String p = "${0} #{0}, #{1} ${1}";
        for (int i = 0; i < 3; ++i) {
            Assert.assertTrue(MultiDictionaryService.generate(p, DIR, new String[]{"default", "default"}, new String[]{"d{10-100}", "D{5-5}"}, null).matches(regex));
        }
    }

}
