package openanonymizer.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import openanonymizer.datasource.csv.CsvDataSource;
import openanonymizer.model.dataset.DataSet;
import openanonymizer.model.dataset.DataSetImpl;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import openanonymizer.model.wrapper.EntityWrapperImpl;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

public class CsvDataSourceTest {

    private final static String DIR = "src/test/resources";
    private final EntityDescriber describer = new ObjectMapper().readValue(Objects.requireNonNull(this.getClass().getClassLoader().getResource("user_describer.json")), EntityDescriber.class);

    private EntityWrapper wrapper;
    private EntityDescriber simple;

    public CsvDataSourceTest() throws IOException {
    }

    @Before
    public void before() {
        DOMConfigurator.configure("log4j.xml");
        FieldDescriber field = new FieldDescriber();
        field.setName("name");
        simple = new EntityDescriber();
        simple.setName("TestEntity");
        simple.setSource("test");
        simple.setFields(Collections.singletonList(field));
        wrapper = new EntityWrapperImpl(Collections.singletonMap("name", "Test"), simple);
    }

    @After
    public void after() {
        File file = new File(DIR + "/test.csv");
        if (file.exists()) file.delete();
    }

    @Test(expected = NullPointerException.class)
    public void nullTest() {
        new CsvDataSource(null, ',', 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperation() {
        DataSource dataSource = new CsvDataSource(new File(DIR), ',', 0);
        dataSource.updateEntities(null, null);
    }

    @Test
    public void readDataSet() {
        CsvDataSource dataSource = new CsvDataSource(new File(DIR), ',', 1);
        DataSet dataSet = dataSource.readDataSet(describer);
        Assert.assertNotNull(dataSet);
        Assert.assertEquals(10L, dataSet.size());
    }

    @Test
    public void saveDataSet() throws IOException {
        try (CsvDataSource dataSource = new CsvDataSource(new File(DIR), ',', 1)) {
            dataSource.saveEntities(simple, new DataSetImpl(Collections.singletonList(wrapper), simple));
            DataSet dataSet = dataSource.readDataSet(simple);
            Assert.assertEquals(1, dataSet.size());
            Assert.assertEquals("Test", dataSet.next().getValue("name"));
        }
    }

}
