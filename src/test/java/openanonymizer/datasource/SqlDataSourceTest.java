package openanonymizer.datasource;

import openanonymizer.datasource.sql.MySqlDataSource;
import openanonymizer.model.dataset.DataSet;
import openanonymizer.model.dataset.DataSetImpl;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import openanonymizer.model.wrapper.EntityWrapperImpl;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;

/**
 * Tests sql database datasource.
 */
public class SqlDataSourceTest {

    private EntityWrapper wrapper;
    private EntityDescriber simple;
    private DataSource dataSource;
    private ResultSet resultSet;

    public SqlDataSourceTest() throws IOException {
    }

    @Before
    public void before() throws SQLException {
        DOMConfigurator.configure("log4j.xml");
        FieldDescriber field = new FieldDescriber();
        field.setName("name");
        FieldDescriber id = new FieldDescriber();
        id.setName("id");
        simple = new EntityDescriber();
        simple.setId("id");
        simple.setName("TestEntity");
        simple.setSource("test");
        simple.setFields(Arrays.asList(id, field));
        simple.setRelationFields(Collections.emptyList());
        Map<String, Object> map = new HashMap<>();
        map.put("name", "TestName");
        map.put("id", 1);
        wrapper = new EntityWrapperImpl(map, simple);
        resultSet = PowerMockito.mock(ResultSet.class);
        PowerMockito.when(resultSet.next()).thenReturn(true, false);
        PowerMockito.when(resultSet.getObject("name")).thenReturn("TestName");
        dataSource = PowerMockito.mock(DataSource.class);
        Connection connection = PowerMockito.mock(Connection.class);
        PowerMockito.when(dataSource.getConnection()).thenReturn(connection);
        Statement statement = PowerMockito.mock(Statement.class);
        PowerMockito.when(connection.createStatement()).thenReturn(statement);
        PowerMockito.when(statement.execute(anyString())).thenReturn(true);
        PowerMockito.when(statement.executeQuery(anyString())).thenReturn(resultSet);
        PowerMockito.when(statement.executeUpdate(anyString())).thenReturn(1);
    }

    @Test(expected = NullPointerException.class)
    public void nullTest() {
        new MySqlDataSource(null, null);
    }

    @Test
    public void readDataSet() {
        MySqlDataSource dataSource = new MySqlDataSource(this.dataSource, null);
        DataSet dataSet = dataSource.readDataSet(simple);
        Assert.assertNotNull(dataSet);
        Assert.assertEquals(1L, dataSet.size());
    }

    @Test
    public void readDataSetWithEx() throws SQLException {
        dataSource = PowerMockito.mock(DataSource.class);
        PowerMockito.when(dataSource.getConnection()).thenThrow(new SQLException());
        DataSet dataSet = new MySqlDataSource(dataSource, null).readDataSet(simple);
        Assert.assertNotNull(dataSet);
        Assert.assertEquals(0, dataSet.size());
    }

    @Test
    public void readPagedDataSet() throws IOException {
        try (MySqlDataSource dataSource = new MySqlDataSource(this.dataSource, null)) {
            DataSet dataSet = dataSource.readPage(simple, 0, 10);
            Assert.assertEquals(1, dataSet.size());
            Assert.assertEquals("TestName", dataSet.next().getValue("name"));
        }
    }

    @Test
    public void saveDataSet() throws IOException {
        try (MySqlDataSource dataSource = new MySqlDataSource(this.dataSource, Collections.singletonMap("test", "simple script"))) {
            dataSource.saveEntities(simple, new DataSetImpl(Collections.singletonList(wrapper), simple));
            DataSet dataSet = dataSource.readDataSet(simple);
            Assert.assertEquals(1, dataSet.size());
            Assert.assertEquals("TestName", dataSet.next().getValue("name"));
        }
    }

    @Test
    public void updateDataSet() throws IOException {
        try (MySqlDataSource dataSource = new MySqlDataSource(this.dataSource, null)) {
            dataSource.updateEntities(simple, new DataSetImpl(Collections.singletonList(wrapper), simple));
            DataSet dataSet = dataSource.readDataSet(simple);
            Assert.assertEquals(1, dataSet.size());
            Assert.assertEquals("TestName", dataSet.next().getValue("name"));
        }
    }

}
