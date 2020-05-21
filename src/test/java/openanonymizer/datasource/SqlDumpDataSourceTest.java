package openanonymizer.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import openanonymizer.datasource.sqldump.SqlDumpDataSource;
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

/**
 * Tests sql file datasource.
 */
public class SqlDumpDataSourceTest {

    private final static String DIR = "src/test/resources";
    EntityDescriber describer = new ObjectMapper().readValue(Objects.requireNonNull(this.getClass().getClassLoader().getResource("user_describer.json")), EntityDescriber.class);

    private EntityWrapper wrapper;
    private EntityDescriber simple;

    public SqlDumpDataSourceTest() throws IOException {
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
        File file = new File(DIR + "/test.sql");
        if (file.exists()) file.delete();
    }

    @Test(expected = NullPointerException.class)
    public void nullTest() {
        new SqlDumpDataSource(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperation() {
        DataSource dataSource = new SqlDumpDataSource(new File(DIR));
        dataSource.updateEntities(null, null);
    }

    @Test
    public void testRead() {
        SqlDumpDataSource dataSource = new SqlDumpDataSource(new File(DIR));
        DataSet dataSet = dataSource.readDataSet(describer);
        Assert.assertNotNull(dataSet);
        Assert.assertEquals(10L, dataSet.size());
    }

    @Test
    public void saveDataSet() throws IOException {
        try (SqlDumpDataSource dataSource = new SqlDumpDataSource(new File(DIR))) {
            dataSource.saveEntities(simple, new DataSetImpl(Collections.singletonList(wrapper), simple));
            DataSet dataSet = dataSource.readDataSet(simple);
            Assert.assertEquals(1, dataSet.size());
            Assert.assertEquals("Test", dataSet.next().getValue("name"));
        }
    }
}
