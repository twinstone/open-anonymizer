package application.service;

import application.core.random.RandomSequenceGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RandomSequenceGeneratorTest {

    @Test(expected = NullPointerException.class)
    public void nullTest() {
        RandomSequenceGenerator.generate(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullUTest() {
        RandomSequenceGenerator.generateUnique(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalTest() {
        RandomSequenceGenerator.generate("");
    }

    @Test
    public void testDateGeneration() {
        String p = "d{10-30}-0d{1-9}-d{1999-2020}";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String r = RandomSequenceGenerator.generate(p);
        LocalDate date = LocalDate.parse(r, formatter);
        Assert.assertNotNull(date);
    }

    @Test
    public void testBankAccountNumberGeneration() {
        String pattern = "\\d{9}/\\d{4}";
        String p = "D{9-9}/D{4-4}";
        String r = RandomSequenceGenerator.generate(p);
        Assert.assertTrue(r.matches(pattern));
    }

    @Test
    public void generateRandomLowerCase() {
        String pattern = "[a-z]{5}";
        String p = "w{5-5}";
        String r = RandomSequenceGenerator.generate(p);
        Assert.assertTrue(r.matches(pattern));
    }

    @Test
    public void generateRandomUpperCase() {
        String pattern = "[A-Z]{5}";
        String p = "W{5-5}";
        String r = RandomSequenceGenerator.generate(p);
        Assert.assertTrue(r.matches(pattern));
    }

    @Test
    public void generateRandomAscii() {
        String pattern = ".{5}";
        String p = "a{5-5}";
        String r = RandomSequenceGenerator.generate(p);
        Assert.assertTrue(r.matches(pattern));
    }

    @Test
    public void uniqueTest() {
        String p = "d{2-5}";
        String v1 = RandomSequenceGenerator.generateUnique(p);
        String v2 = RandomSequenceGenerator.generateUnique(p);
        String v3 = RandomSequenceGenerator.generateUnique(p);
        Assert.assertNotEquals(v1, v2);
        Assert.assertNotEquals(v2, v3);
        Assert.assertNotEquals(v3, v1);
    }
}
