package openanonymizer.service;

import openanonymizer.core.storage.TransformationStorage;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class TransformationStorageTest {

    @Before
    public void before() {
        DOMConfigurator.configure("log4j.xml");
    }

    @Test(expected = NullPointerException.class)
    public void nullSourceTestInsert() {
        TransformationStorage.insertValue(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullSourceTestGet() {
        TransformationStorage.findByLeft(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullSourceTestClear() {
        TransformationStorage.clearSource(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullLeftValueTestInsert() {
        TransformationStorage.insertValue("test", null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullLeftValueTestGet() {
        TransformationStorage.findByLeft("test", null);
    }

    @Test(expected = NullPointerException.class)
    public void nullRightTestInsert() {
        TransformationStorage.insertValue("test", 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void clearStorageTest() {
        TransformationStorage.clearSource("test");
        TransformationStorage.clearSource("test");
    }

    @Test
    public void storageTest1() {
        TransformationStorage.insertValue("test1", 1, 100);
        TransformationStorage.insertValue("test1", 2, 200);
        Assert.assertTrue(TransformationStorage.findByLeft("test1", 1).isPresent());
        Assert.assertTrue(TransformationStorage.findByLeft("test1", 2).isPresent());
    }

    @Test
    public void storageTest2() {
        TransformationStorage.insertValue("test1", 1, 100);
        TransformationStorage.insertValue("test1", 2, 200);
        Optional<Pair> pair1 = TransformationStorage.findByLeft("test1", 1);
        Optional<Pair> pair2 = TransformationStorage.findByLeft("test1", 2);
        Assert.assertEquals(100, pair1.get().getRight());
        Assert.assertEquals(200, pair2.get().getRight());
        TransformationStorage.clearSource("test1");
        Assert.assertFalse(TransformationStorage.findByLeft("test1", 1).isPresent());
        TransformationStorage.insertValue("test1", 1, 300);
        Assert.assertTrue(TransformationStorage.findByLeft("test1", 1).isPresent());
    }

    @Test
    public void storageTest3() {
        TransformationStorage.insertValue("test1", 1, 100);
        TransformationStorage.insertValue("test2", 2, 200);
        TransformationStorage.clearSource("test2");
        Assert.assertTrue(TransformationStorage.findByLeft("test1", 1).isPresent());
        Assert.assertFalse(TransformationStorage.findByLeft("test2", 2).isPresent());
    }

}
