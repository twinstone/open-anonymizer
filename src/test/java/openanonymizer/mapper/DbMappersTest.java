package openanonymizer.mapper;

import openanonymizer.model.describer.EntityDescriber;
import openanonymizer.model.describer.FieldDescriber;
import openanonymizer.model.describer.RelationFieldDescriber;
import openanonymizer.model.mapper.MappingException;
import openanonymizer.model.mapper.MongoEntityMapper;
import openanonymizer.model.mapper.MySqlEntityMapper;
import openanonymizer.model.mapper.Neo4jEntityMapper;
import openanonymizer.model.wrapper.EntityWrapper;
import openanonymizer.model.wrapper.EntityWrapperImpl;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.value.NodeValue;
import org.powermock.api.mockito.PowerMockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;

/**
 * Contains tests for {@link MongoEntityMapper}, {@link MySqlEntityMapper} and {@link Neo4jEntityMapper}
 */
public class DbMappersTest {

    private static final MySqlEntityMapper sqlMapper = new MySqlEntityMapper();
    private static final MongoEntityMapper mongoMapper = new MongoEntityMapper();
    private static final Neo4jEntityMapper neo4jMapper = new Neo4jEntityMapper();

    private EntityDescriber describer;
    private EntityWrapper wrapper;
    Map<String, Object> map;

    @Before
    public void before() {
        describer = new EntityDescriber();
        FieldDescriber f1 = new FieldDescriber();
        f1.setName("name");
        FieldDescriber f2 = new FieldDescriber();
        f2.setName("age");
        describer.setFields(Arrays.asList(f1, f2));
        RelationFieldDescriber f3 = new RelationFieldDescriber();
        f3.setName("user_id");
        describer.setRelationFields(Collections.singletonList(f3));
        map = new HashMap<>();
        map.put("name", "TestName");
        map.put("age", 10);
        wrapper = new EntityWrapperImpl(map, describer);
    }

    @Test(expected = NullPointerException.class)
    public void nullEntitySqlTest() {
        sqlMapper.getFromEntity(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullEntityMongoTest() {
        mongoMapper.getFromEntity(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullEntityNeo4jTest() {
        neo4jMapper.getFromEntity(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullDescriberSqlTest() {
        ResultSet resultSet = PowerMockito.mock(ResultSet.class);
        sqlMapper.getFromEntity(resultSet, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullDescriberMongoTest() {
        Document document = PowerMockito.mock(Document.class);
        mongoMapper.getFromEntity(document, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullDescriberNeo4jTest() {
        Record record = PowerMockito.mock(Record.class);
        neo4jMapper.getFromEntity(record, null);
    }

    @Test(expected = MappingException.class)
    public void getFromEntitySqlTestEx() throws SQLException {
        ResultSet resultSet = PowerMockito.mock(ResultSet.class);
        PowerMockito.when(resultSet.getObject("name")).thenThrow(new SQLException());
        sqlMapper.getFromEntity(resultSet, describer);
    }

    @Test
    public void getFromEntitySqlTest() throws SQLException {
        ResultSet resultSet = PowerMockito.mock(ResultSet.class);
        PowerMockito.when(resultSet.getObject("name")).thenReturn("TestName");
        PowerMockito.when(resultSet.getObject("age")).thenReturn(10);
        PowerMockito.when(resultSet.getObject("user_id")).thenReturn(1);
        EntityWrapper w = sqlMapper.getFromEntity(resultSet, describer);
        Assert.assertEquals("TestName", w.getValue("name"));
        Assert.assertEquals(10, w.getValue("age"));
        Assert.assertEquals(1, w.getValue("user_id"));
    }

    @Test
    public void getFromEntityMongoTest() {
        Document document = PowerMockito.mock(Document.class);
        PowerMockito.when(document.get("name")).thenReturn("TestName");
        PowerMockito.when(document.get("age")).thenReturn(10);
        PowerMockito.when(document.get("user_id")).thenReturn(1);
        EntityWrapper w = mongoMapper.getFromEntity(document, describer);
        Assert.assertEquals("TestName", w.getValue("name"));
        Assert.assertEquals(10, w.getValue("age"));
        Assert.assertEquals(1, w.getValue("user_id"));
    }

    @Test
    public void getFromEntityNeo4jTest() {
        InternalNode internalNode = PowerMockito.mock(InternalNode.class);
        PowerMockito.when(internalNode.id()).thenReturn(1L);
        PowerMockito.when(internalNode.asMap()).thenReturn(map);
        NodeValue nodeValue = PowerMockito.mock(NodeValue.class);
        PowerMockito.when(nodeValue.asNode()).thenReturn(internalNode);
        Record record = PowerMockito.mock(Record.class);
        PowerMockito.when(record.get(anyInt())).thenReturn(nodeValue);
        EntityWrapper w = neo4jMapper.getFromEntity(record, describer);
        Assert.assertEquals("TestName", w.getValue("name"));
        Assert.assertEquals(10, w.getValue("age"));
        Assert.assertEquals(1L, w.getId());

    }

    @Test(expected = UnsupportedOperationException.class)
    public void fromWrapperSqlTest() {
        sqlMapper.getFromWrapper(null);
    }

    @Test
    public void fromWrapperMongoTest() {
        Document document = mongoMapper.getFromWrapper(wrapper);
        Assert.assertTrue(document.containsKey("name"));
        Assert.assertTrue(document.containsKey("age"));
        Assert.assertEquals("TestName", document.get("name"));
        Assert.assertEquals(10, document.get("age"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void fromWrapperNeo4jTest() {
        neo4jMapper.getFromWrapper(null);
    }
}
