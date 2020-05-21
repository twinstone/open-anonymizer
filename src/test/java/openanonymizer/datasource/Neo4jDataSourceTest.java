package openanonymizer.datasource;

import openanonymizer.datasource.neo4j.Neo4jDataSource;
import openanonymizer.model.dataset.DataSet;
import openanonymizer.model.dataset.DataSetImpl;
import openanonymizer.model.dataset.PagedDataSet;
import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.describer.RelationFieldDescriber;
import openanonymizer.model.wrapper.EntityWrapper;
import openanonymizer.model.wrapper.EntityWrapperImpl;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.*;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;

/**
 * Test neo4j database datasource.
 */
public class Neo4jDataSourceTest {

    private EntityWrapper wrapper;
    private EntityDescriber simple;
    private Driver driver;

    @Before
    public void before() {
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
        map.put("test_ref", 10);
        wrapper = new EntityWrapperImpl(map, simple);

        driver = PowerMockito.mock(Driver.class);
        Session session = PowerMockito.mock(Session.class);
        Transaction transaction = PowerMockito.mock(Transaction.class);
        Result result = PowerMockito.mock(Result.class);
        Record record = PowerMockito.mock(Record.class);
        Value value = PowerMockito.mock(Value.class);
        PowerMockito.when(value.asLong()).thenReturn(1L);
        PowerMockito.when(record.get(anyInt())).thenReturn(value);
        PowerMockito.when(result.single()).thenReturn(record);
        PowerMockito.when(result.list(any())).thenReturn(Collections.singletonList(wrapper));
        PowerMockito.when(session.run(anyString())).thenReturn(result);
        PowerMockito.when(session.run(anyString(), anyMap())).thenReturn(result);
        PowerMockito.when(session.beginTransaction()).thenReturn(transaction);
        PowerMockito.when(driver.session()).thenReturn(session);
    }

    @Test
    public void readDataSet() {
        Neo4jDataSource dataSource = new Neo4jDataSource(driver);
        DataSet dataSet = dataSource.readDataSet(simple);
        Assert.assertNotNull(dataSet);
        Assert.assertEquals(1L, dataSet.size());
        Assert.assertEquals("TestName", dataSet.next().getValue("name"));
    }

    @Test
    public void readPagedDataSet() throws IOException {
        try (Neo4jDataSource dataSource = new Neo4jDataSource(driver)) {
            PagedDataSet dataSet = dataSource.readPage(simple, 0, 10);
            Assert.assertNotNull(dataSet);
            Assert.assertEquals(1L, dataSet.getTotalItems());
            Assert.assertEquals("TestName", dataSet.next().getValue("name"));
        }
    }

    @Test
    public void saveEntitiesTest() {
        RelationFieldDescriber field = new RelationFieldDescriber();
        field.setName("test_ref");
        field.setTargetSource("test_target");
        field.setRelationType(RelationFieldDescriber.RelationType.MANY_TO_ONE);
        simple.setRelationFields(Collections.singletonList(field));
        Neo4jDataSource dataSource = new Neo4jDataSource(driver);
        dataSource.saveEntities(simple, new DataSetImpl(Collections.singletonList(wrapper), simple));
    }

    @Test
    public void updateEntitiesTest() {
        Neo4jDataSource dataSource = new Neo4jDataSource(driver);
        dataSource.updateEntities(simple, new DataSetImpl(Collections.singletonList(wrapper), simple));
    }
}
