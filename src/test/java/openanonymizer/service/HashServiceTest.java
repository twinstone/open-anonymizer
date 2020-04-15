package openanonymizer.service;

import openanonymizer.core.hash.HashService;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HashServiceTest {

    @Before
    public void before() {
        DOMConfigurator.configure("log4j.xml");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyInputTest() {
        HashService.generateHash("", "random");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySecretTest() {
        HashService.generateHash("random", "");
    }

    @Test
    public void hashTest() {
        String hash1 = HashService.generateHash("RandomTestInput1", "ABC");
        String hash2 = HashService.generateHash("RandomTestInput2", "ABC");
        Assert.assertNotNull(hash1);
        Assert.assertNotNull(hash2);
        Assert.assertNotEquals(hash1, hash2);
    }
}
